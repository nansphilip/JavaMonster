package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public abstract class ClientVIP extends Creature {

    public int nbTourAttente;

    public ClientVIP( CopyOnWriteArrayList<Maladie> maladies) {
        super(maladies);
    }

    @Override
    public void attendre(Salle salle) {
        //moral tombe 0 au bout de 4 tours
        this.nbTourAttente++;
        if(this.nbTourAttente == 4){
            this.moral = 0;
            this.nbTourAttente = 0;
            log.info("La créature {} a attendu 4 tours sans être soigné, son moral tombe à 0.", this.nomComplet);
        } else {
            log.info("La créature {} attend.", this.nomComplet);
        }
        notifyMoralObservers();
    }

    @Override
    public boolean etreSoigne(Maladie maladie) {
        this.nbTourAttente = 0;
        return super.etreSoigne(maladie);
    }
}
