package cli

import internal.SettingsManager
import org.apache.commons.cli.CommandLine
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
          when (isDirectory) {
            true -> mkdirs()
            else -> writeBytes(it.readBytes())
          }
        }
        entry = it.nextEntry
      }
    }
  }
}