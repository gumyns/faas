import datetime
import json


class Json:
    def __init__(self, dict=None):
        if dict is not None:
            vars(self).update(dict)
        else:
            pass

    @staticmethod
    def to_json(o):
        return json.dumps(o.__dict__, default=lambda o: o.__dict__, sort_keys=True, indent=2)

    def to_json(self):
        return json.dumps(self.__dict__,
                          default=lambda o: o.__dict__ if not isinstance(o, datetime.datetime)
                          else datetime.datetime.isoformat(
                              o), sort_keys=True, indent=2)
