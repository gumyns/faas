package pl.gumyns.faktura.api.product

import generated.CurrCodeType
import java.math.BigDecimal

open class Product(
  val id: String? = null,
  val name: String? = null,
  val price: BigDecimal? = null,
  val taxRate: BigDecimal? = null,
  val currency: CurrCodeType = CurrCodeType.PLN
)