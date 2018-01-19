package interactive

import com.google.gson.Gson
import model.*
import pl.gumyns.faktura.api.settings.SettingsManager
import pl.gumyns.faktura.api.settings.jsonFiles
import utils.newBigDecimalInputReader
import utils.newRangeInputReader
import java.io.File

object MenuClient : BaseMenu() {
  private val gson = Gson()

  fun showClients(app: AppInteractive): Unit = app.console.run {
    main@ while (true) {
      app.clearScreen()
      var index = 1
      val files = SettingsManager().clientsDir.jsonFiles

      files.forEach { textTerminal.println("${index++}. ${it.nameWithoutExtension}") }
      textTerminal.println("${index++}. Nowy klient")
      showBack(textTerminal, index++)

      val selection = newRangeInputReader(1..(index - 1))
        .read(BaseMenu.selectOption)
      when (selection) {
        (index - 2) -> showClient(app, Client(), true)
        (index - 1) -> break@main
        else -> showClient(app, gson.fromJson(files[selection - 1].reader(), Client::class.java))
      }
    }
  }

  private fun showClient(app: AppInteractive, inClient: Client, isNew: Boolean = false): Unit = app.console.run {
    var client = inClient
    if (isNew) {
      var id: String? = null
      while (id == null || id.isEmpty()) {
        app.clearScreen()
        id = newStringInputReader().read("Krótka nazwa firmy (ID w systemie)")
        val file = SettingsManager().clientsDir.find(id)
        if (file != null) {
          client = gson.fromJson(file.reader(), Client::class.java)
        } else {
          client.id = id
        }
      }
    }
    main@ while (true) {
      app.clearScreen()
      var index = 1
      client.apply {
        showValue(textTerminal, index++, "ID", id)
        showValue(textTerminal, index++, "Nazwa firmy", name)
        showValue(textTerminal, index++, "Adres", MenuAddress.shortDescription(address))
        showValue(textTerminal, index++, "NIP", nip)
        showValue(textTerminal, index++, "Stawka godzinowa", hourlyRate)
        showValue(textTerminal, index++, "Waluta", currency)
        showValue(textTerminal, index++, "Termin płatności w dniach", paymentDelay)
        showValue(textTerminal, index++, "Wzór faktury", template)
        showValue(textTerminal, index++, "Typ daty", dateDayType.description)
        showValue(textTerminal, index++, "Typ liczebności", productType.description)
        showValue(textTerminal, index++, "Typ", type)
      }
      showSave(textTerminal, index++)
      showRemove(textTerminal, index++)
      showBack(textTerminal, index++)
      val selection = newRangeInputReader(1..(index - 1))
        .read(BaseMenu.selectOption)
      when (selection) {
        (index - 3) -> save(client)
        (index - 2) -> remove(client)
        (index - 1) -> break@main
        else -> edit(app, selection, client)
      }
    }
  }

  private fun save(client: Client) =
    File(SettingsManager().clientsDir, "${client.id}.json").writeText(gson.toJson(client))

  private fun remove(client: Client) =
    File(SettingsManager().clientsDir, "${client.id}.json").delete()

  private fun edit(app: AppInteractive, index: Int, client: Client) = app.console.apply {
    when (index) {
      1 -> client.id = newStringInputReader().read("Krótka nazwa firmy (ID w systemie)")
      2 -> client.name = newStringInputReader().read("Nazwa firmy")
      3 -> MenuAddress.showAddress(app, client.address)
      4 -> client.nip = newStringInputReader().read("NIP")
      5 -> client.hourlyRate = newBigDecimalInputReader().read("Stawka godzinowa")
      6 -> client.currency = newEnumInputReader(Currency::class.java).read("Waluta")
      7 -> client.paymentDelay = newIntInputReader().withMinVal(0).read("Termin płatności w dniach")
      8 -> client.template = SettingsManager().templatesDir.templateList
        .let {
          it.forEachIndexed { index, name -> textTerminal.println("${index + 1}. $name") }
          it[newRangeInputReader(1..it.size).read("Wzór faktury") - 1]
        }
      9 -> client.dateDayType = InvoiceDate.values()[newRangeInputReader(1..(InvoiceDate.values().size)).read(
        StringBuilder("Typ daty określa jaka data jest na fakturze przy założeniu, że generanowanie odbywa się do ostatniego dnia miesiąca.\n").apply {
          InvoiceDate.values().forEachIndexed { index, date -> append("${index + 1}. ${date.description}\n") }
          append(BaseMenu.selectOption)
        }.toString()
      ) - 1]
      10 -> client.productType = ProductType.values()[newRangeInputReader(1..(ProductType.values().size)).read(
        StringBuilder("Typ określa liczebność na fakturze.\n").apply {
          ProductType.values().forEachIndexed { index, type -> append("${index + 1}. ${type.description}\n") }
          append(BaseMenu.selectOption)
        }.toString()
      ) - 1]
      11 -> client.type = newEnumInputReader(ClientType::class.java).read("Typ klienta")
    }
  }
}