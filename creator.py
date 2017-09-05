# coding=utf-8
# !/bin/bash python
import os

from models import Json, Owner, Account, Client

if not os.path.exists('owners'):
    os.makedirs('owners')
if not os.path.exists('clients'):
    os.makedirs('clients')


def create(file_name, source):
    if not os.path.isfile(file_name):
        with open(file_name, 'w') as new_file:
            new_file.write(source.to_json())


print("Welcome to the data wizard!\n"
      "In step-by-step process, you will be able to build the configuration files.\n"
      "Type:\n"
      "1. To create owner data.\n"
      "2. To create client data.\n"
      "3. To generate sample data.\n"
      "Any other key to exit.\n")
user_input = raw_input('Select option:')

if user_input == '4':
    owner = Owner('Sample Owner Company', '43-190 Mikołów\nul. Fajna 66', 1111111111,
                  Account('Cool bank', '33 5555 5555 5555 5555 5555 5555', "123", "Przelew"))

    client = Client('Sample client company', '43-190 Mikołów\nul. Fajna 77', 2222222222, 60, 'templatePL', 14)

    deliver_to = Client('Sample deliver company', '43-190 Mikołów\nul. Fajna 77', 3333333333, 20, None, 0)

    create('owners/owner.json', owner)
    create('clients/client.json', client)
    create('clients/delivery.json', deliver_to)
    print('Created sample clients!')
