class Owner:
    def __init__(self, name, address, nip, bank, account):
        self.name = name
        self.address = address
        self.nip = nip
        self.bank = bank
        self.account = account

class Client:
    def __init__(self, name, address, nip):
        self.name = name
        self.address = address
        self.nip = nip