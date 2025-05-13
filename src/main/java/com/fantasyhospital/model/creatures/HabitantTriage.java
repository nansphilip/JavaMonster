package com.fantasyhospital.model.creatures;

import com.fantasyhospital.enums.ActionType;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public abstract class HabitantTriage extends Creature {

    public HabitantTriage(CopyOnWriteArrayList<Maladie> maladies) {
        super(maladies);
    }

    @Override
    public void attendre(Salle salle) {
        if(salle == null) {
            return;
        }
        //attendre patiemment (-5 pts) si il est avec au moins 1 autre creature meme espece, sinon -10pts
        boolean estAvecTriage = false;
        for(Creature creature : salle.getCreatures()){
            if(creature.getClass().getSuperclass().getSimpleName().equals(this.getClass().getSuperclass().getSimpleName()) && !creature.equals(this)){
                estAvecTriage = true;
                break;
            }
        }
        int baisseMoral = 0;
        if(estAvecTriage){
            baisseMoral = ActionType.CREATURE_ATTENTE_TRIAGE_NONSEUL.getVariationMoral();
        } else {
            baisseMoral = ActionType.CREATURE_ATTENTE_TRIAGE_SEUL.getVariationMoral();
        }
        log.info("La créature {} attend ({} points).", this.nomComplet, baisseMoral);
        this.setMoral(Math.max(this.getMoral() + baisseMoral, 0));
        notifyMoralObservers();
    }
}
