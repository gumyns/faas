package pl.gumyns.faktura.api.settings

import com.google.gson.Gson
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
    get() = Directory(settings.workingDir).apply { mkdirs() }

  val templatesDir
    get() = TemplatesDir(workingDir).apply { mkdirs() }

  val ownersDir
    get() = OwnersDir(workingDir).apply { mkdirs() }

  val clientsDir
    get() = ClientsDir(workingDir).apply { mkdirs() }

  val invoicesDir
    get() = Directory(workingDir, "invoices").apply { mkdirs() }

  val jpkDir
    get() = Directory(workingDir, "jpk").apply { mkdirs() }

  val pdfDir
    get() = Directory(workingDir, "pdf").apply { mkdirs() }

  val projectsFile
    get() = File(workingDir, "projects.json")

  var settings: Settings
    get() {
      val settings = gson.fromJson<Settings?>(FileReader(settingsFile), Settings::class.java)
      if (settings != null) {
        return settings
      }
      return Settings(Directory(jarDir, "output").absolutePath)
        .also { this.settings = it }
    }
    set(value) = settingsFile.writeText(gson.toJson(value))

  fun copyNeededFiles() {
    copyTemplateFiles { !File(templatesDir, it).exists() }
  }

  fun copyTemplateFiles(onCondition: (String) -> Boolean) {
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