package com.fantasyhospital;

import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import com.fantasyhospital.observer.ExitObserver;
import com.fantasyhospital.observer.MoralObserver;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;


@Slf4j
public class SimulationConsole {

    public static void main(String[] args) throws InterruptedException {
        // Création de la liste des créatures (thread-safe)
        CopyOnWriteArrayList<Creature> creatures = new CopyOnWriteArrayList<>();

        // Création de l'hôpital avec un nom et un nombre max de services
        Hospital hospital = new Hospital("Marseille", 10);

        // Création des services medicaux
        MedicalService emergency = new MedicalService("Urgence", 50.0, 1, BudgetType.getRandomBudget());
        MedicalService cardiac = new MedicalService("Cardiologie", 50.0, 1, BudgetType.getRandomBudget());
        MedicalService gastro = new MedicalService("Gastrologie", 50.0, 10, BudgetType.getRandomBudget());
        Crypt crypt = new Crypt("Crypt", 50, 5, BudgetType.getRandomBudget());

        // Création de la room d'attente
        Room roomAttente = new Room("Room d'attente", 70, 100);

        // Création d'un médecin et affectation au service d'emergency
        Doctor doctor = new Doctor("Dr Cardio", GenderType.MALE, 70, 175, 45, 100, "Lycanthrope", cardiac);
        doctor.addObserver(new MoralObserver(hospital));
        cardiac.addDoctor(doctor);
        Doctor doctor2 = new Doctor("Dr Urgence", GenderType.MALE, 70, 175, 45, 100, "Lycanthrope", emergency);
        doctor2.addObserver(new MoralObserver(hospital));
        emergency.addDoctor(doctor2);
        Doctor doctor3 = new Doctor("Dr Gastro", GenderType.MALE, 70, 175, 45, 100, "Lycanthrope", gastro);
        doctor3.addObserver(new MoralObserver(hospital));
        gastro.addDoctor(doctor3);
//        Doctor doctor4 = new Doctor("Dr Crypt", GenderType.MALE, 70, 175, 45, 100, "Lycanthrope", crypt);
//        doctor4.addObserver(new MoralObserver(hospital));
//        crypt.addDoctor(doctor4);


        // Génération de 5 créatures aléatoires et ajout à la liste
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

        //Boucle d'évolution du jeu
        EvolutionGame jeu = new EvolutionGame(hospital, null, null, null, null);
        jeu.run();
    }
}
