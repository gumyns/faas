# coding=utf-8
import datetime
import json
import xml.dom.minidom
import xml.etree.ElementTree as ET

from models import Json


class Attr:
    """
        XML Attribute object
    """

    def __init__(self, name, value):
        self.name = name
        self.value = value

    def __call__(self, *args):
        args[0].set(self.name, self.value)


class Elem:
    """
        XML Element object
    """

    def __init__(self, name, value=None, attr=None):
        self.name = name
        self.value = value
        self.attr = attr

    def __call__(self, *args):
        if len(args) == 0:
            node = ET.Element(self.name)
        else:
            node = ET.SubElement(args[0], self.name)

        if self.value is not None:
            if isinstance(self.value, list):
                for value in self.value:
                    value(node)
            elif isinstance(self.value, basestring):
                node.text = self.value
            else:
                self.value(node)

        if isinstance(self.attr, list):
            for param in self.attr:
                param(node)
        if len(args) == 0:
            return node
        return args[0]


class Serializable:
    """
        Serializable helper, callable converts class members to Element object
    """

    def __init__(self, name):
        self.name = name

    def __call__(self):
        variables = vars(self)
        values = []
        attr = []
        for var in variables:
            if isinstance(variables[var], Elem):
                values.append(variables[var])
            elif isinstance(variables[var], Attr):
                attr.append(variables[var])
        return Elem(self.name, attr, values)


class TNaglowek(Serializable):
    """
        JPK header definition
    """

    def __init__(self, owner):
        Serializable.__init__(self, "tns:Naglowek")
        self.code = Elem("tns:KodFormularza", "JPK_VAT",
                         [Attr("kodSystemowy", "JPK_VAT (2)"), Attr("wersjaSchemy", "1-0")])
        self.variant = Elem("tns:WariantFormularza", "2")
        self.target = Elem("tns:CelZlozenia", "1")
        self.create_date = Elem("tns:DataWytworzeniaJPK", datetime.datetime.now().strftime("%Y-%m-%dT%H:%M:%S"))
        self.from_date = Elem("tns:DataOd", "2017-01-01")
        self.to_date = Elem("tns:DataDo", "2017-01-31")
        self.currency = Elem("tns:DomyslnyKodWaluty", "PLN")
        self.code = Elem("tns:KodUrzedu", owner.gov_code)


class IdentyfikatorPodmiotu(Serializable):
    def __init__(self, owner):
        Serializable.__init__(self, "tns:IdentyfikatorPodmiotu")
        self.nip = Elem("etd:NIP", owner.nip)
        self.full_name = Elem("etd:PelnaNazwa", owner.name)
        self.regon = Elem("etd:REGON", "2")  # TODO


class AdresPodmiotu(Serializable):
    def __init__(self, address):
        Serializable.__init__(self, "tns:AdresPodmiotu")
        self.country_code = Elem("tns:KodKraju", "PL")
        self.woj = Elem("tns:Wojewodztwo", address.province.upper())
        self.powiat = Elem("tns:Powiat", address.district.upper())
        self.gmina = Elem("tns:Gmina", address.commune.upper())
        self.ul = Elem("tns:Ulica", address.street.upper())
        self.nr_dom = Elem("tns:NrDomu", address.house_number)
        self.nr_lok = Elem("tns:NrLokalu", address.flat_number)
        self.city = Elem("tns:Miejscowosc", address.city.upper())
        self.postal = Elem("tns:KodPocztowy", address.postal_code)
        self.post = Elem("tns:Poczta", address.city.upper())


class Podmiot1(Serializable):
    def __init__(self, owner):
        Serializable.__init__(self, "tns:Podmiot1")
        self.id = IdentyfikatorPodmiotu(owner)()
        self.addr = AdresPodmiotu(owner.address)()


