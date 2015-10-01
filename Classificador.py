# -*- coding: cp1252 -*-
# -*- coding: utf-8 -*-
from __future__ import print_function, unicode_literals
import nltk
base = []
base_negativas = open("negativas_sem.txt","r")
lista_negativa = base_negativas.readlines()
for x in lista_negativa:
        
        base.append((str(x).encode("utf8"),"negativo"))
print (base)



stopwords = ["a","se","com","as","isso","uma","nos","do",
"da","de","que","como","um","sempre","para","tem","ou","e","o","no",
"nas","eu","eles","tu","algumas","algum","esse","me","ta","nas","dilma"]

stemmer = nltk.stem.RSLPStemmer()
global palavras_caracteristicas
frases = []
for (palavras, sentimento) in base:
        filtrado = [str(stemmer.stem(e)) for e in palavras.split() if e not in stopwords]
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

tweet = "Dilma sair sera como saber que nos curamos de um cancer"
tweet_stemming = []
for (palavras) in tweet.split():
        filtrado = [e for e in palavras.split()]
        tweet_stemming.append(str(stemmer.stem(filtrado[0])))

print (classificador.classify(extrator_caracteristicas(tweet_stemming)))

