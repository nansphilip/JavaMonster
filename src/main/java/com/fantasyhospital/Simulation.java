package com.fantasyhospital;

import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    public static void main(String[] args) {
        List<Creature> creatures = new ArrayList<>();

        ServiceMedical urgence = new ServiceMedical("Urgence", 50.0, 10, "Mediocre");
        ServiceMedical psychologie = new ServiceMedical("Psychologie", 100.0, 10, "Moyen");
        Salle salleAttente = new Salle("Salle d'attente", 70, 100);

        Medecin docTest = new Medecin("Dr. Zoidberg", "H", 70, 175, 45, 100, "Lycanthrope", urgence);

        for (int i = 0; i < 5; i++) {
            Creature creature = Game.randomCreature();
            creatures.add(creature);

//            if (creature != null && creature.getMoral() > 50) {
//                urgence.ajouterCreature(creature);
//            }
            //salleAttente.ajouterCreature(creature);
        }
        for(Creature creature : creatures){
            System.out.println(creature.toString());
        }
        salleAttente.setCreatures(creatures);

        //salleAttente.ajouterCreature(creatures.get(1));
        urgence.ajouterMedecin(docTest);
        urgence.afficherInfosService();
        //psychologie.afficherInfosService();
        salleAttente.afficherInfosService();

        docTest.transferer(salleAttente.getCreatures().get(0),salleAttente, urgence);
        //docTest.transferer(salleAttente.getCreatures().get(0),salleAttente, urgence);
        docTest.transferer(urgence.getCreatures().get(0),psychologie);


        urgence.afficherInfosService();
        psychologie.afficherInfosService();
        salleAttente.afficherInfosService();

    }
}
