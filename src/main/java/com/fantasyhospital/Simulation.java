package com.fantasyhospital;

import com.fantasyhospital.model.Hopital;
import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.MoralThread;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;

import java.util.LinkedHashSet;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Simulation {

    private static final Logger logger = LoggerFactory.getLogger(Simulation.class);

    public static void main(String[] args) throws InterruptedException {
        CopyOnWriteArrayList<Creature> creatures = new CopyOnWriteArrayList<>();

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
        Creature creature = salleAttente.getFirstCreature();
        Creature c2 =  salleAttente.getLastCreature();
        MoralThread threadMoral = new MoralThread(creature, hopital);
        MoralThread threadMoral2 = new MoralThread(c2, hopital);
        Thread thread = new Thread(threadMoral);
        Thread thread2 = new Thread(threadMoral2);
        thread.start();
        thread2.start();

        //Test 4 maladies
        //Thread.sleep(1000);
        while(creature.getMaladies().size() <= 4) {
            creature.tomberMalade(new Maladie());
        }
        Thread.sleep(1000);
        //c2.getHighLevelMaladie().setNiveauActuel(c2.getHighLevelMaladie().getNIVEAU_MAX());
        //salleAttente.enleverCreature(c2);
        medecin.transferer(c2, salleAttente, urgence);

    }
}
