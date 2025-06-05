package com.fantasyhospital.model.creatures;

import com.fantasyhospital.enums.ActionType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.observer.MoralObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DoctorTest {

    @Mock
    private MedicalService medicalService;

    @Mock
    private Doctor doctorMock;

    private Doctor doctor;

    private Hospital hospital;

    @BeforeEach
    void setUp() {
        medicalService = new MedicalService();
        doctor = new Doctor(medicalService);
        doctor.addObserver(new MoralObserver(hospital));
        medicalService.addDoctor(doctor);
    }

    @Test
    void goToRemoveDoctor() {
        MedicalService medicalServiceTo = new MedicalService();
        doctor.goTo(medicalService, medicalServiceTo);

        assertThat(medicalService.getDoctors()).doesNotContain(doctor);
    }

    @Test
    void goToAddDoctor() {
        MedicalService medicalServiceTo = new MedicalService();
        doctor.goTo(medicalService, medicalServiceTo);

        assertThat(medicalServiceTo.getDoctors()).contains(doctor);
    }


    @Test
    void healCreatureHasDisease() {

    }

    @Test
    void transferGroup() {
    }

    @Test
    void depression() {
        doctor.setMorale(100);
        doctor.depression();
        int depression = ActionType.DOCTOR_DEPRESSION.getMoraleVariation();
        assertEquals(doctor.getMorale(), (100 + depression));
    }

    @Test
    void depressionLessThanZero() {
        doctor.setMorale(10);
        doctor.depression();
        int depression = ActionType.DOCTOR_DEPRESSION.getMoraleVariation();
        assertEquals(doctor.getMorale(), 0);
    }

    @Test
    void doctorHasHarakiri() {
        doctor.haraKiri();
        assertThat(medicalService.getDoctors()).doesNotContain(doctor);
    }

    @Test
    void doctorHasNotHaraKiri() {
        assertThat(medicalService.getDoctors()).contains(doctor);
    }

    @Test
    void doctorHarakiriDepression() {
        doctor.setMorale(10);
        Doctor spyDoctor = spy(doctor); //spy permet d'exécuter la logique de l'objet et donc l'appel de méthodes en cascade
        spyDoctor.depression();

        verify(spyDoctor).haraKiri();
    }
}