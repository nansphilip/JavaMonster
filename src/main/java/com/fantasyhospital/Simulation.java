package com.fantasyhospital;

import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.MoralThread;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.races.Elfe;
import com.fantasyhospital.model.maladie.Maladie;
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
        HashSet<Maladie> maladies = new HashSet<Maladie>();
        maladies.add(new Maladie());
        Elfe elfe = new Elfe(maladies);

        // Ajout des créatures à la salle d'attente
        salleAttente.setCreatures(creatures);

        // Ajout des services à l'hôpital
        hospital.ajouterService(salleAttente);
        hospital.ajouterService(urgence);

        // Lancement d'un thread MoralThread pour chaque créature (gestion du moral, hurlements, trépas…)
        for (Creature creature : creatures) {
            MoralThread action = new MoralThread(creature, hospital);
            Thread thread = new Thread(action);
            thread.start();
        }

        // On force une créature à avoir au moins 4 maladies
        Creature rndCreature = salleAttente.getRandomCreature();
        while (rndCreature.getMaladies().size() < 4) {
            rndCreature.tomberMalade(new Maladie());
        }

        // Pause pour simuler l'écoulement du temps
        Thread.sleep(1000);

        // On aggrave la maladie d'une créature (niveau max)
        rndCreature = salleAttente.getRandomCreature();
        rndCreature.getHighLevelMaladie().setNiveauActuel(rndCreature.getHighLevelMaladie().getNIVEAU_MAX());

        // Pause
        Thread.sleep(1000);

        // On aggrave la maladie d'une autre créature (niveau max)
        rndCreature = salleAttente.getRandomCreature();
        rndCreature.getHighLevelMaladie().setNiveauActuel(rndCreature.getHighLevelMaladie().getNIVEAU_MAX());

        // On transfère deux créatures de la salle d'attente vers les urgences
        rndCreature = salleAttente.getRandomCreature();
        medecin.transferer(rndCreature, salleAttente, urgence);
        rndCreature = salleAttente.getRandomCreature();
        medecin.transferer(rndCreature, salleAttente, urgence);

        // Affichage des informations du service d'urgence
        urgence.afficherInfosService();

        // Le médecin soigne une créature au hasard dans le service d'urgence
        medecin.soigner(urgence.getRandomCreature());

        // Le médecin simule une dépression (perte de moral)
        medecin.depression();
    }

//        Creature creature = salleAttente.getFirstCreature();
//        Creature c2 =  salleAttente.getLastCreature();
//        MoralThread threadMoral = new MoralThread(creature, hospital);
//        MoralThread threadMoral2 = new MoralThread(c2, hospital);
//        Thread thread = new Thread(threadMoral);
//        Thread thread2 = new Thread(threadMoral2);
//        thread.start();
//        thread2.start();
//
//        //Test 4 maladies
//        //Thread.sleep(1000);
//        while(creature.getMaladies().size() <= 4) {
//            creature.tomberMalade(new Maladie());
//        }
//        Thread.sleep(1000);
//        //c2.getHighLevelMaladie().setNiveauActuel(c2.getHighLevelMaladie().getNIVEAU_MAX());
//        //salleAttente.enleverCreature(c2);
//        medecin.transferer(c2, salleAttente, urgence);
}
