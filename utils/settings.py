import json
import os

from json_helper import Json


class Settings(Json):
    def __init__(self, output_path="", wkhtmltopdf_path="", json=None):
        Json.__init__(self, dict=json)
        if json is None:
            self.output_path = output_path
            self.wkhtmltopdf_path = wkhtmltopdf_path
        else:
            self.output_path = self.output_path if self.output_path is "" else os.path.abspath(self.output_path) + '/'
            self.wkhtmltopdf_path = self.wkhtmltopdf_path if self.wkhtmltopdf_path is "" else os.path.abspath(
                self.wkhtmltopdf_path) + '/'

    @staticmethod
    def _settings_file():
        return os.path.abspath(os.path.join(os.path.abspath(__file__), os.path.pardir, os.path.pardir, "settings.json"))

    @staticmethod
    def get():
        try:
            with open(Settings._settings_file(), 'r') as new_file:
                settings = Settings(json=json.loads(new_file.read().decode('utf-8')))
        except IOError:
            return Settings()
        return settings

    @staticmethod
    def _create_if_none(path):
        if not os.path.exists(path):
            os.makedirs(path)
        return path

    @staticmethod
    def clients_dir():
        return Settings._create_if_none(os.path.abspath(os.path.join(Settings.get().output_path, 'clients')))

    @staticmethod
    def client_file(name):
        return os.path.join(Settings.clients_dir(), name)

    @staticmethod
    def owners_dir():
        return Settings._create_if_none(os.path.abspath(os.path.join(Settings.get().output_path, 'owners')))

    @staticmethod
    def owner_file(name):
        return os.path.join(Settings.owners_dir(), name)

    @staticmethod
    def output_dir():
        return Settings._create_if_none(os.path.abspath(os.path.join(Settings.get().output_path, 'output')))

    @staticmethod
    def invoice_dir(owner, invoice):
        return Settings._create_if_none(
            os.path.join(Settings.output_dir(), owner.name.replace(' ', '_'), str(invoice.date.year)))

    @staticmethod
    def invoice_json_dir(owner, invoice):
        return Settings._create_if_none(os.path.join(Settings.invoice_dir(owner, invoice), 'json'))

    @staticmethod
    def invoice_json_file(owner, invoice):
        return os.path.join(Settings.invoice_json_dir(owner, invoice), u'{}.json'.format(invoice.filename))

    @staticmethod
    def invoice_pdf_dir(owner, invoice):
        return Settings._create_if_none(os.path.join(Settings.invoice_dir(owner, invoice), 'pdf'))

    @staticmethod
    def invoice_pdf_file(owner, invoice):
        return os.path.join(Settings.invoice_pdf_dir(owner, invoice), u'{}.pdf'.format(invoice.filename))

    def save(self):
        with open(Settings._settings_file(), 'w') as jsonOutput:
            jsonOutput.write(self.to_json())
