package pl.gumyns.faktura.api.settings

import java.io.File

//region directory
typealias Directory = File

val Directory.jsonFiles
  get() = listFiles().filter { it.extension == "json" }

val Directory.odtFiles
  get() = listFiles().filter { it.extension == "odt" }

//endregion

//region ClientsDir
class ClientsDir(directory: Directory) : Directory(directory, "clients") {
  val clientList
    get() = jsonFiles.map { it.nameWithoutExtension }

  fun find(name: String) = jsonFiles.find { it.nameWithoutExtension == name }
}
//endregion

//region OwnersDir
class OwnersDir(directory: Directory) : Directory(directory, "owners") {
  val ownerList
    get() = jsonFiles.map { it.nameWithoutExtension }

  fun find(name: String) = jsonFiles.find { it.nameWithoutExtension == name }
}
//endregion

//region TemplatesDir
class TemplatesDir(directory: Directory) : Directory(directory, "templates") {
  val templateList
    get() = odtFiles.map { it.nameWithoutExtension }

  fun find(name: String) = odtFiles.find { it.nameWithoutExtension == name }
}
//endregion

//region ProductsDir
class ProductsDir(directory: Directory) : Directory(directory, "products") {
  val productList
    get() = jsonFiles.map { it.nameWithoutExtension }

  fun find(name: String) = jsonFiles.find { it.nameWithoutExtension == name }
}
//endregion
