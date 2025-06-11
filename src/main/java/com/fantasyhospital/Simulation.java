package com.fantasyhospital;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import com.fantasyhospital.controller.GridMedicalServiceController;
import com.fantasyhospital.controller.HospitalStructureController;
import com.fantasyhospital.controller.ListCreatureController;
import com.fantasyhospital.controller.ListDoctorsController;
import com.fantasyhospital.controller.WaitingRoomController;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import com.fantasyhospital.observer.ExitObserver;
import com.fantasyhospital.observer.MoralObserver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class Simulation {

    private final HospitalStructureController hospitalStructureController;

    @Getter
    private EvolutionGame jeu;

    @Getter
    private volatile boolean running = false;

    // Controllers lists for JavaFX
    private final ListCreatureController listCreatureController;
    private final ListDoctorsController listDoctorsController;
    private final GridMedicalServiceController gridMedicalServiceController;
    private final WaitingRoomController waitingRoomController;

    public synchronized void startSimulation() {

        // Prevent multiple simulations from running at the same time
        if (running) {
            log.info("Simulation déjà en cours, lancement ignoré.");
            return;
        }

        // Set running to true
        running = true;

        // Create a list of creatures (thread-safe)
        CopyOnWriteArrayList<Creature> creatures = new CopyOnWriteArrayList<>();

        // Create a hospital with a name and a maximum number of services
        Hospital hospital = new Hospital("Marseille");

        // Create medical services and doctor
        for(int i = 0; i < 3; i++){
            MedicalService medicalService = new MedicalService();
            Doctor doctor = new Doctor(medicalService);
            doctor.addObserver(new MoralObserver(hospital));
            medicalService.addDoctor(doctor);
            hospital.addService(medicalService);
            listDoctorsController.addDoctor(doctor);
        }

        Crypt crypt = new Crypt("Crypt", 50, 2, 60 + new Random().nextInt(40));
//        crypt.setBudget(0);
        Quarantine quarantine = new Quarantine("Quarantaine", 50, 2, 60 + new Random().nextInt(40));
//        quarantine.setBudget(0);
        // Create a waiting room
        Room roomAttente = new Room("Room d'attente", 70, 100);

        // Generate 10 random creatures and add them to the list
        for (int i = 0; i < 10; i++) {
            Creature creature = Game.randomCreature();
            creature.addExitObserver(new ExitObserver(hospital));
            creature.addMoralObserver(new MoralObserver(hospital));
            creatures.add(creature);
            //log.info("Créature générée : {}", creature);

            // TODO: use patientRepository ?
            listCreatureController.addCreature(creature);
        }

        // Add creatures to the waiting room
        roomAttente.setCreatures(creatures);

        // Add services to the hospital
        hospital.addService(roomAttente);
        hospital.addService(crypt);
        hospital.addService(quarantine);

        // Provide hospital to JavaFX controllers
        waitingRoomController.setHospital(hospital);
        gridMedicalServiceController.setHospital(hospital);
        hospitalStructureController.setHospital(hospital);

        // TODO: Faire dynamiquement ? Docs en du pour l'instant
        // Provide hospital and doctors list to JavaFX controllers
        listCreatureController.setHospital(hospital);
        listDoctorsController.setHospital(hospital);

        // Evolution game loop
        this.jeu = new EvolutionGame(hospital, listCreatureController, listDoctorsController, waitingRoomController, gridMedicalServiceController);
        jeu.runNextRound();
    }

    /**
     * Restart the simulation by stopping current one and resetting to welcome screen
     */
    public synchronized void restartSimulation() {
        log.info("Stopping current simulation and returning to welcome screen...");
        
        // Stop current simulation
        if (running) {
            running = false;
            
            // Wait a bit for threads to finish
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Interrupted while waiting for simulation to stop");
            }
        }
        
        // Reset game reference
        jeu = null;
        
        // Clear all UI controllers data
        clearUIControllers();
        
        // Reset hospital structure to welcome screen
        if (hospitalStructureController != null) {
            hospitalStructureController.resetToWelcomeScreen();
        }
        
        log.info("Application reset to welcome screen successfully");
    }

    /**
     * Clear all UI controllers data for restart
     */
    private void clearUIControllers() {
        log.info("Clearing UI controllers data...");
        
        if (listCreatureController != null) {
            listCreatureController.clearCreatures();
        }
        if (listDoctorsController != null) {
            listDoctorsController.clearDoctors();
        }
        if (waitingRoomController != null) {
            waitingRoomController.clearWaitingRoom();
        }
        if (gridMedicalServiceController != null) {
            gridMedicalServiceController.clearServices();
        }
        
        log.info("UI controllers data cleared successfully");
    }

}
