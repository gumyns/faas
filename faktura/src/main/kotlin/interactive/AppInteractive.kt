package interactive

import org.beryx.textio.TextIO
import org.beryx.textio.TextIoFactory
import utils.AppMode
import utils.newRangeInputReader

class AppInteractive : AppMode {
  val console: TextIO = TextIoFactory.getTextIO()

  override fun run() {
    console.textTerminal.setBookmark("start")
    showMainMenu()
  }

  fun clearScreen() = console.textTerminal.resetToBookmark("start")

  private fun showMainMenu(): Unit = console.textTerminal.run {
    main@ while (true) {
      clearScreen()
      println("""Tryb interaktywny:
1. Wystawcy faktur
2. Klienci
3. Produkty
4. JPK(FA)
5. Opcje
6. Nara.""")
      when (console.newRangeInputReader(1..6).read(BaseMenu.selectOption)) {
        1 -> MenuOwner.showOwners(this@AppInteractive)
        2 -> MenuClient.showClients(this@AppInteractive)
        3 -> MenuProducts.showProducts(this@AppInteractive)
        4 -> MenuJPK.run(this@AppInteractive)
        5 -> MenuSettings.run(this@AppInteractive)
        6 -> System.exit(0)
      }
    }
  }
}
