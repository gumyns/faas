package model

import java.math.BigDecimal

enum class TaxRate(val rate: BigDecimal) {
  TAX_0(0.toBigDecimal()),
  TAX_5(5.toBigDecimal().divide(100.toBigDecimal())),
  TAX_8(8.toBigDecimal().divide(100.toBigDecimal())),
  TAX_23(23.toBigDecimal().divide(100.toBigDecimal()));

  override fun toString(): String = "%d%%".format(rate.multiply(100.toBigDecimal()).toInt())
}