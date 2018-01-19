package cli

import org.apache.commons.cli.CommandLine
import pl.gumyns.faktura.api.settings.SettingsManager
import pl.gumyns.faktura.api.settings.jsonFiles
import pl.gumyns.faktura.api.settings.odtFiles
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class CliCreateBackup(cli: CommandLine) : BaseCliHandler(cli) {
  private val settings = SettingsManager()

  fun handle() {
    println("Creating backup...")
    val file = File("backup.zip")
    ZipOutputStream(FileOutputStream(file)).use {
      it.putNextEntry(ZipEntry("owners/"))
      settings.ownersDir.jsonFiles.forEach { file ->
        it.putNextEntry(ZipEntry("owners/${file.name}"))
        it.write(file.readBytes())
        it.closeEntry()
      }
      it.closeEntry()
      it.putNextEntry(ZipEntry("clients/"))
      settings.clientsDir.jsonFiles.forEach { file ->
        it.putNextEntry(ZipEntry("clients/${file.name}"))
        it.write(file.readBytes())
        it.closeEntry()
      }
      it.closeEntry()
      it.putNextEntry(ZipEntry("templates/"))
      settings.templatesDir.odtFiles.forEach { file ->
        it.putNextEntry(ZipEntry("templates/${file.name}"))
        it.write(file.readBytes())
        it.closeEntry()
      }
      it.closeEntry()
      it.putNextEntry(ZipEntry("invoices/"))
      settings.invoicesDir.listFiles().forEach { file ->
        it.putNextEntry(ZipEntry("invoices/${file.name}"))
        it.write(file.readBytes())
        it.closeEntry()
      }
      it.closeEntry()
      it.putNextEntry(ZipEntry("projects.json"))
      it.write(settings.projectsFile.readBytes())
      it.closeEntry()
    }
    println("Backup created and saved to '${file.absolutePath}'")
  }
}