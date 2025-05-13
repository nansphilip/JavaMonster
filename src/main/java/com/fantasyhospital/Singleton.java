package com.fantasyhospital;

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
    private Stack<Creature> creatureTrepasStack = new Stack<>();

    /**
     * Collection de type Stack qui stocke les créatures qui ont été soignées
     */
    private Stack<Creature> creatureSoigneStack = new Stack<>();

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
     * Méthode pour ajouter une créature à la stack Trépas
     * @param creature
     */
    public void addCreatureTrepas(Creature creature){
        creatureTrepasStack.push(creature);
    }

    /**
     * Méthode pour pop (retirer) une créature de la stack Trépas
     * @return Creature
     */
    public Creature popCreatureTrepas(){
        return creatureTrepasStack.pop();
    }

    /**
     * Méthode qui retourne true si la stack Trépas est vide, false sinon
     * @return boolean
     */
    public boolean isStackTrepasEmpty(){
        return creatureTrepasStack.isEmpty();
    }

    /**
     * Méthode pour ajouter une créature à la stack Soignés
     * @param creature
     */
    public void addCreatureSoigne(Creature creature){
        creatureSoigneStack.push(creature);
    }

    /**
     * Méthode pour pop (retirer) une créature de la stack Soignés
     * @return Creature
     */
    public Creature popCreatureSoigne(){
        return creatureSoigneStack.pop();
    }

    /**
     * Méthode qui retourne true si la stack Soignés est vide, false sinon
     * @return boolean
     */
    public boolean isStackSoigneEmpty(){
        return creatureSoigneStack.isEmpty();
    }
}
