package com.fantasyhospital.observer;

import com.fantasyhospital.model.creatures.abstractclass.Beast;

/**
 * Interface du patron du design pattern Observer
 * Il permet la notification aux observer à chaque modification de l'état d'une bête
 */
public interface CreatureObserver {

    /**
     * Méthode appelée à chaque modification (maladie, moral) d'une bête
     * @param bete
     */
    void onStateChanged(Beast beast);
}
