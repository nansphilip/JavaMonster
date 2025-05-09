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
        int nbTotalCreatures = 0;
        Scanner sc  = new Scanner(System.in);
        while(!endOfGame) {
            log.info("#############################################", nbTour);
            log.info("################ TOUR : {} ##################", nbTour);
            log.info("#############################################", nbTour);

            sc.nextLine();

            //Si plus aucune créature hopital, fin du jeu
            nbTotalCreatures = hospital.getNbCreaturesHopital();
            if(nbTotalCreatures == 0) {
                endOfGame = true;
                break;
            }

            for(Salle salle : hospital.getServices()){
                for(Creature creature : salle.getCreatures()){
                    //Diminuer le moral de 5 pts par maladie, et augmenter maladies 1 niveau
                    for(Maladie maladie : creature.getMaladies()){
                        maladie.augmenterNiveau();
                        verifierCreatureSortHopital(creature);
                        creature.setMoral(Math.max(creature.getMoral() - 5,0));
                    }
                }
            }

            nbTotalCreatures = hospital.getNbCreaturesHopital();
            if(nbTotalCreatures == 0) {
                endOfGame = true;
                break;
            }

            for(Salle salle : hospital.getServices()){
                for(Creature creature : salle.getCreatures()){
                    creature.attendre(hospital.getSalleOfCreature(creature));
                    verifierCreatureSortHopital(creature);
                }
            }

            nbTotalCreatures = hospital.getNbCreaturesHopital();
            if(nbTotalCreatures == 0) {
                endOfGame = true;
                break;
            }

            for(Salle salle : hospital.getServices()){
                for(Creature creature : salle.getCreatures()){
                    creature.verifierMoral(this.hospital.getSalleOfCreature(creature));
                    verifierCreatureSortHopital(creature);
                }
            }

            nbTotalCreatures = hospital.getNbCreaturesHopital();
            if(nbTotalCreatures == 0) {
                endOfGame = true;
                break;
            }

            for(ServiceMedical service : hospital.getServicesMedicaux()){
                List<Medecin> medecins = service.getMedecins();
                for(Medecin medecin : medecins){
                    Creature creature = medecin.examiner(hospital);
                    verifierCreatureSortHopital(creature);
                }
            }
            hospital.afficherServices();
            nbTour++;
        }
        sc.close();
        log.info("#############################################");
        log.info("################## FIN DU JEU ###############");
        log.info("#############################################");
    }

    public void verifierCreatureSortHopital(Creature creature){
        boolean getsOut = creature.hasCreatureToleaveHospital(this.hospital.getSalleOfCreature(creature));
        String interfaceCreature = "";
        if(creature.getClass().getInterfaces().length > 0){
            interfaceCreature = creature.getClass().getInterfaces()[0].getSimpleName();
        }
        if(getsOut){
            this.hospital.getSalleOfCreature(creature).enleverCreature(creature);
            return;
        }
    }
}
