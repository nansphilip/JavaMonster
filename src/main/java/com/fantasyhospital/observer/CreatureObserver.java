package com.fantasyhospital.observer;

import com.fantasyhospital.model.creatures.abstractclass.Bete;
import com.fantasyhospital.model.creatures.abstractclass.Creature;

public interface CreatureObserver {

    void onStateChanged(Bete bete);
}
