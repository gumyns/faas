# coding=utf-8
# !/bin/bash python

import argparse
import subprocess


# region lib
def generate_pdf(source, target):
    subprocess.call('wkhtmltopdf "{}" "{}"'.format(source, target), shell=True)


# endregion lib


# region script
parser = argparse.ArgumentParser()
parser.add_argument("source", help=u'Plik zródłowy HTML', type=str)
parser.add_argument("target", help=u'Plik docelowy PDF', type=str)
arguments = parser.parse_args()

generate_pdf(arguments.source.encode("utf-8"), arguments.target.encode('utf-8'))
# endregion script
