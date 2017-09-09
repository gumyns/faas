# coding=utf-8
import sqlite3


class Province:
    def __init__(self, nr, id, name):
        self.nr = nr
        self.id = id
        self.name = name

    def __str__(self):
        return '{}. {}'.format(self.nr, self.name.encode("utf-8"))


class City:
    def __init__(self, province, city):
        self.province_id = province.id
        self.province = province.name
        self.district = city[1]
        self.commune = city[2]
        self.city = city[3]


class DB:
    def __init__(self):
        self.db = sqlite3.connect("utils/teryt.sqlite")

    def get_province_list(self):
        query = self.db.execute('select WOJ, NAZWA from TERC where POW=""')
        cursor = query.fetchall()
        provinces = []
        for i, province in enumerate(cursor):
            provinces.append(Province(i + 1, province[0], province[1]))
        query.close()
        return provinces

    def search_city(self, province, name):
        query = self.db.execute('select WOJ, POW, GMI, NAZWA from TERC where WOJ=(?) and NAZWA like (?) and POW!=""',
                                (province.id, name + '%',))
        cursor = query.fetchall()
        cities = []
        for city in cursor:
            if len(city[2]) > 0:
                cities.append(City(province, city))
        # TODO here we need to fill up all objects with data
        if len(cities) > 1:
            print "Wiyncyj miast!"
        query.close()
        return cities
