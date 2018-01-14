package interactive

import model.Address
import utils.newRangeInputReader

object MenuAddress: BaseMenu() {
	fun shortDescription(address: Address?) =
		"${address?.street ?: ""} ${address?.houseNumber ?: ""} ${address?.city ?: ""}"
			.let { if (it.length > 60) it.substring(0..60) + "..." else it }

	fun showAddress(app: AppInteractive, address: Address) = app.console.apply {
		main@ while (true) {
			app.clearScreen()
			var index = 1
			address.apply {
				showValue(textTerminal, index++, "Ulica", street)
				showValue(textTerminal, index++, "Numer domu", houseNumber)
				showValue(textTerminal, index++, "Numer mieszkania", flatNumber)
				showValue(textTerminal, index++, "Województwo", province)
				showValue(textTerminal, index++, "Powiat", district)
				showValue(textTerminal, index++, "Gmina", commune)
				showValue(textTerminal, index++, "Miejscowość", city)
				showValue(textTerminal, index++, "Kod pocztowy", postalCode)
			}
			showBack(textTerminal, index++)
			val selection = newRangeInputReader(1..(index - 1))
				.read(BaseMenu.selectOption)
			when (selection) {
				(index - 1) -> break@main
				else -> edit(app, selection, address)
			}
		}
	}

	private fun edit(app: AppInteractive, index: Int, address: Address) = app.console.apply {
		when (index) {
			1 -> address.street = newStringInputReader().read("Ulica")
			2 -> address.houseNumber = newIntInputReader().read("Numer domu")
			3 -> address.flatNumber = newIntInputReader().read("Numer mieszkania")
			4 -> address.province = newStringInputReader().read("Województwo")
			5 -> address.district = newStringInputReader().read("Powiat")
			6 -> address.commune = newStringInputReader().read("Gmina")
			7 -> address.city = newStringInputReader().read("Miejscowość")
			8 -> address.postalCode = newStringInputReader().read("Kod pocztowy")
		}
	}
}