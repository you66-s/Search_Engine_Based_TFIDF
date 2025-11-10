# TFIDF-Search-Engine

Un moteur de recherche textuel implémenté en **Java**, basé sur le calcul du **TF-IDF** pour mesurer la similarité entre une requête et un corpus de documents.  
Ce projet met en œuvre la **logique interne d’un moteur de recherche**, dans le but d’explorer les concepts de *text mining* et de *recherche d’information*.

---

## Objectif du projet

L’objectif principal est de construire un système capable de :
- Analyser un corpus de documents textuels,
- Calculer les scores TF-IDF pour chaque terme,
- Évaluer la similarité entre une requête et les documents,
- Classer les documents les plus pertinents en fonction de la similarité cosinus.

---

## Structure du projet

```
TFIDF-Search-Engine/
│
├── dataset/
│   └── origDocs/
│       ├── doc1.txt
│       ├── doc2.txt
│       ├── doc3.txt
│       └── ...
│
└── src/
    ├── Documents.java         # Représente un document avec son contenu et son nom
    ├── FileCorpus.java        # Gère le corpus et calcule l’IDF de chaque terme
    ├── Index.java             # Crée l’index des documents et prépare les vecteurs TF-IDF
    ├── SearchEngine.java      # Recherche et calcule la similarité cosinus
    ├── SearchResult.java      # Structure contenant le nom et le score de chaque résultat
    └── TextTraitement.java    # Nettoyage, tokenisation, suppression de stopwords, etc.
```

---

## ⚙Fonctionnalités principales

 - Chargement automatique d’un ensemble de fichiers texte depuis `dataset/origDocs/`  
 - Nettoyage et prétraitement linguistique (tokenisation, suppression de nombres, minuscules, etc.)  
 - Calcul des poids **TF** et **IDF** pour chaque mot du corpus  
 - Génération d’un **index global** pour l’ensemble des documents  
 - Calcul de la **similarité cosinus** entre une requête et chaque document  
 - Application d’un **seuil minimal (0.0001)** pour filtrer les résultats non significatifs  

---

## Exécution du projet

1. **Compiler le projet :**
   ```bash
   javac -d bin src/*.java
   ```

2. **Exécuter le moteur de recherche :**
   ```bash
   java -cp bin SearchEngine
   ```

3. **Exemple de fonctionnement :**
   - Le moteur lit les fichiers du dossier `dataset/origDocs/`
   - L’utilisateur saisit une requête :  
     ```
     Entrez votre requête : intelligence artificielle et apprentissage
     ```
   - Le moteur renvoie les documents les plus similaires avec leur score de similarité.

---

## 🧮 Exemple de sortie

```
Résultats pour la requête : "intelligence artificielle et apprentissage"

1. doc3.txt  →  0.5423
2. doc1.txt  →  0.3310
3. doc5.txt  →  0.1127
```

---

## Concepts utilisés

- **TF (Term Frequency)** : fréquence du terme dans le document  
- **IDF (Inverse Document Frequency)** : importance d’un terme dans le corpus  
- **TF-IDF** : pondération combinée TF et IDF  
- **Similarité cosinus** : mesure de proximité entre vecteurs  
- **Prétraitement linguistique** : normalisation, suppression des mots vides, stemming  

---

## Futur développement

Les prochaines étapes prévues pour ce projet :

- Intégration dans une **API REST Spring Boot** pour exposer les résultats de recherche via le web  
- Ajout d’une **interface utilisateur** 
- Persistance du corpus et des index dans une base de données  
- Amélioration du prétraitement pour la **langue arabe**

---

## Etudiant

**Achehboune Youssef**  
Projet réalisé dans le cadre du module *Text Mining*  
FSDM — 2025  

---
