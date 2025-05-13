package com.fantasyhospital;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.races.Vampire;
import com.fantasyhospital.model.creatures.races.Zombie;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class EvolutionJeu {

    /**
     * L'hopital du jeu
     */
    private Hospital hospital;

    public EvolutionJeu(Hospital hospital) {
        this.hospital = hospital;
    }

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
            //verifierMoralCreatures();
            if (checkEndOfGame()) break;
            faireExaminerMedecins();

            hospital.afficherServices();
            nbTour++;
        }
        sc.close();
        logFinJeu();
        afficherCreaturesSortiesHospital();
    }

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
     * Vérifie le moral de tous les médecins, et le sort de l'hopital si il en a fini
     */
    public void verifierMoralMedecins(){
        for(ServiceMedical service : hospital.getServicesMedicaux()){
            List<Medecin> listMedecin = new ArrayList<>(service.getMedecins());
            for(Medecin medecin : listMedecin){
                medecin.verifierMoral();
            }
        }
    }

    /**
     * Applique les effets et évolutions des maladies pour toutes les créatures (-5 pts moral par maladie)
     * Fait aussi évoluer les maladies des créatures de manière aléatoire
     * Elles ont toutes 5% de chance de tomber malade à chaque tour
     */
    private void appliquerEffetsMaladies() {
        for (Salle salle : hospital.getServices()) {
            for (Creature creature : salle.getCreatures()) {
                if(Math.random() < 0.05){
                    Maladie maladie = new Maladie();
                    creature.tomberMalade(maladie);
                    log.info("La créature {} n'a pas de chance, elle vient de contracter la maladie {} de manière complétement aléatoire.", creature.getNomComplet(), maladie.getNom());
                }
                for (Maladie maladie : creature.getMaladies()) {
                    maladie.augmenterNiveau();
                    //verifierCreatureSortHopital(creature);
                    //verifierMoralMedecins();
                    creature.setMoral(Math.max(creature.getMoral() - 5, 0));
                }
                creature.notifyExitObservers();
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
                //verifierCreatureSortHopital(creature);
                //verifierMoralMedecins();
            }
        }
    }

    /**
     * Vérifie le moral de chaque créature et les effets associés
     */
    private void verifierMoralCreatures() {
        for (Salle salle : hospital.getServices()) {
            for (Creature creature : salle.getCreatures()) {
                if(creature.verifierMoral(hospital.getSalleOfCreature(creature))){
                    salle.enleverCreature(creature);
                }
                //verifierCreatureSortHopital(creature);
                //verifierMoralMedecins();
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
                //verifierCreatureSortHopital(creature);
                //verifierMoralMedecins();
            }
        }
    }

    private void logFinJeu() {
        log.info("#############################################");
        log.info("################## FIN DU JEU ###############");
        log.info("#############################################");
        log.info("");
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

        //Si creature meurt, médecin le plus faible du service perd du moral
        if(getsOut){
            if(!creature.getMaladies().isEmpty()){
                if(salleCreature instanceof ServiceMedical){
                    ((ServiceMedical) salleCreature).getWeakerMedecin().depression();
                }
            }
            salleCreature.enleverCreature(creature);
            return;
        }

        //Si regenerant qui meurt mais reste quand même dans l'hopital, applique depression a medecin
        if(isDead){
            if(salleCreature instanceof ServiceMedical){
                ((ServiceMedical) salleCreature).getWeakerMedecin().depression();
            }
        }
    }

    /**
     * Récupère toutes les créatures soignées et trépassées des stack du singleton
     * et les affiche
     */
    public void afficherCreaturesSortiesHospital(){
        Singleton instance = Singleton.getInstance();

        log.info("#################################");
        log.info("#### CREATURES TREPASSEES : #####");
        log.info("#################################");

        while(!instance.isStackTrepasEmpty()){
            log.info("{}", instance.popCreatureTrepas());
        }

        log.info("#################################");
        log.info("###### CREATURES SOIGNEES : #####");
        log.info("#################################");

        while(!instance.isStackSoigneEmpty()){
            log.info("{}", instance.popCreatureSoigne());
        }
    }
}
