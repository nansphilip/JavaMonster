package com.fantasyhospital.rooms.medicalservice;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Regenerating;

import java.util.HashMap;
import java.util.Random;

public final class Crypt extends MedicalService {

    private boolean airflow;
    private int temperature;
    private HashMap<Creature, Integer> nbToursCreatureWaits = new HashMap<>();

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
            //return super.addCreature(creature);
        }
        return false;
    }

    public void airflowBreakDown(){
        this.airflow = false;
    }

    public void airflowRepair(){
        this.airflow = true;
    }

    public void updateTemperature(){
        if(this.airflow && this.temperature > 20){
            this.temperature = Math.max(20, this.temperature - 10);
        } else {
            this.temperature += 2 + new Random().nextInt(10);
        }
    }

    public void manageAirFlow(){
        if(this.airflow && Math.random() < 0.35){
            airflowBreakDown();
        } else if(!this.airflow && Math.random() < 0.20){
            airflowRepair();
        }
    }
}
