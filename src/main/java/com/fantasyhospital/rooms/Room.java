package com.fantasyhospital.rooms;

import com.fantasyhospital.model.creatures.abstractclass.Creature;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Setter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Getter @Slf4j
public class Room {

    @Setter
    protected String name;
    protected double area;
    protected final int MAX_CREATURE;
    @Setter
    protected CopyOnWriteArrayList<Creature> creatures = new CopyOnWriteArrayList<>();
    public Random random = new Random();

    public Room(String name, double area, int MAX_CREATURE) {
        this.name = name;
        this.area = area;
        this.MAX_CREATURE = MAX_CREATURE;
    }

    public boolean addCreature(Creature creature){
        if (creatures.size() >= MAX_CREATURE) {
            return false;
        } else {
            creatures.add(creature);
            return true;
        }
    }

    public boolean removeCreature(Creature creature){
        return this.creatures.remove(creature);
    }

    public void displayInfosService(){
        log.info("\n{}", this);
    }

    public void displayInfosCreatures(){

    }

    public void setArea(double area) {
        this.area = area;
    }

	public Creature getFirstCreature() {
        return creatures.iterator().next();
    }

    public Creature getLastCreature() {
        return (Creature) creatures.toArray()[ creatures.size()-1 ];
    }

    public Creature getCreatureByName(String creatureName){
        for(Creature creature : creatures){
            if(creature.getFullName().equals(creatureName)){
                return creature;
            }
        }
        return null;
    }

    public Creature getRandomCreature(){
        Random random = new Random();
        return (Creature) this.creatures.toArray()[random.nextInt(this.creatures.size())];
    }

    public Creature getRandomCreatureWithoutThisOne(Creature creature){
        if(creatures.size() <= 1){
            return null;
        }
        CopyOnWriteArrayList<Creature> creaturesCopy = new CopyOnWriteArrayList<>(creatures);
        creaturesCopy.remove(creature);
        return creaturesCopy.get(random.nextInt(creaturesCopy.size()));
    }

    public Creature getRandomCreatureWithoutThem(List<Creature> creaturesToExclude){
        CopyOnWriteArrayList<Creature> creaturesCopy = new CopyOnWriteArrayList<>(this.creatures);
        for(Creature creature : creaturesToExclude){
            creaturesCopy.remove(creature);
        }
        if(creaturesCopy.isEmpty()){
            return null;
        }
        return creaturesCopy.get(random.nextInt(creaturesCopy.size()));
    }

    public Creature getCreatureWithHighLevelDisease(){
        Creature creatureToReturn = creatures.getFirst();
        for(Creature creature : this.creatures) {
            creatureToReturn = (creature.getHighLevelDisease().getCurrentLevel() > creatureToReturn.getHighLevelDisease().getCurrentLevel()) ? creature : creatureToReturn;
        }
        return creatureToReturn;
    }

    public Creature getCreatureWithNbMaxDisease(){
        Creature creatureToReturn = creatures.getFirst();
        for(Creature creature : this.creatures) {
            creatureToReturn = (creature.getDiseases().size() > creatureToReturn.getDiseases().size()) ? creature : creatureToReturn;
        }
        return creatureToReturn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Room : ").append(name).append(" ---\n");
        sb.append("Superficie : ").append(area).append(" m²\n");
        sb.append("Nombre de créatures maximale : ").append(MAX_CREATURE).append("\n");

        sb.append("\n🏥 Room :\n");
        if (creatures.isEmpty()) {
            sb.append("  Aucune créature dans ce service.\n");
        } else {
            for (Creature c : creatures) {
                sb.append("  - ").append(c).append("\n");
            }
        }

        return sb.toString();
    }
}
