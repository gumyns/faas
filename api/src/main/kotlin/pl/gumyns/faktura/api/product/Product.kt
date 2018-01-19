package pl.gumyns.faktura.api.product

import generated.CurrCodeType
import java.math.BigDecimal

open class Product(
  var id: String? = null,
  var name: String? = null,
  var price: BigDecimal? = null,
  var taxRate: BigDecimal? = null,
  var currency: CurrCodeType = CurrCodeType.PLN
)