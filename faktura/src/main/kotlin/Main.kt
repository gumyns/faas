import cli.AppCLI
import cli.OptionSet
import interactive.AppInteractive
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.ParseException
import pl.gumyns.faktura.api.settings.SettingsManager
import utils.AppMode

class Main {
  companion object {
    private val usage = "java -jar faktura.jar"
    private val header = "Apka do generowania faktur"

    @JvmStatic fun main(args: Array<String>) {
      val options = OptionSet.options
      val cli: CommandLine
      val app: AppMode?

      SettingsManager().copyNeededFiles()

      try {
        cli = DefaultParser().parse(options, args)
      } catch (e: ParseException) {
        System.out.println(e.message)
        HelpFormatter().printHelp(usage, header, options, null)
        System.exit(1)
        return
      }

      if (args.isEmpty()) {
        HelpFormatter().printHelp(usage, header, options, null)
        System.exit(1)
      }

      app = if (cli.hasOption("interactive")) {
        AppInteractive()
      } else {
        AppCLI(cli)
      }
      app.run()
      println("BB")
      System.exit(0)
    }
  }
}