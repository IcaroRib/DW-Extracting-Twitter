# -*- coding: cp1252 -*-
# -*- coding: utf-8 -*-
#from __future__ import print_function, unicode_literals
import nltk
import MySQLdb
from limpeza import *

base = []
base_negativas = open("negativas_sem.txt","r")
lista_negativa = base_negativas.readlines()
for x in lista_negativa:
    x = x.replace("\n","")
    base.append((str(x), "negativo") )		
base_negativas.close()

base_positivas = open("positivas_sem.txt","r")
lista_positiva = base_positivas.readlines()
for x in lista_positiva:
    x = x.replace("\n","")
    base.append((str(x),"positivo"))		
base_positivas.close()

base_neutras = open("neutras_sem.txt","r")
lista_neutra = base_neutras.readlines()
for x in lista_neutra:
    x = x.replace("\n","")
    base.append((str(x),"neutro"))		
base_neutras.close()

stopwords = nltk.corpus.stopwords.words("portuguese")
arq_stopword = open("stopwords_sem.txt","r")
lista_stopword = arq_stopword.readlines()
for i in lista_stopword:
    i = i.replace("\n","")
    stopwords.append(i)

stemmer = nltk.stem.RSLPStemmer()
global palavras_caracteristicas
frases = []
for (palavras, sentimento) in base:
    print palavras
    filtrado = [str(stemmer.stem(e)) for  e in palavras.split() if e not in stopwords]
    frases.append((filtrado, sentimento))



def busca_palavras_frases(frases):
    todas_palavras = []
    for (palavras, sentimento) in frases:
            todas_palavras.extend(palavras)

    return todas_palavras
def busca_frequencia_palavras(lista_palavras):
    lista_palavras = nltk.FreqDist(lista_palavras)
    return lista_palavras
def busca_palavras_unicas(lista_frequencia):
    frequencia = lista_frequencia.keys()
    return frequencia
def extrator_caracteristicas(documento):
    doc = set(documento)
    caracteristicas={}
    for palavra in palavras_caracteristicas:
            caracteristicas["contem(%s)" % palavra] = (palavra in doc)
    return caracteristicas

palavras = busca_palavras_frases(frases)
frequencia = busca_frequencia_palavras(palavras)
palavras_caracteristicas = busca_palavras_unicas(frequencia)
treino = nltk.classify.apply_features(extrator_caracteristicas, frases)
classificador = nltk.NaiveBayesClassifier.train(treino)

con = MySQLdb.connect(host='localhost', user='root', passwd='JME.megasin-02', db='twitter')
c = con.cursor()
sql1 = "SELECT id_post, texto FROM post"
c.execute(sql1)
results = c.fetchall()
c.close()
c = con.cursor()
con.autocommit(True)

for row in results:
    tweet = limpeza(str(row[1]))
    idTweet = long(row[0])
    tweet_stemming = []
    atualizar = True
    contador = 0
    for (palavras) in tweet.split():
            filtrado = [e for e in palavras.split()]
            try:
                tweet_stemming.append(str(stemmer.stem(filtrado[0])))
            except:
                contador +=1
                atualizar = False                

    if atualizar == True:
        sentimento = classificador.classify(extrator_caracteristicas(tweet_stemming))
        
        if sentimento == "neutro": idSentimento = 1
        elif sentimento == "positivo": idSentimento = 2
        else: idSentimento = 3
        
        sql2 = "UPDATE post SET id_sentimento = " + str(idSentimento) + " WHERE id_Post = " + str(idTweet)
        print sql2
        c.execute(sql2)
        c.fetchall()

c.close()
con.close()
print "-==================="
print "Foram contabilizados " + str(contador) + " erros"
