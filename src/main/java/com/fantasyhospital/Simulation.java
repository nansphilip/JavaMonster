package com.fantasyhospital;

import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import com.fantasyhospital.controller.GridMedicalServiceController;
import com.fantasyhospital.controller.ListCreatureController;
import com.fantasyhospital.controller.ListDoctorsController;
import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.observer.ExitObserver;
import com.fantasyhospital.observer.MoralObserver;
import com.fantasyhospital.rooms.Room;
import com.fantasyhospital.rooms.medicalservice.MedicalService;
import com.fantasyhospital.service.PatientService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class Simulation {

	private final PatientService patientService;
	@Getter
	private EvolutionGame jeu;

	@Getter
	private volatile boolean running = false;

	private final ListCreatureController listCreatureController;
	private final ListDoctorsController listDoctorsController;
	private final GridMedicalServiceController gridMedicalServiceController;

	public synchronized void startSimulation() {

		if (running) {
			log.info("Simulation déjà en cours, lancement ignoré.");
			return;
		}
		running = true;

		// Création de la liste des créatures (thread-safe)
		CopyOnWriteArrayList<Creature> creatures = new CopyOnWriteArrayList<>();

            // Création de l'hôpital avec un nom et un nombre max de services
            Hospital hospital = new Hospital("Marseille", 10);

        // Création des services medicaux
        MedicalService emergency = new MedicalService("Urgence", 50.0, 10, "Mediocre");
        MedicalService cardiac = new MedicalService("Cardiologie", 50.0, 10, "Mediocre");
        MedicalService gastro = new MedicalService("Gastrologie", 50.0, 10, "Mediocre");
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
        Doctor doctor3 = new Doctor("Dr Gastro", GenderType.MALE, 70, 175, 45, 100, "Lycanthrope", gastro);
        doctor3.addObserver(new MoralObserver(hospital));
        gastro.addDoctor(doctor3);

		listCreatureController.setHospital(hospital);
		// A revoir pour faire dynamiuquement !
		// les docs sont en dur pour l'instant
		listDoctorsController.addDoctor(doctor);
		listDoctorsController.addDoctor(doctor2);
		listDoctorsController.addDoctor(doctor3);

		// Génération de 5 créatures aléatoires et ajout à la liste
		for (int i = 0; i < 10; i++) {
			Creature creature = Game.randomCreature();
			creature.addExitObserver(new ExitObserver(hospital));
			creature.addMoralObserver(new MoralObserver(hospital));
			creatures.add(creature);
			log.info("Créature générée : {}", creature);

			// TODO: use patientRepository
			listCreatureController.addCreature(creature);
		}

        //roomAttente.setCreatures(creatures);
        roomAttente.setCreatures(creatures);
        hospital.addService(roomAttente);
        hospital.addService(emergency);
        hospital.addService(cardiac);
        hospital.addService(gastro);
        //doctor.transferer(creatures.getFirst(), roomAttente, emergency);
        roomAttente.getAllCreaturesOfSameRace();

		gridMedicalServiceController.setHospital(hospital);

		//Boucle d'évolution du jeu
		this.jeu = new EvolutionGame(hospital, listCreatureController, listDoctorsController);
		jeu.runNextRound();

	}
}
