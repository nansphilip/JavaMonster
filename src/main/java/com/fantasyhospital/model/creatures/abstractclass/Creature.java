package com.fantasyhospital.model.creatures.abstractclass;

import java.util.HashSet;
import com.fantasyhospital.salles.Salle;

import static com.fantasyhospital.model.creatures.abstractclass.Bete.log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import lombok.extern.slf4j.Slf4j;


import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;

@Slf4j
public abstract class Creature extends Bete {
    protected HashSet<Maladie> maladies = new HashSet<>();
    public static Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(Creature.class);
    private int nbHurlements;

    public Creature( HashSet<Maladie> maladies) {
        super();
        this.maladies = maladies;
        this.nbHurlements = 0;
    }

    public void hurler(){
        log.info("La créature {} a le moral dans les chaussettes, elle hurle.", this.nomComplet);
    }


    public void semporter(Salle salle){
        if(salle.getCreatures().isEmpty()){
            return;
        }
        Random random = new Random();
        double rnd = random.nextDouble();
        if(rnd < 0.15){
            Creature creature = salle.getRandomCreature();
            while(creature.equals(this)){
                creature = salle.getRandomCreature();
            }
            Maladie maladie = this.getRandomMaladie();
            if(maladie == null){
                return;
            }
            creature.tomberMalade(maladie);
            logger.info("La créature {} s'emporte et contamine {} en lui transmettant {} dans la bagarre.", this.nomComplet, creature.nomComplet, maladie.getNomAbrege());
        }
    }

    public void verifierMoral(Salle salle){
        if(this.moral == 0){
            hurler();
            this.nbHurlements++;
        }
        if(this.nbHurlements > 2){
            semporter(salle);
        }
    }

    public boolean verifierSante(Salle salle){
        for(Maladie maladie : this.maladies){
            if(maladie.estLethale()){
                logger.info("La maladie {} de {} était à son apogée.", maladie.getNomAbrege(), this.nomComplet);
                trepasser(salle.getCreatures());
                return true;
            }
        }
        if(this.maladies.size() >= 4){
            logger.info("{} a contracté trop de maladies.", this.nomComplet);
            trepasser(salle.getCreatures());
            return true;
        }
        //Rajouter 30% chance trepasser quand il s'emporte
        return false;
    }

    public void tomberMalade(Maladie maladie){
        //Si add retourne faux c'est parce que la creature avait déjà cette maladie, on lui ajoute donc un niveau supplémentaire
        if(!this.maladies.add(maladie)){
            for(Maladie maladieAModifier : this.maladies){
                if(maladieAModifier.equals(maladie)){
                    maladieAModifier.augmenterNiveau();
                }
            }
        }
    }

    public boolean etreSoigne(Maladie maladie){
        return this.maladies.remove(maladie);
    public boolean etreSoigne(Maladie maladie){
        return this.maladies.remove(maladie);
    }

    // Getters et setters omis pour la clarté
    public String getRace() {
        return this.getClass().getSimpleName();
    }

    public HashSet<Maladie> getMaladies() {
        return maladies;
    }

    public void setMaladies(HashSet<Maladie> maladies) {
        this.maladies = maladies;
    }

    public Maladie getRandomMaladie(){
        if(this.maladies.isEmpty()){
            return null;
        }
        Random random = new Random();
        return (Maladie) this.maladies.toArray()[random.nextInt(this.maladies.size())];
    }

    public Maladie getHighLevelMaladie(){
        if(this.maladies.isEmpty()){
            return null;
        }
        Maladie maladieWithHighLevel = this.maladies.iterator().next();
        for(Maladie maladie : this.maladies){
            if(maladie.getNiveauActuel() > maladieWithHighLevel.getNiveauActuel()){
                maladieWithHighLevel = maladie;
            }
        }
        return maladieWithHighLevel;
    }

    @Override
    public String toString() {
        return "[" + getRace() + "] nom='" + nomComplet + "', sexe='" + sexe + "', âge=" + age + ", moral=" + moral + ", maladie(s) : " + this.maladies;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Creature creature = (Creature) o;
        return Objects.equals(nomComplet, creature.nomComplet) && Objects.equals(taille, creature.taille) && Objects.equals(poids, creature.poids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomComplet, taille, poids);
    }
}