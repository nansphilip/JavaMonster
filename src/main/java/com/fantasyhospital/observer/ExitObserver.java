package com.fantasyhospital.observer;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.creatures.abstractclass.Bete;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.salles.Salle;
import com.fantasyhospital.salles.servicemedical.ServiceMedical;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExitObserver implements CreatureObserver {

    private Hospital hospital;

    public ExitObserver(Hospital hospital) {
        this.hospital = hospital;
    }

    @Override
    public void onStateChanged(Bete bete) {
        checkExitCreature((Creature) bete);
    }



    private void checkExitCreature(Creature creature) {
        Salle salleCreature = this.hospital.getSalleOfCreature(creature);
        //Si la créature est déjà sortie de l'hopital
        if(salleCreature == null){
            return;
        }

        boolean getsOut = creature.hasCreatureToleaveHospital(salleCreature);

        //Si creature meurt, médecin le plus faible du service perd du moral
        if(getsOut){
            if(!creature.getMaladies().isEmpty()){
                if(salleCreature instanceof ServiceMedical){
                    Medecin medecin = ((ServiceMedical) salleCreature).getWeakerMedecin();
                    if(medecin != null){
                        medecin.depression();
                        medecin.notifyObservers();
                    }
                }
            }
            salleCreature.enleverCreature(creature);
        }
    }

}
