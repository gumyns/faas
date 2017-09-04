# coding=utf-8
#!/bin/bash python

import argparse
import sys
import os
import shutil
import subprocess

from models import Owner, Account, Client, Invoice

parser = argparse.ArgumentParser()
parser.add_argument("owner", help="The seller of products issued on the invoice.", type=str)
parser.add_argument("client", help="The receiver of the products issued on the invoice.", type=str)
parser.add_argument("--delivery", help="The company's that is receiving the delivery of the invoice.", type=str)
parser.parse_args()
print(sys.argv)

with open("owners/{}.json".format(sys.argv[1]), 'r') as new_file:
    print(new_file.read())

with open("clients/{}.json".format(sys.argv[2]), 'r') as new_file:
    print(new_file.read())

invoice = Invoice(Owner('Sample Owner Company', '43-190 Mikołów\nul. Fajna 66', 1111111111,
              Account('Cool bank', '33 5555 5555 5555 5555 5555 5555')), Client('Sample client company', '43-190 Mikołów\nul. Fajna 77', 2222222222, 60, 14), None)

with open("templates/templatePL.html", 'r') as htmlInput:
    html = htmlInput.read().format(**invoice.asFormatter())
    if not os.path.exists('output'):
        os.makedirs('output')
    with open('output/fakturka.html', 'w') as htmlOutput:
        htmlOutput.write(html)
        htmlOutput.close()
    shutil.copyfile('templates/pdf.css', 'output/pdf.css')
    subprocess.call('wkhtmltopdf fakturka.html fakturka.pdf', shell=True, cwd='output')


