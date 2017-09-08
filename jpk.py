import os
import xml.dom.minidom
import xml.etree.ElementTree as ET

from models import Owner


# http://www.mf.gov.pl/documents/764034/5134536/Schemat_JPK_VAT%282%29_v1-0.pdf

def generate(owner):
    if not os.path.exists('output'):
        os.makedirs('output')

    jpk = ET.Element("tns:JPK")
    jpk.set("xmlns:etd", "http://crd.gov.pl/xml/schematy/dziedzinowe/mf/2016/01/25/eD/DefinicjeTypy/")
    jpk.set("xmlns:kck", "http://crd.gov.pl/xml/schematy/dziedzinowe/mf/2013/05/23/eD/KodyCECHKRAJOW/")
    jpk.set("xmlns:tns", "http://jpk.mf.gov.pl/wzor/2016/10/26/10261/")
    jpk.set("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance")

    header = ET.SubElement(jpk, "tns:Naglowek")
    _header = ET.SubElement(header, "tns:KodFormularza")
    _header.set("kodSystemowy", "JPK_VAT (2)")
    _header.set("wersjaSchemy", "1-0")
    _header.text = "JPK_VAT"
    ET.SubElement(header, "tns:WariantFormularza").text = "2"
    ET.SubElement(header, "tns:CelZlozenia").text = "1"
    ET.SubElement(header, "tns:DataWytworzeniaJPK").text = "2017-02-17T09:30:47"
    ET.SubElement(header, "tns:DataOd").text = "2017-01-01"
    ET.SubElement(header, "tns:DataDo").text = "2017-01-31"
    ET.SubElement(header, "tns:DomyslnyKodWaluty").text = "PLN"
    ET.SubElement(header, "tns:KodUrzedu").text = "0202"

    firm = ET.SubElement(jpk, "tns:Podmiot1")
    Owner.id_xml(owner, ET.SubElement(firm, "tns:IdentyfikatorPodmiotu"))
    Owner.address_xml(owner, ET.SubElement(firm, "tns:AdresPodmiotu"))

    file_name = "output/jpk.xml"
    ET.ElementTree(jpk).write(file_name, encoding='utf-8', xml_declaration=True)

    parsed = xml.dom.minidom.parse(file_name)
    with open(file_name, 'w') as new_file:
        new_file.write(parsed.toprettyxml())
