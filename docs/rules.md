# Fantasy Hospital V1

> [!WARNING]  
> En cours de rédaction. A revérifier.

## Hopital

- L'hopital soigne des créatures magiques (elfes, orcs, vampires, etc.)
- L'hopital est composé de services : classiques ou spéciaux
- Chaque créature a : nom, sexe, poids, taille, âge, moral, liste de diseases

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

Status de monstres :
  - VIP
  - 

Actions possibles :
  - Attendre (baisse le moral)
  - Hurler (si moral bas)
  - S'emporter (après plusieurs hurlements, risque de contamination)
  - Tomber malade
  - Être soigné
  - Trépasser

## Diseases

Types de diseases :
  - DRS (Dépendance aux Réseaux Sociaux)
  - FOMO (Fear Of Missing Out)
  - MDC (Disease Débilitante Chronique)
  - NDMAD (Nom De Disease À Définir)
  - PEC (Porphyrie Érythropoïétique Congénitale)
  - ZPL (Zoopathie Paraphrénique Lycanthropique)

Caractéristiques :
  - Nom complet et abrégé
  - Niveau actuel et maximum (léthal)

## Doctors

Les médecins sont des créatures magiques avec un type aussi.
Ils n'ont accès qu'aux services du même type que le leur.
Caractéristiques : nom, sexe, âge
Immunité aux diseases

# Nombres

Médecins
  - Moral : de 0 à 100% -> se suicide à 0%
  - Dépression : lorsqu'un patient meurt, le médecin le plus fragile perd 40 pts (%) de moral VICTIME
  - Soigne : gagne 15 points de moral par patient soigné (Soigne 1 disease ou totalement ?)

Monstres
  - Moral : de 0 à 100%, hurle à chaque tour à partir de 0%
  - Attendre : 
    - si Triage : si autre triage, perd 5 points moral. Sinon, 10 points. Perd 10 point moral par disease
    - si VIP : au bout de 4 tours, moral = 0%
  - Hurlement : compteur jusqu'à 3 fois avant de s'emporter
  - S'emporter : a 15% chance de contaminer une autre créature à proximité
  - Tomber malade :
    - échelle de disease sur 10
    - état disease (à l'arrivée à l'hopital) aléatoire entre 0 et 7 max (ex: 7 sur 10pts)
    - son état s'agrave de 1pts par tour
  - Etre soigne :
    - redonne 50 pts (%) de moral et supprime une disease
  - Trepasser :
    -  si disease niveau max
    -  si > 4 diseases
    -  30% chance par tour quand il s'emporte
    - Regénérants : quand meurt, si il avait une seule disease, revit et a 50% de chance de reprendre une disease ou sortir de l'hosto
    - Contaminants : 
    - Demoralisants : démoralise 2 des créatures de leur service, 10 points moral

Diseases :
  - Moral : reduit de 5 points par tour et par disease

Services :
selon budget du service ? 
- Quarantaine :
  - Isolation : Nbr places (2), elligible après 3 tours d'hurlement
- Crypte : 
  - Nbr places (2), autogestion

Ecoulement du jeu, modifications aléatoires
- Modifier l'état de certaines créatures : 
  - ajouter diseases : Les créatures ont toutes 5% de chance de contracter une nouvelle disease à chaque tour
- Modifier l'état de services : A voir random modification du budget d'un service
- 

(Enum : budget)

# Écoulement du jeu

Initialisation
  - récupération des paramètres du jeu choisis par l'utilisateur
    - nombre de services max
    - nombre nouveaux médecins par tour
    - nombre nouvelles créatures par tour
  - créer l'hopital
  - créer 5 créatures aléatoire avec une disease aléatoire
  - créer la room d'attente et y mettre les monstres
  - affecter les créatures à la room d'attente
  - créer 3 médecins
  - créer les services adaptés aux monstres et les ajouter à l'hopital
  - affecter les médecins aux services
  - commencer le 1er tour

Boucles
  - attendre : moral -> hurelement -> emportement
  - évolution 
  - logs








tant que la partie n'est pas terminée
//modifier aléatoirement l'état de certaines créatures
//(ajout disease, évoluer disease, évoluer moral)
//pour chaque évolution disease, doit vérifier si créature meurt
prendre une liste de créatures random
leur ajouter une disease random
si créature a déjà cette disease, on augmente le niveau de la disease de 1
si disease foudroyante ou si créature a trop de diseases (plus de 5 par ex ?)
créature meurt (disease niveau max, dépend si niveau actuel disease random quand on créé une disease random)
ajouter créature stats
fin si  
prendre une liste de créatures random
augmenter d'un niveau une de leur disease (random aussi ? plusieurs niveau à la fois ? plusieurs diseases en même temps ?)
si disease passe au niveau max
1 chance sur 2 que créature meurt instant, sinon fin du tour (laisse 1 chance que médecin soigne)
fin si
pour toutes les créatures, suivant paramètres, faire baisser leur moral à cause diseases

//modifier aléatoirement l'état de certains services médicaux (budget, isolation, temp…)
prendre liste services médicaux random
baisser ou augmenter leur budget random
pour services spéciaux, modifier random leur attribut
créer random des médecins ?

//actions des créatures
pour toutes les créatures
si moral au plus bas
hurler
fin si
s'emporter (définir cette méthode ?)
fin pour tout

//actions de tous les médecins
//suivant paramètres (privilégier soin créature ou isolation créature presque morte?)
soigner une disease d'une créature (ce qui redonne du moral à la créature et au médecin)
déplacer des créatures
déplacer créatures room attente vers service
ajuster budget services médicaux

//vérifier si des créatures meurent (disease niveau max)
pour toutes les créatures
pour toutes leurs diseases
si disease est au niveau max
créature meurt (dans méthode trepasser, suivant si implémente interface méthode appelée de l'interface)
moral des médecins du service baisse
fin si
fin pour tout
fin pour tout


fin