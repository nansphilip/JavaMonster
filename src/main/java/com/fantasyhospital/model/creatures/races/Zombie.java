package com.fantasyhospital.model.creatures.races;

import com.fantasyhospital.model.creatures.HabitantTriage;
import com.fantasyhospital.model.maladie.Maladie;
import com.fantasyhospital.model.creatures.interfaces.Regenerant;

import java.util.List;

public class Zombie extends HabitantTriage implements Regenerant {

    public Zombie() {
        super(genererNomAleatoire(), genererSexeAleatoire(), genererPoids(), genererTaille(), genererAge(), genererMoral(), null);
    }

    public Zombie(List<Maladie> maladies) {
        super(genererNomAleatoire(), genererSexeAleatoire(), genererPoids(), genererTaille(), genererAge(), genererMoral(), maladies);
    }

    public Zombie(String nom, String sexe, int poids, int taille, int age, int moral, List<Maladie> maladies) {
        super(nom, sexe, poids, taille, age, moral, maladies);
    }

    public void trepasser(){
        regenerer();
    }
} 