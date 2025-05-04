package com.fantasyhospital;

import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;

import java.util.LinkedHashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Simulation {

    private static final Logger logger = LoggerFactory.getLogger(Simulation.class);

    public static void main(String[] args) {
        LinkedHashSet<Creature> creatures = new LinkedHashSet<>();

        ServiceMedical urgence = new ServiceMedical("Urgence", 50.0, 10, "Mediocre");
//        ServiceMedical psychologie = new ServiceMedical("Psychologie", 100.0, 10, "Moyen");
        Salle salleAttente = new Salle("Salle d'attente", 70, 100);

        Medecin medecin = new Medecin("Dr. Zoidberg", "H", 70, 175, 45, 100, "Lycanthrope", urgence);

        for (int i = 0; i < 5; i++) {
            Creature creature = Game.randomCreature();
            creatures.add(creature);

//            if (creature != null && creature.getMoral() > 50) {
//                urgence.ajouterCreature(creature);
//            }
            //salleAttente.ajouterCreature(creature);
        }
        for(Creature creature : creatures){
            logger.info("Créature générée : {}", creature);
        }
        salleAttente.setCreatures(creatures);

        //salleAttente.ajouterCreature(creatures.get(1));
        urgence.ajouterMedecin(medecin);
        Creature creature = salleAttente.getRandomCreature();
        creature.setMoral(0);
        for(int i = 0; i < 50; i++){
            creature.verifierMoral(salleAttente);
        }

//        salleAttente.afficherInfosService();

//        medecin.transferer(salleAttente.getCreatures().iterator().next(), salleAttente, urgence);
//        urgence.afficherInfosService();
//        medecin.transferer(urgence.getCreatures().iterator().next(), urgence, psychologie);
//        urgence.afficherInfosService();
//        psychologie.afficherInfosService();
        urgence.ajouterMedecin(medecin);

    }
}
