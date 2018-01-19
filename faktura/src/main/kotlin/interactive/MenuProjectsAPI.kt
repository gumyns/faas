package interactive

import com.google.gson.Gson
import model.Owner
import model.ProjectMap
import pl.gumyns.faktura.api.settings.SettingsManager
import pl.gumyns.faktura.api.settings.jsonFiles
import utils.newRangeInputReader

@Deprecated("it goes away, won't be used in the future")
object MenuProjectsAPI : BaseMenu() {
  val gson = Gson()
  val manager = SettingsManager()

  fun showProjects(app: AppInteractive) = app.console.apply {
    main@ while (true) {
      app.clearScreen()
      var index = 1
      val files = SettingsManager().ownersDir.jsonFiles

      files.forEach { textTerminal.println("${index++}. ${it.nameWithoutExtension}") }
      showBack(textTerminal, index++)

      val selection = newRangeInputReader(1..(index - 1))
        .read(BaseMenu.selectOption)
      when (selection) {
        (index - 1) -> break@main
        else -> showProjects(app, gson.fromJson(files[selection - 1].reader(), Owner::class.java))
      }
    }
  }


  fun showProjects(app: AppInteractive, owner: Owner) = app.console.apply {
    if (!manager.projectsFile.exists()) {
      manager.projectsFile.createNewFile()
      manager.projectsFile.writeText(gson.toJson(mutableListOf(ProjectMap(owner.id))))
    }
    val projectsList = gson.fromJson(manager.projectsFile.reader(), Array<ProjectMap>::class.java).toMutableList()
    var projects: ProjectMap? = projectsList.firstOrNull { it.owner == owner.id }
    if (projects == null) {
      projectsList.add(ProjectMap(owner.id))
      projects = projectsList.first { it.owner == owner.id }
    }
    main@ while (true) {
      app.clearScreen()
      textTerminal.println(projects.showUnorderedList())
      textTerminal.println("1. Nowy projekt")
      textTerminal.println("2. Usuń projekt")
      showBack(textTerminal, 3)

      val selection = newRangeInputReader(1..3)
        .read(BaseMenu.selectOption)
      when (selection) {
        1 -> add(app, projects)
        2 -> remove(app, projects)
        3 -> break@main
      }
    }
    manager.projectsFile.writeText(gson.toJson(projectsList))
  }

  private fun add(app: AppInteractive, projects: ProjectMap) = app.console.apply {
    app.clearScreen()
    projects.projects.add(newStringInputReader().read("Nazwa projektu"))
    app.clearScreen()
    var index = 1
    val files = manager.clientsDir.clientList
    files.forEach { textTerminal.println("${index++}. $it") }
    projects.clients.add(files[newRangeInputReader(1..files.size)
      .read(BaseMenu.selectOption) - 1])
  }

  private fun remove(app: AppInteractive, projects: ProjectMap) = app.console.run {
    app.clearScreen()
    textTerminal.println(projects.showOrderedList())
    showBack(textTerminal, projects.projects.size + 1)
    val selection = newRangeInputReader(1..(projects.projects.size + 1))
      .read(BaseMenu.selectOption)
    when (selection) {
      (projects.projects.size + 1) -> return@run
      else -> {
        projects.projects.removeAt(selection - 1)
        projects.clients.removeAt(selection - 1)
      }
    }
  }
}