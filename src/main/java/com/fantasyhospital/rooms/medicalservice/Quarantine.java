package com.fantasyhospital.rooms.medicalservice;

import java.util.Arrays;
import java.util.List;

import com.fantasyhospital.model.creatures.abstractclass.Creature;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Quarantine extends MedicalService {

    @Getter
    private final boolean isolation;
    private final MedicalService parentService;
    private static final List<String> CONTAMINATING_RACES = Arrays.asList("Orque", "HommeBete", "Lycanthrope", "Vampire");

    /**
     * Crée une salle de quarantaine liée à un service médical parent
     * La capacité maximale est automatiquement calculée à 10% de celle du service parent
     */
    public Quarantine(String name, double area, MedicalService parentService, String budget) {
        // La capacité maximale est de 10% de celle du service médical parent
        super(name, area, (int) Math.ceil(parentService.getMAX_CREATURE() * 0.1), budget);
        this.isolation = true;
        this.parentService = parentService;
    }

    /**
     * Surcharge de la méthode addCreature pour n'accepter que les créatures contaminantes
     * et respecter la limite de capacité
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
     * Vérifie si la race est contaminante
     */
    private boolean isContaminatingRace(String race) {
        return CONTAMINATING_RACES.contains(race);
    }
    
    /**
     * Détermine si une créature peut contaminer d'autres créatures
     * @param creature La créature à vérifier
     * @return false si la créature est en quarantaine, true sinon
     */
    public boolean canCreatureContaminate(Creature creature) {
        // Si la créature est dans cette quarantaine, elle ne peut pas contaminer
        return !this.creatures.contains(creature);
    }
    
    /**
     * Détermine si le moral d'une créature peut changer
     * @param creature La créature à vérifier
     * @return false si la créature est en quarantaine, true sinon
     */
    public boolean canCreatureMoraleChange(Creature creature) {
        // Si la créature est dans cette quarantaine, son moral ne change pas
        return !this.creatures.contains(creature);
    }
    
    /**
     * Retourne le service médical parent de cette quarantaine
     */
    public MedicalService getParentService() {
        return parentService;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Quarantaine : ").append(name).append(" ---\n");
        sb.append("Superficie : ").append(area).append(" m²\n");
        sb.append("Nombre de créatures maximale : ").append(MAX_CREATURE).append("\n");
        sb.append("Budget : ").append(budget).append("\n");
        sb.append("Isolation : ").append(isolation).append("\n");
        sb.append("Service parent : ").append(parentService.getName()).append("\n");

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
