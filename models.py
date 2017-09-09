# coding=utf-8
import datetime
import json

from utils import slownie


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


class Address:
    def __init__(self, postal_code=None, city=None, province=None, district=None, commune=None, street=None,
                 house_number=None, flat_number=None, raw=None):
        self.postal_code = postal_code
        self.city = city
        self.province = province  # wojewodztwo
        self.district = district  # powiat
        self.commune = commune  # gmina
        self.street = street
        self.house_number = house_number
        self.flat_number = flat_number


class Owner(Json):
    def __init__(self, name=None, address=None, nip=None, account=None, annual_number=True, gov_code=None):
        Json.__init__(self)
        self.name = name
        self.address = address
        self.nip = nip
        self.account = account
        self.annual_number = annual_number
        self.gov_code = gov_code


class Client(Json):
    def __init__(self, name, address, nip, hourly_rate, template, payment_delay, currency="PLN", date_day_type=0):
        Json.__init__(self)
        self.name = name
        self.address = address
        self.nip = nip
        self.hourly_rate = hourly_rate
        self.payment_delay = payment_delay
        self.template = template
        self.currency = currency
        self.date_day_type = date_day_type


class Account(Json):
    def __init__(self, bank_name=None, number=None, swift=None, transfer=None):
        Json.__init__(self)
        self.bank_name = bank_name
        self.number = number
        self.swift = swift
        self.transfer = transfer


class Invoice(Json):
    def __init__(self, owner, client, amount, delivery, date=datetime.datetime.now(),
                 name='usługa informatyczna'.decode('utf-8')):
        Json.__init__(self)
        self.owner = owner
        self.client = client
        self.delivery = delivery
        self.amount = float(amount)
        self.date = self.invoice_issue_date(date, client.date_day_type)
        self.name = name

    def asFormatter(self):
        self.calculate()
        return {
            'date_created': self.date.strftime("%Y-%m-%d"),
            'number': self.number,
            'ownerName': self.owner.name,
            'ownerAddress': "{} {}\n{} {}".format(self.owner.address.street, self.owner.address.house_number,
                                                  self.owner.address.postal_code, self.owner.address.city),
            'ownerVatId': self.owner.nip,
            'clientName': self.client.name,
            'clientAddress': "{} {}\n{} {}".format(self.owner.address.street, self.owner.address.house_number,
                                                   self.owner.address.postal_code, self.owner.address.city),
            'clientVatId': self.client.nip,
            'paymentType': self.owner.account.transfer,
            'paymentDueDate': self.dueDate.strftime("%Y-%m-%d"),
            'paymentBankName': self.owner.account.bank_name,
            'paymentSwift': self.owner.account.swift,
            'paymentAccount': self.owner.account.number,
            'articleNumber': 1,
            'articleName': self.name,
            'articleCount': self.amount,
            'articleNetPrice': "{0:.2f}".format(self.client.hourly_rate),
            'articleNetValue': "{0:.2f}".format(self.netPrice),
            'articleVatRate': "23%",
            'articleVatAmount': "{0:.2f}".format(self.taxPrice),
            'articleGrossValue': "{0:.2f}".format(self.grossPrice),
            'totalGrossValue': "{0:.2f}".format(self.grossPrice),
            'totalNetValue': "{0:.2f}".format(self.netPrice),
            'totalVatAmount': "{0:.2f}".format(self.taxPrice),
            'currency': self.client.currency,
            'priceStringPL': self.priceStringPL
        }

    def calculate(self):
        self.netPrice = float(self.client.hourly_rate) * self.amount
        self.grossPrice = self.netPrice * 1.23
        self.taxPrice = self.grossPrice - self.netPrice
        self.dueDate = self.date + datetime.timedelta(days=int(self.client.payment_delay))
        nextNumber = 1  # TODO
        self.number = "{}/{}".format(nextNumber,
                                     self.date.year) if self.owner.annual_number is True else "{}/{}/{}".format(
            nextNumber, self.date.month, self.date.year)
        self.filename = "{}_{}_{}{}".format(self.owner.name, self.client.name, self.date.strftime("%Y%m%d"), nextNumber) \
            .replace(' ', '_').replace('.', '_')
        self.priceStringPL = slownie.slownie(self.grossPrice)

    def invoice_issue_date(self, date, date_calculation_type):
        if date_calculation_type == 0:
            return date.replace(day=1) - datetime.timedelta(days=1)
        if date_calculation_type == 1:
            return date.replace(day=1)
        return date

