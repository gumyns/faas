package pl.gumyns.fakturka.toptotal

import java.util.concurrent.TimeUnit

data class Json(
	val type: String,
	val data: Array<ProjectSum>
)

data class ProjectEntry(
	val project: String,
	val duration: String
) {
	val durationMinutes: Long
		get() = duration.split(':').let {
			TimeUnit.HOURS.toMinutes(it[0].toLong()) + it[1].toLong()
		}
}

data class ProjectSum(
	val project: String,
	val minutesSpent: Long
) {
	companion object {
		fun create(list: List<ProjectEntry>): ProjectSum =
			list.map { ProjectSum(it.project, it.durationMinutes) }
				.reduce { acc, right -> ProjectSum(acc.project, acc.minutesSpent + right.minutesSpent) }
	}
}
