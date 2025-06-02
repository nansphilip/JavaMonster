package com.fantasyhospital.view;

import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.disease.Disease;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

public class CreatureCellView extends ListCell<Creature> {
    private HBox topRow;
    private HBox nameGenderAgeBox;
    private HBox moraleBox;
    private ImageView moraleTrendImageView;
    private VBox content;
    private VBox nameAndDiseasesBox;
    private VBox diseasesBox;
    private VBox lifeAndDiseasesBox;
    private ImageView creatureImageView;
    private ImageView moraleImageView;
    private ImageView genderImageView;
    private GridPane diseasesGrid;
    private Text name;
    private Label ageLabel;
    private Label moraleLabel;
    private Label detailsLabel;
    private Label roomLabel;

    private Hospital hospital;


    public CreatureCellView(Hospital hospital) {
        super();
        this.hospital = hospital;

        creatureImageView = new ImageView();
        creatureImageView.setPreserveRatio(true);
        creatureImageView.setFitHeight(30);
        creatureImageView.setFitWidth(30);

        moraleImageView = new ImageView();
        moraleImageView.setFitHeight(10);
        moraleImageView.setFitWidth(65);

        genderImageView = new ImageView();
        genderImageView.setFitHeight(10);
        genderImageView.setFitWidth(10);

        moraleTrendImageView = new ImageView();
        moraleTrendImageView.setFitHeight(10);
        moraleTrendImageView.setFitWidth(10);
        moraleTrendImageView.setVisible(false);

        name = new Text();
        name.setStyle("-fx-font-weight: bold;");

        ageLabel = new Label();
        ageLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 10px;");

        roomLabel = new Label();
        roomLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: gray; -fx-font-size: 10px;");

        moraleLabel = new Label();
        moraleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 10px;");

//        detailsLabel = new Label();
//        detailsLabel.setWrapText(true);
//        detailsLabel.setStyle("-fx-text-fill: lightgray;");
//        detailsLabel.setMaxWidth(300);
//        detailsLabel.setAlignment(Pos.TOP_LEFT);

        diseasesGrid = new GridPane();
        diseasesGrid.setHgap(5);
        ColumnConstraints col1 = new ColumnConstraints(65);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        diseasesGrid.getColumnConstraints().addAll(col1, col2);

        diseasesBox = new VBox(2);

        moraleBox = new HBox(5,moraleTrendImageView, moraleImageView, moraleLabel);
        moraleBox.setAlignment(Pos.CENTER_LEFT);

        lifeAndDiseasesBox = new VBox(2, moraleBox, diseasesBox);
        lifeAndDiseasesBox.setAlignment(Pos.CENTER_LEFT);

        nameGenderAgeBox = new HBox(5, name, genderImageView, ageLabel, roomLabel);
        nameGenderAgeBox.setAlignment(Pos.CENTER_LEFT);

        nameAndDiseasesBox = new VBox(2, nameGenderAgeBox, lifeAndDiseasesBox);
        nameAndDiseasesBox.setAlignment(Pos.CENTER_LEFT);

        topRow = new HBox(10, creatureImageView, nameAndDiseasesBox);
        topRow.setAlignment(Pos.CENTER_LEFT);

        content = new VBox(5, topRow);
//        content = new VBox(5, topRow, detailsLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        this.setOnMouseClicked(event -> {
            Creature selected = getItem();
            if (selected != null) {
                showCreaturePopup(selected);
            }
        });
    }

    @Override
    protected void updateItem(Creature creature, boolean empty) {
        String roomName = "";
        
        super.updateItem(creature, empty);
        if (creature == null || empty) {
            setGraphic(null);
        } else {
            String imagePath = "/images/races/" + creature.getRace().toLowerCase() + ".png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            Image transparentImage = removePngBackground(image);
            Image croppedImage = cropImage(transparentImage);

            creatureImageView.setImage(croppedImage);

            moraleImageView.setImage(getMoraleImageView(creature.getMorale()));

            // Définir l'image de tendance du moral seulement si le moral a changé
            if (creature.isMoraleIncreasing()) {
                moraleTrendImageView.setImage(new Image(getClass().getResourceAsStream("/images/morale/moral-up.png")));
                moraleTrendImageView.setVisible(true);
            } else if (creature.isMoraleDecreasing()) {
                moraleTrendImageView.setImage(new Image(getClass().getResourceAsStream("/images/morale/moral-down.png")));
                moraleTrendImageView.setVisible(true);
            } else {
                moraleTrendImageView.setVisible(false);
            }

            genderImageView.setImage(getGenderImageView(creature.getSex()));

            diseasesBox.getChildren().clear();

            for (Disease disease : creature.getDiseases()) {
                int level = disease.getCurrentLevel();
                if (level > 0) {
                    Image diseaseImage = getDiseaseLevelImage(level);
                    ImageView diseaseView = new ImageView(diseaseImage);
                    diseaseView.setFitHeight(10);
                    diseaseView.setFitWidth(65);

                    Label diseaseLabel = new Label(disease.getName() + " (" + level + "/" + disease.getLEVEL_MAX() + ")");
                    diseaseLabel.setStyle("-fx-text-fill: black;");
                    diseaseLabel.setWrapText(true);

                    HBox diseaseHBox = new HBox(5, diseaseView, diseaseLabel);
                    diseaseHBox.setAlignment(Pos.CENTER_LEFT);

                    diseasesBox.getChildren().add(diseaseHBox);
                }
                if (hospital != null) {
                    var room = hospital.getRoomOfCreature(creature);
                    if (room != null) {
                        roomName = room.getName();
                    }
                }

            }

            name.setText(creature.getFullName());
            ageLabel.setText("(" + creature.getAge() + ")");
            roomLabel.setText(roomName);
            
            moraleLabel.setText("Moral (" + creature.getMorale() + "/100)");
//            detailsLabel.setText(creature.toString());
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

    private Image getDiseaseLevelImage(int level) {
        String imagePath;

        if (level <= 2) {
            imagePath = "/images/diseases/LowDiseasesBar1.png";
        } else if (level <= 3) {
            imagePath = "/images/diseases/LowDiseasesBar2.png";
        } else if (level <= 4) {
            imagePath = "/images/diseases/LowDiseasesBar3.png";
        } else if (level <= 5) {
            imagePath = "/images/diseases/MediumDiseasesBar1.png";
        } else if (level <= 6) {
            imagePath = "/images/diseases/MediumDiseasesBar2.png";
        } else if (level <= 8) {
            imagePath = "/images/diseases/HighDiseasesBar1.png";
        } else if (level <= 9) {
            imagePath = "/images/diseases/HighDiseasesBar2.png";
        } else {
            imagePath = "/images/diseases/FullDiseasesBar.png";
        }

        return new Image(getClass().getResourceAsStream(imagePath));
    }

    private void showCreaturePopup(Creature creature) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Détails de la créature");

        detailsLabel = new Label(creature.toString());
        detailsLabel.setWrapText(true);

        VBox detailContent = new VBox(10, detailsLabel);
        detailContent.setPadding(new Insets(10));

        Scene scene = new Scene(detailContent, 300, 200); // Taille de la popup
        popup.setScene(scene);
        popup.showAndWait();
    }
}

