package com.fantasyhospital.model.rooms.medicalservice;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.creatures.races.Dwarf;
import com.fantasyhospital.model.creatures.races.Zombie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class MedicalServiceTest {

    @Mock
    Creature creatureMock;

    @Mock
    Zombie zombieMock;

    @Mock

    private MedicalService medicalService;

    @BeforeEach
    void setUp() {
        medicalService = new MedicalService();
        zombieMock = new Zombie();
    }

    @Test
    void testAddRaceAccepted_WithMock() {
        Zombie zombie = new Zombie();
        medicalService.addCreature(zombie);
        boolean result = medicalService.addCreature(zombieMock);

        assertTrue(result);
    }

    @Test
    void testAddRaceForbidden_WithMock() {
        Dwarf dwarf = new Dwarf();
        medicalService.addCreature(dwarf);
        boolean result = medicalService.addCreature(zombieMock);

        assertFalse(result);
    }

    @Test
    void testAddCreatureServiceFull_WithMock() {
        for(int i = 0; i < medicalService.getMAX_CREATURE(); i++){
            Zombie zombie = new Zombie();
            medicalService.addCreature(zombie);
        }
        boolean result = medicalService.addCreature(zombieMock);

        assertFalse(result);
    }

    @Test
    void testAddCreatureServiceNotFull_WithMock() {
        for(int i = 0; i < medicalService.getMAX_CREATURE() - 1; i++){
            Zombie zombie = new Zombie();
            medicalService.addCreature(zombie);
        }
        boolean result = medicalService.addCreature(zombieMock);

        assertTrue(result);
    }


}