package cli

import com.google.gson.Gson
import internal.InvoiceGenerator
import model.getClient
import model.getOwner
import org.apache.commons.cli.CommandLine
import pl.gumyns.faktura.api.pipe.getTimesheetPipeInput
import pl.gumyns.faktura.api.settings.SettingsManager

class CliTimesheetPipe(cli: CommandLine) : BaseCliHandler(cli) {
  val gson = Gson()
  val settings = SettingsManager()

  fun handle() {
    validateInput()

    val owner = gson.getOwner(settings.ownersDir.find(cli.getOptionValue("owner")))
    val products = gson.getTimesheetPipeInput(cli.getOptionValue("json"))?.products?.run {
      mapKeys {
        val client = settings.clientsDir.find(it.key)
        if (client == null) {
          println("Client '${it.key}' doesn't exists, check config with --interactive")
          System.exit(-1)
        }
        gson.getClient(settings.clientsDir.find(it.key))
      }
    }
    if (products == null) {
      println("Problems parsing 'json' parameter, check config with --interactive")
      System.exit(-1)
    }

    InvoiceGenerator(settings).apply {
      generate(owner, products!!)
    }
  }

  private fun validateInput() {
    if (!cli.hasOption("owner")) {
      println("ERROR: -owner <arg> is required")
      System.exit(-1)
    }
    if (!cli.hasOption("json")) {
      println("ERROR: -json <arg> is required")
      System.exit(-1)
    }
  }
}