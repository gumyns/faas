# coding=utf-8
# !/bin/bash python

import argparse
import json
import os
import shutil
import subprocess
import sys

import jpk
from models import Json, Invoice

parser = argparse.ArgumentParser()
parser.add_argument("owner", help="The seller of products issued on the invoice.", type=str)
parser.add_argument("client", help="The receiver of the products issued on the invoice.", type=str)
parser.add_argument("amount", help="The amount of the service in invoice.", type=str)
parser.add_argument("--delivery", help="The company's that is receiving the delivery of the invoice.", type=str)
parser.parse_args()
print(sys.argv)

owner = None
client = None

with open("owners/{}.json".format(sys.argv[1]), 'r') as new_file:
    owner = json.loads(new_file.read().decode('utf-8'), object_hook=Json)

with open("clients/{}.json".format(sys.argv[2]), 'r') as new_file:
    client = json.loads(new_file.read().decode('utf-8'), object_hook=Json)

invoice = Invoice(owner, client, sys.argv[3], None)

jpk.generate(owner)


def ensure_path_existence(path):
    if not os.path.exists(path):
        os.makedirs(path)


with open("templates/{}.html".format(client.template), 'r') as htmlInput:
    ensure_path_existence('output')
    json_file_path = 'output/{}/{}/json'.format(owner.name.replace(' ', '_'), str(invoice.date.year))
    ensure_path_existence(json_file_path)
    pdf_file_path = 'output/{}/{}/pdf'.format(owner.name.replace(' ', '_'), str(invoice.date.year))
    ensure_path_existence(pdf_file_path)

    html = htmlInput.read().decode('utf-8').format(**invoice.asFormatter(owner))

    with open('output/fakturka.html', 'w') as htmlOutput:
        htmlOutput.write(html.encode('utf-8'))
    shutil.copyfile('templates/pdf.css', 'output/pdf.css')
    subprocess.call('wkhtmltopdf fakturka.html "{}/{}.pdf"'.format(pdf_file_path.replace('output/', ''),
                                                                   invoice.filename.encode("utf-8")),
                    shell=True, cwd='output')
    os.remove('output/fakturka.html')
    os.remove('output/pdf.css')
    with open('{}/{}.json'.format(json_file_path, invoice.filename.encode('utf-8')), 'w') as jsonOutput:
        jsonOutput.write(invoice.to_json())
