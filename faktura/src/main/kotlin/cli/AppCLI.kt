package cli

import org.apache.commons.cli.CommandLine
import utils.AppMode

class AppCLI(val cli: CommandLine) : AppMode {
  override fun run() {
    when {
      cli.hasOption("from-project-json") -> CliFromProjectJson(cli).handle()
      cli.hasOption("amount") -> CliOwnerClientInvoice(cli).handle()
      cli.hasOption("create-backup") -> CliCreateBackup(cli).handle()
      cli.hasOption("restore-backup") -> CliRestoreBackup(cli).handle()
    }
  }
}
