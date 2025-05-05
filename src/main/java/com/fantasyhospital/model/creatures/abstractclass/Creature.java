package com.fantasyhospital.model.creatures.abstractclass;

import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Creature extends Bete {
    protected HashSet<Maladie> maladies = new HashSet<>();
    protected static final Random RANDOM = new Random();
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
        double rnd = RANDOM.nextDouble();
        if(rnd < 0.15){
            Creature creature = salle.getRandomCreature();
            while(creature.equals(this)){
                creature = salle.getRandomCreature();
            }
            Maladie maladie = this.getRandomMaladie();
            creature.tomberMalade(maladie);
            log.info("La créature {} s'emporte et contamine {} en lui transmettant {} dans la bagarre.", this.nomComplet, creature.nomComplet, maladie.getNom());
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

    public void tomberMalade(Maladie maladie){
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
        Random random = new Random();
        return (Maladie) this.maladies.toArray()[random.nextInt(this.maladies.size())];
    }

    public Maladie getHighLevelMaladie(){
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
} 