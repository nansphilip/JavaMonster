package com.fantasyhospital.model.creatures;

import com.fantasyhospital.enums.ActionType;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Abstract class reprensting the triage resident particular creatures
 * It overrides the method waiting, a triage resident waits patiently in a room with other creatures of this class
 * and so looses 5 points of morale, otherwise it looses 10 points.
 */
@Slf4j
public abstract class TriageResident extends Creature {

    /**
     * Constructor for TriageResident
     * @param diseases the diseases of the creature
     */
    public TriageResident(CopyOnWriteArrayList<Disease> diseases) {
        super(diseases);
    }

    @Override
    public void waiting(Room room) {
        //attendre patiemment (-5 pts) si il est avec au moins 1 autre creature meme espece, sinon -10pts
        boolean withTriage = false;
        for(Creature creature : room.getCreatures()){
            if(creature.getClass().getSuperclass().getSimpleName().equals(this.getClass().getSuperclass().getSimpleName()) && !creature.equals(this)){
                withTriage = true;
                break;
            }
        }
        int decreaseMorale = 0;
        if(withTriage){
            decreaseMorale = ActionType.CREATURE_PENDING_TRIAGE_NOT_ALONE.getMoraleVariation();
        } else {
            decreaseMorale = ActionType.CREATURE_PENDING_TRIAGE_ALONE.getMoraleVariation();
        }
        this.setMorale(Math.max(this.getMorale() + decreaseMorale, 0));
        notifyMoralObservers();
    }
}
