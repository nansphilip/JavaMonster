package com.fantasyhospital.util;

import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

@Getter
@Setter
public class EndGameSummary {
    private Stack<Creature> creaturesDead;
    private Stack<Creature> creaturesHealed;
    private Stack<Doctor> doctorsDead;
    private Stack<MedicalService> medicalServicesClosed;
}
