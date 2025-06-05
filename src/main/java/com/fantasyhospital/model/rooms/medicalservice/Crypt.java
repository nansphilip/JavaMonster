package com.fantasyhospital.model.rooms.medicalservice;

import com.fantasyhospital.enums.BudgetType;
import lombok.extern.slf4j.Slf4j;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Regenerating;
import com.fantasyhospital.model.disease.Disease;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static com.fantasyhospital.model.creatures.Doctor.INCREASE_BUDGET_SERVICE;

/**
 * Class of the special room crypt
 * The airflow regulate the temperature to 20°C when it works, or reduce by 10°C each tour if the temperature is over 20°C
 * The temperature rise randomly between 2 and 10°C by tour if the airflow is broken
 * There is 25% chance that a creature repair the airflow when broken each tour
 * There is 30% chance that the airflow break down each tour
 * If a creature waits 3 tours (non consecutivly) in the crypt with a maximum temperature of 30°C, it is completely cured
 * If the temperature rises over 30°C, creatures have 50% to get sick (1 disease)
 * If the temperature rises at maximum, 50°C, they likely to get sick (1 disease)
 */
@Slf4j 
public final class Crypt extends MedicalService {

    // Temperature constants
    private static final int MIN_TEMPERATURE = 20;
    private static final int HEALING_MAX_TEMPERATURE = 30;
    private static final int MAX_TEMPERATURE = 50;
    private static final int TEMPERATURE_DECREASE_RATE = 10;
    private static final int MIN_TEMPERATURE_INCREASE = 2;
    private static final int MAX_TEMPERATURE_INCREASE = 10;
    
    // Probability constants
    private static final double AIRFLOW_BREAKDOWN_CHANCE = 0.30;
    private static final double BASE_AIRFLOW_REPAIR_CHANCE = 0.25;
    private static final double HIGH_TEMPERATURE_DISEASE_CHANCE = 0.5;
    private static final double MAX_TEMPERATURE_DISEASE_CHANCE = 1.0;
    
    // Healing constants
    private static final int REQUIRED_HEALING_TOURS = 3;

    // Budget constants
    private static final int DECREASE_CALCUL_BUDGET = 5;

    /**
     * Airflow, true if working false otherwise
     */
    private boolean airflow;
    private int temperature;

    /**
     * The list of the creatures of the crypt, associated with the int number of tour they are waiting in the crypt with temperature < 30°C
     */
    private final ConcurrentHashMap<Creature, Integer> creatureWaitNbTour = new ConcurrentHashMap<>();
    
    private final Random random = new Random();

    public Crypt(String name, double area, int MAX_CAPACITY, int budget) {
        super(name, area, MAX_CAPACITY, budget);
        this.airflow = true;
        this.temperature = MIN_TEMPERATURE;
    }

    // Ajout des getters manquants
    /**
     * Retourne l'état actuel du système de ventilation
     * @return true si le système fonctionne, false sinon
     */
    public boolean isAirflow() {
        return airflow;
    }

    /**
     * Retourne la température actuelle de la crypte
     * @return température en degrés Celsius
     */
    public int getTemperature() {
        return temperature;
    }

    /**
     * Retourne la map des créatures et de leur nombre de tours d'attente
     * @return la map des créatures et de leur nombre de tours d'attente
     */
    public Map<Creature, Integer> getCreatureWaitNbTour() {
        return creatureWaitNbTour;
    }

    /**
     * Adds a creature to the crypt if it implements Regenerating interface and there's space available.
     *
     * @param creature the creature to add to the service
     * @return true if the creature was successfully added, false otherwise
     * @throws NullPointerException if creature is null
     */
    @Override
    public boolean addCreature(Creature creature) {
        Objects.requireNonNull(creature, "Creature cannot be null");
        
        // Check if creature implements Regenerating interface and if there's space
        if (!Regenerating.class.isAssignableFrom(creature.getClass())) {
            log.debug("Cannot add creature {} to crypt: creature must implement Regenerating interface", creature.getFullName());
            return false;
        }
        
        if (creatures.size() >= MAX_CREATURE) {
            log.debug("Cannot add creature {} to crypt: maximum capacity reached", creature.getFullName());
            return false;
        }
        
        // Add the creature and initialize its waiting counter
        this.creatures.add(creature);
        this.creatureWaitNbTour.put(creature, 0);
        return true;
    }

    /**
     * Remove a creature from the crypt
     *
     * @param creature to remove
     * @return true if removed, false else
     * @throws NullPointerException if creature is null
     */
    @Override
    public boolean removeCreature(Creature creature) {
        Objects.requireNonNull(creature, "Creature cannot be null");
        creatureWaitNbTour.remove(creature);
        return super.removeCreature(creature);
    }

    /**
     * Calculate the real crypt budget according to the temperature and the airflow
     */
    public int getCryptBudget() {
        // The temperature and airflow is included in the calcul of the crypt budget
        int calculatedBudget = this.budget;
        calculatedBudget += this.airflow ? DECREASE_CALCUL_BUDGET : - DECREASE_CALCUL_BUDGET;
        if(this.temperature < HEALING_MAX_TEMPERATURE){
            calculatedBudget += DECREASE_CALCUL_BUDGET;
        } else {
            calculatedBudget -= DECREASE_CALCUL_BUDGET;
        }
        if(calculatedBudget < 0){
            calculatedBudget = 0;
        } else if(calculatedBudget > 100){
            calculatedBudget = 100;
        }
        log.info("budget crypt : {}",  calculatedBudget);
        return calculatedBudget;
    }

