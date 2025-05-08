package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.HabitantTriage;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.model.creatures.interfaces.Regenerant;
import com.fantasyhospital.salles.Salle;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class Zombie extends HabitantTriage implements Regenerant {

    public Zombie() {
        super(null);
    }

    public Zombie(CopyOnWriteArrayList<Maladie> maladies) {
        super(maladies);
    }

//    public Zombie(String nom, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
//        super(nom, sexe, poids, taille, age, moral, maladies);
//    }

    @Override
    public boolean trepasser(Salle salle) {
        super.trepasser(salle);
        //Supprime maladie qui l'a tué
        Maladie maladie = getHighLevelMaladie();
        this.maladies.remove(maladie);
        //Regénère avec les maladies qu'il avait
        if(!maladies.isEmpty()) {
            log.info("La créature {} revient à la vie, alleluia !!", this.nomComplet);
            return false;
        }
        //si il avait 1 seule maladie, 50% chance attraper autre maladie ou sortir hopital
        if(Math.random() > 0.5){
            maladie = new Maladie();
            maladies.add(maladie);
            log.info("La créature {} revient à la vie, elle contracte la maladie {} en regénérant.", this.nomComplet, maladie.getNom());
            return false;
        } else {
            //Creature sort de l'hopital
            log.info("La créature {} revient à la vie, et sort de l'hopital puisqu'elle n'est plus malade !",  this.nomComplet);
            return true;
        }
    }
}