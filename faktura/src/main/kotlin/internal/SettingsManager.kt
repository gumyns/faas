package internal

import com.google.gson.Gson
import model.Settings
import java.io.File
import java.io.FileReader
import java.io.InputStream


class SettingsManager {
  private val jarDir = File(SettingsManager::class.java.protectionDomain.codeSource.location.path).parent
  private val settingsDir = File(jarDir, "settings")
  private val settingsFile = File(settingsDir, "settings.json")
  private val gson = Gson()

  init {
    File(jarDir).mkdirs()
    settingsDir.mkdirs()
    if (!settingsFile.exists()) {
      settingsFile.createNewFile()
    }
  }

  val tempDir
    get() = File(settings.workingDir, "temp").apply { mkdirs() }

  val templatesDir
    get() = File(settings.workingDir, "templates").apply { mkdirs() }

  val ownersDir
    get() = File(settings.workingDir, "owners").apply { mkdirs() }

  val clientsDir
    get() = File(settings.workingDir, "clients").apply { mkdirs() }

  val invoicesDir
    get() = File(settings.workingDir, "invoices").apply { mkdirs() }

  val jpkDir
    get() = File(settings.workingDir, "jpk").apply { mkdirs() }

  val pdfDir
    get() = File(settings.workingDir, "pdf").apply { mkdirs() }

  val projectsFile
    get() = File(settings.workingDir, "projects.json")

  var settings: Settings
    get() {
      val settings = gson.fromJson<Settings?>(FileReader(settingsFile), Settings::class.java)
      if (settings != null) {
        return settings
      }
      return Settings(
        File(jarDir, "output").absolutePath, null
      ).also { this.settings = it }
    }
    set(value) = settingsFile.writeText(gson.toJson(value))

  fun copyNeededFiles() {
    val classLoader = javaClass.classLoader
    arrayOf(
      "Faktura-single.odt",
      "Faktura-multiple.odt",
      "FakturaUE-EN-single.odt",
      "FakturaUE-EN-multiple.odt"
    ).forEach {
      if (!File(templatesDir, it).exists()) {
        File(templatesDir, it).apply {
          createNewFile()
          copyInputStreamToFile(classLoader.getResourceAsStream("templates/$it"))
        }
      }
    }
  }

  private fun File.copyInputStreamToFile(inputStream: InputStream) {
    inputStream.use { input ->
      this.outputStream().use { fileOut ->
        input.copyTo(fileOut)
      }
    }
  }
}