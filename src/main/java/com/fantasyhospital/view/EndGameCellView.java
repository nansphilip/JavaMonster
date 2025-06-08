package com.fantasyhospital.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class EndGameCellView extends VBox {

    @Getter
    private ImageView creatureImageView;

    @Getter
    private Label name;
    @Getter
    private Label ageLabel;

    private HBox topRow;

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
