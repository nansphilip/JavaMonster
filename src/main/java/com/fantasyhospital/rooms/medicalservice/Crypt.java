package com.fantasyhospital.rooms.medicalservice;

import lombok.extern.slf4j.Slf4j;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Regenerating;
import com.fantasyhospital.model.disease.Disease;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

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
@Slf4j public final class Crypt extends MedicalService {

    /**
     * Airflow, true if working false otherwise
     */
    private boolean airflow;
    private int temperature;

    /**
     * The list of the creatures of the crypt, associated with the int number of tour they are waiting in the crypt with temperature < 30°C
     */
    private final ConcurrentHashMap<Creature, Integer> creatureWaitNbTour = new ConcurrentHashMap<>();

    public Crypt(String name, double area, int MAX_CAPACITY, String budget) {
        super(name, area, MAX_CAPACITY, budget);
        this.airflow = true;
        this.temperature = 20;
    }

    /**
     * Adds a creature to the crypte if the capacity and interface (regenerating) are compatible.
     *
     * @param creature the creature to add to the service
     */
    @Override
    public boolean addCreature(Creature creature) {
        if(Regenerating.class.isAssignableFrom(creature.getClass())) {
            if (creatures.size() >= MAX_CREATURE) {
                return false;
            }
            this.creatures.add(creature);
            this.creatureWaitNbTour.put(creature, 0);
            //return super.addCreature(creature);
        }
        return false;
    }

    /**
     * Remove a creature from the list of the room
     *
     * @param creature to remove
     * @return true if removed, false else
     */
    @Override
    public boolean removeCreature(Creature creature) {
        creatureWaitNbTour.remove(creature);
        return super.removeCreature(creature);
    }

    /**
     * Make the airflow break down
     */
    public void airflowBreakDown(){
        this.airflow = false;
    }

    /**
     * Make the airflow repaired
     */
    public void airflowRepair(){
        this.airflow = true;
    }

    /**
     * Manage the temperature according to the rules of the crypt
     */
    public void manageTemperature(){
        if(this.airflow && this.temperature > 20){
            this.temperature = Math.max(20, this.temperature - 10);
            log.info("La clim fait baisser la température de la crypte : {}°C", temperature);
        } else if(!this.airflow && this.temperature < 50){
            this.temperature = Math.min(50, this.temperature + 2 + new Random().nextInt(8));
            log.info("La température augmente encore dans la crypt : {}°C !",  this.temperature);
        } else {
            log.info("Température : {}°C Clim : {}", this.temperature, this.airflow);
        }
    }

    /**
     * Manage the airflow according to the rules of the crypt
     */
    public void manageAirFlow(){
        if(this.airflow && Math.random() < 0.30){
            airflowBreakDown();
            log.info("Pas de chance, la clim a encore lâché..");
        } else if(!this.airflow && Math.random() < 0.25){
            airflowRepair();
            log.info("Un regénérant climatiseur répare la ventilation !");
        }
    }

    /**
     * Manage the creatures according to the rules
     */
    public void manageCreatures(){
        if(this.creatures.isEmpty()){
            return;
        }
        //Check for each creature if it is waiting for 3 rounds, so they get out
        HashMap<Creature, Integer> listCopy = new HashMap<>(this.creatureWaitNbTour);
        listCopy.forEach((creature, nb) -> {
            if(nb == 3){
                //Cure the creature
                log.info("La créature {} a attendu 3 tours au frais de la crypte, elle est soignée !", creature.getFullName());
                ((Regenerating) creature).cureCreatureInCrypt(creature);
            }
        });

        double chanceToGetSick;
        if(this.temperature == 50){ //Temp 50°C, 100% chance tomber malade
            chanceToGetSick = 1;
        } else if (this.temperature >= 30) { //Temp > 30°C, 50% chance tomber malade
            chanceToGetSick = 0.5;
        } else { // Temp < 30°C, creature waits
            chanceToGetSick = 0.0;
            creatureWaitNbTour.forEach((k, v) -> {
                v++; //Increments nb for each entry
                creatureWaitNbTour.replace(k, v);
            });
        }
        creatureWaitNbTour.forEach((k, v) -> {
            if(Math.random() < chanceToGetSick){
                Disease disease = new Disease();
                log.info("Quel cagnard dans la crypte, la créature {} attrape {} !", k.getFullName(), disease.getName());
                k.fallSick(disease);
            }
        });
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
