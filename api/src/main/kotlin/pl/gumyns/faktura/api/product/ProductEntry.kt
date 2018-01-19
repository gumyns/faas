package pl.gumyns.faktura.api.product

import generated.CurrCodeType
import java.math.BigDecimal

class ProductEntry(
  product: Product? = null,
  var amount: BigDecimal = 1.toBigDecimal(),
  var netValue: BigDecimal? = product?.price?.multiply(amount),
  var totalPrice: BigDecimal? = product?.taxRate?.add(1.toBigDecimal())?.multiply(amount.multiply(product.price))
) : Product(product?.id, product?.name, product?.price, product?.taxRate, product?.currency ?: CurrCodeType.PLN)