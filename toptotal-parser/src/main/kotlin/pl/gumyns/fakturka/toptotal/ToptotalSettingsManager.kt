package pl.gumyns.fakturka.toptotal

import pl.gumyns.faktura.api.settings.Directory
import pl.gumyns.faktura.api.settings.SettingsManager

class ToptotalSettingsManager : SettingsManager() {

  val toptotalDir
    get() = Directory(workingDir, "toptotal-parser").apply { mkdirs() }
}