class SprzedazWiersz(Serializable):
    def __init__(self, invoice):
        Serializable.__init__(self, "tns:SprzedazWiersz")
        self.typ = Attr("typ", "G")  # whatever :)
        self.LpSprzedazy = Elem("tns:LpSprzedazy")
        self.NrKontrahenta = Elem("tns:NrKontrahenta")
        self.NazwaKontrahenta = Elem("tns:NazwaKontrahenta")
        self.AdresKontrahenta = Elem("tns:AdresKontrahenta")
        self.DowodSprzedazy = Elem("tns:DowodSprzedazy")
        self.DataWystawienia = Elem("tns:DataWystawienia")
        self.DataSprzedazy = Elem("tns:DataSprzedazy")
        # Kwota netto - Dostawa towarów oraz świadczenie usług na terytorium kraju, zwolnione od podatku
        self.K_10 = Elem("tns:K_10", str(0))
        # Kwota netto - Dostawa towarów oraz świadczenie usług poza terytorium kraju
        self.K_11 = Elem("tns:K_11", str(0))
        # Kwota netto - w tym świadczenie usług, o których mowa w art. 100 ust. 1 pkt 4 ustawy
        self.K_12 = Elem("tns:K_12", str(0))
        # Kwota netto - Dostawa towarów oraz świadczenie usług na terytorium kraju, opodatkowane stawką 0%
        self.K_13 = Elem("tns:K_13", str(0))
        # Kwota netto - w tym dostawa towarów, o której mowa w art. 129 ustawy
        self.K_14 = Elem("tns:K_14", str(0))
        # Kwota netto - Dostawa towarów oraz świadczenie usług na terytorium kraju, opodatkowane stawką 5%
        self.K_15 = Elem("tns:K_15", str(0))
        # Kwota podatku należnego - Dostawa towarów oraz świadczenie usług na terytorium kraju, opodatkowane stawką 5%
        self.K_16 = Elem("tns:K_16", str(0))
        # Kwota netto - Dostawa towarów oraz świadczenie usług na terytorium kraju, opodatkowane stawką 7% albo 8%
        self.K_17 = Elem("tns:K_17", str(0))
        # Kwota podatku należnego - Dostawa towarów oraz świadczenie usług na terytorium kraju, opodatkowane stawką 7% albo 8%
        self.K_18 = Elem("tns:K_18", str(0))
        # Kwota netto - Dostawa towarów oraz świadczenie usług na terytorium kraju, opodatkowane stawką 22% albo 23%
        self.K_19 = Elem("tns:K_19", str(0))
        # Kwota podatku należnego - Dostawa towarów oraz świadczenie usług na terytorium kraju, opodatkowane stawką 22% albo 23%
        self.K_20 = Elem("tns:K_20", str(0))
        # Kwota netto - Wewnątrzwspólnotowa dostawa towarów
        self.K_21 = Elem("tns:K_21", str(0))
        # Kwota netto - Eksport towarów
        self.K_22 = Elem("tns:K_22", str(0))
        # Kwota netto - Wewnątrzwspólnotowe nabycie towarów
        self.K_23 = Elem("tns:K_23", str(0))
        # Kwota podatku należnego - Wewnątrzwspólnotowe nabycie towarów
        self.K_24 = Elem("tns:K_24", str(0))
        # Kwota netto - Import towarów podlegający rozliczeniu zgodnie z art. 33a ustawy
        self.K_25 = Elem("tns:K_25", str(0))
        # Kwota podatku należnego - Import towarów podlegający rozliczeniu zgodnie z art. 33a ustawy
        self.K_26 = Elem("tns:K_26", str(0))
        # Kwota netto - Import usług z wyłączeniem usług nabywanych od podatników podatku od wartości dodanej, do których stosuje sięart. 28b ustawy
        self.K_27 = Elem("tns:K_27", str(0))
        # Kwota podatku należnego - Import usług z wyłączeniem usług nabywanych od podatników podatku od wartości dodanej, do których stosuje się art. 28b ustawy
        self.K_28 = Elem("tns:K_28", str(0))
        # Kwota netto - Import usług nabywanych od podatników podatku od wartości dodanej, do których stosuje się art. 28b ustawy
        self.K_29 = Elem("tns:K_29", str(0))
        # Kwota podatku należnego - Import usług nabywanych od podatników podatku od wartości dodanej, do których stosuje się art. 28b ustawy
        self.K_30 = Elem("tns:K_30", str(0))
        # Kwota netto - Dostawa towarów oraz świadczenie usług, dla których podatnikiem jest nabywca zgodnie z art. 17 ust. 1 pkt 7 lub 8 ustawy (wypełnia dostawca)
        self.K_31 = Elem("tns:K_31", str(0))
        # Kwota netto - Dostawa towarów, dla których podatnikiem jest nabywca zgodnie z art. 17 ust. 1 pkt 5 ustawy (wypełnia nabywca)
        self.K_32 = Elem("tns:K_32", str(0))
        # Kwota podatku należnego - Dostawa towarów, dla których podatnikiem jest nabywca zgodnie z art. 17 ust. 1 pkt 5 ustawy (wypełnia nabywca)
        self.K_33 = Elem("tns:K_33", str(0))
        # Kwota netto - Dostawa towarów oraz świadczenie usług, dla których podatnikiem jest nabywca zgodnie z art. 17 ust. 1 pkt 7 lub 8 ustawy (wypełnia nabywca)
        self.K_34 = Elem("tns:K_34", str(0))
        # Kwota podatku należnego - Dostawa towarów oraz świadczenie usług, dla których podatnikiem jest nabywca zgodnie z art. 17 ust. 1 pkt 7 lub 8 ustawy (wypełnia nabywca)
        self.K_35 = Elem("tns:K_35", str(0))
        # Kwota podatku należnego od towarów i usług objętych spisem z natury, o którym mowa w art. 14 ust. 5 ustawy
        self.K_36 = Elem("tns:K_36", str(0))
        # Zwrot odliczonej lub zwróconej kwoty wydatkowanej na zakup kas rejestrujących, o którym mowa w art. 111 ust. 6 ustawy
        self.K_37 = Elem("tns:K_37", str(0))
        # Kwota podatku należnego od wewnątrzwspólnotowego nabycia środków transportu, wykazanego welemencie K_24, podlegająca wpłacie w terminie, o którym mowa w art. 103 ust. 3, w związku z ust. 4 ustawy
        self.K_38 = Elem("tns:K_38", str(0))
        # Kwota podatku od wewnątrzwspólnotowego nabycia paliw silnikowych, podlegająca wpłacie w terminach, o których mowa w art. 103 ust. 5a i 5b ustawy
        self.K_39 = Elem("tns:K_39", str(0))


