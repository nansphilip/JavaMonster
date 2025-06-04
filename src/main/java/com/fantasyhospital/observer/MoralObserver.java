package com.fantasyhospital.observer;

import com.fantasyhospital.enums.FemaleNameType;
import com.fantasyhospital.enums.MaleNameType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Beast;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import lombok.extern.slf4j.Slf4j;

/**
 * Implémentation de l'interface Observer chargée de surveiller le moral des bêtes
 * Le notifier executera les actions potentielles qui découleraient d'un moral à 0 ou autres
 */
@Slf4j
public class MoralObserver implements CreatureObserver {

    /**
     * Attribut qui référence l'hospital pour surveiller une bête, et pouvoir la faire sortir de l'hospital
     */
    private Hospital hospital;

    // Constants of variation
    private static final int DECREASE_BUDGET = 20;

    public MoralObserver(Hospital hospital) {
        this.hospital = hospital;
    }

    /**
     * Implémentation de la méthode onStateChanged
     * Elle appelle la méthode vérifierMoral sur le médecin, qui vérifie si son moral n'est pas à 0
     * et execute les méthodes si c'est le cas
     * Elle vérifie le moral d'une créature avec la méthode verifierMoral, execute les actions potentielles
     * et retire la créature de l'hopital si elle a trépassé
     * @param beast la bête pour laquelle son moral évolue
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
            if(room instanceof MedicalService){
                Doctor doc = ((MedicalService) room).getWeakerDoctor();
                if(doc != null) doc.depression();
            }
            // Decrease the budget of the service if it was in a medical service
            if(room instanceof MedicalService medicalService){
                medicalService.setBudget(Math.max(medicalService.getBudget() - DECREASE_BUDGET,0));
                log.info("La mort de la créature {} fait perdre {} points de budget au service {} ({} pts)", creature.getFullName(), DECREASE_BUDGET, medicalService.getName(), medicalService.getBudget());
            }

            // Make the name of the creature available again
            String name = creature.getFullName();
            switch (creature.getSex()){
                case FEMALE:
                    FemaleNameType enumName = FemaleNameType.valueOf(name.toUpperCase());
                    enumName.setSelected(false);
                    break;
                case MALE:
                    MaleNameType enumMaleName = MaleNameType.valueOf(name.toUpperCase());
                    enumMaleName.setSelected(false);
                    break;
            }
        }
    }
}
