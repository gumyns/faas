package pl.gumyns.faktura.api.pipe

import com.google.gson.Gson
import java.math.BigDecimal

class TimesheetProductEntry : HashMap<String, BigDecimal>()

@PipeType(PipeTypes.Timesheet)
data class TimesheetPipeInput(
  /** map of client names to array of products */
  var products: Map<String, TimesheetProductEntry> = mapOf()
)

fun Gson.getTimesheetPipeInput(it: String) = fromJson(it, TimesheetPipeInput::class.java)