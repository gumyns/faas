import junit.framework.Assert.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.math.BigDecimal
import java.text.DecimalFormat


class PriceRoundingTest : Spek({
  describe("try rounding") {
    it("rest") {
      val format: (BigDecimal) -> String = { (DecimalFormat("0").format(it.minus(it.toInt().toBigDecimal()).multiply(100.toBigDecimal())?.toDouble()) + "/100") }
      assertEquals("45/100", format(BigDecimal("0.45")))
      assertEquals("45/100", format(BigDecimal("0.4545")))
      assertEquals("46/100", format(BigDecimal("0.4551")))
      assertEquals("71/100", format(BigDecimal("7041.7080")))
    }
  }
})

