# -*- coding: cp1252 -*-
import MySQLdb

con = MySQLdb.connect(host='localhost', user='root', passwd='JME.megasin-02', db='twitter')
c = con.cursor()
arq = open("ids.txt","a")

sql1 = "SELECT id_post FROM post"
c.execute(sql1)
results = c.fetchall()

for row in results:
    arq.write(str(row[0]) + "\n")
    
c.close()
con.close()
