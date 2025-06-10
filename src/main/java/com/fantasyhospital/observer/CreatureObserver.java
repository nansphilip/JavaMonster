package com.fantasyhospital.observer;

import com.fantasyhospital.model.creatures.abstractclass.Beast;

/**
 * Interface for the Observer design pattern, allowing observers to be notified each time a beast's state changes.
 */
public interface CreatureObserver {

    /**
     * Method called each time a beast's state changes (illness, morale, etc.)
     * @param beast the beast whose state has changed
     */
    void onStateChanged(Beast beast);
}
