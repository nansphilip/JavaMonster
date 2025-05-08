# 🌳 Arbre d'héritage — Fantasy Hospital

## 🧬 Classes principales

```
Bete (abstraite)
│
├── Creature (abstraite)
│   │
│   ├── HabitantTriage (abstraite)
│   │   ├── Orque         [Contaminant]
│   │   ├── HommeBete     [Contaminant]
│   │   ├── Lycanthrope   [Contaminant]
│   │   └── Zombie        [Regenerant]
│   │
│   └── ClientVIP (abstraite)
│       ├── Elfe          [Demoralisant]
│       ├── Nain
│       ├── Reptilien
│       └── Vampire       [Regenerant, Contaminant, Demoralisant]
│
└── Medecin (concrète)
```

---

## 🏥 Salles et services

```
Salle
│
└── ServiceMedical
    ├── Quarantaine
    └── Crypte
```

---

## 🏢 Autres entités

```
Hopital
Maladie
```

---

## 🧵 Threads

```
MoralThread (Runnable)
```

---

## 🧩 Interfaces comportementales

```
Contaminant
Regenerant
Demoralisant
```

---

## 🏷️ Enums

```
Races
MaladieType
ActionType
```
