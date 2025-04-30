# Pseudo-code

```java
// Constantes de paramétrages
final int MAX_BUDGET = 30;
final int MAX_SERVICES = 8;
final int RANDOM_CREATURES_PER_CYCLE = 10;
final int SECONDS_PER_CYCLE = 5;
final double CHANCE_TO_GET_A_NEW_DOCTOR_PER_CYCLE = 0.1;

// Créer l'hôpital
Hospital hospital = new Hospital("Fantasy Hospital", MAX_BUDGET, MAX_SERVICES);

// Créer la liste d'attente
ArrayList<Creature> waitingList = new ArrayList<>();

// Créer les budgets
Budget budget = new Budget({
    NONE: 0,
    LOW: 1,
    MEDIUM: 2,
    HIGH: 3
});

// Créer des docteurs
Doctor doctor1 = new Doctor(...);
Doctor doctor2 = new Doctor(...);
Doctor doctor3 = new Doctor(...);

/*
  Créer des créatures aléatoires
    - moral aléatoire
    - niveau maladie aléatoire
*/
Creature creature1 = hospital.addRandomCreature();
Creature creature2 = hospital.addRandomCreature();
Creature creature3 = hospital.addRandomCreature();

// Créer des services adaptés aux types des créatures
Service service1 = new ClassicService(creature1.getType(), ...);
Service service2 = new ClassicService(creature2.getType(), ...);
Service service3 = new ClassicService(creature3.getType(), ...);

// Caculer le budget max d'un serrvice par rapport au nombre total de services
int maxBudgetPerService = MAX_BUDGET / MAX_SERVICES;

// Assigner des budgets aux services
service1.setBudget(budget.LOW);
service2.setBudget(budget.LOW);
service3.setBudget(budget.MEDIUM);

// Lancer la simulation
while (simulationActive) {

    // Attendre 5 secondes
    wait(SECONDS_PER_CYCLE);

    // Faire évoluer les états des créatures
    for (int i = 0; i < MAX_ACTIONS_PER_CYCLE; i++) {
        // Choisir un monstre aléatoire
        Creature creature = chooseRandomCreature();

        // Obtenir le budget du service de la créature
        int budget = creature.getService().getBudget();

        // Obtenir le nombre de medecins du service
        int numberOfDoctors = creature.getService().getNumberOfDoctors();

        // Faire évoluer l'état de la maladie de la créature selon le budget du service
        creature.evolveStateRandomly(budget, numberOfDoctors);

        // 5% de chance que le monstre déclenche son comportement spécial (hurlement, contagion,)
    }

    // Ajouter 5 monstres
    for (int i = 0; i < RANDOM_CREATURES_PER_CYCLE; i++) {
        Creature creature = hospital.addRandomCreature();
        waitingList.add(creature);
    }

    // Tous les monstres sont morts
    if (hospital.getCreatures().isEmpty()) simulationActive = false;

    // 1% de chance qu'un monstre pète un cable et tue tout le monde
    simulationActive = hospital.explode();
}

// Afficher le résultat de la simulation
Scene scene = new Scene(hospital);
```
