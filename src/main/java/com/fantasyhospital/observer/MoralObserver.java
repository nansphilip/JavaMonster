package com.fantasyhospital.observer;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Beast;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.rooms.Room;

/**
 * Implémentation de l'interface Observer chargée de surveiller le moral des bêtes
 * Le notifier executera les actions potentielles qui découleraient d'un moral à 0 ou autres
 */
public class MoralObserver implements CreatureObserver {

    /**
     * Attribut qui référence l'hospital pour surveiller une bête, et pouvoir la faire sortir de l'hospital
     */
    private Hospital hospital;

    public MoralObserver(Hospital hospital) {
        this.hospital = hospital;
    }

    /**
     * Implémentation de la méthode onStateChanged
     * Elle appelle la méthode vérifierMoral sur le médecin, qui vérifie si son moral n'est pas à 0
     * et execute les méthodes si c'est le cas
     * Elle vérifie le moral d'une créature avec la méthode verifierMoral, execute les actions potentielles
     * et retire la créature de l'hopital si elle a trépassé
     * @param bete
     */
    @Override
    public void onStateChanged(Beast beast) {
        if(beast instanceof Doctor){
            ((Doctor) beast).checkMorale();
            return;
        }
        Creature creature = (Creature) beast;
        Room room = hospital.getRoomOfCreature(creature);
        if(creature.checkMorale(room)){
            room.removeCreature(creature);
        }
    }
}
