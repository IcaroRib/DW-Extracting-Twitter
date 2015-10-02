# -*- coding: cp1252 -*-
# -*- coding: utf-8 -*-

def limpeza(new):
    new = new.lower()
    new = new.replace("à","a").replace("á","a").replace("é","e").replace("í","i")
    new = new.replace("á","a").replace("ã","a").replace("â","a").replace("ó","o")
    new = new.replace("ú","u").replace("ô","o").replace("ç","c").replace("õ","o")
    new = new.replace("î","i").replace("û","u").replace("ñ","nao").replace("è","e")
    new = new.replace("ì","i").replace("ü","u").replace("ê","e").replace("“","")
    new = new.replace("#","").replace("”","")
 
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
