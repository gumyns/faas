package model

data class Account(
  var currency: Currency = Currency.PLN,
  var bankName: String? = null,
  var number: String? = null,
  var swift: String? = null,
  var transfer: String? = null
) {
  fun toShortString() = "$currency - ${bankName ?: ""} ${number ?: ""}"
}

fun List<Account>.showUnorderedList(): String = StringBuilder().also { sb ->
  if (isEmpty()) sb.append("Brak")
  else forEach { sb.append("\n - " + it.toShortString()) }
}.toString()

fun List<Account>.showOrderedList(): String = StringBuilder().also { sb ->
  forEachIndexed { index, it -> sb.append("${index + 1}. ${it.toShortString()}\n") }
}.toString().trimEnd()