    /**
     * Make the airflow break down
     */
    public void airflowBreakDown() {
        this.airflow = false;
        log.info("Ah shit, here we go again. La clim est tombé en panne");
    }

    /**
     * Make the airflow repaired
     */
    public void airflowRepair() {
        this.airflow = true;
        log.info("Un ouvrier regénérant répare la clim !");
    }

    /**
     * Manage the temperature according to the rules of the crypt
     * More gradual temperature changes based on current conditions
     */
    public void manageTemperature() {
        int previousTemperature = this.temperature;
        
        if (this.airflow && this.temperature > MIN_TEMPERATURE) { // The airflow doesnt work and the temperature is above min temp
            int coolingRate = Math.min(TEMPERATURE_DECREASE_RATE, (this.temperature - MIN_TEMPERATURE) / 2 + 1);
            this.temperature = Math.max(MIN_TEMPERATURE, this.temperature - coolingRate);
            
            if (previousTemperature != this.temperature) {
                log.info("La clim fait baisser la température de la crypte : {}°C (-{}°C)", temperature, previousTemperature - temperature);
            }
        } else if (!this.airflow && this.temperature < MAX_TEMPERATURE) {
            // More gradual increase based on current temperature
            int maxIncrease = Math.min(MAX_TEMPERATURE_INCREASE, (MAX_TEMPERATURE - this.temperature) / 5 + 1);
            int increase = MIN_TEMPERATURE_INCREASE + random.nextInt(Math.max(1,maxIncrease - MIN_TEMPERATURE_INCREASE + 1));
            this.temperature = Math.min(MAX_TEMPERATURE, this.temperature + increase);
            
            log.info("La température augmente dans la crypt : {}°C (+{}°C) !", this.temperature, this.temperature - previousTemperature);
        } else {
            log.info("Température : {}°C Clim : {}", this.temperature, this.airflow ? "fonctionnelle" : "en panne");
        }
    }

    /**
     * Manage the airflow according to the rules of the crypt
     * Repair chance scales with the number of creatures present in the crypt (more creatures = higher chance)
     */
    public void manageAirFlow() {
        if (this.airflow && random.nextDouble() < AIRFLOW_BREAKDOWN_CHANCE) {
            airflowBreakDown();
        } else if (!this.airflow) {
            // Repair chance increases with more creatures (up to double the base chance with MAX_CREATURE)
            double repairChance = BASE_AIRFLOW_REPAIR_CHANCE * (1 + (double) creatures.size() / MAX_CREATURE);
            repairChance = Math.min(repairChance, 2 * BASE_AIRFLOW_REPAIR_CHANCE);
            
            if (random.nextDouble() < repairChance) {
                airflowRepair();
            }
        }
    }

    /**
     * Manage creatures according to the rules of the crypt (to get sick)
     */
    public void manageCreatures(){
        if(this.creatures.isEmpty()){
            return;
        }

        double chanceToGetSick = getChanceToGetSick();

        creatureWaitNbTour.forEach((creature, nb) -> {
            if(nb == REQUIRED_HEALING_TOURS){
                //Cure the creature
                log.info("La créature {} a attendu {} tours au frais de la crypte, elle est soignée !", creature.getFullName(), REQUIRED_HEALING_TOURS);
                ((Regenerating) creature).cureCreatureInCrypt(creature);
                this.setBudget(Math.min(this.getBudget() + INCREASE_BUDGET_SERVICE,100));
                log.info("Le soin fait augmenter le budget du service {} de {} points ({} pts)", this.getName(), INCREASE_BUDGET_SERVICE, this.getBudget());
            } else if(chanceToGetSick == 0.0){
                nb++;
                creatureWaitNbTour.replace(creature, nb);
            } else if(Math.random() < chanceToGetSick){
                Disease disease = new Disease();
                log.info("Quel cagnard dans la crypte, la créature {} attrape {} !", creature.getFullName(), disease.getName());
                creature.fallSick(disease);
            }
        });
    }

    /**
     * Compare the temperature to get the chance to get sick for the creatures
     * If temperature is enough cool, chance is 0
     * @return the chance
     */
    private double getChanceToGetSick() {
        double chanceToGetSick;
        if(this.temperature == MAX_TEMPERATURE){ //Temp 50°C, 100% chance tomber malade
            chanceToGetSick = MAX_TEMPERATURE_DISEASE_CHANCE;
        } else if (this.temperature >= HEALING_MAX_TEMPERATURE) { //Temp > 30°C, 50% chance tomber malade
            chanceToGetSick = HIGH_TEMPERATURE_DISEASE_CHANCE;
        } else { // Temp < 30°C, creature waits
            chanceToGetSick = 0.0;
        }
        return chanceToGetSick;
    }

    /**
     * Manage all the crypt
     */
    public void manageCrypt(){
        manageAirFlow();
        manageTemperature();
        manageCreatures();
    }
}
