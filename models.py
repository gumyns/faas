class Owner:
    def __init__(self, name, full_address, nip, account):
        self.name = name
        self.address = full_address
        self.nip = nip
        self.account = account


class Client:
    def __init__(self, name, full_address, nip):
        self.name = name
        self.address = full_address
        self.nip = nip


class Account:
    def __init__(self, bank_name, number):
        self.bank_name = bank_name
        self.number = number
