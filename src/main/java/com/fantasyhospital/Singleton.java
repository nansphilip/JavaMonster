package com.fantasyhospital;

import com.fantasyhospital.enums.StackType;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Beast;
import com.fantasyhospital.model.creatures.abstractclass.Creature;

import java.util.Stack;

/**
 * Classe qui implémente un design pattern Singleton
 * Le but est de partager une seule instance de cette classe dans toute l'application
 * On peut ainsi ajouter les créatures trépassées et soignées aux attributs de type Stack (pile)
 * Pour ensuite afficher ces créatures à la fin du jeu
 */
public final class Singleton {

    /**
     * Instance unique de cette classe
     */
    private static Singleton instance;

    /**
     * Collection de type Stack qui stocke les créatures qui ont trépassé
     */
    private Stack<Creature> creatureDieStack = new Stack<>();

    /**
     * Collection de type Stack qui stocke les créatures qui ont été soignées
     */
    private Stack<Creature> creatureHealStack = new Stack<>();

    /**
     * Collection de type Stack qui stocke les médecins qui ont été harakiri
     */
    private Stack<Doctor> doctorStack = new Stack<>();

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
     * Méthode pour pop (retirer) une beast de la stack correspondante
     * @param stackType le type de stack
     * @return Beast la beast retirée
     */
    public Beast popBeastFromStack(StackType stackType){
        return switch (stackType) {
            case DIE -> creatureDieStack.pop();
            case HEAL -> creatureHealStack.pop();
            case DOCTOR -> doctorStack.pop();
        };
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
        };
    }
}
