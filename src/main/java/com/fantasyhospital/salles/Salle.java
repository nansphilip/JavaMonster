package com.fantasyhospital.salles;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fantasyhospital.model.creatures.abstractclass.Creature;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Slf4j
public class Salle {

    @Setter protected String nom;
    @Setter protected double superficie;
    protected final int NB_MAX_CREATURE;
    @Setter @Getter protected CopyOnWriteArrayList<Creature> creatures = new CopyOnWriteArrayList<>();

    public Salle(String nom, double superficie, int NB_MAX_CREATURE) {
        this.nom = nom;
        this.superficie = superficie;
        this.NB_MAX_CREATURE = NB_MAX_CREATURE;
    }

    public boolean ajouterCreature(Creature creature){
        if (creatures.size() >= NB_MAX_CREATURE) {
            return false;
        } else {
            creatures.add(creature);
            return true;
        }
    }

    public boolean enleverCreature(Creature creature){
        return this.creatures.remove(creature);
    }

    public void afficherInfosService(){
        log.info("\n{}", this);
    }

    public void afficherInfosCreatures(){

    }

	public Creature getFirstCreature() {
        return creatures.iterator().next();
    }

    public Creature getLastCreature() {
        return (Creature) creatures.toArray()[ creatures.size()-1 ];
    }

	public Creature getCreatureByName(String creatureName){
        for(Creature creature : creatures){
            if(creature.getNomComplet().equals(creatureName)){
                return creature;
            }
        }
        return null;
    }

    public Creature getRandomCreature(){
        Random random = new Random();
        return (Creature) this.creatures.toArray()[random.nextInt(this.creatures.size())];
    }

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Salle : ").append(nom).append(" ---\n");
        sb.append("Superficie : ").append(superficie).append(" m²\n");
        sb.append("Nombre de créatures maximale : ").append(NB_MAX_CREATURE).append("\n");

        sb.append("\n🏥 Salle :\n");
        if (creatures.isEmpty()) {
            sb.append("  Aucune créature dans ce service.\n");
        } else {
            for (Creature c : creatures) {
                sb.append("  - ").append(c).append("\n");
            }
        }

        return sb.toString();
    }
}
