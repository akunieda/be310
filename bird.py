# -*- coding: utf-8 -*-

import MySQLdb


def get_bird(name, db=None):
    if db is None:
        db = MySQLdb.connect(host="localhost", user='root', db='be310')
    c = db.cursor()

    query_passaro = """SELECT * FROM Passaro
                       WHERE NomeCientifico="{}";""".format(name)
    c.execute(query_passaro)
    passaro = c.fetchone()

    if passaro is None:
        return None, None

    query = """SELECT * FROM RegistroPassaro
               WHERE NomeCientifico="{}";""".format(name)
    c.execute(query)

    return passaro, c.fetchall()


def get_birds(db=None):
    if db is None:
        db = MySQLdb.connect(host="localhost", user='root', db='be310')
    c = db.cursor()

    query = """SELECT * FROM Passaro;"""
    c.execute(query)

    return c.fetchall()


def get_last_records(db=None):
    if db is None:
        db = MySQLdb.connect(host="localhost", user='root', db='be310')
    c = db.cursor()

    query = """SELECT * FROM RegistroPassaro
               WHERE URIFoto IS NOT NULL
               AND URISom IS NOT NULL
               ORDER BY Timestamp DESC
               LIMIT 10;"""
    c.execute(query)

    registros = c.fetchall()
    locais = map(lambda x: x[2], registros)
    locais = '|'.join(locais)
    return registros, locais


def store(name, common_name, date, time, image_filename, sound_filename, coord_lat, coord_lng, db=None):
    if db is None:
        db = MySQLdb.connect(host="localhost", user='root', db='be310')

    if image_filename is None:
        image_filename = "NULL"
    else:
        image_filename = '"' + image_filename + '"'

    if sound_filename is None:
        sound_filename = "NULL"
    else:
        sound_filename = '"' + sound_filename + '"'

    query_passaro = """SELECT * FROM Passaro
                       WHERE NomeCientifico="{}";""".format(name)
    c = db.cursor()
    c.execute(query_passaro)
    if c.fetchone() is None:
        query_passaro = """INSERT INTO Passaro
                           (NomeCientifico, Nome)
                           VALUES ("{}", "{}");""".format(name, common_name)
        try:
            c.execute(query_passaro)
            db.commit()
        except:
            db.rollback()
            return False

    query = """INSERT INTO RegistroPassaro
               (NomeCientifico, Localizacao, DataHora, URISom, URIFoto)
               VALUES ("{}", "{}, {}", "{} {}:00", {}, {});"""
    query = query.format(name, coord_lat, coord_lng, date, time, sound_filename, image_filename)
    print query

    try:
        c.execute(query)
        db.commit()
        c.close()
        return True
    except:
        db.rollback()
        c.close()
        return False


if __name__ == '__main__':
    pass
