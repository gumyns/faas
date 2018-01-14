package interactive

import org.beryx.textio.TextTerminal

abstract class BaseMenu {
  companion object {
    val selectOption = "Wybierz numerek:"
  }

  protected fun showValue(terminal: TextTerminal<*>, id: Int, key: String, value: Any?) {
    terminal.println("$id. $key: ${value ?: ""}")
  }

  protected fun showBack(terminal: TextTerminal<*>, id: Int) = terminal.println("$id. Wróć")

  protected fun showSave(terminal: TextTerminal<*>, id: Int) = terminal.println("$id. Zapisz")

  protected fun showRemove(terminal: TextTerminal<*>, id: Int) = terminal.println("$id. Usuń")
}