package com.fantasyhospital.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

public class GameBoardController {

    @FXML
    private GridPane gridPane;

    private static final int TILE_SIZE = 54;

    @FXML
    public void initialize() {
        String[][] levelMap = {
                {"CoinHautGauche", "Mur", "Mur", "Mur", "CoinHautDroite"},
                {"Mur", "Sol", "Sol", "Sol", "Mur"},
                {"Mur", "Sol", "Sol", "Sol", "Mur"},
                {"Mur", "Sol", "Sol", "Sol", "Mur"},
                {"CoinBasGauche", "Mur", "Mur", "Mur", "CoinBasDroite"},
        };


        for (int row = 0; row < levelMap.length; row++) {
            for (int col = 0; col < levelMap[row].length; col++) {
                String type = levelMap[row][col];
                Image img = new Image(getClass().getResourceAsStream("/tiles/" + type + ".png"));

                Image transparentImage = removePngBackground(img);
                Image croppedImage = cropImage(transparentImage);


                ImageView view = new ImageView(croppedImage);
                view.setFitWidth(TILE_SIZE);
                view.setFitHeight(TILE_SIZE);
                view.setPreserveRatio(false);
                gridPane.add(view, col, row);
            }
        }
    }
}
