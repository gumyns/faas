# coding=utf-8
# !/bin/bash python

import argparse
import json
import os
import shutil
import sys

from utils.htmlToPdf import generate_pdf
from utils.json_helper import Json
from utils.models import Invoice
from utils.settings import Settings

# region script
parser = argparse.ArgumentParser()
parser.add_argument("owner", help="The seller of products issued on the invoice.", type=str)
parser.add_argument("client", help="The receiver of the products issued on the invoice.", type=str)
parser.add_argument("amount", help="The amount of the service in invoice.", type=str)
parser.add_argument("--delivery", help="The company's that is receiving the delivery of the invoice.", type=str)
arguments = parser.parse_args()

with open(Settings.owner_file("{}.json".format(arguments.owner)), 'r') as new_file:
    owner = json.loads(new_file.read().decode('utf-8'), object_hook=Json)

with open(Settings.client_file("{}.json".format(arguments.client)), 'r') as new_file:
    client = json.loads(new_file.read().decode('utf-8'), object_hook=Json)

invoice = Invoice(owner, client, arguments.amount, None)

# jpk.generate(owner)

with open("templates/{}.html".format(client.template), 'r') as htmlInput:
    html = htmlInput.read().decode('utf-8').format(**invoice.asFormatter())

    shutil.copyfile('templates/pdf.css', os.path.join(Settings.output_dir(), 'pdf.css'))
    with open(os.path.join(Settings.output_dir(), 'fakturka.html'), 'w') as htmlOutput:
        htmlOutput.write(html.encode('utf-8'))

    print os.path.dirname(os.path.abspath(sys.argv[0]))

    generate_pdf(os.path.join(Settings.output_dir(), 'fakturka.html'), Settings.invoice_pdf_file(owner, invoice))

    os.remove(os.path.join(Settings.output_dir(), 'fakturka.html'))
    os.remove(os.path.join(Settings.output_dir(), 'pdf.css'))
    with open(Settings.invoice_json_file(owner, invoice), 'w') as jsonOutput:
        jsonOutput.write(invoice.to_json())
# endregion script
