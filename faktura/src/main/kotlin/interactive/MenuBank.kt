package interactive

import generated.CurrCodeType
import model.Account
import model.showOrderedList
import utils.newRangeInputReader

object MenuBank : BaseMenu() {
  fun showAccounts(app: AppInteractive, accounts: MutableList<Account>) = app.console.apply {
    main@ while (true) {
      app.clearScreen()
      textTerminal.println(accounts.showOrderedList())
      var index = accounts.size + 1
      textTerminal.println("${index++}. Nowe konto")
      textTerminal.println("${index++}. Usuń konto")
      showBack(textTerminal, index++)

      val selection = newRangeInputReader(1..(index - 1))
        .read(BaseMenu.selectOption)
      when (selection) {
        (index - 3) -> showAccount(app, Account()).also { accounts += it }
        (index - 2) -> remove(app, accounts)
        (index - 1) -> break@main
        else -> showAccount(app, accounts[selection - 1])
      }
    }
  }

  private fun showAccount(app: AppInteractive, account: Account): Account = app.console.run {
    main@ while (true) {
      app.clearScreen()
      var index = 1
      account.apply {
        showValue(textTerminal, index++, "Nazwa banku", bankName)
        showValue(textTerminal, index++, "Numer konta", number)
        showValue(textTerminal, index++, "Numer SWIFT (opcja, tylko zagraniczne)", swift)
        showValue(textTerminal, index++, "Typ płatności", transfer)
        showValue(textTerminal, index++, "Waluta", currency)
      }
      showBack(textTerminal, index++)
      val selection = newRangeInputReader(1..(index - 1))
        .read(BaseMenu.selectOption)
      when (selection) {
        (index - 1) -> break@main
        else -> edit(app, selection, account)
      }
    }
    return account
  }

  private fun edit(app: AppInteractive, index: Int, account: Account) = app.console.apply {
    when (index) {
      1 -> account.bankName = newStringInputReader().read("Nazwa banku")
      2 -> account.number = newStringInputReader().read("Numer konta")
      3 -> account.swift = newStringInputReader().read("Numer SWIFT (tylko FVAT UE, PL puste)")
      4 -> account.transfer = newStringInputReader().read("Typ płatności")
      5 -> account.currency = newEnumInputReader(CurrCodeType::class.java).read("Waluta")
    }
  }

  private fun remove(app: AppInteractive, accounts: MutableList<Account>) = app.console.run {
    app.clearScreen()
    textTerminal.println(accounts.showOrderedList())
    showBack(textTerminal, accounts.size + 1)
    val selection = newRangeInputReader(1..(accounts.size + 1))
      .read(BaseMenu.selectOption)
    when (selection) {
      (accounts.size + 1) -> return@run
      else -> accounts.removeAt(selection - 1)
    }
  }
}