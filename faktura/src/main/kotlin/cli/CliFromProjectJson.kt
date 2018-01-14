package cli

import com.google.gson.Gson
import internal.InvoiceGenerator
import internal.SettingsManager
import model.Owner
import model.getClient
import model.getProjects
import model.getProjectsArray
import org.apache.commons.cli.CommandLine
import java.math.BigDecimal

class CliFromProjectJson(cli: CommandLine) : BaseCliHandler(cli) {
  val gson = Gson()
  val settings = SettingsManager()

  fun handle() {
    validateInput()
    val ownersProjects = cli.getOptionValue("owner").let { owner ->
      gson.getProjectsArray(settings.projectsFile).firstOrNull { it.owner == owner }
    }
    if (ownersProjects == null) {
      println("Owner ${cli.getOptionValue("owner")} doesn't exists, check config with --interactive")
      return
    }

    // map project with time
    val projectMap = HashMap<String, BigDecimal>()
    gson.getProjects(cli.getOptionValue("from-project-json")).also { map ->
      if (map == null) {
        println("Project json is malformed, can't be parsed")
        return
      }
      map.data.forEach { projectMap.put(it.project!!, it.minutesSpent!!.toBigDecimal()) }
    }

    // get client for each project
    val clientMap = HashMap<String, BigDecimal>()
      .apply {
        projectMap.keys.forEach {
          val index = ownersProjects.projects.indexOf(it)
          if (index >= 0) {
            val client = ownersProjects.clients[index]
            if (containsKey(client)) {
              this[client] = this[client]!!.plus(projectMap[it]!!)
            } else {
              put(ownersProjects.clients[index], projectMap[it]!!)
            }
          } else {
            println("Project $it not found, skipping")
          }
        }
      }
      // get client for each name
      .let { it.mapKeys { entry -> gson.getClient(settings.clientsDir.listFiles().first { it.nameWithoutExtension == entry.key }) } }
      // convert to hours
      .let { it.mapValues { it.value.divide(60.toBigDecimal()) } }

    val owner = gson.fromJson(settings.ownersDir.listFiles().first { it.nameWithoutExtension == cli.getOptionValue("owner") }.reader(), Owner::class.java)


    InvoiceGenerator(settings).apply {
      generate(owner, clientMap)
    }
  }

  private fun validateInput() {
    if (!cli.hasOption("owner")) {
      println("ERROR: -owner <arg> is required")
      System.exit(-1)
    }
  }
}