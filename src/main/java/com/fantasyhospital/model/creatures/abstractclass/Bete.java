package com.fantasyhospital.model.creatures.abstractclass;

import java.util.concurrent.CopyOnWriteArrayList;

import static com.fantasyhospital.model.creatures.abstractclass.BeteUtils.genererAge;
import static com.fantasyhospital.model.creatures.abstractclass.BeteUtils.genererMoral;
import static com.fantasyhospital.model.creatures.abstractclass.BeteUtils.genererNomAleatoire;
import static com.fantasyhospital.model.creatures.abstractclass.BeteUtils.genererPoids;
import static com.fantasyhospital.model.creatures.abstractclass.BeteUtils.genererSexeAleatoire;
import static com.fantasyhospital.model.creatures.abstractclass.BeteUtils.genererTaille;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
public abstract class Bete {

    protected String nomComplet;
    protected String sexe;
    protected int poids;
    protected int taille;
    protected int age;
    protected int moral;

    public Bete() {
        this(genererNomAleatoire(), genererSexeAleatoire(), genererPoids(), genererTaille(), genererAge(), genererMoral());
    }

    public Bete(String nomComplet, String sexe, int poids, int taille, int age, int moral) {
        this.nomComplet = nomComplet;
        this.sexe = sexe;
        this.poids = poids;
        this.taille = taille;
        this.age = age;
        this.moral = moral;
    }

    public abstract void attendre();

    public void trepasser(CopyOnWriteArrayList<Creature> creatures) {
        //mourir
        log.info("La créature {} se meurt.", this.nomComplet);
    }

}
