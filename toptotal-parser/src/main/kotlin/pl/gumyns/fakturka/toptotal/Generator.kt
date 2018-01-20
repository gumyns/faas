package pl.gumyns.fakturka.toptotal

import com.google.gson.Gson
import pl.gumyns.faktura.api.pipe.TimesheetPipeInput
import pl.gumyns.faktura.api.pipe.TimesheetProductEntry
import java.io.File
import java.util.concurrent.TimeUnit

object Generator {
  private val PROJECT = "project"
  private val DURATION = "duration"

  private val gson = Gson()

  fun generate(settings: ToptotalSettingsManager, path: String): String = File(path).let { file ->
    if (file.exists()) parseFile(file, settings) else "File $path doesn't exists, skipping"
  }

  private fun getMinutes(duration: String) = duration.split(':').let {
    TimeUnit.HOURS.toMinutes(it[0].toLong()) + it[1].toLong()
  }.toBigDecimal()

  private fun parseFile(file: File, settings: ToptotalSettingsManager): String {
    file.readText().lines()
      .map { parseLine(it, ',') }
      .let { list ->
        val projectIndex = list.first().indexOfFirst { it == PROJECT }
        val durationIndex = list.first().indexOfFirst { it == DURATION }

        // calculate hours per project
        val entries = list
          .filter { it[projectIndex] != PROJECT }
          .groupBy({ it[projectIndex] })
          .mapValues {
            it.value
              .map { getMinutes(it[durationIndex]) }
              .reduce { acc, l -> acc + l }
              .div(60.toBigDecimal())
          }
        // gather map of client -> map of product -> amount
        val map = settings.topTotalSettings.readLines()
          .map { it.split(",") }
          .filter { it[0] != "project" }
          .groupBy { it[1] }
          .mapValues {
            TimesheetProductEntry().apply {
              putAll(it.value
                .map { Pair(it[2], entries[it[0]]) }
                .groupBy({ it.first }, { it.second!! })
                .mapValues { it.value.reduce { acc, add -> acc.add(add) } })
            }
          }
        return gson.toJson(TimesheetPipeInput(map))
      }
  }

  private fun parseLine(line: String, separator: Char): List<String> {
    val result = mutableListOf<String>()
    var builder = StringBuilder()
    var quotes = 0
    for (ch in line) {
      when {
        ch == '\"' -> {
          quotes++
        }
        (ch == '\n') || (ch == '\r') -> {
        }
        (ch == separator) && (quotes % 2 == 0) -> {
          result.add(builder.toString())
          builder = StringBuilder()
        }
        else -> builder.append(ch)
      }
    }
    return result
  }
}
