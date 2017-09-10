# coding=utf-8
# !/bin/bash python

import argparse
import json
import os
import shutil
import subprocess
import sys

import jpk
from utils.models import Json, Invoice


# region lib
def ensure_path_existence(path):
    if not os.path.exists(path):
        os.makedirs(path)


# endregion lib


# region script
parser = argparse.ArgumentParser()
parser.add_argument("owner", help="The seller of products issued on the invoice.", type=str)
parser.add_argument("client", help="The receiver of the products issued on the invoice.", type=str)
parser.add_argument("amount", help="The amount of the service in invoice.", type=str)
parser.add_argument("--delivery", help="The company's that is receiving the delivery of the invoice.", type=str)
arguments = parser.parse_args()

owner = None
client = None

with open("owners/{}.json".format(arguments.owner), 'r') as new_file:
    owner = json.loads(new_file.read().decode('utf-8'), object_hook=Json)

with open("clients/{}.json".format(arguments.client), 'r') as new_file:
    client = json.loads(new_file.read().decode('utf-8'), object_hook=Json)

invoice = Invoice(owner, client, arguments.amount, None)

jpk.generate(owner)

with open("templates/{}.html".format(client.template), 'r') as htmlInput:
    ensure_path_existence('output')
    json_file_path = 'output/{}/{}/json'.format(owner.name.replace(' ', '_'), str(invoice.date.year))
    ensure_path_existence(json_file_path)
    pdf_file_path = 'output/{}/{}/pdf'.format(owner.name.replace(' ', '_'), str(invoice.date.year))
    ensure_path_existence(pdf_file_path)

    html = htmlInput.read().decode('utf-8').format(**invoice.asFormatter())

    shutil.copyfile('templates/pdf.css', 'output/pdf.css')
    with open('output/fakturka.html', 'w') as htmlOutput:
        htmlOutput.write(html.encode('utf-8'))

    print os.path.dirname(os.path.abspath(sys.argv[0]))

    subprocess.call('wkhtmltopdf fakturka.html "{}/{}.pdf"'.format(pdf_file_path.replace('output/', ''),
                                                                   invoice.filename.encode("utf-8")),
                    shell=True, cwd='output')
    os.remove('output/fakturka.html')
    os.remove('output/pdf.css')
    with open('{}/{}.json'.format(json_file_path, invoice.filename.encode('utf-8')), 'w') as jsonOutput:
        jsonOutput.write(invoice.to_json())
#endregion script
