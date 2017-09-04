import json

import datetime


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
        return json.dumps(self.__dict__, default=lambda o: o.__dict__, sort_keys=True, indent=2)


class Owner(Json):
    def __init__(self, name=None, full_address=None, nip=None, account=None):
        Json.__init__(self)
        self.name = name
        self.address = full_address
        self.nip = nip
        self.account = account


class Client(Json):
    def __init__(self, name, full_address, nip, hourlyRate, template, paymentDelay):
        Json.__init__(self)
        self.name = name
        self.address = full_address
        self.nip = nip
        self.hourlyRate = hourlyRate
        self.paymentDelay = paymentDelay
        self.template = template


class Account(Json):
    def __init__(self, bank_name=None, number=None, swift=None, transfer=None):
        Json.__init__(self)
        self.bank_name = bank_name
        self.number = number
        self.swift = swift
        self.transfer = transfer


class Invoice(Json):
    def __init__(self, owner, client, amount, delivery, date = datetime.datetime.now()):
        Json.__init__(self)
        self.owner = owner
        self.client = client
        self.delivery = delivery
        self.amount = float(amount)
        self.date = date

    def asFormatter(self):
        self.calculate()
        return {
            'date_created': self.date.strftime("%Y-%m-%d"),
            'number': None,
            'ownerName': self.owner.name,
            'ownerAddress': self.owner.address,
            'ownerVatId': self.owner.nip,
            'clientName': self.client.name,
            'clientAddress': self.client.address,
            'clientVatId': self.client.nip,
            'paymentType': self.owner.account.transfer,
            'paymentDueDate': self.dueDate.strftime("%Y-%m-%d"),
            'paymentBankName': self.owner.account.bank_name,
            'paymentSwift': self.owner.account.swift,
            'paymentAccount': self.owner.account.number,
            'articleNumber': 1,
            'articleName': None,
            'articleCount': self.amount,
            'articleNetPrice': "{0:.2f}".format(self.client.hourlyRate),
            'articleNetValue': "{0:.2f}".format(self.netPrice),
            'articleVatRate': "23%",
            'articleVatAmount': "{0:.2f}".format(self.taxPrice),
            'articleGrossValue': "{0:.2f}".format(self.grossPrice),
            'totalGrossValue': "{0:.2f}".format(self.grossPrice),
            'totalNetValue': "{0:.2f}".format(self.netPrice),
            'totalVatAmount': "{0:.2f}".format(self.taxPrice),
        }

    def calculate(self):
        self.netPrice = float(self.client.hourlyRate) * self.amount
        self.grossPrice = self.netPrice * 1.23
        self.taxPrice = self.grossPrice - self.netPrice
        self.dueDate = self.date + datetime.timedelta(days=int(self.client.paymentDelay))
