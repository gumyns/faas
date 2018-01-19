package pl.gumyns.faktura.api.product

import generated.CurrCodeType
import java.math.BigDecimal

class ProductEntry(
  product: Product? = null,
  var amount: BigDecimal = 1.toBigDecimal(),
  var netValue: BigDecimal? = product?.price?.multiply(amount),
  var totalPrice: BigDecimal? = product?.taxRate?.add(1.toBigDecimal())?.multiply(amount.multiply(product.price)),
  var taxPrice: BigDecimal? = product?.taxRate?.multiply(amount.multiply(product.price))
) : Product(product?.id, product?.name, product?.price, product?.taxType ?: TaxType.TAX_23, product?.currency ?: CurrCodeType.PLN)