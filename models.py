import json


class Json:
    def __init__(self, dict=None):
        if dict is not None:
            vars(self).update(dict)
        else:
            pass

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
    def __init__(self, name, full_address, nip, hourlyRate, paymentDelay):
        Json.__init__(self)
        self.name = name
        self.address = full_address
        self.nip = nip
        self.hourlyRate = hourlyRate
        self.paymentDelay = paymentDelay


class Account(Json):
    def __init__(self, bank_name=None, number=None):
        Json.__init__(self)
        self.bank_name = bank_name
        self.number = number


class Invoice(Json):
    def __init__(self, owner, client, amount, delivery):
        Json.__init__(self)
        self.owner = owner
        self.client = client
        self.delivery = delivery

    def asFormatter(self):
        return {
            'date_created': None,
            'number': None,
            'ownerName': self.owner.name,
            'ownerAddress': self.owner.address,
            'ownerVatId': self.owner.nip,
            'clientName': self.client.name,
            'clientAddress': self.client.address,
            'clientVatId': self.client.nip,
            'paymentType': None,
            'paymentDueDate': None,
            'paymentBankName': self.owner.account.bank_name,
            'paymentAccount': self.owner.account.number,
            'articleNumber': None,
            'articleName': None,
            'articleCount': None,
            'articleNetPrice': None,
            'articleNetValue': None,
            'articleVatRate': None,
            'articleVatAmount': None,
            'articleGrossValue': None,
            'totalGrossValue': None,
            'totalNetValue': None,
            'totalVatAmount': None,
        }
