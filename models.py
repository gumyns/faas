class Owner:
    def __init__(self, name, full_address, nip, account, keyword):
        self.name = name
        self.address = full_address
        self.nip = nip
        self.account = account
        self.keyword = keyword


class Client:
    def __init__(self, name, full_address, nip, hourlyRate, keyword):
        self.name = name
        self.address = full_address
        self.nip = nip
        self.hourlyRate = hourlyRate
        self.keyword = keyword


class Account:
    def __init__(self, bank_name, number):
        self.bank_name = bank_name
        self.number = number
