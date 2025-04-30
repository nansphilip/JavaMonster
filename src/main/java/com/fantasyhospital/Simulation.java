package com.fantasyhospital;

import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.servicemedical.ServiceMedical;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    public static void main(String[] args) {
        List<Creature> creatures = new ArrayList<>();

        ServiceMedical urgence = new ServiceMedical("Urgence", 50.0, 10, "Mediocre");
        ServiceMedical psychologie = new ServiceMedical("Psychologie", 100.0, 10, "Moyen");

        Medecin docTest = new Medecin("Dr. Zoidberg", "H", 70, 175, 45, 100, "Lycanthrope", urgence);

        for (int i = 0; i < 10; i++) {
            Creature creature = Game.randomCreature();
            creatures.add(creature);

            if (creature != null && creature.getMoral() > 50) {
                urgence.ajouterCreature(creature);
            }
        }
        for(Creature creature : creatures){
            System.out.println(creature.toString());
        }
        urgence.ajouterMedecin(docTest);
        urgence.afficherInfosService();
        psychologie.afficherInfosService();
    }
}
