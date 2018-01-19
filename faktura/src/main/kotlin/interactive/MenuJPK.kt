package interactive

import com.google.gson.Gson
import generated.CurrCodeType
import generated.JPK
import model.Invoice
import model.Owner
import model.initialize
import pl.gumyns.faktura.api.settings.SettingsManager
import pl.gumyns.faktura.api.settings.jsonFiles
import utils.newDateInputReader
import utils.newRangeInputReader
import java.io.File
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller


object MenuJPK : BaseMenu() {
  val gson = Gson()

  fun run(app: AppInteractive): Unit = app.console.run {
    main@ while (true) {
      app.clearScreen()
      textTerminal.println("Lista wystawców faktur:")
      var index = 1
      val files = SettingsManager().ownersDir.jsonFiles

      files.forEach { textTerminal.println("${index++}. ${it.nameWithoutExtension}") }
      showBack(textTerminal, index++)

      val selection = newRangeInputReader(1..(index - 1))
        .read(BaseMenu.selectOption)
      when (selection) {
        (index - 1) -> break@main
        else -> showJPK(app, gson.fromJson(files[selection - 1].reader(), Owner::class.java))
      }
    }
  }

  private fun showJPK(app: AppInteractive, owner: Owner): Unit = app.console.textTerminal.run {
    val from = app.console.newDateInputReader()
      .read("Podaj date od (dd/mm/rrrr)")
    val to = app.console.newDateInputReader()
      .read("Podaj date do (dd/mm/rrrr)")
    val manager = SettingsManager()
    val ownersInvoices = manager.invoicesDir.listFiles()
      .filter { it.nameWithoutExtension.contains(owner.id!!) }
      .map { gson.fromJson(it.reader(), Invoice::class.java) }

    CurrCodeType.values().forEach { currency ->
      val file = File(manager.jpkDir, "jpk_$currency.xml")
      val invoices = ownersInvoices.filter { it.client.currency == currency }
      if (invoices.isNotEmpty()) {
        JAXBContext.newInstance(JPK::class.java).createMarshaller().apply {
          setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, java.lang.Boolean.TRUE)
          marshal(JPK().apply {
            naglowek = JPK.Naglowek().initialize(owner, currency, from, to)
            podmiot1 = JPK.Podmiot1().initialize(owner)
            faktura = invoices.map { JPK.Faktura().initialize(it) }
            fakturaCtrl = JPK.FakturaCtrl().initialize(invoices)
            stawkiPodatku = JPK.StawkiPodatku().initialize()
            fakturaWiersz = invoices
              .map { invoice -> invoice.products.map { JPK.FakturaWiersz().initialize(invoice, it) } }
              .reduce { acc, list -> acc + list }
            fakturaWierszCtrl = JPK.FakturaWierszCtrl().initialize(invoices)
          }, file)
        }
      }
    }
  }
}
