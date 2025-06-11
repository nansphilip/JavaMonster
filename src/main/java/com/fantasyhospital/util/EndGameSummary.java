package com.fantasyhospital.util;

import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

/**
 * Represents a summary of the end game state in the Fantasy Hospital simulation.
 * It contains stacks of dead creatures, healed creatures, dead doctors, and closed medical services.
 */
@Getter
@Setter
public class EndGameSummary {
    private Stack<Creature> creaturesDead;
    private Stack<Creature> creaturesHealed;
    private Stack<Doctor> doctorsDead;
    private Stack<MedicalService> medicalServicesClosed;
}
