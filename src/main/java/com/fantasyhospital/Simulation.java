package com.fantasyhospital;

import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.races.Zombie;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.rooms.Room;
import com.fantasyhospital.rooms.medicalservice.MedicalService;
import com.fantasyhospital.observer.ExitObserver;
import com.fantasyhospital.observer.MoralObserver;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;


@Slf4j
public class Simulation {

    public static void main(String[] args) throws InterruptedException {
        // Création de la liste des créatures (thread-safe)
        CopyOnWriteArrayList<Creature> creatures = new CopyOnWriteArrayList<>();

        // Création de l'hôpital avec un nom et un nombre max de services
        Hospital hospital = new Hospital("Marseille", 10);

        // Création d'un service médical "Urgence"
        MedicalService emergency = new MedicalService("Urgence", 50.0, 10, "Mediocre");
        MedicalService cardiac = new MedicalService("Cardiologie", 50.0, 10, "Mediocre");
        //  ServiceMedical psychologie = new ServiceMedical("Psychologie", 100.0, 10, "Moyen");

        // Création de la room d'attente
        Room roomAttente = new Room("Room d'attente", 70, 100);

        // Création d'un médecin et affectation au service d'emergency
        Doctor doctor = new Doctor("Dr Cardio", GenderType.MALE, 70, 175, 45, 100, "Lycanthrope", cardiac);
        doctor.addObserver(new MoralObserver(hospital));
        cardiac.addDoctor(doctor);
        Doctor doctor2 = new Doctor("Dr Urgence", GenderType.MALE, 70, 175, 45, 100, "Lycanthrope", emergency);
        doctor2.addObserver(new MoralObserver(hospital));
        emergency.addDoctor(doctor2);

        // Génération de 5 créatures aléatoires et ajout à la liste
        for (int i = 0; i < 10; i++) {
            Creature creature = Game.randomCreature();
            creature.addExitObserver(new ExitObserver(hospital));
            creature.addMoralObserver(new MoralObserver(hospital));
            creatures.add(creature);
            log.info("Créature générée : {}", creature);
        }

        //roomAttente.setCreatures(creatures);
        cardiac.setCreatures(creatures);
        hospital.addService(roomAttente);
        hospital.addService(emergency);
        hospital.addService(cardiac);
        //doctor.transferer(creatures.getFirst(), roomAttente, emergency);

        //Boucle d'évolution du jeu
        EvolutionGame jeu = new EvolutionGame(hospital);
        jeu.run();
    }

}
