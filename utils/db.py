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

    def as_string(self):
        return u'Województwo: {}, powiat: {}, miejscowość: {}, typ: {}' \
            .format(self.province, self.district, self.city, self.commune)


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
        query = self.db.execute('select WOJ, POW, NAZDOD, NAZWA from TERC where WOJ=(?) and NAZWA like (?) and POW!=""',
                                (province.id, name + '%',))
        cursor = query.fetchall()
        cities = []
        for city in cursor:
            if len(city[2]) > 0:
                target = [
                    province.name,
                    self.search_district_name(province, city[1]),
                    city[2],
                    city[3]
                ]
                cities.append(City(province, target))
        query.close()
        return cities

    def search_district_name(self, province, id):
        query = self.db.execute('select NAZWA from TERC where WOJ=(?) and GMI!="" and POW=(?)', (province.id, id,))
        cursor = query.fetchall()
        name = cursor[0][0]
        query.close()
        return name

    def search_for_trouble(self, province, city):
        troublemaker = None
        target = city
        while True:
            print u'Searching {}'.format(target)
            query = self.db.execute('select KOD from SKARB where WOJ=(?) and NAZWA like (?)',
                                    (province, u'%{}%'.format(target),))
            cursor = query.fetchall()
            if len(cursor) > 0:
                troublemaker = cursor[0][0]
            query.close()
            if troublemaker is not None:
                return troublemaker
            target = target[:-1]
