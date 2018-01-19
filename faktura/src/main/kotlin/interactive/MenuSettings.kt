package interactive

import pl.gumyns.faktura.api.settings.SettingsManager
import java.io.File

object MenuSettings {
  internal fun run(app: AppInteractive): Unit = app.console.textTerminal.run {
    val manager = SettingsManager()
    var selection = 0
    main@ while (selection != 3) {
      val settings = manager.settings
      app.clearScreen()
      println("""Change settings:
1. Output directory. Current:
${settings.workingDir}
2. Restore default invoice templates
3. Nothing, go back""")
      selection = app.console.newIntInputReader()
        .withMinVal(1)
        .withMaxVal(3)
        .withInputTrimming(true)
        .read("Select option:")
      when (selection) {
        1 -> {
          manager.settings = manager.settings.apply {
            val path = app.console.newStringInputReader()
              .read("New working directory")
            try {
              val file = File(path)
              workingDir = file.absolutePath + '/'
            } catch (ex: Exception) {
            }
          }
        }
        2 -> {
          manager.copyTemplateFiles({ true })
        }
        3 -> break@main
      }
    }
  }
}
