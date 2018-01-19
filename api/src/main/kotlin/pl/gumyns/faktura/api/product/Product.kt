package pl.gumyns.faktura.api.product

import generated.CurrCodeType
import java.math.BigDecimal

open class Product(
  var id: String? = null,
  var name: String? = null,
  var price: BigDecimal? = null,
  var taxType: TaxType = TaxType.TAX_23,
  var currency: CurrCodeType = CurrCodeType.PLN,
  var taxRate: BigDecimal? = taxType.rate
)