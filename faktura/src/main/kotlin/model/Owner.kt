package model

data class Owner(
  var id: String? = null,
  var name: String? = null,
  var address: Address = Address(),
  var nip: String? = null,
  var vatid: String? = null,
  var regon: String? = null,
  var accounts: MutableList<Account> = mutableListOf(),
  var annualNumber: InvoiceNumber = InvoiceNumber.YEARLY,
  var govCode: String? = null
)
