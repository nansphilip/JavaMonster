package com.fantasyhospital.util;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class GridUtils {

    /**
     * Crée un GridPane avec deux colonnes :
     * - une colonne fixe
     * - une colonne qui s'étire (hgrow=ALWAYS)
     *
     * @param fixedColumnWidth largeur fixe de la première colonne
     * @param hgap espacement horizontal entre les colonnes
     * @return GridPane configuré
     */
    public static GridPane createTwoColumnGrid(double fixedColumnWidth, double hgap) {
        GridPane grid = new GridPane();
        grid.setHgap(hgap);

        ColumnConstraints col1 = new ColumnConstraints(fixedColumnWidth);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2);

        return grid;
    }
}
