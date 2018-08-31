package cli

import com.google.gson.Gson
import internal.InvoiceGenerator
import model.Client
import model.Owner
import org.apache.commons.cli.CommandLine
import pl.gumyns.faktura.api.product.Product
import pl.gumyns.faktura.api.product.ProductEntry
import pl.gumyns.faktura.api.settings.SettingsManager

class CliOwnerClientInvoice(cli: CommandLine) : BaseCliHandler(cli) {
  val gson = Gson()
  val settings = SettingsManager()

  fun handle() {
    validateInput()
    val owner = settings.ownersDir.find(cli.getOptionValue("owner"))?.reader()?.let { gson.fromJson(it, Owner::class.java) }
    if (owner == null) {
      println("Owner '${cli.getOptionValue("owner")}' doesn't exists, check config with --interactive")
      return
    }

    val client = settings.clientsDir.find(cli.getOptionValue("client"))?.reader()?.let { gson.fromJson(it, Client::class.java) }
    if (client == null) {
      println("Client '${cli.getOptionValue("client")}' doesn't exists, check config with --interactive")
      return
    }

    val products = cli.getOptionValue("product").let { it.split(",") }.map { settings.productsDir.find(it)?.reader()?.let { gson.fromJson(it, Product::class.java) } }
    if (products.any { it == null }) {
      println("Product '${cli.getOptionValue("product")}' doesn't exists, check config with --interactive")
      return
    }

    val amounts = cli.getOptionValue("amount").let { it.split(",") }.map { it.toBigDecimal() }
    if (amounts.any { it <= 0.toBigDecimal() }) {
      println("Amount '${cli.getOptionValue("amount")}' is too small")
      return
    }

    InvoiceGenerator(settings).apply {
      generate(owner, client, products.mapIndexed { i, product -> ProductEntry(product, amounts[i]) }.toTypedArray())
    }
  }

  private fun validateInput() {
    if (!cli.hasOption("owner")) {
      println("ERROR: -owner <arg> is required")
      System.exit(-1)
    }
    if (!cli.hasOption("client")) {
      println("ERROR: -client <arg> is required")
      System.exit(-1)
    }
    if (!cli.hasOption("amount")) {
      println("ERROR: -amount <arg> is required")
      System.exit(-1)
    }
  }
}