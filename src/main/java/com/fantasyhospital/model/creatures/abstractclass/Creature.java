package com.fantasyhospital.model.creatures.abstractclass;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Creature extends Bete {
    @Setter @Getter protected CopyOnWriteArrayList<Maladie> maladies = new CopyOnWriteArrayList<>();
    protected static final Random RANDOM = new Random();
    private int nbHurlements;

    public Creature( CopyOnWriteArrayList<Maladie> maladies) {
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
        double rnd = new Random().nextDouble();
        //15% de chance de contaminer creature lorsqu'il s'emporte
        if(rnd < 0.15){
            Creature creature = salle.getRandomCreatureWithoutThisOne(this);
            Maladie maladie = this.getRandomMaladie();
            if(maladie == null){
                return;
            }
            creature.tomberMalade(maladie);
            log.info("La créature {} s'emporte et contamine {} en lui transmettant {} dans la bagarre.", this.nomComplet, creature.nomComplet, maladie.getNom());
        }
    }

    public void verifierMoral(Salle salle){
        if(this.nbHurlements > 2){
            semporter(salle);
            return;
        }
        if(this.moral == 0){
            hurler();
            this.nbHurlements++;
        } else {
            this.nbHurlements = 0;
        }
    }

    public boolean hasCreatureToleaveHospital(Salle salle){
        boolean creatureGetsOut = true;
        if(this.maladies.isEmpty()){
            return false;
        }
        for(Maladie maladie : this.maladies){
            if(maladie.estLethale()){
                log.info("La maladie {} de {} était à son apogée.", maladie.getNom(), this.nomComplet);
                creatureGetsOut = trepasser(salle);
                return creatureGetsOut;
            }
        }
        if(this.maladies.size() >= 4){
            log.info("{} a contracté trop de maladies.", this.nomComplet);
            creatureGetsOut = trepasser(salle);
            return creatureGetsOut;
        }
        //Rajouter 30% chance trepasser quand il s'emporte
        return false;
    }

    public void tomberMalade(Maladie maladie){
        if(this.maladies.contains(maladie)){
            for(Maladie maladieAModifier : this.maladies){
                if(maladieAModifier.equals(maladie)){
                    maladieAModifier.augmenterNiveau();
                }
            }
        } else {
            this.maladies.add(maladie);
        }
    }

    public boolean etreSoigne(Maladie maladie){
        return this.maladies.remove(maladie);
    }

    // Getters et setters omis pour la clarté
    public String getRace() {
        return this.getClass().getSimpleName();
    }

    public Maladie getRandomMaladie(){
        if(this.maladies.isEmpty()){
            log.error("La créature {} n'a aucune maladie.", this.nomComplet);
            return null;
        }
        Random random = new Random();
        return (Maladie) this.maladies.toArray()[random.nextInt(this.maladies.size())];
    }

    public Maladie getHighLevelMaladie(){
        if(this.maladies.isEmpty()){
            log.error("La créature {} n'a aucune maladie.", this.nomComplet);
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