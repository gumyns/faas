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
    def __init__(self):
        Serializable.__init__(self, "tns:Naglowek")
        self.code = Elem("tns:KodFormularza", "JPK_VAT",
                         [Attr("kodSystemowy", "JPK_VAT (2)"), Attr("wersjaSchemy", "1-0")])
        self.variant = Elem("tns:WariantFormularza", "2")
        self.target = Elem("tns:CelZlozenia", "1")
        self.create_date = Elem("tns:DataWytworzeniaJPK", "2017-02-17T09:30:47")
        self.from_date = Elem("tns:DataOd", "2017-01-01")
        self.to_date = Elem("tns:DataDo", "2017-01-31")
        self.currency = Elem("tns:DomyslnyKodWaluty", "PLN")
        self.code = Elem("tns:KodUrzedu", "0202")


class IdentyfikatorPodmiotu(Serializable):
    def __init__(self, owner):
        Serializable.__init__(self, "tns:IdentyfikatorPodmiotu")
        self.nip = Elem("etd:NIP", owner.nip)
        self.full_name = Elem("etd:PelnaNazwa", owner.name)
        self.regon = Elem("etd:REGON", "2")  # TODO


class AdresPodmiotu(Serializable):
    def __init__(self, owner):
        Serializable.__init__(self, "tns:AdresPodmiotu")
        self.country_code = Elem("tns:KodKraju", "PL")
        self.woj = Elem("tns:Wojewodztwo", "2")  # TODO
        self.powiat = Elem("tns:Powiat", "2")  # TODO
        self.gmina = Elem("tns:Gmina", "2")  # TODO
        self.ul = Elem("tns:Ulica", "2")  # TODO
        self.nr_dom = Elem("tns:NrDomu", "2")  # TODO
        self.nr_lok = Elem("tns:NrLokalu", "2")  # TODO
        self.city = Elem("tns:Miejscowosc", "2")  # TODO
        self.postal = Elem("tns:KodPocztowy", "2")  # TODO
        self.post = Elem("tns:Poczta", "2")  # TODO


class Podmiot1(Serializable):
    def __init__(self, owner):
        Serializable.__init__(self, "tns:Podmiot1")
        self.id = IdentyfikatorPodmiotu(owner)()
        self.addr = AdresPodmiotu(owner)()


class JPK(Serializable):
    """
        JPK xml definition
    """

    def __init__(self, owner):
        Serializable.__init__(self, "tns:JPK")
        self.etd = Attr("xmlns:etd", "http://crd.gov.pl/xml/schematy/dziedzinowe/mf/2016/01/25/eD/DefinicjeTypy/")
        self.kck = Attr("xmlns:kck", "http://crd.gov.pl/xml/schematy/dziedzinowe/mf/2013/05/23/eD/KodyCECHKRAJOW/")
        self.tns = Attr("xmlns:tns", "http://jpk.mf.gov.pl/wzor/2016/10/26/10261/")
        self.xsi = Attr("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance")
        self.header = TNaglowek()()
        self.podmiot1 = Podmiot1(owner)()


###################################################################################################


var = Elem("tns:IdentyfikatorPodmiotu", [Attr("a", "a")], "123")
var2 = Elem("tns:Identyfikator", value=var)

with open("owners/theon.json") as new_file:
    owner = json.loads(new_file.read().decode('utf-8'), object_hook=Json)

jpk = JPK(owner)()()

file_name = "output/jpk.xml"
ET.ElementTree(jpk).write(file_name, encoding='utf-8', xml_declaration=True)

parsed = xml.dom.minidom.parse(file_name)
print(parsed.toprettyxml())
