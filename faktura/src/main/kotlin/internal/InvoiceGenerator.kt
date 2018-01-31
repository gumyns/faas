package internal

import com.google.gson.Gson
import model.*
import pl.gumyns.faktura.api.product.Product
import pl.gumyns.faktura.api.product.ProductEntry
import pl.gumyns.faktura.api.settings.SettingsManager
import java.io.File
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*


class InvoiceGenerator(val settings: SettingsManager) {
  val gson = Gson()

  fun saveInvoice(invoice: Invoice) = settings.invoicesDir.also { dir ->
    File(dir, invoice.filename + ".json").writeText(gson.toJson(invoice))
  }

  fun generate(owner: Owner, map: Map<Client, Array<ProductEntry>>): List<Invoice> =
    map.map { entry -> generate(owner, entry.key, entry.value) }

  fun generate(owner: Owner, client: Client, product: Product, amount: BigDecimal) =
    generate(owner, client, arrayOf(product).map { ProductEntry(it, amount) }.toTypedArray())

  fun generate(owner: Owner, client: Client, products: Array<ProductEntry>): Invoice =
    Invoice(owner, client, products).apply {
      date = generateIssueDate(client).time
      dueDate = generateIssueDate(client).apply {
        set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + (client.paymentDelay ?: 0))
      }.time
      val invoiceDate = GregorianCalendar.getInstance().apply {
        time = Date(System.currentTimeMillis())
        set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) - 5)
      }
      number = when (owner.annualNumber) {
        InvoiceNumber.YEARLY -> settings.invoicesDir.listFiles()
          .filter { it.nameWithoutExtension.endsWith(invoiceDate[Calendar.YEAR].toString()) }
          .run { size + 1 }
          .let { "$it/${invoiceDate[Calendar.YEAR]}" }
        InvoiceNumber.MONTHLY -> settings.invoicesDir.listFiles()
          .filter { it.nameWithoutExtension.endsWith("${invoiceDate[Calendar.MONTH] + 1}_${invoiceDate[Calendar.YEAR]}") }
          .run { size + 1 }
          .let { "$it/${invoiceDate[Calendar.MONTH] + 1}/${invoiceDate[Calendar.YEAR]}" }
      }

      if (client.productType == ProductType.TOTAL) {
        products.forEach {
          it.amount = 1.toBigDecimal()
          it.price = it.netValue
        }
      }

      filename = "${SimpleDateFormat("yyyyMMdd").format(date)}_${owner.id}_${client.id}_$number"
        .replace(' ', '_').replace('.', '_').replace('/', '_')

      saveInvoice(this)
      PdfGenerator(settings).generate(this)
    }

  private fun generateIssueDate(client: Client): Calendar = when (client.dateDayType) {
    InvoiceDate.LAST -> GregorianCalendar.getInstance().apply {
      clear()
      time = Date(System.currentTimeMillis())
      set(Calendar.MONTH, get(Calendar.MONTH) + 1) // set next month
      set(Calendar.DAY_OF_MONTH, 0) // get last day of current month
      while (isHoliday(this))
        set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) - 1)
    }
    InvoiceDate.FIRST -> GregorianCalendar.getInstance().apply {
      clear()
      time = Date(System.currentTimeMillis())
      set(Calendar.DAY_OF_MONTH, 1) // get first day of month
      while (get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
        set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1)
    }
    InvoiceDate.TODAY -> GregorianCalendar.getInstance().apply {
      clear()
      time = Date(System.currentTimeMillis())
    }
  }.apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
  }

  private fun isHoliday(calendar: Calendar): Boolean {
    if (calendar[Calendar.DAY_OF_WEEK] == Calendar.SATURDAY || calendar[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY)
      return true
    val easter = getEasterDate(calendar.get(Calendar.YEAR))
    // wielkanoc
    if (calendar[Calendar.MONTH] == easter[Calendar.MONTH] && calendar[Calendar.DAY_OF_MONTH] == easter[Calendar.DAY_OF_MONTH])
      return true
    // poniedzialek wielkanocny
    if (calendar[Calendar.MONTH] == easter[Calendar.MONTH] && calendar[Calendar.DAY_OF_MONTH] == easter[Calendar.DAY_OF_MONTH] + 1)
      return true
    // boze cialo
    easter[Calendar.DAY_OF_MONTH] = easter[Calendar.DAY_OF_MONTH] + 60
    if (calendar[Calendar.MONTH] == easter[Calendar.MONTH] && calendar[Calendar.DAY_OF_MONTH] == easter[Calendar.DAY_OF_MONTH])
      return true
    // polish holidays
    when (calendar[Calendar.MONTH]) {
      Calendar.JANUARY -> return calendar[Calendar.DAY_OF_MONTH] == 1 || calendar[Calendar.DAY_OF_MONTH] == 6
      Calendar.MAY -> return calendar[Calendar.DAY_OF_MONTH] == 1 || calendar[Calendar.DAY_OF_MONTH] == 3
      Calendar.NOVEMBER -> return calendar[Calendar.DAY_OF_MONTH] == 1 || calendar[Calendar.DAY_OF_MONTH] == 11
      Calendar.DECEMBER -> return calendar[Calendar.DAY_OF_MONTH] == 25 || calendar[Calendar.DAY_OF_MONTH] == 26
    }
    return false
  }

  private fun getEasterDate(year: Int): Calendar {
    val a = year % 19
    val b = year / 100
    val c = year % 100
    val d = b / 4
    val e = b % 4
    val f = (b + 8) / 25
    val g = (b - f + 1) / 3
    val h = (19 * a + b - d - g + 15) % 30
    val i = c / 4
    val k = c % 4
    val l = (32 + 2 * e + 2 * i - h - k) % 7
    val m = (a + 11 * h + 22 * l) / 451
    val n = (h + l - 7 * m + 114) / 31
    val p = (h + l - 7 * m + 114) % 31
    val calendar = GregorianCalendar.getInstance()
    calendar.clear()
    calendar.set(year, n - 1, p + 1)
    return calendar
  }
}