class SprzedazCtrl(Serializable):
    def __init__(self, invoices):
        Serializable.__init__(self, "tns:SprzedazCtrl")
        self.rows = Elem("tns:LiczbaWierszySprzedazy", str(len(invoices)))
        self.tax = Elem("tns:PodatekNalezny", str(0))


class JPK(Serializable):
    """
        JPK xml definition
    """
    def __init__(self, owner, invoices):
        Serializable.__init__(self, "tns:JPK")
        self.etd = Attr("xmlns:etd", "http://crd.gov.pl/xml/schematy/dziedzinowe/mf/2016/01/25/eD/DefinicjeTypy/")
        self.kck = Attr("xmlns:kck", "http://crd.gov.pl/xml/schematy/dziedzinowe/mf/2013/05/23/eD/KodyCECHKRAJOW/")
        self.tns = Attr("xmlns:tns", "http://jpk.mf.gov.pl/wzor/2016/10/26/10261/")
        self.xsi = Attr("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance")
        self.header = TNaglowek(owner)()
        self.podmiot1 = Podmiot1(owner)()
        d = dict()
        for i, invoice in enumerate(invoices): d["invoice{}".format(i)] = SprzedazWiersz(invoice)()
        vars(self).update(d)
        self.sale_ctrl = SprzedazCtrl(invoices)()


###################################################################################################

with open("owners/theon.json") as new_file:
    owner = json.loads(new_file.read().decode('utf-8'), object_hook=Json)

jpk = JPK(owner, [None])()()

file_name = "output/jpk.xml"
ET.ElementTree(jpk).write(file_name, encoding='utf-8', xml_declaration=True)

parsed = xml.dom.minidom.parse(file_name)
print(parsed.toprettyxml())
with open(file_name, 'w') as new_file:
    new_file.write(parsed.toprettyxml().encode("utf-8"))
