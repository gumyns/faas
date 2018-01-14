import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.Assert.assertEquals
import java.util.*

class DateSeekerTest : Spek({
	describe("last business day of a month") {
		it("should return last business day of a month") {
			val calendar = GregorianCalendar.getInstance().apply {
				clear()
				set(Calendar.YEAR, 2017)
				set(Calendar.MONTH, Calendar.DECEMBER)
				set(Calendar.MONTH, get(Calendar.MONTH) + 1) // set next month
				set(Calendar.DAY_OF_MONTH, -1) // get last day of month
				while (get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
					set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) - 1)
			}

			assertEquals(29, calendar.get(Calendar.DAY_OF_MONTH))
		}
	}

	describe("first business day of a month") {
		it("should return first business day of a month") {
			val calendar = GregorianCalendar.getInstance().apply {
				clear()
				set(Calendar.YEAR, 2017)
				set(Calendar.MONTH, Calendar.OCTOBER)
				set(Calendar.DAY_OF_MONTH, 1) // get first day of month
				while (get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
					set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1)
			}

			assertEquals(2, calendar.get(Calendar.DAY_OF_MONTH))
		}
	}
})

