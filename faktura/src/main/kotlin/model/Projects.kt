package model

import com.google.gson.Gson

data class Projects(
	val data: List<ProjectEntry> = listOf(),
	val type: String? = null
)

fun Gson.getProjects(it: String): Projects? = fromJson(it, Projects::class.java)