package com.fantasyhospital;

import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.races.Zombie;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.rooms.Room;
import com.fantasyhospital.rooms.medicalservice.MedicalService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Simulation {

    public static void main(String[] args) throws InterruptedException {
        // Création de la liste des créatures (thread-safe)
        CopyOnWriteArrayList<Creature> creatures = new CopyOnWriteArrayList<>();

        // Création de l'hôpital avec un nom et un nombre max de services
        Hospital hospital = new Hospital("Marseille", 10);

        // Création d'un service médical "Urgence"
        MedicalService emergency = new MedicalService("Urgence", 50.0, 10, "Mediocre");
        //  ServiceMedical psychologie = new ServiceMedical("Psychologie", 100.0, 10, "Moyen");

        // Création de la room d'attente
        Room roomAttente = new Room("Room d'attente", 70, 100);

        // Création d'un médecin et affectation au service d'emergency
        Doctor doctor = new Doctor("Dr. Zoidberg", GenderType.MALE, 70, 175, 45, 100, "Lycanthrope", emergency);
        emergency.addDoctor(doctor);

        // Génération de 5 créatures aléatoires et ajout à la liste
        for (int i = 0; i < 10; i++) {
            Creature creature = Game.randomCreature();
            creature = new Zombie();
            Disease disease = new Disease();
            CopyOnWriteArrayList<Disease> diseases = new CopyOnWriteArrayList<>();
            diseases.add(disease);
            creature.setDiseases(diseases);
            creatures.add(creature);
            log.info("Créature générée : {}", creature);
        }

        //roomAttente.setCreatures(creatures);
        emergency.setCreatures(creatures);
        hospital.addService(roomAttente);
        hospital.addService(emergency);
        //doctor.transferer(creatures.getFirst(), roomAttente, emergency);

        //Thread d'évolution du jeu et vérifie moral creatures
        EvolutionGameThread evol = new EvolutionGameThread(hospital);
        Thread threadMoral = new Thread(evol);
        threadMoral.start();
    }

}
