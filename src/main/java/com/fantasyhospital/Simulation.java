package com.fantasyhospital;

import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.ThreadVerifieCreatureSortHopital;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
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
        Medecin medecin = new Medecin("Dr. Zoidberg", "H", 70, 175, 45, 100, "Lycanthrope", urgence);
        urgence.ajouterMedecin(medecin);

        // Génération de 5 créatures aléatoires et ajout à la liste
        for (int i = 0; i < 5; i++) {
            Creature creature = Game.randomCreature();
            creatures.add(creature);
            log.info("Créature générée : {}", creature);
        }

        //Thread vérifie si creature doit sortir de l'hopital
//        ThreadVerifieCreatureSortHopital action = new ThreadVerifieCreatureSortHopital(null, hospital);
//        Thread thread = new Thread(action);
//        thread.start();

        salleAttente.setCreatures(creatures);
        hospital.ajouterService(salleAttente);
        hospital.ajouterService(urgence);
        //medecin.transferer(creatures.getFirst(), salleAttente, urgence);

        //Thread d'évolution du jeu et vérifie moral creatures
        EvolutionJeuThread evol = new EvolutionJeuThread(hospital);
        Thread threadMoral = new Thread(evol);
        threadMoral.start();
    }

}
