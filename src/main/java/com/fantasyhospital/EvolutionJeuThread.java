package com.fantasyhospital;

import com.fantasyhospital.model.Hopital;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class EvolutionJeuThread implements Runnable {

    private Hopital hopital;

    public EvolutionJeuThread(Hopital hopital) {
        this.hopital = hopital;
    }

    @Override
    public void run() {
        boolean endOfGame = false;
        int nbTour = 1;
        Scanner sc  = new Scanner(System.in);
        while(!endOfGame) {
            log.info("TOUR : {}", nbTour);
            sc.nextLine();

            for(Salle salle : hopital.getServices()){
                for(Creature creature : salle.getCreatures()){
                    //Diminuer le moral de 5 pts par maladie, et augmenter maladies 1 niveau
                    for(Maladie maladie : creature.getMaladies()){
                        maladie.augmenterNiveau();
                        creature.setMoral(Math.max(creature.getMoral() - 5,0));
                    }
                    creature.attendre(hopital.getSalleOfCreature(creature));
                    creature.verifierMoral(this.hopital.getSalleOfCreature(creature));

                }
            }
            hopital.afficherToutesCreatures();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            nbTour++;
        }
        sc.close();
    }
}
