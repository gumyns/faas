package internal

import com.google.gson.Gson
import com.ibm.icu.text.RuleBasedNumberFormat
import com.ibm.icu.util.ULocale
import fr.opensagres.xdocreport.converter.ConverterTypeTo
import fr.opensagres.xdocreport.converter.ConverterTypeVia
import fr.opensagres.xdocreport.converter.Options
import fr.opensagres.xdocreport.core.document.DocumentKind
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry
import fr.opensagres.xdocreport.template.TemplateEngineKind
import fr.opensagres.xdocreport.template.velocity.internal.VelocityTemplateEngine
import model.Invoice
import model.ProductType
import org.odftoolkit.odfdom.doc.OdfTextDocument
import pl.gumyns.faktura.api.product.ProductEntry
import pl.gumyns.faktura.api.product.TaxType
import pl.gumyns.faktura.api.settings.SettingsManager
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class PdfGenerator(val settings: SettingsManager) {
  val gson by lazy { Gson() }

  private val priceFormatter = object {
    fun format(number: BigDecimal): String = DecimalFormat("0.00").format(number)
  }

  fun generate(file: File) = generate(gson.fromJson(file.reader(), Invoice::class.java))
  fun generate(invoice: Invoice) {
    val file = invoice.client.template?.let { template -> settings.templatesDir.find(template)?.inputStream() }
    val odt = OdfTextDocument.loadDocument(file)
    odt.getTableByName("ProductTable").apply {
      getRowByIndex(1).apply {
        val invoiceLineTemplates = arrayOf("\$no", "\$name", "\$amount", "\$price", "\$netPrice", "\$taxRate", "\$taxPrice", "\$grossPrice")
        val productProperties = arrayOf(null, ProductEntry::name, ProductEntry::amount, ProductEntry::price, ProductEntry::netValue, ProductEntry::taxType, ProductEntry::taxPrice, ProductEntry::totalPrice)
        val invoiceLineIndexes = arrayOfNulls<Int>(invoiceLineTemplates.size)

        (0..(cellCount - 1))
          .filter { invoiceLineTemplates.indexOf(getCellByIndex(it).displayText) >= 0 }
          .forEach { invoiceLineIndexes[invoiceLineTemplates.indexOf(getCellByIndex(it).displayText)] = it }

        // take care of products
        insertRowsBefore(2, invoice.products.size)
        invoice.products.forEachIndexed { rowIndex, entry ->
          getRowByIndex(rowIndex + 1).apply {
            invoiceLineIndexes.forEachIndexed { index, i ->
              if (i != null) {
                getCellByIndex(i).apply {
                  if (productProperties[index] != null) {
                    val property = productProperties[index]
                    val value = property?.get(entry)
                    this.setDisplayText(when (property) {
                      ProductEntry::taxType -> (value as? TaxType)?.showValue
                      ProductEntry::amount -> value.toString()
                      else -> when (value) {
                        is BigDecimal -> DecimalFormat("0.00").format(value)
                        else -> value.toString()
                      }
                    }, when (property) {
                      ProductEntry::name -> "ProductStyle"
                      else -> "ProductStyleCenter"
                    })
                  } else {
                    this.setDisplayText("${rowIndex + 1}.", "ProductStyleCenter")
                  }
                }
              }
            }
          }
        }
        removeRowsByIndex(invoice.products.size + 1, 1)
      }
      // take care of sums
      getRowByIndex(rowCount - 1).apply {
        val invoiceLineTemplates = arrayOf("\$netPrice", "\$taxRate", "\$taxPrice", "\$grossPrice")
        val productProperties = arrayOf(ProductEntry::netValue, ProductEntry::taxType, ProductEntry::taxPrice, ProductEntry::totalPrice)
        val invoiceLineIndexes = arrayOfNulls<Int>(invoiceLineTemplates.size)

        (0..(cellCount - 1))
          .filter { invoiceLineTemplates.indexOf(getCellByIndex(it).displayText) >= 0 }
          .forEach { invoiceLineIndexes[invoiceLineTemplates.indexOf(getCellByIndex(it).displayText)] = it }

        val taxMap = invoice.products
          .groupBy { it.taxType }
          .mapValues {
            it.value.reduce { acc, entry ->
              acc.apply {
                netValue = netValue?.add(entry.netValue)
                taxPrice = taxPrice?.add(entry.taxPrice)
                totalPrice = totalPrice?.add(entry.totalPrice)
              }
            }
          }

        appendRows(taxMap.keys.size - 1)
        taxMap.values.forEachIndexed { index, taxType ->
          getRowByIndex(rowCount - 1 - index).apply {
            invoiceLineIndexes.forEachIndexed { index, i ->
              if (i != null) {
                getCellByIndex(i).apply {
                  val property = productProperties[index]
                  val value = property.get(taxType)
                  this.setDisplayText(when (property) {
                    ProductEntry::taxType -> (value as? TaxType)?.showValue
                    else -> when (value) {
                      is BigDecimal -> DecimalFormat("0.00").format(value)
                      else -> value.toString()
                    }
                  }, "ProductStyleCenter")
                }
              }
            }
          }
        }
      }
    }
    // save copy in memory
    val buffer = ByteArrayOutputStream()
    odt.save(buffer)
    // load template
    val template = XDocReportRegistry.getRegistry().loadReport(buffer.toByteArray().inputStream(), TemplateEngineKind.Velocity)
    template.templateEngine = VelocityTemplateEngine(Properties())
    // fill template with invoice data and save to file
    template.createContext().apply {
      put("obj", invoice)
      put("account", invoice.owner.accounts.find { it.currency == invoice.client.currency })
      put("date", SimpleDateFormat("dd/MM/yyyy").format(invoice.date))
      put("dueDate", SimpleDateFormat("dd/MM/yyyy").format(invoice.dueDate))
      put("price", priceFormatter)
      put("productAmount", when (invoice.client.productType) {
        ProductType.TOTAL -> 1.toBigDecimal()
        ProductType.HOURS -> invoice.products.first().amount
      })
      put("productPrice", DecimalFormat("0.00").format(when (invoice.client.productType) {
        ProductType.TOTAL -> invoice.netPrice
        ProductType.HOURS -> invoice.products.firstOrNull()?.price
      }))
      put("priceSpelled", getPriceSpelled(invoice.grossPrice))
    }.also { context ->
      FileOutputStream(File(settings.pdfDir, invoice.filename + ".pdf")).use {
        template.convert(context, Options.getFrom(DocumentKind.ODT).via(ConverterTypeVia.ODFDOM).to(ConverterTypeTo.PDF), it)
      }
      println("Invoice ${invoice.number} generated")
    }
  }

  private fun getPriceSpelled(price: BigDecimal?) = mapOf<String, String>(
    "pl" to RuleBasedNumberFormat(ULocale("pl"), RuleBasedNumberFormat.SPELLOUT).format(price?.toInt()),
    "en" to RuleBasedNumberFormat(ULocale("en"), RuleBasedNumberFormat.SPELLOUT).format(price?.toInt()),
    "de" to RuleBasedNumberFormat(ULocale("de"), RuleBasedNumberFormat.SPELLOUT).format(price?.toInt()),
    "rest" to (DecimalFormat("0").format(price?.minus(price.toInt().toBigDecimal())?.multiply(100.toBigDecimal())) + "/100")
  )
}