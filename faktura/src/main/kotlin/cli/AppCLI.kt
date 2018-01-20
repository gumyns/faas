package cli

import org.apache.commons.cli.CommandLine
import pl.gumyns.faktura.api.pipe.PipeTypes
import utils.AppMode

class AppCLI(val cli: CommandLine) : AppMode {
  override fun run() {
    when {
      cli.hasOption("amount") -> CliOwnerClientInvoice(cli).handle()
      cli.hasOption("create-backup") -> CliCreateBackup(cli).handle()
      cli.hasOption("restore-backup") -> CliRestoreBackup(cli).handle()
      cli.hasOption("pipe") && PipeTypes.valueOf(cli.getOptionValue("pipe")) == PipeTypes.Timesheet -> CliTimesheetPipe(cli).handle()
      else -> println("No idea what did ya mean :(")
    }
  }
}
