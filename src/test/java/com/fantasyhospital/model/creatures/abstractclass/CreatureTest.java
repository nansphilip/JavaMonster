package com.fantasyhospital.model.creatures.abstractclass;

import java.lang.reflect.Field;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fantasyhospital.model.creatures.races.Dwarf;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;

@ExtendWith(MockitoExtension.class)
class CreatureTest {

    private Creature creature;
    private Room room;

    @BeforeEach
    void setUp() {
        // Using Dwarf as a concrete implementation of Creature for testing
        CopyOnWriteArrayList<Disease> diseases = new CopyOnWriteArrayList<>();
        creature = new Dwarf(diseases);
        creature.setFullName("Test Creature");
        
        // Create a simple room for testing
        room = new Room("Test Room", 50.0, 5);
    }

    // ====== howl ====== //

    @Test
    void howl() {
        // Test that howl method executes without throwing exceptions
        assertDoesNotThrow(() -> creature.howl());
    }

    // ====== checkMorale ====== //

    @Test
    void checkMoraleWithNullRoom() {
        // Test checkMorale with null room should return false
        boolean result = creature.checkMorale(null);
        assertFalse(result);
    }

    @Test
    void checkMoraleWithMoraleZeroShouldCallHowlAndIncrementHowlCount() throws Exception {
        // Set morale to 0
        creature.setMorale(0);
        
        // Get initial howlCount using reflection
        Field howlCountField = Creature.class.getDeclaredField("howlCount");
        howlCountField.setAccessible(true);
        int initialHowlCount = (Integer) howlCountField.get(creature);
        
        // Call checkMorale
        boolean result = creature.checkMorale(room);
        
        // Should return false and increment howlCount
        assertFalse(result);
        int newHowlCount = (Integer) howlCountField.get(creature);
        assertEquals(initialHowlCount + 1, newHowlCount);
    }

    @Test
    void checkMoraleWithMoraleGreaterThanZeroShouldResetHowlCount() throws Exception {
        // Set morale to a positive value
        creature.setMorale(50);
        
        // Set howlCount to a non-zero value using reflection
        Field howlCountField = Creature.class.getDeclaredField("howlCount");
        howlCountField.setAccessible(true);
        howlCountField.set(creature, 2);
        
        // Call checkMorale
        boolean result = creature.checkMorale(room);
        
        // Should return false and reset howlCount to 0
        assertFalse(result);
        int newHowlCount = (Integer) howlCountField.get(creature);
        assertEquals(0, newHowlCount);
    }

    @Test
    void checkMoraleWithHowlCountGreaterThanTwoShouldCallLoseControl() throws Exception {
        // Set howlCount to 3 using reflection
        Field howlCountField = Creature.class.getDeclaredField("howlCount");
        howlCountField.setAccessible(true);
        howlCountField.set(creature, 3);
        
        // Since loseControl has random behavior, we just verify it doesn't throw exceptions
        assertDoesNotThrow(() -> creature.checkMorale(room));
    }

    @Test
    void checkMoraleNormalFlow() {
        // Test normal flow with positive morale and room
        creature.setMorale(75);
        
        boolean result = creature.checkMorale(room);
        
        // Should return false in normal conditions
        assertFalse(result);
    }

    // ====== beCured ====== //

    
}