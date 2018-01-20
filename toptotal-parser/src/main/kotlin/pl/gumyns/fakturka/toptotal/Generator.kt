package pl.gumyns.fakturka.toptotal

import com.google.gson.Gson
import java.io.File

object Generator {
  private val PROJECT = "project"
  private val DURATION = "duration"

  fun generate(path: String): String = File(path).let { file ->
    if (file.exists()) parseFile(file) else "File $path doesn't exists, skipping"
  }

  private fun parseFile(file: File): String {
    file.readText().lines()
      .map { parseLine(it, ',') }
      .let { list ->
        val projectIndex = list.first().indexOfFirst { it == PROJECT }
        val durationIndex = list.first().indexOfFirst { it == DURATION }

        val entries = prepareProjects(projectIndex, list)
          .apply {
            list.map { parseLine(projectIndex, durationIndex, it) }
              .forEach { this[it.project]?.add(it) }
            remove(PROJECT)
          }
          .map { ProjectSum.create(it.value) }
        File(file.nameWithoutExtension + ".json").writeText(Gson().toJson(Json("projects", entries.toTypedArray())))
        return Gson().toJson(Json("projects", entries.toTypedArray()))
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

  private fun prepareProjects(projectIndex: Int, list: List<List<String>>): MutableMap<String, MutableList<ProjectEntry>> =
    list.map { it[projectIndex] }
      .toHashSet()
      .groupBy { it }
      .mapValues { mutableListOf<ProjectEntry>() }
      .toMutableMap()


  private fun parseLine(projectIndex: Int, durationIndex: Int, line: List<String>) =
    ProjectEntry(line[projectIndex], line[durationIndex])
}
