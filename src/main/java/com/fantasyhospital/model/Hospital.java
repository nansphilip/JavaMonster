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
 * Represents the Fantasy Hospital. Manages the list of services (rooms),
 * doctors, and global operations within the hospital.
 */
@Slf4j
public class Hospital {

    /**
     * Name of the hospital
     */
    @Setter
    @Getter
    private String name;

    /**
     * Maximum number of services the hospital can contain.
     */
    @Getter
    private final int MAX_SERVICE_COUNT;

    /**
     * List of the hospital's services (rooms)
     */
    @Setter
    @Getter
    private List<Room> services = new ArrayList<Room>();

    /**
     * Creates a new hospital. Randomly generate the maximum number of services
     * between 8 and 12 included.
     *
     * @param name the name of the hospital
     */
    public Hospital(String name) {
        this.name = name;
        this.MAX_SERVICE_COUNT = 8 + (int) (Math.random() * 5);
    }

    /**
     * Displays information about each of the hospital's services in the logs.
     */
    public void displayServices() {
        for (Room room : this.services) {
            log.info("{}", room);
        }
    }

    /**
     * Displays the total number of creatures in the hospital (to be completed).
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
     * Displays the total number of doctors in the hospital.
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
     * Displays all creatures present in all the hospital's services.
     */
    public void displayAllCreatures() {
        for (Room room : this.services) {
            for (Creature creature : room.getCreatures()) {
                log.info("{}", creature);
            }
        }
    }

    /**
     * Randomly modifies creatures (to be completed).
     */
    public void modifyRandomCreatures() {

    }

    /**
     * Randomly modifies services (to be completed).
     */
    public void modifyRandomServices() {

    }

    /**
     * Adds a service (room) to the hospital.
     *
     * @param room the room to add
     */
    public void addService(Room room) {
        services.add(room);
    }

    /**
     * Starts the hospital simulation (to be completed).
     */
    public void simulation() {
    }

    /**
     * Returns the room where a given creature is located.
     *
     * @param creature the creature to search for
     * @return the room containing the creature, or null if not found
     */
    public Room getRoomOfCreature(Creature creature) {
        for (Room room : this.services) {
            //log.info("room : {}", room.getNom());
            if (room.getCreatures().contains(creature)) {
                return room;
            }
        }
        return null;
    }

    /**
     * Searches and returns a room by its name.
     *
     * @param name the name of the service to search for
     * @return the searched room, or null if not found
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
     * Returns only the medical service rooms in the hospital.
     *
     * @return List<MedicalService>
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
     * Returns only the crypts in the hospital.
     *
     * @return List<Crypt>
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
     * Returns the total number of creatures in the hospital.
     *
     * @return int
     */
    public int getTotalCreaturesHospital() {
        int TotalCreatures = 0;
        for (Room room : this.services) {
            TotalCreatures += room.getCreatures().size();
        }
        return TotalCreatures;
    }

    /**
     * Display all creatures in waiting room
     */
    public Room getWaitingRoom() {
        for (Room service : services) {
            if (service.getName().equalsIgnoreCase("Room d'attente")) {
                return service;
            }
        }
        return null;
    }

    public MedicalService getMedicalServiceByName(String name) {
        List<MedicalService> allServices = this.getMedicalServices();

        for (MedicalService medicalService : allServices ) {
            if (medicalService.getName().equals("Crypt")) {
                return medicalService;
            }
        }
        return null;
    }
}
