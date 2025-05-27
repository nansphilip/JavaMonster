package com.fantasyhospital.model.rooms.medicalservice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.Room;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a specialized medical service in Fantasy Hospital.
 * Inherits from Room and adds management of doctors and budget.
 */
@Setter
@Getter
public class MedicalService extends Room {

    /**
     * List of doctors assigned to this service
     */
    protected List<Doctor> doctors = new ArrayList<>();

    /**
     * Service budget (e.g., nonexistent, poor, insufficient, low)
     */
    protected BudgetType budgetType;

    /**
     * Creates a medical service with name, area, capacity, and budget.
     */
    public MedicalService(String name, double area, int MAX_CREATURE, BudgetType budgetType) {
        super(name, area, MAX_CREATURE);
        this.budgetType = budgetType;
    }

    /**
     * Adds a creature to the medical service if the capacity and race are compatible.
     */
    @Override
    public boolean addCreature(Creature creature) {
        if (creatures.size() >= MAX_CREATURE) {
            return false;
        }
        if (creatures.isEmpty()) {
            creatures.add(creature);
            return true;
        }
        Iterator<Creature> iterator = creatures.iterator();
        String allowedRace = iterator.next().getRace();
        if (creature.getRace().equals(allowedRace)) {
            creatures.add(creature);
            return true;
        }
        return false;
    }

    /**
     * Adds a doctor to the list of doctors in the service.
     */
    public void addDoctor(Doctor doctor) {
        this.doctors.add(doctor);
        doctor.setMedicalService(this);
    }

    /**
     * Removes a doctor from the service.
     */
    public void removeDoctor(Doctor doctor) {
        this.doctors.remove(doctor);
        doctor.setMedicalService(null);
    }

    /**
     * Recherche et retourne le médecin le plus faible moralement du service
     * @return Medecin
     */
    public Doctor getWeakerDoctor() {
        if(!doctors.isEmpty()){
            Doctor doctor = this.doctors.get(0);
            for(Doctor m : doctors) {
                doctor = m.getMorale() < doctor.getMorale() ? m : doctor;
            }
            return doctor;
        }
        return null;
    }

    /**
     * Heals a creature by a doctor (to be completed).
     */
    public void healCreatures(Doctor doctor, Creature creature) {

    }

    /**
     * Revises the service's budget (to be completed).
     */
    public void reviseBudget(int value) {

    }

    /**
     * Returns a textual representation of the medical service, its doctors,
     * and its creatures.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n--- Service : ").append(name).append(" ---\n");
        sb.append("Superficie : ").append(area).append(" m²\n");
        sb.append("Nombre de créatures maximale : ").append(MAX_CREATURE).append("\n");
        sb.append("Budget : ").append(budgetType).append("\n");

        sb.append("\n🧍 Médecins :\n");
        if (doctors.isEmpty()) {
            sb.append("  Aucun médecin dans ce service.\n");
        } else {
            for (Doctor m : doctors) {
                sb.append("  - ").append(m.toString()).append("\n");
            }
        }

        sb.append("\n👾 Créatures :\n");
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
