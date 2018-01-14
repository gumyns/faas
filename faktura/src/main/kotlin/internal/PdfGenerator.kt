package internal

import com.google.gson.Gson
import fr.opensagres.xdocreport.converter.ConverterTypeTo
import fr.opensagres.xdocreport.converter.ConverterTypeVia
import fr.opensagres.xdocreport.converter.Options
import fr.opensagres.xdocreport.core.document.DocumentKind
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry
import fr.opensagres.xdocreport.template.TemplateEngineKind
import model.Invoice
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class PdfGenerator(val settings: SettingsManager) {
  val gson by lazy { Gson() }

  private val priceFormatter = object {
    fun format(number: BigDecimal): String = DecimalFormat("0.00").format(number)
  }

  fun generate(file: File) = generate(gson.fromJson(file.reader(), Invoice::class.java))
  fun generate(invoice: Invoice) {
    // load template
    val template = XDocReportRegistry.getRegistry().loadReport(invoice.client.template?.let { template ->
      settings.templatesDir.listFiles().firstOrNull { it.nameWithoutExtension == template }?.inputStream()
    }, TemplateEngineKind.Velocity)
    // fill template with invoice data and save to file
    template.createContext().apply {
      put("obj", invoice)
      put("account", invoice.owner.accounts.find { it.currency == invoice.client.currency })
      put("date", SimpleDateFormat("dd/MM/yyyy").format(invoice.date))
      put("dueDate", SimpleDateFormat("dd/MM/yyyy").format(invoice.dueDate))
      put("price", priceFormatter)
    }.also { context ->
      FileOutputStream(File(settings.pdfDir, invoice.filename + ".pdf")).use {
        template.convert(context, Options.getFrom(DocumentKind.ODT).via(ConverterTypeVia.ODFDOM).to(ConverterTypeTo.PDF), it)
      }
      println("Invoice ${invoice.number} generated")
    }
  }
}