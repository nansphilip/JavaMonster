package com.fantasyhospital.model.creatures.abstractclass;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fantasyhospital.model.creatures.races.Dwarf;
import com.fantasyhospital.model.disease.Disease;

@ExtendWith(MockitoExtension.class)
class CreatureTest {

    private Creature creature;

    @BeforeEach
    void setUp() {
        // Using Dwarf as a concrete implementation of Creature for testing
        CopyOnWriteArrayList<Disease> diseases = new CopyOnWriteArrayList<>();
        creature = new Dwarf(diseases);
        creature.setFullName("Test Creature");
    }

    @Test
    void howl() {
        // Test that howl method executes without throwing exceptions
        assertDoesNotThrow(() -> creature.howl());
    }
}