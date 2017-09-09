# coding=utf-8
# !/bin/bash python
import os

from models import Getch, Owner, Account, Client, Address

if not os.path.exists('owners'):
    os.makedirs('owners')
if not os.path.exists('clients'):
    os.makedirs('clients')


def clear():
    os.system('cls' if os.name == 'nt' else 'clear')


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
        createOwner()
    elif user_input == '2':
        createClient()
    elif user_input == '3':
        owner = Owner('Sample Owner Company', '43-190 Mikołów\nul. Fajna 66', 1111111111,
                      Account('Cool bank', '33 5555 5555 5555 5555 5555 5555', "123", "Przelew"))

        client = Client('Sample client company', Address('43-190', 'Mikołów', street='Fajna', house_number='77'),
                        2222222222, 60, 'templatePL', 14)

        deliver_to = Client('Sample deliver company',
                            Address('43-190', 'Mikołów', street='Not Cool', house_number='66'), 3333333333, 20, None, 0)

        create('owners/owner.json', owner)
        create('clients/client.json', client)
        create('clients/delivery.json', deliver_to)
        print('Created sample clients!')
    elif user_input == '4':
        exit()
    else:
        clear()
        step1()


def createOwner():
    clear()
    name = raw_input("Nazwa firmy: ")
    street = raw_input("Ulica: ")
    house_number = raw_input("Numer domu: ")
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
    create('owners/{}.json'.format(save_name),
           Owner(name, Address(postal_code=postal, city=city, street=street, house_number=house_number), nip,
                 Account(bank_name, account, swift, transfer),
                 annual_number))
    clear()
    print("Owner {} utworzony".format(save_name))
    step1()


def createClient():
    clear()
    name = raw_input("Nazwa firmy klienta: ")
    street = raw_input("Ulica: ")
    house_number = raw_input("Numer domu: ")
    city = raw_input("Miasto: ")
    postal = raw_input("Kod pocztowy: ")
    nip = raw_input("NIP/VAT ID: ")
    rate = raw_input("Stawka godzinowa: ")
    currency = raw_input("Waluta: ")
    payment_delay = raw_input("Termin platnosci w dniach: ")
    template = raw_input("Wzor (html bez rozszerzenia): ")
    save_name = raw_input("Nazwa (bez spacji): ")
    print('''Data na fakturze:
1. Ostatni dzien roboczy poprzedniego miesiaca
2. Pierwszy dzien roboczy obecnego miesiaca
3. Dzien generowania faktury''')
    date_day = Getch().__call__()
    if date_day == '1':
        date_day = 0
    elif date_day == '2':
        date_day = 1
    else:
        date_day = 2
    create('clients/{}.json'.format(save_name),
           Client(name, Address(postal, city, street=street, house_number=house_number),
                  nip, rate, template, payment_delay, currency, date_day))
    clear()
    print("Klient {} utworzony".format(save_name))
    step1()


clear()
step1()
