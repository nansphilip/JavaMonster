package com.fantasyhospital.model;

import java.util.ArrayList;
import java.util.List;

import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Represents the Fantasy Hospital managing services, doctors, and creatures.
 * The hospital can contain 8-12 services randomly determined at creation.
 */
@Slf4j
public class Hospital {

    /**
     * Hospital name.
     */
    @Setter
    @Getter
    private String name;

    /**
     * Maximum number of services (8-12, randomly generated).
     */
    @Getter
    private final int MAX_SERVICE_COUNT;

    /**
     * List of hospital services (rooms).
     */
    @Setter
    @Getter
    private List<Room> services = new ArrayList<Room>();

    /**
     * List of hospital doctors.
     */
    private List<Doctor> doctors = new ArrayList<>();

    /**
     * Creates a new hospital with random service capacity (8-12).
     *
     * @param name the hospital name
     */
    public Hospital(String name) {
        this.name = name;
        this.MAX_SERVICE_COUNT = 8 + (int) (Math.random() * 5);
    }

    /**
     * Displays all services information in logs.
     */
    public void displayServices() {
        for (Room room : this.services) {
            log.info("{}", room);
        }
    }

    /**
     * Returns all creatures in the hospital.
     * 
     * @return list of all creatures across all services
     */
    public List<Creature> displayCreaturesList() {
        List<Creature> allCreatures = new ArrayList<>();
        for (Room room : this.services) {
            if (room.getCreatures() != null) {
                allCreatures.addAll(room.getCreatures());
            }
        }
        return allCreatures;
    }

    /**
     * Returns the list of all doctors.
     * 
     * @return the doctors list
     */
    public List<Doctor> getDoctorsList() {
        List<Doctor> allDoctors = new ArrayList<>();
        for (Room room : this.services) {
            if (room instanceof MedicalService medicalService) {
                allDoctors.addAll(medicalService.getDoctors());
            }
        }
        return allDoctors;
    }

    /**
     * Displays all creatures in logs.
     */
    public void displayAllCreatures() {
        for (Room room : this.services) {
            for (Creature creature : room.getCreatures()) {
                log.info("{}", creature);
            }
        }
    }

    /**
     * Randomly modifies creatures (not implemented).
     * 
     * @deprecated Logic moved to EvolutionGame
     */
    public void modifyRandomCreatures() {

    }

    /**
     * Randomly modifies services (not implemented).
     * 
     * @deprecated Logic moved to EvolutionGame
     */
    public void modifyRandomServices() {

    }

    /**
     * Adds a service to the hospital.
     *
     * @param room the service to add
     */
    public void addService(Room room) {
        services.add(room);
    }

    /**
     * Starts hospital simulation (not implemented).
     * 
     * @deprecated Use EvolutionGame instead
     */
    public void simulation() {
    }

    /**
     * Finds the room containing a specific creature.
     *
     * @param creature the creature to locate
     * @return the room containing the creature, or null if not found
     */
    public Room getRoomOfCreature(Creature creature) {
        for (Room room : this.services) {
            if (room.getCreatures().contains(creature)) {
                return room;
            }
        }
        return null;
    }

    /**
     * Finds a room by name.
     *
     * @param name the room name to search for
     * @return the room if found, null otherwise
     */
    public Room getRoomByName(String name) {
        for (Room room : services) {
            if (room.getName().equals(name)) {
                return room;
            }
        }
        return null;
    }

    /**
     * Returns only medical services from the hospital.
     *
     * @return list of medical services
     */
    public List<MedicalService> getMedicalServices() {
        List<MedicalService> servicesList = new ArrayList<>();
        for (Room room : services) {
            if (room instanceof MedicalService) {
                servicesList.add((MedicalService) room);
            }
        }
        return servicesList;
    }

    /**
     * Returns only crypts from the hospital.
     *
     * @return list of crypts
     */
    public List<Crypt> getCrypts() {
        List<Crypt> cryptList = new ArrayList<>();
        for (Room room : services) {
            if (room instanceof Crypt) {
                cryptList.add((Crypt) room);
            }
        }
        return cryptList;
    }

    /**
     * Returns total number of creatures in the hospital.
     *
     * @return total creature count
     */
    public int getTotalCreaturesHospital() {
        int totalCreatures = 0;
        for (Room room : this.services) {
            totalCreatures += room.getCreatures().size();
        }
        return totalCreatures;
    }

    /**
     * Returns the waiting room.
     *
     * @return the waiting room if found, null otherwise
     */
    public Room getWaitingRoom() {
        for (Room service : services) {
            if (service.getName().equalsIgnoreCase("Room d'attente")) {
                return service;
            }
        }
        return null;
    }
}
