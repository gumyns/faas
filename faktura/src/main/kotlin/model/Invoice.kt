package model

import pl.gumyns.faktura.api.product.ProductEntry
import java.math.BigDecimal
import java.util.*

data class Invoice(
  val owner: Owner,
  val client: Client,
  var products: Array<ProductEntry>,
  var delivery: Client? = null,
  var date: Date? = null,
  var netPrice: BigDecimal? = products.map { it.netValue }.reduce { acc, net -> acc?.add(net) },
  var grossPrice: BigDecimal? = products.map { it.netValue?.multiply(it.taxRate?.add(1.toBigDecimal())) }.reduce { acc, gross -> acc?.add(gross) },
  var taxPrice: BigDecimal? = products.map { it.netValue?.multiply(it.taxRate) }.reduce { acc, gross -> acc?.add(gross) },
  var dueDate: Date? = null,
  var number: String? = null,
  var filename: String? = null) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Invoice

    if (owner != other.owner) return false
    if (client != other.client) return false
    if (delivery != other.delivery) return false
    if (date != other.date) return false
    if (netPrice != other.netPrice) return false
    if (grossPrice != other.grossPrice) return false
    if (taxPrice != other.taxPrice) return false
    if (dueDate != other.dueDate) return false
    if (number != other.number) return false
    if (filename != other.filename) return false
    if (!Arrays.equals(products, other.products)) return false

    return true
  }

  override fun hashCode(): Int {
    var result = owner.hashCode()
    result = 31 * result + client.hashCode()
    result = 31 * result + (delivery?.hashCode() ?: 0)
    result = 31 * result + (date?.hashCode() ?: 0)
    result = 31 * result + (netPrice?.hashCode() ?: 0)
    result = 31 * result + (grossPrice?.hashCode() ?: 0)
    result = 31 * result + (taxPrice?.hashCode() ?: 0)
    result = 31 * result + (dueDate?.hashCode() ?: 0)
    result = 31 * result + (number?.hashCode() ?: 0)
    result = 31 * result + (filename?.hashCode() ?: 0)
    result = 31 * result + Arrays.hashCode(products)
    return result
  }
}