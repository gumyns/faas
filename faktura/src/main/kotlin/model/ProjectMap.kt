package model

import com.google.gson.Gson
import java.io.File

data class ProjectMap(
  val owner: String? = null,
  val clients: MutableList<String> = mutableListOf(),
  val projects: MutableList<String> = mutableListOf()
) {
  fun showOrderedList() = StringBuilder().apply {
    projects.forEachIndexed { index, project -> appendln("${index + 1}. $project -> ${clients[index]}") }
  }.toString().trim()

  fun showUnorderedList() = StringBuilder().apply {
    projects.forEachIndexed { index, project -> appendln("$project -> ${clients[index]}") }
  }.toString().trim()
}

fun Gson.getProjectsArray(it: File) = fromJson(it.reader(), kotlin.Array<ProjectMap>::class.java)
