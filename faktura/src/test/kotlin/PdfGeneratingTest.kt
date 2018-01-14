import com.google.gson.Gson
import fr.opensagres.xdocreport.converter.ConverterRegistry
import fr.opensagres.xdocreport.converter.ConverterTypeTo
import fr.opensagres.xdocreport.converter.ConverterTypeVia
import fr.opensagres.xdocreport.converter.Options
import fr.opensagres.xdocreport.core.document.DocumentKind
import internal.PdfGenerator
import internal.SettingsManager
import junit.framework.Assert.assertEquals
import model.Invoice
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.io.File
import java.io.FileOutputStream


class PdfGeneratingTest : Spek({
	describe("generate pdf") {
		it("work motherfucker...") {
			val options = Options.getFrom(DocumentKind.ODT).to(ConverterTypeTo.PDF).via(ConverterTypeVia.ODFDOM)
			val converter = ConverterRegistry.getRegistry().getConverter(options)
			val `in` = javaClass.classLoader.getResourceAsStream("templates/Faktura.odt")
			File("build/test-output/").mkdirs()
			val out = FileOutputStream(File("build/test-output/ODTHelloWord2PDF.pdf"))
			converter.convert(`in`, out, options)

			assertEquals(29, 29)
		}
	}
	describe("generate pdf from invoice json") {
		it("generates pdf from json using odt template") {
			val json = """{
	"owner": {
		"id": "ay",
		"name": "gumyns",
		"address": {
			"street": "Lol",
			"houseNumber": 23,
			"province": "ŚLĄSKIE",
			"district": "GLIWICE",
			"commune": "GLIWICE",
			"city": "GLIWICE",
			"postalCode": "12-123"
		},
		"nip": "1234",
		"vatid": "PL1234",
		"regon": "12356123",
		"accounts": [
			{
				"currency": "PLN",
				"bankName": "mBank",
				"number": "1123",
				"transfer": "Przelew"
			},
			{
				"currency": "EUR",
				"bankName": "mBank",
				"number": "123415",
				"swift": "12341",
				"transfer": "Przelew"
			}
		],
		"annualNumber": "YEARLY"
	},
	"client": {
		"id": "ya",
		"name": "Enasd",
		"address": {
			"street": "asd",
			"houseNumber": 12,
			"flatNumber": 1,
			"city": "Zadupie",
			"postalCode": "12312"
		},
		"nip": "123",
		"hourlyRate": 25,
		"paymentDelay": 14,
		"template": "FakturaUE-EN",
		"currency": "EUR",
		"dateDayType": "LAST"
	},
	"amount": 103,
	"date": "Dec 29, 2017 12:00:00 AM",
	"name": "usługa informatyczna",
	"netPrice": 2575,
	"grossPrice": 2575,
	"taxPrice": 0,
	"dueDate": "Jan 12, 2018 12:00:00 AM",
	"number": "3/2017",
	"filename": "20171229_ay_ya_3_2017"
}""".trimIndent()
			val invoice = Gson().fromJson(json, Invoice::class.java)
			val manager = SettingsManager()
			manager.copyNeededFiles()
			PdfGenerator(manager).generate(invoice)
		}
	}
})

