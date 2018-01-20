package model

import com.google.gson.Gson
import java.io.File

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

fun Gson.getOwner(it: File?) = fromJson(it?.reader(), Owner::class.java)
