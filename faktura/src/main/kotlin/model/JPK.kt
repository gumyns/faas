package model

import generated.*
import pl.gumyns.faktura.api.product.ProductEntry
import java.text.DecimalFormat
import java.util.*
import javax.xml.datatype.DatatypeFactory

fun JPK.Naglowek.initialize(owner: Owner, currency: CurrCodeType, from: Date, to: Date = Date(System.currentTimeMillis())) = apply {
  wariantFormularza = 1
  celZlozenia = 1
  DatatypeFactory.newInstance().also {
    dataWytworzeniaJPK = it.newXMLGregorianCalendar(GregorianCalendar().apply { time = Date(System.currentTimeMillis()) })
    dataOd = it.newXMLGregorianCalendar(GregorianCalendar().apply { time = from })
    dataDo = it.newXMLGregorianCalendar(GregorianCalendar().apply { time = to })
  }
  domyslnyKodWaluty = currency
  kodUrzedu = owner.govCode
}

fun JPK.Podmiot1.initialize(owner: Owner) = apply {
  identyfikatorPodmiotu = TIdentyfikatorOsobyNiefizycznej().apply {
    nip = owner.nip
    pelnaNazwa = owner.name
    regon = owner.regon
  }
  adresPodmiotu = TAdresPolski().apply {
    kodKraju = TKodKraju.PL
    wojewodztwo = owner.address.province
    powiat = owner.address.district
    gmina = owner.address.commune
    ulica = owner.address.street
    nrDomu = owner.address.houseNumber?.toString()
    nrLokalu = owner.address.flatNumber?.toString()
    miejscowosc = owner.address.city
    kodPocztowy = owner.address.postalCode
    poczta = owner.address.city // TODO wrong
  }
}

fun JPK.Faktura.initialize(invoice: Invoice) = apply {
  typ = "G"

  val UE = invoice.client.type == ClientType.UE
  DatatypeFactory.newInstance().also {
    p1 = it.newXMLGregorianCalendar(GregorianCalendar().apply { time = invoice.date })
//		p6 = it.newXMLGregorianCalendar(GregorianCalendar().apply { time = invoice.date }) // TODO opcjonalna data otrzymania hajsu, wtf?
  }
  p2A = invoice.number?.split("/")!![0]
  p3A = invoice.client.name
  p3B = invoice.client.address.formatAddressJPK()
  p3C = invoice.owner.name
  p3D = invoice.owner.address.formatAddressJPK()
  if (UE) {
    p4A = MSCountryCodeType.PL // owner code
    p5A = MSCountryCodeType.valueOf(invoice.client.nip!!.substring(0..1))
    p5B = invoice.client.nip?.substring(2)
  } else {
    p5B = invoice.client.nip
  }
  p4B = invoice.owner.nip

  isP16 = false // metoda kasowa
  isP17 = false // samofakturowanie
  isP18 = UE // reverse charge
  isP19 = false //usług zwolnionych od podatku
  isP20 = false // komornik?
  isP21 = false // przedstawicielstwo
  isP23 = false
  if (UE) {
    p135 = invoice.netPrice
    p145 = 0.toBigDecimal()
    p15 = invoice.netPrice
  } else { // PL
    p131 = invoice.netPrice
    p141 = invoice.taxPrice
  }
//	p136
//	p137
//	p15

  isP106E2 = false
  isP106E3 = false
  rodzajFaktury = "VAT"
//	zalZaplata
//	zalPodatek
}

fun JPK.FakturaCtrl.initialize(invoices: List<Invoice>) = apply {
  liczbaFaktur = invoices.size.toBigInteger()
  wartoscFaktur = invoices.map { it.grossPrice!! }
    .reduce { acc, next -> acc + next }
}

