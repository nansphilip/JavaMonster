# Modélisation des classes et héritages

Ce dossier contient toutes les classes, interfaces et héritages principaux du projet Fantasy Hospital.

## Structure du dossier `model`

```
model/
│
├── Creature.java           # Classe abstraite de base pour toutes les créatures
├── Maladie.java           # Classe abstraite de base pour toutes les maladies
├── Service.java           # Classe abstraite de base pour tous les services
├── Hopital.java          # Classe principale de l'hôpital
├── Medecin.java           # Classe médecin (hérite de Creature)
│
├── creatures/             # Toutes les créatures concrètes
│   ├── Elfe.java
│   ├── Nain.java
│   ├── Orque.java
│   ├── HommeBete.java
│   ├── Zombie.java
│   ├── Vampire.java
│   ├── Lycanthrope.java
│   └── Reptilien.java
│
├── maladies/             # Toutes les maladies concrètes
│   ├── MDC.java
│   ├── FOMO.java
│   ├── DRS.java
│   ├── PEC.java
│   ├── ZPL.java
│   └── NDMAD.java
│
├── services/             # Services médicaux concrets
│   ├── ServiceClassique.java
│   ├── CentreQuarantaine.java
│   └── Crypte.java
│
└── interfaces/           # Interfaces pour comportements spécifiques
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

Service (abstraite)
│
├── ServiceClassique
├── CentreQuarantaine
└── Crypte

Medecin (hérite de Creature)

Hopital

// Interfaces
Bestial, Regenerant, Contagieux, VIP
```

## Explications

- **Creature.java** : classe abstraite de base pour toutes les créatures
- **Maladie.java** : classe abstraite de base pour toutes les maladies
- **Service.java** : classe abstraite de base pour tous les services
- **Hopital.java** : classe principale gérant l'hôpital
- **creatures/** : toutes les créatures concrètes (patients et médecins)
- **maladies/** : toutes les maladies concrètes
- **services/** : tous les services médicaux concrets
- **interfaces/** : comportements spécifiques (contamination, régénération, VIP, etc.)

Chaque classe concrète hérite de la classe abstraite appropriée et implémente les interfaces correspondant à ses comportements spécifiques.
