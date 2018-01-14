package model

import java.math.BigDecimal
import java.util.*

data class Invoice(
  val owner: Owner,
  val client: Client,
  var delivery: Client? = null,
  var amount: BigDecimal? = null,
  var date: Date? = null,
  var name: String? = null,
  var netPrice: BigDecimal? = null,
  var grossPrice: BigDecimal? = null,
  var taxPrice: BigDecimal? = null,
  var taxRate: BigDecimal = BigDecimal("0.23"),
  var dueDate: Date? = null,
  var number: String? = null,
  var filename: String? = null,
  var priceStringPL: String? = null
)