package com.fantasyhospital;

import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.ThreadVerifieCreatureSortHopital;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.races.Zombie;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.observer.ExitObserver;
import com.fantasyhospital.observer.MoralObserver;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Simulation {

    public static void main(String[] args) throws InterruptedException {
        // Création de la liste des créatures (thread-safe)
        CopyOnWriteArrayList<Creature> creatures = new CopyOnWriteArrayList<>();

        // Création de l'hôpital avec un nom et un nombre max de services
        Hospital hospital = new Hospital("Marseille", 10);

        // Création d'un service médical "Urgence"
        ServiceMedical urgence = new ServiceMedical("Urgence", 50.0, 10, "Mediocre");
        //  ServiceMedical psychologie = new ServiceMedical("Psychologie", 100.0, 10, "Moyen");

        // Création de la salle d'attente
        Salle salleAttente = new Salle("Salle d'attente", 70, 100);

        // Création d'un médecin et affectation au service d'urgence
        Medecin medecin = new Medecin("Dr. Zoidberg", GenderType.MALE, 70, 175, 45, 20, "Lycanthrope", urgence);
        medecin.addObserver(new MoralObserver(hospital));
        urgence.ajouterMedecin(medecin);

        // Génération de 5 créatures aléatoires et ajout à la liste
        for (int i = 0; i < 10; i++) {
            Creature creature = Game.randomCreature();

//            creature = new Zombie();
//            Maladie maladie = new Maladie();
//            CopyOnWriteArrayList<Maladie> maladies = new CopyOnWriteArrayList<>();
//            maladies.add(maladie);
//            creature.setMaladies(maladies);
            creature.addExitObserver(new ExitObserver(hospital));
            creature.addMoralObserver(new MoralObserver(hospital));
            creatures.add(creature);
            log.info("Créature générée : {}", creature);
        }

        //salleAttente.setCreatures(creatures);
        urgence.setCreatures(creatures);
        hospital.ajouterService(salleAttente);
        hospital.ajouterService(urgence);
        //medecin.transferer(creatures.getFirst(), salleAttente, urgence);

        //Boucle d'évolution du jeu et vérifie moral creatures
        EvolutionJeu jeu = new EvolutionJeu(hospital);
        jeu.run();
//        Thread threadMoral = new Thread(evol);
//        threadMoral.start();
    }

}
