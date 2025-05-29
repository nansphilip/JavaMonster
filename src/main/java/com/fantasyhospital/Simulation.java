package com.fantasyhospital;

import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.enums.RaceType;
import com.fantasyhospital.rooms.medicalservice.Crypt;
import org.springframework.stereotype.Service;

import com.fantasyhospital.controller.GridMedicalServiceController;
import com.fantasyhospital.controller.ListCreatureController;
import com.fantasyhospital.controller.ListDoctorsController;
import com.fantasyhospital.controller.WaitingRoomController;
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
	private final WaitingRoomController waitingRoomController;

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
        MedicalService elf = new MedicalService("Elfe", 50.0, 10, BudgetType.INEXISTANT);
		elf.setExpectedRace(RaceType.ELF);
        MedicalService dwarf = new MedicalService("Nains", 50.0, 10, BudgetType.INEXISTANT);
		dwarf.setExpectedRace(RaceType.DWARF);
        MedicalService orc = new MedicalService("Orques", 50.0, 10, BudgetType.INEXISTANT);
		orc.setExpectedRace(RaceType.ORC);
		MedicalService werebeast = new MedicalService("Hommes-Bête", 50.0, 10, BudgetType.INEXISTANT);
		werebeast.setExpectedRace(RaceType.WEREBEAST);
		MedicalService zombie = new MedicalService("Zombies", 50.0, 10, BudgetType.INEXISTANT);
		zombie.setExpectedRace(RaceType.ZOMBIE);
		MedicalService vampire = new MedicalService("Vampires", 50.0, 10, BudgetType.INEXISTANT);
		vampire.setExpectedRace(RaceType.VAMPIRE);
		MedicalService lycanthrope = new MedicalService("Lycanthropes", 50.0, 10, BudgetType.INEXISTANT);
		lycanthrope.setExpectedRace(RaceType.LYCANTHROPE);
		MedicalService reptilian = new MedicalService("Reptiliens", 50.0, 10, BudgetType.INEXISTANT);
		reptilian.setExpectedRace(RaceType.REPTILIAN);
		Crypt crypt = new Crypt("Crypte", 50.0, 10, BudgetType.BON, 1,10);

        //  ServiceMedical psychologie = new ServiceMedical("Psychologie", 100.0, 10, "Moyen");

		// Création de la room d'attente
		Room roomAttente = new Room("Room d'attente", 70, 100);

        // Création d'un médecin et affectation au service d'emergency
        Doctor doctor = new Doctor("Dr Cardio", GenderType.MALE, 70, 175, 45, 100, "Lycanthrope", lycanthrope);
        doctor.addObserver(new MoralObserver(hospital));
        lycanthrope.addDoctor(doctor);
        Doctor doctor2 = new Doctor("Dr Urgence", GenderType.MALE, 70, 175, 45, 100, "Lycanthrope", orc);
        doctor2.addObserver(new MoralObserver(hospital));
        orc.addDoctor(doctor2);
        Doctor doctor3 = new Doctor("Dr Gastro", GenderType.MALE, 70, 175, 45, 100, "Lycanthrope", elf);
        doctor3.addObserver(new MoralObserver(hospital));
        elf.addDoctor(doctor3);

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
        hospital.addService(elf);
        hospital.addService(dwarf);
        hospital.addService(orc);
		hospital.addService(werebeast);
		hospital.addService(zombie);
		hospital.addService(vampire);
		hospital.addService(lycanthrope);
		hospital.addService(reptilian);
		hospital.addService(crypt);
        //doctor.transferer(creatures.getFirst(), roomAttente, emergency);
        roomAttente.getAllCreaturesOfSameRace();

		waitingRoomController.setHospital(hospital);

		gridMedicalServiceController.setHospital(hospital);

		//Boucle d'évolution du jeu
		this.jeu = new EvolutionGame(hospital, listCreatureController, listDoctorsController, waitingRoomController, gridMedicalServiceController);
		jeu.runNextRound();

	}
}