fun JPK.FakturaWiersz.initialize(invoice: Invoice, product: ProductEntry) = apply {
  typ = "G"
  // Kolejny numer faktury, nadany w ramach jednej lub więcej serii, który w sposób jednoznaczny indentyfikuje fakturę
  p2B = invoice.number!!.split("/")[0] // TODO this is weird...
  // Nazwa (rodzaj) towaru lub usługi. Pole opcjonalne wyłącznie dla przypadku określonego w art 106j ust.3 pkt 2 ustawy (faktura korekta)
  p7 = product.name
  // Miara dostarczonych towarów lub zakres wykonanych usług. Pole opcjonalne dla przypadku określonego w	art 106e ust. 5 pkt 3 ustawy.
  p8A = when (invoice.client.productType) {
    ProductType.TOTAL -> "szt"
    else -> "h"
  }
  // Ilość (liczba) dostarczonych towarów lub zakres wykonanych usług.	Pole opcjonalne dla przypadku określonego w art 106e ust. 5 pkt 3 ustawy.
  p8B = when (invoice.client.productType) {
    ProductType.TOTAL -> 1.toBigDecimal()
    else -> product.amount
  }
  // Cena jednostkowa towaru lub usługi bez kwoty podatku (cena jednostkowa netto). Pole opcjonalne dla przypadków określonych
  // w art. 106e ust.2 i 3 ustawy (gdy	przynajmniej jedno z pól P_106E_2 i P_106E_3 przyjmuje wartość "true")
  // oraz dla przypadku określonego w art 106e ust. 5 pkt 3 ustawy
  p9A = when (invoice.client.productType) {
    ProductType.TOTAL -> invoice.netPrice
    else -> invoice.client.hourlyRate
  }
  // W	przypadku zastosowania art.106e ustawy, cena wraz z kwotą	podatku (cena jednostkowa brutto)
  p9B = invoice.grossPrice
  // Kwoty wszelkich opustów lub obniżek cen, w tym w formie rabatu z tytułu wcześniejszej zapłaty, o ile nie zostały
  // one uwzględnione w cenie jednostkowej netto. Pole opcjonalne dla przypadków określonych w art. 106e ust.2 i 3 ustawy
  // (gdy przynajmniej jedno z pól	P_106E_2 i P_106E_3 przyjmuje wartość "true")
  // oraz dla przypadku określonego w art. 106e ust. 5	pkt 1 ustawy.
  p10 = null
  // Wartość dostarczonych towarów lub wykonanych usług, objętych transakcją, bez kwoty podatku (wartość sprzedaży netto).
  // Pole opcjonalne dla przypadków	określonych w art. 106e ust.2 i 3 ustawy (gdy przynajmniej jedno z pól P_106E_2 i P_106E_3 przyjmuje wartość "true")
  // oraz dla przypadku określonego w art. 106e ust. 5 pkt 3 ustawy.
  p11 = invoice.netPrice
  // W przypadku zastosowania art. 106e ust.7 i 8 ustawy, wartość sprzedaży brutto
  p11A = invoice.grossPrice
  // Stawka podatku. Pole opcjonalne dla przypadków określonych w art.	106e ust.2 i 3 ustawy
  // (gdy przynajmniej jedno z pól P_106E_2 i P_106E_3 przyjmuje wartość	"true"),
  // a także art. 106e ust.4 pkt 3 i ust. 5 pkt 1 -	3 ustawy.
  p12 = if (invoice.client.type == ClientType.UE) "0" else DecimalFormat("0.00").format(product.taxRate)
}

fun JPK.FakturaWierszCtrl.initialize(invoices: List<Invoice>) = apply {
  liczbaWierszyFaktur = invoices.map { it.products.size.toBigInteger() }
    .reduce { acc, i -> acc + i }
  wartoscWierszyFaktur = invoices.map { it.grossPrice!! }
    .reduce { acc, next -> acc + next }
}

fun JPK.StawkiPodatku.initialize() = apply {
  stawka1 = 0.23.toBigDecimal()
  stawka2 = 0.08.toBigDecimal()
  stawka3 = 0.05.toBigDecimal()
  stawka4 = 0.00.toBigDecimal()
  stawka5 = 0.00.toBigDecimal()
}