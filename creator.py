# coding=utf-8
# !/bin/bash python
import os
from models import Getch, Json, Owner, Account, Client

if not os.path.exists('owners'):
    os.makedirs('owners')
if not os.path.exists('clients'):
    os.makedirs('clients')

def clear():
    os.system('cls' if os.name=='nt' else 'clear')

def create(file_name, source):
    if not os.path.isfile(file_name):
        with open(file_name, 'w') as new_file:
            new_file.write(source.to_json())


def step1():
    print('''Welcome to the data wizard!
In step-by-step process, you will be able to build the configuration files.
Type:
1. To create owner data.
2. To create client data.
3. To generate sample data.
4. To exit.
Any other key to exit.
Select option:''')

    user_input = Getch().__call__()

    if user_input == '1':
        owners()
    elif user_input == '2':
        print("case 2")
    elif user_input == '3':
        owner = Owner('Sample Owner Company', '43-190 Mikołów\nul. Fajna 66', 1111111111,
                      Account('Cool bank', '33 5555 5555 5555 5555 5555 5555', "123", "Przelew"))

        client = Client('Sample client company', '43-190 Mikołów\nul. Fajna 77', 2222222222, 60, 'templatePL', 14)

        deliver_to = Client('Sample deliver company', '43-190 Mikołów\nul. Fajna 77', 3333333333, 20, None, 0)

        create('owners/owner.json', owner)
        create('clients/client.json', client)
        create('clients/delivery.json', deliver_to)
        print('Created sample clients!')
    elif user_input == '4':
        exit()
    else:
        clear()
        step1()

def owners():
    clear()
    name = raw_input("Nazwa firmy: ")
    street = raw_input("Ulica i numer domu: ")
    city = raw_input("Miasto: ")
    postal = raw_input("Kod pocztowy: ")
    nip = raw_input("NIP/VAT ID: ")
    bank_name = raw_input("Nazwa banku: ")
    account = raw_input("Numer konta: ")
    swift = raw_input("Numer SWIFT (tylko FVAT UE):")
    print('''Numerowanie faktur:
1. Roczne
2. Miesieczne''')
    annual_number = Getch().__call__() == '1'
    transfer = "Przelew"
    save_name = raw_input("Nazwa (bez spacji): ")
    create('owners/{}.json'.format(save_name), Owner(name, "{}\n{} {}".format(street, postal, city), nip, Account(bank_name, account, swift, transfer), annual_number))
    clear()
    print("Owner {} utworzony".format(save_name))
    step1()


clear()
step1()