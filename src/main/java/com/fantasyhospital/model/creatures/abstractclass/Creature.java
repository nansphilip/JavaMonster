package com.fantasyhospital.model.creatures.abstractclass;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.Singleton;
import com.fantasyhospital.enums.ActionType;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Classe abstraite représentant une créature dans Fantasy Hospital. Gère les
 * maladies, le moral, les réactions et les interactions avec les salles.
 */
@Slf4j
public abstract class Creature extends Bete {

    /**
     * Ensemble des maladies contractées par la créature
     */
    @Setter @Getter protected CopyOnWriteArrayList<Maladie> maladies = new CopyOnWriteArrayList<>();

    /**
     * Générateur aléatoire partagé
     */
    protected static final Random RANDOM = new Random();
    private int nbHurlements;

    /**
     * Construit une créature avec un ensemble de maladies initial.
     */
    public Creature( CopyOnWriteArrayList<Maladie> maladies) {
        super();
        this.maladies = maladies;
        this.nbHurlements = 0;
    }

    /**
     * Fait hurler la créature si son moral est trop bas.
     */
    public void hurler() {
        log.info("La créature {} a le moral dans les chaussettes, elle hurle.", this.nomComplet);
    }

    /**
     * La créature s'emporte et peut contaminer une autre créature de la salle.
     * Elle a 30% de chance de trépasser en s'emportant
     * @return true si creature trepasse, false sinon
     */
    public boolean semporter(Salle salle) {
        if (salle.getCreatures().isEmpty()) {
            return false;
        }
        if(Math.random() < 0.30){
            log.info("La créature {} s'emporte trop fort, elle trépasse.", this.nomComplet);
            return true;
        }

        //15% de chance de contaminer creature lorsqu'il s'emporte
        if(Math.random() < 0.15){
            Creature creature = salle.getRandomCreatureWithoutThisOne(this);
            Maladie maladie = this.getRandomMaladie();

            if (maladie == null || creature == null) {
                log.info("La créature {} s'emporte mais n'a aucune créature à contaminer...", this.nomComplet);
                return false;
            }

            creature.tomberMalade(maladie);

            log.info("La créature {} s'emporte et contamine {} en lui transmettant {} dans la bagarre.", this.nomComplet, creature.nomComplet, maladie.getNom());
        }
        return false;
    }

    /**
     * Vérifie le moral de la créature et déclenche des réactions si besoin.
     * @return true si la créature trépasse, false sinon
     */
    public boolean verifierMoral(Salle salle) {
        if (this.nbHurlements > 2) {
            return semporter(salle);
        }
        if (this.moral == 0) {
            hurler();
            this.nbHurlements++;
        } else {
            this.nbHurlements = 0;
        }
        return false;
    }

    /**
     * Vérifie la santé de la créature (décès si maladie létale ou trop de
     * maladies).
     *
     * @return true si la créature doit quitter la salle (décès), false sinon
     */
    public boolean hasCreatureToleaveHospital(Salle salle){
        boolean creatureGetsOut = true;

        Singleton instanceSingleton = Singleton.getInstance();

        if(this.maladies.isEmpty()){
            instanceSingleton.addCreatureSoigne(this);
            return true;
        }
        for(Maladie maladie : this.maladies){
            if(maladie.estLethale()){
                log.info("La maladie {} de {} était à son apogée.", maladie.getNom(), this.nomComplet);
                creatureGetsOut = trepasser(salle);
                if(creatureGetsOut){
                    instanceSingleton.addCreatureTrepas(this);
                }
                return creatureGetsOut;
            }
        }
        if(this.maladies.size() >= 4){
            log.info("{} a contracté trop de maladies.", this.nomComplet);
            creatureGetsOut = trepasser(salle);
            if(creatureGetsOut){
                instanceSingleton.addCreatureTrepas(this);
            }
            return creatureGetsOut;
        }
        // TODO: Rajouter 30% chance trepasser quand il s'emporte
        return false;
    }

    /**
     * Fait contracter une maladie à la créature (ou augmente son niveau si déjà
     * présente).
     */
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

    /**
     * Soigne la créature d'une maladie donnée.
     * Et lui redonne 50 pts de moral
     * @return true si la maladie a été retirée
     */
    public boolean etreSoigne(Maladie maladie) {
        if(!this.maladies.contains(maladie)){
            return false;
        }
        this.moral = Math.min(this.moral + ActionType.CREATURE_SOIN.getVariationMoral(), 100);
        return this.maladies.remove(maladie);
    }

    /**
     * Retourne la race de la créature (nom de la classe concrète).
     */
    public String getRace() {
        return this.getClass().getSimpleName();
    }

    /**
     * Retourne une maladie aléatoire parmi celles de la créature.
     */
    public Maladie getRandomMaladie(){
        if(this.maladies.isEmpty()){
            log.error("La créature {} n'a aucune maladie.", this.nomComplet);
            return null;
        }
        Random random = new Random();
        return (Maladie) this.maladies.toArray()[random.nextInt(this.maladies.size())];
    }

    /**
     * Retourne la maladie avec le niveau le plus élevé.
     */
    public Maladie getHighLevelMaladie() {
        if (this.maladies.isEmpty()) {
            log.error("La créature {} n'a aucune maladie.", this.nomComplet);
            return null;
        }
        Maladie maladieWithHighLevel = this.maladies.iterator().next();
        for (Maladie maladie : this.maladies) {
            if (maladie.getNiveauActuel() > maladieWithHighLevel.getNiveauActuel()) {
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Creature creature = (Creature) o;
        return Objects.equals(nomComplet, creature.nomComplet) && Objects.equals(taille, creature.taille) && Objects.equals(poids, creature.poids);
    }



    @Override
    public int hashCode() {
        return Objects.hash(nomComplet, taille, poids);
    }
}
