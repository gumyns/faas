package utils

import org.beryx.textio.InputReader
import org.beryx.textio.IntInputReader
import org.beryx.textio.TextIO
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

fun IntInputReader.withRange(range: IntRange) = withMinVal(range.min()).withMaxVal(range.max())

fun TextIO.newRangeInputReader(range: IntRange) = newIntInputReader().withRange(range)

fun TextIO.newBigDecimalInputReader() = newGenericInputReader<BigDecimal> {
	try {
		return@newGenericInputReader InputReader.ParseResult(BigDecimal(it))
	} catch (e: Exception) {
		return@newGenericInputReader InputReader.ParseResult(BigDecimal.ZERO, "Something went wrong, try again")
	}
}

fun TextIO.newDateInputReader() = newGenericInputReader<Date> {
	try {
		return@newGenericInputReader InputReader.ParseResult(SimpleDateFormat("dd/MM/yyyy").parse(it))
	} catch (e: Exception) {
		return@newGenericInputReader InputReader.ParseResult(Date(), "Wrong format, try again")
	}
}