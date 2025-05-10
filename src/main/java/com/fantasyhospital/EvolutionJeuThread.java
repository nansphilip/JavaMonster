package com.fantasyhospital;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.interfaces.Regenerant;
import com.fantasyhospital.model.creatures.races.Vampire;
import com.fantasyhospital.model.creatures.races.Zombie;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Scanner;

@Slf4j
public class EvolutionJeuThread implements Runnable {

    /**
     * L'hopital du jeu
     */
    private Hospital hospital;

    public EvolutionJeuThread(Hospital hospital) {
        this.hospital = hospital;
    }

    @Override
    public void run() {
        boolean endOfGame = false;
        int nbTour = 1;
        Scanner sc  = new Scanner(System.in);

        while (!endOfGame) {
            logTour(nbTour);
            sc.nextLine();

            if (checkEndOfGame()) break;
            appliquerEffetsMaladies();
            if (checkEndOfGame()) break;
            faireAttendreCreatures();
            if (checkEndOfGame()) break;
            verifierMoralCreatures();
            if (checkEndOfGame()) break;
            faireExaminerMedecins();

            hospital.afficherServices();
            nbTour++;
        }
        sc.close();
        logFinJeu();
    }

    // Méthodes privées extraites

    private void logTour(int nbTour) {
        log.info("#############################################", nbTour);
        log.info("################ TOUR : {} ##################", nbTour);
        log.info("#############################################", nbTour);
    }

    /**
     * Vérifie si l'hopital est vide
     * @return vrai si c'est le cas, faux sinon
     */
    private boolean checkEndOfGame() {
        int nbTotalCreatures = hospital.getNbCreaturesHopital();
        if (nbTotalCreatures == 0) {
            return true;
        }
        return false;
    }

    /**
     * Applique les effets et évolutions des maladies pour toutes les créatures
     */
    private void appliquerEffetsMaladies() {
        for (Salle salle : hospital.getServices()) {
            for (Creature creature : salle.getCreatures()) {
                for (Maladie maladie : creature.getMaladies()) {
                    maladie.augmenterNiveau();
                    verifierCreatureSortHopital(creature);
                    creature.setMoral(Math.max(creature.getMoral() - 5, 0));
                }
            }
        }
    }

    /**
     * Fait attendre toutes les créatures avec les effets associés
     */
    private void faireAttendreCreatures() {
        for (Salle salle : hospital.getServices()) {
            for (Creature creature : salle.getCreatures()) {
                creature.attendre(hospital.getSalleOfCreature(creature));
                verifierCreatureSortHopital(creature);
            }
        }
    }

    /**
     * Vérifie le moral de chaque créature et les effets associés
     */
    private void verifierMoralCreatures() {
        for (Salle salle : hospital.getServices()) {
            for (Creature creature : salle.getCreatures()) {
                creature.verifierMoral(hospital.getSalleOfCreature(creature));
                verifierCreatureSortHopital(creature);
            }
        }
    }

    /**
     * Execute les actions des médecins pour l'hopital
     */
    private void faireExaminerMedecins() {
        for (ServiceMedical service : hospital.getServicesMedicaux()) {
            List<Medecin> medecins = service.getMedecins();
            for (Medecin medecin : medecins) {
                Creature creature = medecin.examiner(hospital);
                verifierCreatureSortHopital(creature);
            }
        }
    }

    private void logFinJeu() {
        log.info("#############################################");
        log.info("################## FIN DU JEU ###############");
        log.info("#############################################");
    }

    /**
     * Fonction qui vérifie si la créature doit sortir de l'hopital (mort ou soigné)
     * @param creature
     */
    public void verifierCreatureSortHopital(Creature creature){
        Salle salleCreature = this.hospital.getSalleOfCreature(creature);
        //Si la créature est déjà sortie de l'hopital
        if(salleCreature == null){
            return;
        }

        //Récupération interface creature pour les regenerants
        String interfaceCreature = "";
        if(creature.getClass().getInterfaces().length > 0){
            interfaceCreature = creature.getClass().getInterfaces()[0].getSimpleName();
        }

        //Avant de potentiellement faire trepasser la creature, si regenerant, on check si creature va mourir
        //Si va mourir, on appliquera depression sur medecin
        boolean isDead = false;
        if(interfaceCreature.equals("Regenerant")){
            if(creature.getRace().equals("Zombie")){
                Zombie zombieCreature = (Zombie)creature;
                isDead = zombieCreature.isCreatureDeadButWillRevive(creature);
            } else {
                Vampire vampire = (Vampire)creature;
                isDead = vampire.isCreatureDeadButWillRevive(creature);
            }
        }

        boolean getsOut = creature.hasCreatureToleaveHospital(salleCreature);

        //Si creature meurt, médecins du service perd du moral
        if(getsOut){
            log.info("creature {} sort, nb maladies : {}", creature.getNomComplet(), creature.getMaladies().size());
            if(!creature.getMaladies().isEmpty()){
                if(salleCreature instanceof ServiceMedical){
                    List<Medecin> medecins = ((ServiceMedical) salleCreature).getMedecins();
                    for(Medecin medecin : medecins){
                        medecin.depression();
                    }
                }
            }
            salleCreature.enleverCreature(creature);
            return;
        }

        //Si regenerant qui meurt mais reste quand même dans l'hopital, applique depression a medecin
        if(isDead){
            if(salleCreature instanceof ServiceMedical){
                List<Medecin> medecins = ((ServiceMedical) salleCreature).getMedecins();
                for(Medecin medecin : medecins){
                    medecin.depression();
                }
            }
        }
    }
}
