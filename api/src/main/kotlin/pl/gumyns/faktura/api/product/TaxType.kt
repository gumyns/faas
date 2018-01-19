package pl.gumyns.faktura.api.product

import java.math.BigDecimal

enum class TaxType(val rate: BigDecimal, val showValue: String, private val description: String) {
  TAX_ZW(0.toBigDecimal(), "ZW", "Zwolnione"),
  TAX_NP(0.toBigDecimal(), "NP", "Nie podlega"),
  TAX_0(0.toBigDecimal(), "0%", "0%"),
  TAX_5(5.toBigDecimal().divide(100.toBigDecimal()), "5%", "5%"),
  TAX_8(8.toBigDecimal().divide(100.toBigDecimal()), "8%", "8%"),
  TAX_23(23.toBigDecimal().divide(100.toBigDecimal()), "23%", "23%");

  override fun toString(): String = description
}