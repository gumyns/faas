class Owner:
    def __init__(self, name, full_address, nip, account):
        self.name = name
        self.address = full_address
        self.nip = nip
        self.account = account


class Client:
    def __init__(self, name, full_address, nip, hourlyRate, paymentDelay):
        self.name = name
        self.address = full_address
        self.nip = nip
        self.hourlyRate = hourlyRate
        self.paymentDelay = paymentDelay


class Account:
    def __init__(self, bank_name, number):
        self.bank_name = bank_name
        self.number = number

class Invoice:
    def __init__(self, owner, client, delivery):
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