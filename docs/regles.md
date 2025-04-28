# Fantasy Hospital

> [!WARNING]  
> En cours de rédaction. A revérifier.

## Hopital

- L'hopital soigne des créatures magiques (elfes, orcs, vampires, etc.)
- L'hopital est composé de services : classiques ou spéciaux
- Chaque créature a : nom, sexe, poids, taille, âge, moral, liste de maladies

## Services

Types de services :
  - Service classiques : soigne un (ou plusieurs) type(s) de créature(s)
  - Service crypte : soigne les morts-vivants, avec ventilation et température
  - Service quarantaine : soigne les créatures contaminées, avec isolation

Caractéristiques :
  - Nom et superficie
  - Capacité maximum et nombre actuel
  - Liste des créatures
  - Budget

## Budget

- Un budget total est fixé pour l'hôpital (30 points)
- Chaque service a un budget :
    - Budget inexistant : 0 point
    - Budget médiocre : 1 point
    - Budget faible : 2 points
    - Budget correct : 3 points
    - Budget bon : 4 points
- Le budget d'un service affecte la qualité des soins et le moral des créatures

## Temps

- Le temps s'écoule par cycles de 5 secondes
- À chaque cycle, l'état des créatures et des services peut évoluer
- Le joueur incarne un ou plusieurs médecins et prend des décisions à chaque cycle

## Actions

- Examiner un service médical (voir l'état et la liste des créatures)
- Soigner une créature
- Changer le budget d'un service
- Envoyer une créature à l'autre monde (ex : crypte, quarantaine)

## But du jeu

- Gérer au mieux un hôpital peuplé de créatures magiques, en maintenant leur santé, leur moral et l'équilibre des services, tout en respectant les contraintes de budget.

## Monstres

Types de monstres et comportements :
  - Elfe : démoralise à sa mort, VIP prioritaire
  - HommeBete : peut contaminer, attend mieux en groupe
  - Lycanthrope : peut contaminer, attend mieux en groupe
  - Nain : VIP prioritaire
  - Orque : peut contaminer, attend mieux en groupe
  - Reptilien : VIP prioritaire
  - Vampire : VIP prioritaire, démoralise à sa mort, peut contaminer, régénère
  - Zombie : attend mieux en groupe, régénère

Actions possibles :
  - Attendre (baisse le moral)
  - Hurler (si moral bas)
  - S'emporter (après plusieurs hurlements, risque de contamination)
  - Tomber malade
  - Être soigné
  - Trépasser

## Maladies

Types de maladies :
  - DRS (Dépendance aux Réseaux Sociaux)
  - FOMO (Fear Of Missing Out)
  - MDC (Maladie Débilitante Chronique)
  - NDMAD (Nom De Maladie À Définir)
  - PEC (Porphyrie Érythropoïétique Congénitale)
  - ZPL (Zoopathie Paraphrénique Lycanthropique)

Caractéristiques :
  - Nom complet et abrégé
  - Niveau actuel et maximum (léthal)

## Medecins

Les médecins sont des créatures magiques avec un type aussi.
Ils n'ont accès qu'aux services du même type que le leur.
Caractéristiques : nom, sexe, âge
Immunité aux maladies
