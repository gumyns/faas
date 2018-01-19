package cli

import org.apache.commons.cli.CommandLine
import pl.gumyns.faktura.api.settings.SettingsManager
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipInputStream

class CliRestoreBackup(cli: CommandLine) : BaseCliHandler(cli) {
  private val settings = SettingsManager()

  fun handle() {
    val file = File(cli.getOptionValue("restore-backup"))
    if (!file.exists()) {
      println("Backup file not found, check arguments")
      System.exit(-1)
    }
    ZipInputStream(FileInputStream(file)).use {
      println("Restoring data...")
      var entry = it.nextEntry
      while (entry != null) {
        File(settings.workingDir, entry.name).apply {
          when (entry.isDirectory) {
            true -> mkdirs().also { println("Created directory $absolutePath") }
            else -> writeBytes(it.readBytes()).also { println("Restored file $absolutePath") }
          }
        }
        entry = it.nextEntry
      }
      println("Restoring data done!")
    }
  }
}