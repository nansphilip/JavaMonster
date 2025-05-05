package com.fantasyhospital.model.creatures.abstractclass;

import com.fantasyhospital.Simulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;
import static com.fantasyhospital.model.creatures.abstractclass.BeteUtils.genererAge;
import static com.fantasyhospital.model.creatures.abstractclass.BeteUtils.genererMoral;
import static com.fantasyhospital.model.creatures.abstractclass.BeteUtils.genererNomAleatoire;
import static com.fantasyhospital.model.creatures.abstractclass.BeteUtils.genererPoids;
import java.util.LinkedHashSet;
import static com.fantasyhospital.model.creatures.abstractclass.BeteUtils.genererSexeAleatoire;
import static com.fantasyhospital.model.creatures.abstractclass.BeteUtils.genererTaille;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j public abstract class Bete {

    protected String nomComplet;
    protected String sexe;
    protected int poids;
    protected int taille;
    protected int age;
    protected int moral;
    private static final Logger logger = LoggerFactory.getLogger(Bete.class);


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
        logger.info("La créature {} se meurt.", this.nomComplet);
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public int getPoids() {
        return poids;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getMoral() {
        return moral;
    }

    public void setMoral(int moral) {
        this.moral = moral;
    }
}
