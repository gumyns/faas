package pl.gumyns.fakturka.toptotal

import pl.gumyns.faktura.api.settings.Directory
import pl.gumyns.faktura.api.settings.SettingsManager
import java.io.File

class ToptotalSettingsManager : SettingsManager() {

  val toptotalDir
    get() = Directory(workingDir, "toptotal-parser").apply { mkdirs() }

  val topTotalSettings
    get() = File(toptotalDir, "settings.csv")

  init {
    if (!topTotalSettings.exists()) {
      topTotalSettings.apply {
        createNewFile()
        writeText("project,client,product")
      }
    }
  }

}