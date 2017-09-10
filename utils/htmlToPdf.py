# coding=utf-8
import subprocess

from settings import Settings


def generate_pdf(source, target):
    subprocess.call('"{}wkhtmltopdf" "{}" "{}"'.format(Settings.get().wkhtmltopdf_path, source, target), shell=True)
