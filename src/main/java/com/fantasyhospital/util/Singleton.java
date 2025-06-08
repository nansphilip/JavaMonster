package com.fantasyhospital.util;

import com.fantasyhospital.enums.StackType;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Beast;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Stack;

/**
 * Classe qui implémente un design pattern Singleton
 * Le but est de partager une seule instance de cette classe dans toute l'application
 * On peut ainsi ajouter les créatures trépassées et soignées aux attributs de type Stack (pile)
 * Pour ensuite afficher ces créatures à la fin du jeu
 */
@Slf4j
public final class Singleton {

    /**
     * Instance unique de cette classe
     */
    private static Singleton instance;

    /**
     * Collection de type Stack qui stocke les créatures qui ont trépassé
     */
    @Getter
    private Stack<Creature> creatureDieStack = new Stack<>();

    /**
     * Collection de type Stack qui stocke les créatures qui ont été soignées
     */
    @Getter
    private Stack<Creature> creatureHealStack = new Stack<>();

    /**
     * Collection de type Stack qui stocke les médecins qui ont été harakiri
     */
    @Getter
    private Stack<Doctor> doctorStack = new Stack<>();

    /**
     * Collection de type Stack qui stocke les services qui ont fermé
     */
    @Getter
    private Stack<MedicalService> medicalServiceStack = new Stack<>();

    /**
     *  All stats for end game summary
     */
    @Getter
    @Setter
    private EndGameSummary endGameSummary;

    private Singleton(){

    }

    /**
     * Méthode qui instancie la classe Singleton si elle n'existe pas encore
     * Ou retourne l'instance si elle a déjà été instanciée
     * @return Singleton
     */
    public static Singleton getInstance(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    /**
     * Méthode pour ajouter une beast à la stack correspondante via le paramètre enum StackType
     * @param beast the beast
     * @param stackType le type de stack
     */
    public void addBeastToStack(Beast beast, StackType stackType){
        switch (stackType){
            case DIE -> creatureDieStack.push((Creature) beast);
            case HEAL -> creatureHealStack.push((Creature) beast);
            case DOCTOR -> doctorStack.push((Doctor) beast);
        }
    }

    /**
     * Méthode pour ajouter une salle à la stack correspondante via le paramètre enum StackType
     * @param room the medical service
     * @param stackType le type de stack
     */
    public void addRoomToStack(Room room, StackType stackType){
            medicalServiceStack.push((MedicalService) room);
    }

    /**
     * Méthode pour pop (retirer) une beast de la stack correspondante
     * @param stackType le type de stack
     * @return Beast la beast retirée
     */
    public Beast popBeastFromStack(StackType stackType){
        return switch (stackType) {
            case DIE -> creatureDieStack.pop();
            case HEAL -> creatureHealStack.pop();
            case DOCTOR -> doctorStack.pop();
            case MEDICAL_SERVICE -> null;
        };
    }

    /**
     * Récupère le dernier Doctor de la stack DOCTOR sans le retirer (ou le remet aussitôt).
     *
     * @return le dernier médecin mort ou null si la stack est vide
     */
    public synchronized Doctor peekDoctorStack() {
        if (!doctorStack.isEmpty()) {
            return doctorStack.peek(); // peek() ne retire pas l'élément
        } else {
            return null;
        }
    }

    /**
     * Méthode qui retourne true si la stack correspondate est vide, false sinon
     * @param stackType le type de stack
     * @return boolean
     */
    public boolean isStackEmpty(StackType stackType){
        return switch (stackType) {
            case DIE -> creatureDieStack.isEmpty();
            case HEAL -> creatureHealStack.isEmpty();
            case DOCTOR -> doctorStack.isEmpty();
            case MEDICAL_SERVICE -> false;
        };
    }

    public String getEndGameLog() {
        StringBuilder sb = new StringBuilder();

        sb.append("#################################\n");
        sb.append("#### CREATURES TREPASSEES : #####\n");
        sb.append("#################################\n");
        Stack<Creature> stackDie = new Stack<>();
        stackDie.addAll(creatureDieStack);
        while(!stackDie.isEmpty()){
            sb.append(stackDie.pop()).append("\n");
        }

        sb.append("#################################\n");
        sb.append("###### CREATURES SOIGNEES : #####\n");
        sb.append("#################################\n");
        Stack<Creature> stackHeal = new Stack<>();
        stackHeal.addAll(creatureHealStack);
        while(!stackHeal.isEmpty()){
            sb.append(stackHeal.pop()).append("\n");
        }

        sb.append("#################################\n");
        sb.append("###### MEDECINS HARAKIRI : ######\n");
        sb.append("#################################\n");
        Stack<Doctor> stackDoctor = new Stack<>();
        stackDoctor.addAll(doctorStack);
        while(!stackDoctor.isEmpty()){
            sb.append(stackDoctor.pop()).append("\n");
        }

        sb.append("########################################\n");
        sb.append("###### SERVICES MEDICAUX FERMES : ######\n");
        sb.append("########################################\n");
        Stack<MedicalService> stackMedicalService = new Stack<>();
        stackMedicalService.addAll(medicalServiceStack);
        while(!stackMedicalService.isEmpty()){
            sb.append(stackMedicalService.pop()).append("\n");
        }

        return sb.toString();
    }

    public EndGameSummary buildEndGameSummary() {
        EndGameSummary summary = new EndGameSummary();
        summary.setCreaturesDead(creatureDieStack);
        summary.setCreaturesHealed(creatureHealStack);
        summary.setDoctorsDead(doctorStack);
        summary.setMedicalServicesClosed(medicalServiceStack);
        return summary;
    }
}
