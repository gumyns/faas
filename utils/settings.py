import json
import os

from models import Json


class Settings(Json):
    def __init__(self, output_path=None, wkhtmltopdf_path="", json=""):
        Json.__init__(self, dict=json)
        if json is None:
            self.output_path = output_path
            self.wkhtmltopdf_path = wkhtmltopdf_path

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

    def save(self):
        with open(Settings._settings_file(), 'w') as jsonOutput:
            jsonOutput.write(self.to_json())
