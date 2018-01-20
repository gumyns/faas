package pl.gumyns.fakturka.toptotal

class Main {
  companion object {
    @JvmStatic fun main(args: Array<String>) {
      if (args.size != 1) {
        println("GTFO, no files")
        return
      }
      val settings = ToptotalSettingsManager()
      args.forEach { println(Generator.generate(settings, it)) }
    }
  }
}
