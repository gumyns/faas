package pl.gumyns.faktura.api.product

import com.google.gson.Gson
import generated.CurrCodeType
import java.io.File
import java.math.BigDecimal

open class Product(
  var id: String? = null,
  var name: String? = null,
  var price: BigDecimal? = null,
  var taxType: TaxType = TaxType.TAX_23,
  var currency: CurrCodeType = CurrCodeType.PLN,
  var taxRate: BigDecimal? = taxType.rate
)

fun Gson.getProduct(file: File?) = fromJson(file?.reader(), Product::class.java)