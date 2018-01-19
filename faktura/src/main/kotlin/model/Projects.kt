package model

import com.google.gson.Gson
import pl.gumyns.faktura.api.model.ProjectEntries

data class Projects(
  val data: List<ProjectEntries> = listOf(),
  val type: String? = null
)

fun Gson.getProjects(it: String): Projects? = fromJson(it, Projects::class.java)