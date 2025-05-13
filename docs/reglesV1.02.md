# Fantasy Hospital V1

> [!WARNING]  
> En cours de rédaction. A revérifier.

## Hopital

- L'hopital soigne des créatures magiques (elfes, orcs, vampires, etc.)
- L'hopital est composé de services : classiques ou spéciaux
- Chaque créature a : nom, sexe, poids, taille, âge, moral, liste de maladies

## Services

Types de services :
  - Service classiques : soigne un (ou plusieurs) type(s) de créature(s) A VERIFIER POUR PLUSIEURS MEDECINS
  - Service crypte : soigne les morts-vivants, avec ventilation et température A IMPLEMENTER
  - Service quarantaine : soigne les créatures contaminées, avec isolation A IMPLEMENTER

Caractéristiques :
  - Nom et superficie
  - Capacité maximum et nombre actuel
  - Liste des créatures
  - Budget

## Budget

- Un budget total est fixé pour l'hôpital (30 points) A IMPLEMENTER
- Chaque service a un budget :
    - Budget inexistant : 0 point
    - Budget médiocre : 1 point
    - Budget faible : 2 points
    - Budget correct : 3 points
    - Budget bon : 4 points
- Le budget d'un service affecte la qualité des soins et le moral des créatures A IMPLEMENTER

## Temps

- Le temps s'écoule par cycles de 5 secondes
- À chaque cycle, l'état des créatures et des services peut évoluer A IMPLEMENTER
- Le joueur incarne un ou plusieurs médecins et prend des décisions à chaque cycle A IMPLEMENTER

## Actions

- Examiner un service médical (voir l'état et la liste des créatures) PREMIERE VERSION, A AMELIORER
- Soigner une créature DONE
- Changer le budget d'un service A IMPLEMENTER
- Envoyer une créature à l'autre monde (ex : crypte, quarantaine) A IMPLEMENTER

## But du jeu
A IMPLEMENTER
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
  - Attendre (baisse le moral) DONE
  - Hurler (si moral bas) DONE
  - S'emporter (après plusieurs hurlements, risque de contamination) DONE
  - Tomber malade DONE
  - Être soigné DONE
  - Trépasser DONE

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
Caractéristiques : nom, sexe, âge
Immunité aux maladies

# Nombres

Médecins
  - Moral : de 0 à 100% -> se suicide à 0% DONE
  - Dépression : lorsqu'un patient meurt, le médecin le plus fragile perd 40 pts (%) de moral VICTIME DONE
  - Soigne : gagne 15 points de moral par patient soigné (Soigne 1 maladie) DONE

Monstres
  - Moral : de 0 à 100%, hurle à chaque tour à partir de 0%
Actuellement, à 0 de moral la créature hurle 3 fois puis s'emporte, puis hurle à nouveau si moral toujours à 0
  - Attendre : 
    - si Triage : si autre triage, perd 5 points moral. Sinon, 10 points. Perd 5 point moral par maladie DONE
    - si VIP : au bout de 4 tours, moral = 0% DONE
  - Hurlement : compteur jusqu'à 3 fois avant de s'emporter DONE
  - S'emporter : a 15% chance de contaminer une autre créature à proximité DONE
  - Tomber malade :
    - échelle de maladie sur 10 DONE
    - état maladie (à l'arrivée à l'hopital) aléatoire entre 0 et 7 max (ex: 7 sur 10pts)
    Actuellement, maladie a toujours niveau 0 lorsqu'il tombe malade
    - son niveau de maladie s'aggrave de 1pts par tour DONE
  - Etre soigne :
    - redonne 50 pts (%) de moral et supprime une maladie DONE
  - Trepasser :
    -  si maladie niveau max DONE
    -  si > 4 maladies DONE
    -  30% chance par tour quand il s'emporte DONE
    - Regénérants : quand meurt, si il avait une seule maladie, revit et a 50% de chance de reprendre une maladie ou sortir de l'hosto DONE
    - Contaminants : 
    - Demoralisants : démoralise 2 des créatures de leur service, 10 points moral DONE

Maladies :
  - Moral : reduit de 5 points par tour et par maladie DONE

Services :
selon budget du service ? 
- Quarantaine :
  - Isolation : Nbr places (2), elligible après 3 tours d'hurlement A IMPLEMENTER
- Crypte : 
  - Nbr places (2), autogestion A IMPLEMENTER

Ecoulement du jeu, modifications aléatoires
- Modifier l'état de certaines créatures : 
  - ajouter maladies : Les créatures ont toutes 5% de chance de contracter une nouvelle maladie à chaque tour DONE
- Modifier l'état de services : A voir random modification du budget d'un service A IMPLEMENTER
- 

(Enum : budget)

# Écoulement du jeu

Initialisation
  - récupération des paramètres du jeu choisis par l'utilisateur
    - nombre de services max
    - nombre nouveaux médecins par tour
    - nombre nouvelles créatures par tour
  - créer l'hopital
  - créer 5 créatures aléatoire avec une maladie aléatoire
  - créer la salle d'attente et y mettre les monstres
  - affecter les créatures à la salle d'attente
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
//(ajout maladie, évoluer maladie, évoluer moral)
//pour chaque évolution maladie, doit vérifier si créature meurt
prendre une liste de créatures random
leur ajouter une maladie random
si créature a déjà cette maladie, on augmente le niveau de la maladie de 1
si maladie foudroyante ou si créature a trop de maladies (plus de 5 par ex ?)
créature meurt (maladie niveau max, dépend si niveau actuel maladie random quand on créé une maladie random)
ajouter créature stats
fin si  
prendre une liste de créatures random
augmenter d'un niveau une de leur maladie (random aussi ? plusieurs niveau à la fois ? plusieurs maladies en même temps ?)
si maladie passe au niveau max
1 chance sur 2 que créature meurt instant, sinon fin du tour (laisse 1 chance que médecin soigne)
fin si
pour toutes les créatures, suivant paramètres, faire baisser leur moral à cause maladies

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
soigner une maladie d'une créature (ce qui redonne du moral à la créature et au médecin)
déplacer des créatures
déplacer créatures salle attente vers service
ajuster budget services médicaux

//vérifier si des créatures meurent (maladie niveau max)
pour toutes les créatures
pour toutes leurs maladies
si maladie est au niveau max
créature meurt (dans méthode trepasser, suivant si implémente interface méthode appelée de l'interface)
moral des médecins du service baisse
fin si
fin pour tout
fin pour tout


fin