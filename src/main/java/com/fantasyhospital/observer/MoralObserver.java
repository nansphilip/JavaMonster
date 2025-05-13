package com.fantasyhospital.observer;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.abstractclass.Bete;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;

import java.util.ArrayList;
import java.util.List;

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
    public void onStateChanged(Bete bete) {
        if(bete instanceof Medecin){
            ((Medecin) bete).verifierMoral();
            return;
        }
        Creature creature = (Creature) bete;
        Salle salle = hospital.getSalleOfCreature(creature);
        if(creature.verifierMoral(salle)){
            salle.enleverCreature(creature);
        }
    }
}
