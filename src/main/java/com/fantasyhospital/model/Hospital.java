package com.fantasyhospital.model;

import java.util.ArrayList;
import java.util.List;

import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.rooms.Room;

import com.fantasyhospital.rooms.medicalservice.MedicalService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents the Fantasy Hospital.
 * Manages the list of services (rooms), doctors, and global operations within the hospital.
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
     * Maximum number of services the hospital can contain
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
     * List of the hospital's doctors
     */
    private List<Doctor> doctors = new ArrayList<>();

    /**
     * Creates a new hospital.
     *
     * @param name the name of the hospital
     * @param MAX_SERVICE_COUNT the maximum number of services
     */
    public Hospital(String name, int MAX_SERVICE_COUNT) {
        this.name = name;
        this.MAX_SERVICE_COUNT = MAX_SERVICE_COUNT;
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
    public void displayCreaturesList() {
        /* ... */ }

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
             if(room.getCreatures().contains(creature)) {
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
            if(room.getName().equals(name)) {
                return room;
            }
        }
        return null;
    }

    /**
     * Returns only the medical service rooms in the hospital.
     * @return List<MedicalService>
     */
    public List<MedicalService> getMedicalServices() {
        List<MedicalService> servicesList =  new ArrayList<>();
        for (Room room : services) {
            if(room instanceof MedicalService) {
                servicesList.add((MedicalService) room);
            }
        }
        return servicesList;
    }

    /**
     * Returns the total number of creatures in the hospital.
     * @return int
     */
    public int getTotalCreaturesHospital(){
        int TotalCreatures = 0;
        for(Room room : this.services) {
            TotalCreatures += room.getCreatures().size();
        }
        return TotalCreatures;
    }
}
