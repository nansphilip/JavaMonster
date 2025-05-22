package com.fantasyhospital.view;

import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.model.creatures.abstractclass.Creature;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

public class CreatureCellView extends ListCell<Creature> {
    private VBox content;
    private HBox topRow;
    private VBox imageAndBar;
    private ImageView creatureImageView;
    private ImageView moraleImageView;
    private ImageView genderImageView;
    private StackPane diseasesStackPane;
    private ImageView backgroundView;
    private ImageView barView;
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

        genderImageView = new ImageView();
        genderImageView.setFitHeight(10);
        genderImageView.setFitWidth(10);

        backgroundView = new ImageView();
        barView = new ImageView();

        backgroundView.setFitHeight(12);
        backgroundView.setFitWidth(948);
        backgroundView.setOpacity(0.2);
        barView.setFitHeight(12);
        barView.setFitWidth(948);

        diseasesStackPane = new StackPane(backgroundView, barView);

        name = new Text();
        detailsLabel = new Label();
        detailsLabel.setWrapText(true);
        detailsLabel.setStyle("-fx-text-fill: lightgray;");

        imageAndBar = new VBox(3, creatureImageView, moraleImageView, diseasesStackPane);
        imageAndBar.setAlignment(Pos.CENTER);

        topRow = new HBox(10, imageAndBar, name, genderImageView);
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

            moraleImageView.setImage(getMoraleImageView(creature.getMorale()));

            genderImageView.setImage(getGenderImageView(creature.getSex()));

            backgroundView.setImage(new Image(getClass().getResourceAsStream("/images/diseases/diseasesBarBackground.png")));
            barView.setImage(new Image(getClass().getResourceAsStream("/images/diseases/diseasesBar.png")));

            name.setText(creature.getFullName());
            detailsLabel.setText(creature.toString());
            setGraphic(content);

        }
    }

    private Image getMoraleImageView(int morale) {
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

    private Image getGenderImageView(GenderType genderType) {
        String genderImagePath;

        if (genderType == GenderType.MALE) {
            genderImagePath = "/images/gender/Male.png";
        } else {
            genderImagePath = "/images/gender/Female.png";
        }
        return new Image(getClass().getResourceAsStream(genderImagePath));
    }
}

