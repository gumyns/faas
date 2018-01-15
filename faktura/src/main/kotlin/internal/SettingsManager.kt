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

  val workingDir
    get() = File(settings.workingDir).apply { mkdirs() }

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
        File(jarDir, "output").absolutePath
      ).also { this.settings = it }
    }
    set(value) = settingsFile.writeText(gson.toJson(value))

  fun copyNeededFiles() {
    copyTemplateFiles { !File(templatesDir, it).exists() }
  }

  fun copyTemplateFiles(onCondition:(String)->Boolean) {
    val classLoader = javaClass.classLoader
    arrayOf(
      "Faktura.odt",
      "FakturaUE-EN.odt"
    ).filter { onCondition.invoke(it) }
      .forEach {
        File(templatesDir, it).apply {
          createNewFile()
          copyInputStreamToFile(classLoader.getResourceAsStream("templates/$it"))
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