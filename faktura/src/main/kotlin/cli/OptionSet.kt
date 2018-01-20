package cli

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

class OptionSet {
  companion object {
    val options = Options()
      .apply {
        addOption(Option("owner", true, "The seller of products issued on the invoice."))
        addOption(Option("client", true, "The receiver of the products issued on the invoice."))
        addOption(Option("amount", true, "The amount of the product in invoice."))
        addOption(Option("product", true, "The product in invoice."))
        addOption(Option("pipe", true, "Json generated from various timesheets"))
        addOption(Option("json", true, "Json for pipe API"))
        addOption(Option(null, "create-backup", false, "Makes backup."))
        addOption(Option(null, "restore-backup", true, "Restores backup from file."))
        addOption(Option(null, "interactive", false, "Interactive mode"))
      }
  }
}
