package model

import com.google.gson.Gson
import interactive.MenuProjectsAPI.gson
import java.io.File
import java.math.BigDecimal

data class Client(
	var id: String? = null,
	var type: ClientType = ClientType.PL,
	var name: String? = null,
	var address: Address = Address(),
	var nip: String? = null,
	var hourlyRate: BigDecimal? = null,
	var paymentDelay: Int? = null,
	var template: String? = null,
	var currency: Currency = Currency.PLN,
	var dateDayType: InvoiceDate = InvoiceDate.LAST
)

fun Gson.getClient(it: File) = gson.fromJson(it.reader(), Client::class.java)