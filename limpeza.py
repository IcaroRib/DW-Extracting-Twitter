# -*- coding: cp1252 -*-
# -*- coding: utf-8 -*-

def limpeza(new):
    new = new.lower()
    new = new.replace("�","a").replace("�","a").replace("�","e").replace("�","i")
    new = new.replace("�","a").replace("�","a").replace("�","a").replace("�","o")
    new = new.replace("�","u").replace("�","o").replace("�","c").replace("�","o")
    new = new.replace("�","i").replace("�","u").replace("�","nao").replace("�","e")
    new = new.replace("�","i").replace("�","u").replace("�","e").replace("�","")
    new = new.replace("#","").replace("�","")
 
    try:
        if ":" and "rt" in new:
            new = new.split(":",1)[1]
    except:
        print new

    marcador = False
    palavra = ""
    tamanho = len(new)
    for char in new:

        if char == "@":
            marcador = True
        elif char == " ":
            marcador = False

        if marcador == False:
            palavra = palavra + char


    for i in range(len(new)-4):
        if palavra[i:i+4] == "http":
            palavra = palavra[:i]
            break

    #palavra = palavra.replace(" "," ").replace("   "," ").replace("    "," ").replace("     "," ")
    print palavra
    return palavra
