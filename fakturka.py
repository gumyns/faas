# coding=utf-8
# !/bin/bash python

import json
import argparse
import sys
import os
import shutil
import subprocess

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

with open("templates/{}.html".format(client.template), 'r') as htmlInput:
    html = htmlInput.read().decode('utf-8').format(**invoice.asFormatter())
    if not os.path.exists('output'):
        os.makedirs('output')
    with open('output/fakturka.html', 'w') as htmlOutput:
        htmlOutput.write(html.encode('utf-8'))
        htmlOutput.close()
    shutil.copyfile('templates/pdf.css', 'output/pdf.css')
    subprocess.call('wkhtmltopdf fakturka.html fakturka.pdf', shell=True, cwd='output')
