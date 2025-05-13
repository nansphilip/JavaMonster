package com.fantasyhospital.observer;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.abstractclass.Bete;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;

import java.util.ArrayList;
import java.util.List;

public class MoralObserver implements CreatureObserver {

    private Hospital hospital;

    public MoralObserver(Hospital hospital) {
        this.hospital = hospital;
    }

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

//    public void verifierMoralMedecin(Medecin medecin){
//        for(ServiceMedical service : hospital.getServicesMedicaux()){
//            List<Medecin> listMedecin = new ArrayList<>(service.getMedecins());
//            for(Medecin medecin : listMedecin){
//                medecin.verifierMoral();
//            }
//        }
//    }
}
