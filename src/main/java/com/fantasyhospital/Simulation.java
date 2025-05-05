package com.fantasyhospital;

import com.fantasyhospital.model.Hopital;
import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.MoralThread;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;

import java.util.LinkedHashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Simulation {

    private static final Logger logger = LoggerFactory.getLogger(Simulation.class);

    public static void main(String[] args) throws InterruptedException {
        LinkedHashSet<Creature> creatures = new LinkedHashSet<>();

        Hopital hopital = new Hopital("Marseille", 10);
        ServiceMedical urgence = new ServiceMedical("Urgence", 50.0, 10, "Mediocre");
        ServiceMedical psychologie = new ServiceMedical("Psychologie", 100.0, 10, "Moyen");
        Salle salleAttente = new Salle("Salle d'attente", 70, 100);

        Medecin medecin = new Medecin("Dr. Zoidberg", "H", 70, 175, 45, 100, "Lycanthrope", urgence);
        urgence.ajouterMedecin(medecin);

        for (int i = 0; i < 5; i++) {
            Creature creature = Game.randomCreature();
            creatures.add(creature);
            logger.info("Créature générée : {}", creature);
        }

        salleAttente.setCreatures(creatures);
        hopital.ajouterService(salleAttente);
        hopital.ajouterService(urgence);
        Creature creature = salleAttente.getRandomCreature();

        MoralThread threadMoral = new MoralThread(creature, hopital);
        Thread thread = new Thread(threadMoral);
        thread.start();

        //Test 4 maladies
        //Thread.sleep(1000);
        while(creature.getMaladies().size() <= 4) {
            creature.tomberMalade(new Maladie());
        }
        //medecin.transferer(creature, salleAttente, urgence);

    }
}
