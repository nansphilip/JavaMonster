package com.fantasyhospital.view;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

public class CreatureCellView extends ListCell<Creature> {
    private VBox content;
    private HBox topRow;
    private ImageView creatureImageView;
    private ImageView moraleImageView;
    private Text name;
    private Label detailsLabel;

    public CreatureCellView() {
        super();
        creatureImageView = new ImageView();
        creatureImageView.setFitHeight(30);
        creatureImageView.setFitWidth(30);

        moraleImageView = new ImageView();
        moraleImageView.setFitHeight(10);
        moraleImageView.setFitWidth(65);

        name = new Text();
        detailsLabel = new Label();
        detailsLabel.setWrapText(true);
        detailsLabel.setStyle("-fx-text-fill: lightgray;");

        VBox imageAndBar = new VBox(3, creatureImageView, moraleImageView);
        imageAndBar.setAlignment(Pos.CENTER);

        topRow = new HBox(10, imageAndBar, name);
        topRow.setAlignment(Pos.CENTER_LEFT);

        content = new VBox(5, topRow, detailsLabel);
    }

    @Override
    protected void updateItem(Creature creature, boolean empty) {
        super.updateItem(creature, empty);
        if (creature == null || empty) {
            setGraphic(null);
        } else {
            String imagePath = "/images/" + creature.getRace().toLowerCase() + ".png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            Image transparentImage = removePngBackground(image);
            Image croppedImage = cropImage(transparentImage);

            creatureImageView.setImage(croppedImage);

            moraleImageView.setImage(getMoraleImage(creature.getMorale()));

            name.setText(creature.getFullName());
            detailsLabel.setText(creature.toString());
            setGraphic(content);

        }
    }

    private Image getMoraleImage(int morale) {
        String moraleImagePath;

        if (morale == 0) {
            moraleImagePath = "/images/morale/EmptyLifeBar.png";
        } else if (morale <= 10) {
            moraleImagePath = "/images/morale/LowLifeBar1.png";
        } else if (morale <= 20) {
            moraleImagePath = "/images/morale/LowLifeBar2.png";
        } else if (morale <= 30) {
            moraleImagePath = "/images/morale/LowLifeBar3.png";
        } else if (morale <= 40) {
            moraleImagePath = "/images/morale/MediumLifeBar1.png";
        } else if (morale <= 50) {
            moraleImagePath = "/images/morale/MediumLifeBar2.png";
        } else if (morale <= 70) {
            moraleImagePath = "/images/morale/HighLifeBar1.png";
        } else if (morale <= 80) {
            moraleImagePath = "/images/morale/HighLifeBar2.png";
        } else {
            moraleImagePath = "/images/morale/FullLifeBar.png";
        }

        return new Image(getClass().getResourceAsStream(moraleImagePath));
    }
}

