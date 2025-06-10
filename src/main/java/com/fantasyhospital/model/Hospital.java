package com.fantasyhospital.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fantasyhospital.enums.ServiceNameType;
import com.fantasyhospital.enums.StackType;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;

import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import com.fantasyhospital.observer.MoralObserver;
import com.fantasyhospital.util.Singleton;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Represents the Fantasy Hospital. Manages the list of services (rooms),
 * doctors, and global operations within the hospital.
 */
@Slf4j
@Setter @Getter
public class Hospital {

    /**
     * Name of the hospital
     */
    private String name;

    /**
     * Maximum number of services the hospital can contain.
     */
    private final int MAX_SERVICE_COUNT;

    /**
     * List of the hospital's services (rooms)
     */
    private List<Room> services = new ArrayList<Room>();

    //Constants
    private static final int MIN_AVERAGE_BUDGET_NEEDED_TO_CREATE_SERVICE = 75;

    /**
     * Creates a new hospital. The max number of services is set to 9.
     *
     * @param name the name of the hospital
     */
    public Hospital(String name) {
        this.name = name;
        this.MAX_SERVICE_COUNT = 9;
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
     * Search and return a medical service that has no doctor
     * @return the medical service if found, else null
     */
    public MedicalService getMedicalServiceWithNoDoctor(){
        for(MedicalService medicalService : this.getMedicalServices()){
            if(medicalService.getDoctors().isEmpty()){
                return medicalService;
            }
        }
        return null;
    }

    /**
     * Search and return the medical service with the max number of creatures in it
     * @return the medical service if found, else null
     */
    public MedicalService getMedicalServiceWithNbMaxCreatures(){
        MedicalService medicalServiceWithNbMaxCreatures = this.getMedicalServices().get(0);
        for(MedicalService medicalService : this.getMedicalServices()){
            medicalServiceWithNbMaxCreatures = medicalService.getCreatures().size() > medicalServiceWithNbMaxCreatures.getCreatures().size() ? medicalService :  medicalServiceWithNbMaxCreatures;
        }
        return null;
    }

    /**
     * Check all the medical services's budget, if the budget is at 0 the medical service has to close
     */
    public void checkBudgetServices(){
        for(MedicalService medicalService : getMedicalServices()){
            // Si crypt, on appelle methode getcryptbudget qui est calculé en fonction temperature et clim
            if((medicalService instanceof Crypt && ((Crypt) medicalService).getCryptBudget() == 0) || medicalService.getBudget() == 0){
                medicalService.setHasServiceToClose(true);
                log.info("Le service {} a un budget terrible, l'état ordonne sa fermeture d'ici le mois prochain.", medicalService.getName());
            }
        }
    }

    /**
     * Check all the medical services, and close the service if needed
     */
    public void reviewBudgetServicesClose() {
        for(MedicalService medicalService : getMedicalServices()){
            if(medicalService.isHasServiceToClose()){
                // Transfer all the creatures to waiting room
                medicalService.transferAllCreaturesToWaitingRoom(this);

                // Fire the doctor(s)
                for(Doctor doctor : medicalService.getDoctors()){
                    doctor.setMorale(0);
                }

                // Make the name available again (except for crypt or quarantine)
                if(!(medicalService instanceof Crypt || medicalService instanceof Quarantine)){
                    ServiceNameType nameEnum = ServiceNameType.valueOf(medicalService.getName().toUpperCase());
                    nameEnum.setSelected(false);
                }

                // Remove the service
                removeService(medicalService);

                Singleton instance = Singleton.getInstance();
                instance.addRoomToStack(medicalService, StackType.MEDICAL_SERVICE);

                log.info("Le service {} ferme car il avait un budget trop catastrophique, le capitalisme frappe à nouveau...", medicalService.getName());
            }
        }
    }

    /**
     * Calculate the average global budget of the hospital
     * If it is over 70, creates a new service with a new doctor
     */
    public void reviewBudgetServiceCreate(){
        int totalBudget = 0;
        int nbMedicalServices = 0;
        for(MedicalService medicalService : getMedicalServices()){
            // If crypt, calls special method to get budget
            totalBudget += medicalService instanceof Crypt ? ((Crypt) medicalService).getCryptBudget() : medicalService.getBudget();
            if(medicalService instanceof Quarantine || medicalService instanceof Crypt){
                continue;
            }
            nbMedicalServices++;
        }

        int averageBudget = Math.round((float) totalBudget / getMedicalServices().size());

        // Creation of a new Service with a doctor if there is enough space in the hospital (- 3 avec salle attente et deux services medicaux speciaux)
        if(averageBudget >= MIN_AVERAGE_BUDGET_NEEDED_TO_CREATE_SERVICE && nbMedicalServices < this.MAX_SERVICE_COUNT - 3){
            MedicalService medicalService = new MedicalService();
            Doctor doctor = new Doctor(medicalService);
            doctor.addObserver(new MoralObserver(this));
            medicalService.addDoctor(doctor);
            this.addService(medicalService);
            log.info("Le budget de l'hosto est remarquable, le service {} vient d'être créé avec le docteur {} !!",  medicalService.getName(), doctor.getFullName());
        } else if(averageBudget >= MIN_AVERAGE_BUDGET_NEEDED_TO_CREATE_SERVICE && nbMedicalServices == this.MAX_SERVICE_COUNT - 3){
            log.info("L'hôpital a atteint son nombre maximum de services, de nouveaux services ne peuvent pas être créés...");
        }
    }

    /**
     * Calculate the global budget of the hospital
     */
    public int getGlobalBudget(){
        int totalBudget = 0;
        for(MedicalService medicalService : getMedicalServices()){
            // If crypt, calls special method to get budget
            totalBudget += medicalService instanceof Crypt ? ((Crypt) medicalService).getCryptBudget() : medicalService.getBudget();
            if(medicalService instanceof Quarantine || medicalService instanceof Crypt){
                continue;
            }
        }
        return Math.round((float) totalBudget / getMedicalServices().size());
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
     * Adds a service (room) to the hospital.
     *
     * @param room the room to add
     */
    public void addService(Room room) {
        services.add(room);
    }

    /**
     * Remove the specified service from the hospital
     * @param room the room
     */
    public void removeService(Room room) {
        services.remove(room);
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
     * Returns the number of medical services in the hospital, excluding crypt and quarantine
     */
    public int getNbMedicalServicesExceptCryptQuarantine() {
        int nb = 0;
        for(MedicalService medicalService : getMedicalServices()){
            if(!(medicalService instanceof Crypt || medicalService instanceof Quarantine)){
                nb++;
            }
        }
        return nb;
    }

    /**
     * Returns the crypt in the hospital.
     *
     * @return Crypt the crypt
     */
    public Crypt getCrypt() {
        for (Room room : services) {
            if (room instanceof Crypt) {
                return (Crypt) room;
            }
        }
        return null;
    }

    /**
     * Returns the quarantine in the hospital.
     *
     * @return Quarantine the quarantine
     */
    public Quarantine getQuarantine() {
        for (Room room : services) {
            if (room instanceof Quarantine) {
                return (Quarantine) room;
            }
        }
        return null;
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
            if (medicalService.getName().equals(name)) {
                return medicalService;
            }
        }
        return null;
    }
}
