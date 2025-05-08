package com.fantasyhospital;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;
import lombok.extern.slf4j.Slf4j;
import java.util.List;


import java.util.Scanner;

@Slf4j
public class EvolutionJeuThread implements Runnable {

    private Hospital hospital;

    public EvolutionJeuThread(Hospital hospital) {
        this.hospital = hospital;
    }

    @Override
    public void run() {
        boolean endOfGame = false;
        int nbTour = 1;
        Scanner sc  = new Scanner(System.in);
        while(!endOfGame) {
            log.info("TOUR : {}", nbTour);
            sc.nextLine();

            ServiceMedical salledAttente = hospital.getServiceWithName("Salle d'attente");


            for(ServiceMedical serviceMedical : hospital.getServices()){
                log.info("Service : " + serviceMedical.getNom() + ", créatures : " + serviceMedical.getCreatures().size() + " !!!");
                if (serviceMedical.getNom().equals("Urgence")) {
                    for (Creature creature : salledAttente.getCreatures()) {
                        serviceMedical.ajouterCreature(creature);
                        log.info("Le service " + serviceMedical.getNom() + " a admis le patient " + creature.getNomComplet() + " ! ");
                        salledAttente.enleverCreature(creature);
                    }
                }

                if (serviceMedical.getMedecins().size() > 0) {
                    for (Medecin medecin : serviceMedical.getMedecins()) {
                        log.info(medecin.getNomComplet());
                        Creature creatureToBeHeal = serviceMedical.getRandomCreature();
                        String maladie = creatureToBeHeal.getHighLevelMaladie().getNom();
                        creatureToBeHeal.etreSoigne(creatureToBeHeal.getHighLevelMaladie());
                        log.info("Le " + medecin.getNomComplet() + " a soigné " + creatureToBeHeal.getNomComplet() + " de la maladie " + maladie + " ! ");

                    }
                }

            }



            for (ServiceMedical serviceMedical : hospital.getServices()) {
                for(Creature creature : serviceMedical.getCreatures()) {
                    if (creature.getMaladies().isEmpty()) {
                        serviceMedical.enleverCreature(creature);
                    } else {
                        //Diminuer le moral de 5 pts par maladie, et augmenter maladies 1 niveau
                        for (Maladie maladie : creature.getMaladies()) {
                            maladie.augmenterNiveau();
                            creature.setMoral(Math.max(creature.getMoral() - 5, 0));
                        }
                    creature.attendre(serviceMedical);
                    }
                    creature.verifierMoral(this.hospital.getSalleOfCreature(creature));
                }
            }

            hospital.afficherToutesCreatures();
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
