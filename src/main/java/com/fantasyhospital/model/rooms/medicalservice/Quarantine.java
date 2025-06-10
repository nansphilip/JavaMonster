package com.fantasyhospital.model.rooms.medicalservice;

import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Class of the special MedicalService Quarantine
 * This service is used to isolate creatures that are contaminated. It only accepts contaminating creatures. The disease's level of a creature in
 * the quarantine cannot increase, but creatures can still lose control
 */
@Getter @Slf4j
public class Quarantine extends MedicalService {

    /**
     * Indicates if the quarantine is isolated from the rest of the hospital
     * This is true for all quarantines
     */
    private final boolean isolation;

    /**
     * List of contaminating races that can be put in quarantine
     */
    public static final List<String> CONTAMINATING_RACES = Arrays.asList("Orc", "Werebeast", "Lycanthrope", "Vampire");

    /**
     * Default Constructor for the Quarantine class
     * @param name
     * @param area
     * @param MAX_CAPACITY
     * @param budgetType
     */
    public Quarantine(String name, double area,int MAX_CAPACITY, int budgetType) {
        super(name, area, MAX_CAPACITY, budgetType);
        this.isolation = true;
    }

    /**
     * Gets a random race from the contaminating races list
     * @return the race as String
     */
    public static String getRandomContaminatingRace() {
        if (CONTAMINATING_RACES.isEmpty()) {
            return null; // Ou gérer autrement si la liste peut être vide
        }
        Random random = new Random();
        return CONTAMINATING_RACES.get(random.nextInt(CONTAMINATING_RACES.size()));
    }


    /**
     * Overrides of the addCreature method to only accept contaminating creatures and respect the capacity limit
     */
    @Override
    public boolean addCreature(Creature creature) {
        // Vérifier si c'est une créature contaminante
        if (!isContaminatingRace(creature.getRace())) {
            log.info("La créature {} n'est pas une créature contaminante et ne peut pas être mise en quarantaine", creature.getFullName());
            return false;
        }
        
        // Vérifier la capacité
        if (creatures.size() >= MAX_CREATURE) {
            log.info("La quarantaine est pleine, impossible d'ajouter {}", creature.getFullName());
            return false;
        }

        // Ajouter la créature (sans vérifier la race comme dans MedicalService)
        creatures.add(creature);
        log.info("La créature {} a été mise en quarantaine", creature.getFullName());
        return true;
    }

    /**
     * Checks if the race given in parameter is a contaminating race
     * @param race the race to check
     * @return true if it is, false otherwise
     */
    private boolean isContaminatingRace(String race) {
        return CONTAMINATING_RACES.contains(race);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Quarantaine : ").append(name).append(" ---\n");
        sb.append("Superficie : ").append(area).append(" m²\n");
        sb.append("Nombre de créatures maximale : ").append(MAX_CREATURE).append("\n");
        //Get the enum budgetType from the int budget
        BudgetType budgetEnum = BudgetType.fromRatio(this.budget);
        sb.append("Budget : ").append(budgetEnum).append(" (").append(this.budget).append(") ").append("\n");
        //sb.append("Budget : ").append(budgetType).append("\n");
        sb.append("Isolation : ").append(isolation).append("\n");

        sb.append("\n👾 Créatures en quarantaine :\n");
        if (creatures.isEmpty()) {
            sb.append("  Aucune créature en quarantaine.\n");
        } else {
            for (Creature c : creatures) {
                sb.append("  - ").append(c).append("\n");
            }
        }

        return sb.toString();
    }
}
