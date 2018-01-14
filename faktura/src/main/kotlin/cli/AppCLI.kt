package cli

import org.apache.commons.cli.CommandLine
import utils.AppMode

class AppCLI(val cli: CommandLine) : AppMode {
	override fun run() {
		if (cli.hasOption("from-project-json")) {
			CliFromProjectJson(cli).handle()
		}
	}
}
