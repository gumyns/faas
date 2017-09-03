# coding=utf-8
import json

from models import Owner, Account, Client

owner = Owner('Sample Owner Company', '43-190 Mikołów\nul. Fajna 66', 1111111111,
              Account('Cool bank', '33 5555 5555 5555 5555 5555 5555'))

client = Client('Sample client company', '43-190 Mikołów\nul. Fajna 77', 2222222222)

deliver_to = Client('Sample deliver company', '43-190 Mikołów\nul. Fajna 77', 3333333333)


def to_json(complex_object):
    return json.dumps(complex_object, default=lambda o: o.__dict__,
                      sort_keys=True, indent=2)


def create(file_name, source):
    with open(file_name, 'w') as new_file:
        new_file.write(to_json(source.__dict__))


create('owner.json', owner)
create('client.json', client)
create('delivery.json', deliver_to)
