package com.fantasyhospital.model.creatures.abstractclass;

import java.lang.reflect.Field;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fantasyhospital.enums.ActionType;
import com.fantasyhospital.enums.DiseaseType;
import com.fantasyhospital.model.creatures.races.Dwarf;
import com.fantasyhospital.model.disease.Disease;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import com.fantasyhospital.observer.CreatureObserver;

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

    @Test
    void beCuredWithDiseaseNotPresentShouldReturnFalse() {
        // Create a disease that the creature doesn't have
        Disease disease = new Disease(DiseaseType.MDC, 10, 5);
        MedicalService medicalService = new MedicalService("Test Service", 50.0, 5, 100);
        
        // Call beCured with a disease the creature doesn't have
        boolean result = creature.beCured(disease, medicalService);
        
        // Should return false
        assertFalse(result);
    }

    @Test
    void beCuredInNormalServiceShouldIncreaseMoraleAndRemoveDisease() {
        // Add a disease to the creature
        Disease disease = new Disease(DiseaseType.MDC, 10, 5);
        creature.getDiseases().add(disease);
        int initialMorale = 30;
        creature.setMorale(initialMorale);
        
        // Create a normal medical service (not quarantine)
        MedicalService medicalService = new MedicalService("Test Service", 50.0, 5, 100);
        
        // Call beCured
        boolean result = creature.beCured(disease, medicalService);
        
        // Should return true, remove disease, increase morale, and set recentlyHealed
        assertTrue(result);
        assertFalse(creature.getDiseases().contains(disease));
        assertEquals(Math.min(initialMorale + ActionType.CREATURE_TREATED.getMoraleVariation(), 100), creature.getMorale());
        assertTrue(creature.isRecentlyHealed());
    }

    @Test
    void beCuredInQuarantineShouldNotIncreaseMorale() {
        // Add a disease to the creature
        Disease disease = new Disease(DiseaseType.MDC, 10, 5);
        creature.getDiseases().add(disease);
        int initialMorale = 30;
        creature.setMorale(initialMorale);
        
        // Create a quarantine service
        Quarantine quarantine = new Quarantine("Quarantine", 30.0, 3, 50);
        
        // Call beCured
        boolean result = creature.beCured(disease, quarantine);
        
        // Should return true, remove disease, NOT increase morale, and set recentlyHealed
        assertTrue(result);
        assertFalse(creature.getDiseases().contains(disease));
        assertEquals(initialMorale, creature.getMorale()); // Morale should remain unchanged
        assertTrue(creature.isRecentlyHealed());
    }

    @Test
    void beCuredWithNullMedicalServiceShouldIncreaseMorale() {
        // Add a disease to the creature
        Disease disease = new Disease(DiseaseType.MDC, 10, 5);
        creature.getDiseases().add(disease);
        int initialMorale = 30;
        creature.setMorale(initialMorale);
        
        // Call beCured with null medical service
        boolean result = creature.beCured(disease, null);
        
        // Should return true, remove disease, increase morale (since not in quarantine), and set recentlyHealed
        assertTrue(result);
        assertFalse(creature.getDiseases().contains(disease));
        assertEquals(Math.min(initialMorale + ActionType.CREATURE_TREATED.getMoraleVariation(), 100), creature.getMorale());
        assertTrue(creature.isRecentlyHealed());
    }

    @Test
    void beCuredShouldNotifyExitObservers() {
        // Add a disease to the creature
        Disease disease = new Disease(DiseaseType.MDC, 10, 5);
        creature.getDiseases().add(disease);
        
        // Create a spy creature to verify observer notification
        Creature spyCreature = spy(creature);
        
        // Create a mock observer
        CreatureObserver mockObserver = mock(CreatureObserver.class);
        spyCreature.addExitObserver(mockObserver);
        
        // Create a normal medical service
        MedicalService medicalService = new MedicalService("Test Service", 50.0, 5, 100);
        
        // Call beCured
        boolean result = spyCreature.beCured(disease, medicalService);
        
        // Should notify exit observers
        assertTrue(result);
        verify(spyCreature).notifyExitObservers();
    }

    @Test
    void beCuredWithMoraleAtMaxShouldNotExceed100() {
        // Add a disease to the creature
        Disease disease = new Disease(DiseaseType.MDC, 10, 5);
        creature.getDiseases().add(disease);
        creature.setMorale(80); // High morale that would exceed 100 with +50
        
        // Create a normal medical service
        MedicalService medicalService = new MedicalService("Test Service", 50.0, 5, 100);
        
        // Call beCured
        boolean result = creature.beCured(disease, medicalService);
        
        // Should cap morale at 100
        assertTrue(result);
        assertEquals(100, creature.getMorale());
    }
}