# 🌳 Inheritance Tree — Fantasy Hospital

## 🧬 Mains classes

```
Beast (abstract)
│
├── Creature (abstract)
│   │
│   ├── TriageResident (abstract)
│   │   ├── Orc         [Contaminant]
│   │   ├── Werebeast     [Contaminant]
│   │   ├── Lycanthrope   [Contaminant]
│   │   └── Zombie        [Regenerating]
│   │
│   └── VIPClient (abstract)
│       ├── Elf          [Demoralizing]
│       ├── Dwarf
│       ├── Reptilian
│       └── Vampire       [Regeneratin, Contaminant, Demoralizing]
│
└── Doctor (concret)
```

---

## 🏥 Rooms and services

```
Room
│
└── MedicalService
    ├── Quarantine
    └── Crypt
```

---

## 🏢 Other Entities

```
Hospital
Disease
```

---

## 🧵 Threads

```
MoralThread (Runnable)
```

---

## 🧩 Behavioral Interfaces

```
Contaminant
Regenerating
Demoralizing
```

---

## 🏷️ Enums

```
Races
DiseaseType
ActionType
```
