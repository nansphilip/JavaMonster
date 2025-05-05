package com.fantasyhospital.model.creatures.abstractclass;

import com.fantasyhospital.model.creatures.Medecin;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.salles.Salle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class Creature extends Bete {
    protected HashSet<Maladie> maladies = new HashSet<>();
    public static Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(Creature.class);
    private int nbHurlements;

    public Creature(String nomComplet, String sexe, int poids, int taille, int age, int moral, HashSet<Maladie> maladies) {
        super(nomComplet, sexe, poids, taille, age, moral);
        this.maladies = maladies;
        this.nbHurlements = 0;
    }

    public static String genererSexeAleatoire() {
        String[] sexes = {"M", "F"};
        return sexes[random.nextInt(sexes.length)];
    }

    public static int genererPoids(){
        return (int) (Math.round((50.0 + random.nextDouble() * 50.0) * 10.0) / 10.0);
    }

    public static int genererTaille(){
        return (int) ((int) Math.round((150.0 + random.nextDouble() * 50.0) * 10.0) / 10.0);
    }

    public static int genererAge(){
        return 18 + random.nextInt(60);
    }

    public static int genererMoral(){
        return 60 + random.nextInt(40);
    }

    public void hurler(){
        logger.info("La créature {} a le moral dans les chaussettes, elle hurle.", this.nomComplet);
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
                trepasser(salle.getCreatures());
                return true;
            }
        }
        if(this.maladies.size() >= 4){
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
        return Objects.hash(maladies, nbHurlements);
    }
}