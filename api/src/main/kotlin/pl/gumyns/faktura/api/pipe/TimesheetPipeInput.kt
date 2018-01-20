package pl.gumyns.faktura.api.pipe

import com.google.gson.Gson
import pl.gumyns.faktura.api.product.ProductEntry

@PipeType(PipeTypes.Timesheet)
class TimesheetPipeInput {
  /** map of client names to array of products */
  var products: Map<String, Array<ProductEntry>> = mapOf()
}

fun Gson.getTimesheetPipeInput(it: String) = fromJson(it, TimesheetPipeInput::class.java)