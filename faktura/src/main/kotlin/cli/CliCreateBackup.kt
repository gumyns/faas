package cli

import internal.SettingsManager
import org.apache.commons.cli.CommandLine
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class CliCreateBackup(cli: CommandLine) : BaseCliHandler(cli) {
  private val settings = SettingsManager()

  fun handle() {
    ZipOutputStream(FileOutputStream("backup.zip")).use {
      it.putNextEntry(ZipEntry("owners/"))
      settings.ownersDir.listFiles().forEach { file ->
        it.putNextEntry(ZipEntry("owners/${file.name}"))
        it.write(file.readBytes())
        it.closeEntry()
      }
      it.closeEntry()
      it.putNextEntry(ZipEntry("clients/"))
      settings.clientsDir.listFiles().forEach { file ->
        it.putNextEntry(ZipEntry("clients/${file.name}"))
        it.write(file.readBytes())
        it.closeEntry()
      }
      it.closeEntry()
      it.putNextEntry(ZipEntry("templates/"))
      settings.templatesDir.listFiles().forEach { file ->
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
  }
}