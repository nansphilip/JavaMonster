package com.fantasyhospital;

import com.fantasyhospital.model.creatures.abstractclass.Creature;

import java.util.Stack;

public final class Singleton {

    private static Singleton instance;
    private Stack<Creature> creatureTrepasStack = new Stack<>();
    private Stack<Creature> creatureSoigneStack = new Stack<>();

    private Singleton(){

    }

    public static Singleton getInstance(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    public void addCreatureTrepas(Creature creature){
        creatureTrepasStack.push(creature);
    }

    public Creature popCreatureTrepas(){
        return creatureTrepasStack.pop();
    }

    public boolean isStackTrepasEmpty(){
        return creatureTrepasStack.isEmpty();
    }

    public void addCreatureSoigne(Creature creature){
        creatureSoigneStack.push(creature);
    }

    public Creature popCreatureSoigne(){
        return creatureSoigneStack.pop();
    }

    public boolean isStackSoigneEmpty(){
        return creatureSoigneStack.isEmpty();
    }
}
