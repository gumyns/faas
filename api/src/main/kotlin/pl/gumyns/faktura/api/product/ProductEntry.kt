package pl.gumyns.faktura.api.product

import generated.CurrCodeType
import java.math.BigDecimal

class ProductEntry(
  product: Product? = null,
  val amount: BigDecimal = 1.toBigDecimal(),
  val netValue: BigDecimal? = product?.price?.multiply(amount),
  val totalPrice: BigDecimal? = product?.taxRate?.add(1.toBigDecimal())?.multiply(amount.multiply(product.price))
) : Product(product?.id, product?.name, product?.price, product?.taxRate, product?.currency ?: CurrCodeType.PLN)