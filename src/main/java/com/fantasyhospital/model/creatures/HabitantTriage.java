package com.fantasyhospital.model.creatures;

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
        //attendre patiemment (-5 pts) si il est avec au moins 1 autre creature meme espece, sinon -10pts
        boolean estAvecTriage = false;
        for(Creature creature : salle.getCreatures()){
            if(creature.getClass().getSuperclass().getSimpleName().equals(this.getClass().getSuperclass().getSimpleName()) && !creature.equals(this)){
                estAvecTriage = true;
                break;
            }
        }
        if(estAvecTriage){
            log.info("La créature {} attend (-5pts).", this.nomComplet);
        } else {
            log.info("La créature {} attend (-10pts).", this.nomComplet);
        }
        this.moral -= estAvecTriage ? 5 : 10;
    }
}
