package com.fantasyhospital;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.observer.ExitObserver;
import com.fantasyhospital.observer.MoralObserver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimulationConsole {

    public static void main(String[] args) throws InterruptedException {
        // Create a list of creatures (thread-safe)
        CopyOnWriteArrayList<Creature> creatures = new CopyOnWriteArrayList<>();

        // Create a hospital with a name and a maximum number of services
        Hospital hospital = new Hospital("Marseille");

        // Create medical services
        MedicalService emergency = new MedicalService("Urgence", 50.0, 10, new Random().nextInt(100));
        MedicalService cardiac = new MedicalService("Cardiologie", 50.0, 10, new Random().nextInt(100));
        MedicalService gastro = new MedicalService("Gastrologie", 50.0, 10, new Random().nextInt(100));
        Crypt crypt = new Crypt("Crypt", 50, 5, new Random().nextInt(100));

        // Create a waiting room
        Room roomAttente = new Room("Room d'attente", 70, 100);

        // Create a doctor and assign it to the emergency service
        Doctor doctor = new Doctor("Dr Cardio", GenderType.MALE, 70, 175, 45, 100, "Lycanthrope", cardiac);
        doctor.addObserver(new MoralObserver(hospital));
        cardiac.addDoctor(doctor);

        Doctor doctor2 = new Doctor("Dr Urgence", GenderType.MALE, 70, 175, 45, 100, "Lycanthrope", emergency);
        doctor2.addObserver(new MoralObserver(hospital));
        emergency.addDoctor(doctor2);

        Doctor doctor3 = new Doctor("Dr Gastro", GenderType.MALE, 70, 175, 45, 100, "Lycanthrope", gastro);
        doctor3.addObserver(new MoralObserver(hospital));
        gastro.addDoctor(doctor3);

        // Generate 10 random creatures and add them to the list
        for (int i = 0; i < 10; i++) {
            Creature creature = Game.randomCreature();
            creature.addExitObserver(new ExitObserver(hospital));
            creature.addMoralObserver(new MoralObserver(hospital));
            creatures.add(creature);
            log.info("Créature générée : {}", creature);
        }

        roomAttente.setCreatures(creatures);
        hospital.addService(roomAttente);
        hospital.addService(emergency);
        hospital.addService(cardiac);
        hospital.addService(gastro);
        hospital.addService(crypt);
        // hospital.addService(quarantine);

        // Evolution game loop
        EvolutionGame jeu = new EvolutionGame(hospital, null, null, null, null);
        jeu.run();
    }
}
