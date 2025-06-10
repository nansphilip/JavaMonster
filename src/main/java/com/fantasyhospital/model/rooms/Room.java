package com.fantasyhospital.model.rooms;

import com.fantasyhospital.enums.RaceType;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents a room where the creatures are stacked
 */
@Getter @Setter @Slf4j
public class Room {

    /**
     * Name of the room
     */
    protected String name;

    /**
     * Area of the room in square meters
     */
    protected double area;

    /**
     * Maximum number of creatures that can fit in the room
     */
    protected final int MAX_CREATURE;

    /**
     * List of creatures in the room, the list is thread-safe and doesn't allow null creatures nor duplicates
     */
    protected CopyOnWriteArrayList<Creature> creatures = new CopyOnWriteArrayList<>();

    public Random random = new Random();

    /**
     * Default Constructor for the Room class
     * @param name name of the room
     * @param area area of the room in square meters
     * @param MAX_CREATURE maximum number of creatures that can fit in the room
     */
    public Room(String name, double area, int MAX_CREATURE) {
        this.name = name;
        this.area = area;
        this.MAX_CREATURE = MAX_CREATURE;
    }

    /**
     * Add a creature to the creatures list of the room if there is enough space
     * @param creature to add
     * @return true if added, false otherwise (if the room is full)
     */
    public boolean addCreature(Creature creature){
        if (creatures.size() >= MAX_CREATURE) {
            return false;
        } else {
            creatures.add(creature);
            return true;
        }
    }

    /**
     * Get the number of available beds in the room
     * @return the number of available beds as int
     */
    public int getAvailableBeds(){
        return this.MAX_CREATURE - this.creatures.size();
    }

    /**
     * Remove a creature from the list of the room
     * @param creature to remove
     * @return true if removed, false otherwise (if the creature was not in the room)
     */
    public boolean removeCreature(Creature creature){
        return this.creatures.remove(creature);
    }

    /**
     * Search for the race that has the most creatures and return the list of this creatures
     * @return the list of creatures, null if creatures are empty
     */
    public List<Creature> getAllCreaturesOfSameRace() {
        if(creatures.isEmpty()){
            return null;
        }

        List<Creature> creaturesSameRace = new ArrayList<>();

        //instanciation d'une map à partir d'un enum (enumMap) pour compter le nombre de creatures associées a chaque race
        EnumMap<RaceType, Integer> map = new EnumMap<>(RaceType.class);

        for(Creature creature : creatures){
            RaceType race = RaceType.valueOf(creature.getRace().toUpperCase());
            map.merge(race, 1, Integer::sum); // merge : Affecte l'int 1 si value est à null, sinon fait somme de 1 sur la valeur
        }

        //Récupération de la race qui a le plus de créatures présentes dans la room
        int count = 0;
        RaceType race = null;
        for(RaceType raceType : map.keySet()){
            if(map.get(raceType) > count){
                count = map.get(raceType);
                race = raceType;
            }
        }

        //Ajout de toutes les créatures de cette race à la liste à retourner
        for(Creature creature : creatures){
            if(RaceType.valueOf(creature.getRace().toUpperCase()).equals(race)){
                creaturesSameRace.add(creature);
            }
        }

        return creaturesSameRace;
    }

    /**
     * Return a random creature from the list, by excluding the one in param
     * @param creature the creature to exclude
     * @return the random creature, null if creatures are empty
     */
    public Creature getRandomCreatureWithoutThisOne(Creature creature){
        if(creatures.size() <= 1){
            return null;
        }
        CopyOnWriteArrayList<Creature> creaturesCopy = new CopyOnWriteArrayList<>(creatures);
        creaturesCopy.remove(creature);
        return creaturesCopy.get(random.nextInt(creaturesCopy.size()));
    }

    /**
     * Return a random creature from the list, by excluding the list of creatures in param
     * @param creaturesToExclude the list of creatures to exclude
     * @return the random creature, null if creatures are empty
     */
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

    /**
     * Search and return the creature with the higher level disease
     * @return the creature, null if list is empty
     */
    public Creature getCreatureWithHighLevelDisease(){
        if(creatures.isEmpty()){
            return null;
        }
        Creature creatureToReturn = creatures.getFirst();
        for(Creature creature : this.creatures) {
            creatureToReturn = (creature.getHighLevelDisease().getCurrentLevel() > creatureToReturn.getHighLevelDisease().getCurrentLevel()) ? creature : creatureToReturn;
        }
        return creatureToReturn;
    }

    /**
     * Search and return the creature with that has the most diseases
     * @return the creature, null if list is empty
     */
    public Creature getCreatureWithNbMaxDisease(){
        if(creatures.isEmpty()){
            return null;
        }
        Creature creatureToReturn = creatures.getFirst();
        for(Creature creature : this.creatures) {
            creatureToReturn = (creature.getDiseases().size() > creatureToReturn.getDiseases().size()) ? creature : creatureToReturn;
        }
        return creatureToReturn;
    }

    /**
     * Return the room type (race) of the room
     * @return the type, null if the room is empty (= no race)
     */
    public String getRoomType(){
        if(creatures.isEmpty()){
            return null;
        }
        return this.creatures.getFirst().getRace();
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
