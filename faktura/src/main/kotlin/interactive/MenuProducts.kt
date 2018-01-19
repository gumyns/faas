package interactive

import com.google.gson.Gson
import generated.CurrCodeType
import model.TaxRate
import pl.gumyns.faktura.api.product.Product
import pl.gumyns.faktura.api.settings.SettingsManager
import pl.gumyns.faktura.api.settings.jsonFiles
import utils.newBigDecimalInputReader
import utils.newRangeInputReader
import java.io.File

object MenuProducts : BaseMenu() {
  private val gson = Gson()

  fun showProducts(app: AppInteractive): Unit = app.console.run {
    main@ while (true) {
      app.clearScreen()
      textTerminal.println("Lista produktów:")
      var index = 1

      SettingsManager().productsDir.productList.forEach {
        textTerminal.println("${index++}. $it")
      }
      textTerminal.println("${index++}. Nowy produkt")
      showBack(textTerminal, index++)

      val files = SettingsManager().productsDir.jsonFiles
      val selection = newRangeInputReader(1..(index - 1))
        .read(BaseMenu.selectOption)
      when (selection) {
        (index - 2) -> showProduct(app, Product(), true)
        (index - 1) -> break@main
        else -> showProduct(app, gson.fromJson(files[selection - 1].reader(), Product::class.java))
      }
    }
  }

  private fun showProduct(app: AppInteractive, inProduct: Product, isNew: Boolean = false): Unit = app.console.run {
    var product = inProduct
    if (isNew) {
      var id: String? = null
      while (id == null || id.isEmpty()) {
        app.clearScreen()
        id = newStringInputReader().read("Krótka nazwa firmy (ID w systemie)")
        val file = SettingsManager().productsDir.find(id)
        if (file != null) {
          product = gson.fromJson(file.reader(), Product::class.java)
        } else {
          product.id = id
        }
      }
    }
    main@ while (true) {
      app.clearScreen()
      textTerminal.println("Pola oznaczone * są opcjonalne")
      var index = 1
      product.apply {
        showValue(textTerminal, index++, "ID", id)
        showValue(textTerminal, index++, "Nazwa produktu", name)
        showValue(textTerminal, index++, "Cena jednostkowa", price)
        showValue(textTerminal, index++, "Podatek", "%d%%".format(taxRate?.multiply(100.toBigDecimal())?.toInt()))
        showValue(textTerminal, index++, "Waluta", currency)
      }
      showSave(textTerminal, index++)
      showRemove(textTerminal, index++)
      showBack(textTerminal, index++)
      val selection = newRangeInputReader(1..(index - 1))
        .read(BaseMenu.selectOption)
      when (selection) {
        (index - 3) -> saveOwner(product)
        (index - 2) -> removeOwner(product)
        (index - 1) -> break@main
        else -> edit(app, selection, product)
      }
    }
  }

  private fun saveOwner(product: Product) =
    File(SettingsManager().productsDir, "${product.id}.json").writeText(gson.toJson(product))

  private fun removeOwner(product: Product) =
    File(SettingsManager().productsDir, "${product.id}.json").delete()

  private fun edit(app: AppInteractive, index: Int, product: Product) = app.console.apply {
    when (index) {
      1 -> product.id = newStringInputReader().read("Krótka nazwa produktu (ID w systemie)")
      2 -> product.name = newStringInputReader().read("Nazwa produktu")
      3 -> product.price = newBigDecimalInputReader().read("Cena jednostkowa")
      4 -> product.taxRate = newEnumInputReader(TaxRate::class.java).read("Stawka podatku").rate
      5 -> product.currency = newEnumInputReader(CurrCodeType::class.java).read("Waluta")
    }
  }
}