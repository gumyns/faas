package cli

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

class OptionSet {
  companion object {
    val options = Options()
      .apply {
        addOption(Option("owner", true, "The seller of products issued on the invoice."))
        addOption(Option("client", true, "The receiver of the products issued on the invoice."))
        addOption(Option("amount", true, "The amount of the service in invoice."))
        addOption(Option(null, "product-name", false, "The amount of the service in invoice. Optional when used with 'amount' parameter."))
        addOption(Option(null, "from-project-json", true, "Json generated from various timesheets"))
        addOption(Option(null, "interactive", false, "Interactive mode"))
      }
  }
}
