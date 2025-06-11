package com.fantasyhospital.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

/**
 * EndGameCellView is a custom view for displaying information about a creature at the end of the game.
 * It includes an image of the creature, its name, and its age.
 */
public class EndGameCellView extends VBox {

    /**
     * ImageView for displaying the creature's image.
     */
    @Getter
    private ImageView creatureImageView;

    /**
     * Labels for displaying the creature's name and age.
     */
    @Getter
    private Label name;

    /**
     * Label for displaying the creature's age.
     */
    @Getter
    private Label ageLabel;

    private HBox topRow;

    /**
     * Constructor for EndGameCellView.
     * Initializes the layout and components for displaying the creature's information.
     */
    public EndGameCellView() {
        super(5);

        creatureImageView = new ImageView();
        creatureImageView.setPreserveRatio(true);
        creatureImageView.setFitHeight(30);
        creatureImageView.setFitWidth(30);

        name = new Label();
        ageLabel = new Label();

        topRow = new HBox(10, creatureImageView, name, ageLabel);
        topRow.setAlignment(Pos.CENTER_LEFT);

        this.getChildren().add(topRow);
        this.setAlignment(Pos.CENTER_LEFT);
    }

}
