# Modélisation des classes et héritages

Ce dossier contient toutes les classes, interfaces et héritages principaux du projet Fantasy Hospital.

## Structure du dossier `model`

```
model/
│
├── Creature.java           # Classe abstraite de base pour toutes les créatures
├── Maladie.java            # Classe abstraite de base pour toutes les maladies
├── README.md               # Ce fichier de documentation
│
├── creatures/              # Toutes les créatures concrètes et le médecin
│   ├── Elfe.java
│   ├── Nain.java
│   ├── Orque.java
│   ├── HommeBete.java
│   ├── Zombie.java
│   ├── Vampire.java
│   ├── Lycanthrope.java
│   ├── Reptilien.java
│   └── Medecin.java
│
├── maladies/               # Toutes les maladies concrètes
│   ├── MDC.java
│   ├── FOMO.java
│   ├── DRS.java
│   ├── PEC.java
│   ├── ZPL.java
│   └── NDMAD.java
│
├── services/               # Tous les services médicaux et l'hôpital
│   ├── ServiceMedical.java
│   ├── CentreQuarantaine.java
│   ├── Crypte.java
│   └── HopitalFantastique.java
│
└── interfaces/             # Interfaces pour comportements spécifiques
    ├── Bestial.java
    ├── Regenerant.java
    ├── Contagieux.java
    └── VIP.java
```

## Schéma d'héritage et d'implémentation

```
Creature (abstraite)
│
├── Elfe         (VIP)
├── Nain         (VIP)
├── Orque        (Bestial, Contagieux)
├── HommeBete    (Bestial, Contagieux)
├── Zombie       (Regenerant, Contagieux)
├── Vampire      (Bestial, Regenerant, Contagieux, VIP)
├── Lycanthrope  (Bestial, Contagieux)
└── Reptilien    (VIP)

Maladie (abstraite)
│
├── MDC
├── FOMO
├── DRS
├── PEC
├── ZPL
└── NDMAD

ServiceMedical (abstraite)
│
├── CentreQuarantaine
└── Crypte

Medecin (hérite de Creature)

HopitalFantastique

// Interfaces
Bestial, Regenerant, Contagieux, VIP
```

## Explications

- **creatures/** : toutes les créatures concrètes (patients et médecins), héritent de `Creature`.
- **maladies/** : toutes les maladies concrètes, héritent de `Maladie`.
- **services/** : tous les services médicaux, héritent de `ServiceMedical` ou sont liés à l'hôpital.
- **interfaces/** : comportements spécifiques (contamination, régénération, VIP, etc.).
- **README.md** : ce fichier explicatif.

Chaque classe concrète hérite de la classe abstraite appropriée et implémente les interfaces correspondant à ses comportements spécifiques.
