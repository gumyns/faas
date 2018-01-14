package interactive

import com.google.gson.Gson
import internal.SettingsManager
import model.InvoiceNumber
import model.Owner
import model.showUnorderedList
import utils.newRangeInputReader
import java.io.File

object MenuOwner : BaseMenu() {
  private val gson = Gson()

  fun showOwners(app: AppInteractive): Unit = app.console.run {
    main@ while (true) {
      app.clearScreen()
      textTerminal.println("Lista wystawców faktur:")
      var index = 1
      val files = SettingsManager().ownersDir.listFiles()
        .filter { it.extension == "json" }

      files.forEach { textTerminal.println("${index++}. ${it.nameWithoutExtension}") }
      textTerminal.println("${index++}. Nowa firma")
      showBack(textTerminal, index++)

      val selection = newRangeInputReader(1..(index - 1))
        .read(BaseMenu.selectOption)
      when (selection) {
        (index - 2) -> showOwner(app, Owner(), true)
        (index - 1) -> break@main
        else -> showOwner(app, gson.fromJson(files[selection - 1].reader(), Owner::class.java))
      }
    }
  }

  private fun showOwner(app: AppInteractive, inOwner: Owner, isNew: Boolean = false): Unit = app.console.run {
    var owner = inOwner
    if (isNew) {
      var id: String? = null
      while (id == null || id.isEmpty()) {
        app.clearScreen()
        id = newStringInputReader().read("Krótka nazwa firmy (ID w systemie)")
        val file = SettingsManager().ownersDir.listFiles().find { it.nameWithoutExtension == id }
        if (file != null) {
          owner = gson.fromJson(file.reader(), Owner::class.java)
        } else {
          owner.id = id
        }
      }
    }
    main@ while (true) {
      app.clearScreen()
      textTerminal.println("Pola oznaczone * są opcjonalne")
      var index = 1
      owner.apply {
        showValue(textTerminal, index++, "ID", id)
        showValue(textTerminal, index++, "Nazwa firmy", name)
        showValue(textTerminal, index++, "Adres", MenuAddress.shortDescription(address))
        showValue(textTerminal, index++, "NIP", nip)
        showValue(textTerminal, index++, "REGON", regon)
        showValue(textTerminal, index++, "VAT UE(*)", vatid)
        showValue(textTerminal, index++, "Konta bankowe", accounts.showUnorderedList())
        showValue(textTerminal, index++, "Numerowanie faktur", annualNumber?.description)
      }
      showSave(textTerminal, index++)
      showRemove(textTerminal, index++)
      showBack(textTerminal, index++)
      val selection = newRangeInputReader(1..(index - 1))
        .read(BaseMenu.selectOption)
      when (selection) {
        (index - 3) -> saveOwner(owner)
        (index - 2) -> removeOwner(owner)
        (index - 1) -> break@main
        else -> edit(app, selection, owner)
      }
    }
  }

  private fun saveOwner(owner: Owner) =
    File(SettingsManager().ownersDir, "${owner.id}.json").writeText(gson.toJson(owner))

  private fun removeOwner(owner: Owner) =
    File(SettingsManager().ownersDir, "${owner.id}.json").delete()

  private fun edit(app: AppInteractive, index: Int, owner: Owner) = app.console.apply {
    when (index) {
      1 -> owner.id = newStringInputReader().read("Krótka nazwa firmy (ID w systemie)")
      2 -> owner.name = newStringInputReader().read("Nazwa firmy")
      3 -> MenuAddress.showAddress(app, owner.address)
      4 -> owner.nip = newStringInputReader().read("NIP")
      5 -> owner.regon = newStringInputReader().read("REGON")
      6 -> owner.vatid = newStringInputReader().read("VAT UE")
      7 -> MenuBank.showAccounts(app, owner.accounts)
      8 -> owner.annualNumber = newRangeInputReader(1..2).read("""Numerowanie faktur:
1. ${InvoiceNumber.YEARLY.description}
2. ${InvoiceNumber.MONTHLY.description}
${BaseMenu.selectOption}""").let {
        when (it) {
          1 -> InvoiceNumber.YEARLY
          else -> InvoiceNumber.MONTHLY
        }
      }

    }
  }
}