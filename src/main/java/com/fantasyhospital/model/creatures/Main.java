package com.fantasyhospital.model.creatures;

import com.fantasyhospital.model.creatures.races.Orque;

public class Main {
    public static void main(String[] args) {
        Orque orque = new Orque("orque", "m", 60, 180, 20, 10, null);
        orque.attendre();
    }
}
