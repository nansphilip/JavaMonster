package com.fantasyhospital.view;

import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.TriageResident;
import com.fantasyhospital.model.creatures.VIPPatient;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.disease.Disease;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

/**
 * CreatureCellView is a custom ListCell for displaying information about a Creature in the Fantasy Hospital application.
 * It shows the creature's image
 */
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
    private Label roomLabel;
    private TranslateTransition moraleAnimation;
    private ImageView healingImageView;


    private Hospital hospital;


    /**
     * Constructor for CreatureCellView.
     * Initializes the UI components and sets up the layout for displaying creature information.
     *
     * @param hospital The hospital instance to which this cell belongs, used for retrieving room information.
     */
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

        healingImageView = new ImageView();
        healingImageView.setFitHeight(10);
        healingImageView.setFitWidth(10);
        healingImageView.setVisible(false);

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

        moraleBox = new HBox(5, moraleTrendImageView, moraleImageView, moraleLabel);
        moraleBox.setAlignment(Pos.CENTER_LEFT);

        lifeAndDiseasesBox = new VBox(2, moraleBox, diseasesBox);
        lifeAndDiseasesBox.setAlignment(Pos.CENTER_LEFT);

        nameGenderAgeBox = new HBox(5, name, genderImageView, ageLabel, roomLabel);
        nameGenderAgeBox.setAlignment(Pos.CENTER_LEFT);

        nameAndDiseasesBox = new VBox(2, nameGenderAgeBox, lifeAndDiseasesBox);
        nameAndDiseasesBox.setAlignment(Pos.CENTER_LEFT);

        StackPane creatureImageStack = new StackPane(creatureImageView, moraleTrendImageView, healingImageView);
        StackPane.setAlignment(moraleTrendImageView, Pos.TOP_RIGHT);
        StackPane.setAlignment(healingImageView, Pos.TOP_LEFT);
        creatureImageStack.setPrefSize(30, 30);

        moraleTrendImageView.setPickOnBounds(false);
        healingImageView.setPickOnBounds(false);

        topRow = new HBox(10, creatureImageStack, nameAndDiseasesBox);
        topRow.setAlignment(Pos.CENTER_LEFT);

        content = new VBox(5, topRow);
//        content = new VBox(5, topRow, detailsLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        this.setOnMouseClicked(event -> {
            Creature selected = getItem();
            if (selected != null) {
                openDetailPanel(selected);
            }
        });
        this.setOnMouseEntered(event -> {
            setCursor(Cursor.HAND);
        });

        this.setOnMouseExited(event -> {
            setCursor(Cursor.DEFAULT);
        });
    }

    /**
     * Updates the cell with the given creature's information.
     * This method is called by the ListView to update the display of each cell.
     *
     * @param creature The creature to display in this cell.
     * @param empty    Indicates whether the cell is empty or not.
     */
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

            if (moraleAnimation != null) {
                moraleAnimation.stop();
                moraleTrendImageView.setTranslateY(0);
            }

            // Définir l'image de tendance du moral seulement si le moral a changé
            if (creature.isMoraleIncreasing()) {
                moraleTrendImageView.setImage(new Image(getClass().getResourceAsStream("/images/morale/MoraleUp.png")));
                moraleTrendImageView.setVisible(true);

                moraleAnimation = new TranslateTransition(Duration.millis(500), moraleTrendImageView);
                moraleAnimation.setFromY(0);
                moraleAnimation.setToY(-3);
                moraleAnimation.setCycleCount(Animation.INDEFINITE);
                moraleAnimation.setAutoReverse(true);
                moraleAnimation.play();

            } else if (creature.isMoraleDecreasing()) {
                moraleTrendImageView.setImage(new Image(getClass().getResourceAsStream("/images/morale/MoraleDown.png")));
                moraleTrendImageView.setVisible(true);

                moraleAnimation = new TranslateTransition(Duration.millis(500), moraleTrendImageView);
                moraleAnimation.setFromY(0);
                moraleAnimation.setToY(3);
                moraleAnimation.setCycleCount(Animation.INDEFINITE);
                moraleAnimation.setAutoReverse(true);
                moraleAnimation.play();

            } else {
                moraleTrendImageView.setVisible(false);
            }

            if (creature.isRecentlyHealed()) {
                healingImageView.setImage(new Image(getClass().getResourceAsStream("/images/diseases/Heal.png")));
                healingImageView.setVisible(true);
                creature.setRecentlyHealed(false);
            } else {
                healingImageView.setVisible(false);
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

    /**
     * Returns the image view for the morale level of the creature.
     *
     * @param morale The morale level of the creature.
     * @return An ImageView representing the morale level.
     */
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

    /**
     * Returns the image view
     *
     * @param genderType
     * @return
     */
    private Image getGenderImageView(GenderType genderType) {
        String genderImagePath;

        if (genderType == GenderType.MALE) {
            genderImagePath = "/images/gender/Male.png";
        } else if (genderType == GenderType.FEMALE) {
            genderImagePath = "/images/gender/Female.png";
        } else {
            genderImagePath = "/images/gender/LgbtFlag.png";
        }
        return new Image(getClass().getResourceAsStream(genderImagePath));
    }

    /**
     * Returns the image representing the disease level.
     *
     * @param level The disease level.
     * @return An Image representing the disease level.
     */
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

    /**
     * Opens a detail panel to display detailed information about the selected creature.
     *
     * @param creature The creature for which to display details.
     */
    private void openDetailPanel(Creature creature) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #ffffff;");
        box.setAlignment(Pos.TOP_CENTER);

        String imagePath = "/images/races/" + creature.getRace().toLowerCase() + ".png";
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        Image transparentImage = removePngBackground(image);
        Image croppedImage = cropImage(transparentImage);

        ImageView largeCreatureImage = new ImageView(croppedImage);
        largeCreatureImage.setPreserveRatio(true);
        largeCreatureImage.setFitHeight(80);

        ImageView genderView = new ImageView(getGenderImageView(creature.getSex()));
        genderView.setFitHeight(14);
        genderView.setFitWidth(14);
        Label genderLabel = new Label("Genre : " + creature.getSex().getLabel());
        HBox genderBox = new HBox(5, genderView, genderLabel);
        genderBox.setAlignment(Pos.CENTER);

        ImageView moraleView = new ImageView(getMoraleImageView(creature.getMorale()));
        moraleView.setFitHeight(10);
        moraleView.setFitWidth(65);
        Label moraleLabel = new Label("Moral (" + creature.getMorale() + "/100)");
        HBox moraleBox = new HBox(5, moraleView, moraleLabel);
        moraleBox.setAlignment(Pos.CENTER);

        String type = "Type : ";
        if (creature instanceof VIPPatient) {
            type += "VIP";
        } else if (creature instanceof TriageResident) {
            type += "Résident en triage";
        } else {
            type += "";
        }
        Label typeLabel = new Label(type);

        Label name = new Label("Nom : " + creature.getFullName());
        Label age = new Label("Âge : " + creature.getAge());
        Label height = new Label("Taille : " + creature.getHeight() + " cm");
        Label weight = new Label("Poids : " + creature.getWeight() + " kg");
        Label diseases = new Label("Maladies :\n" + creature.getDiseases().stream()
                .map(Disease::getFullName)
                .reduce((d1, d2) -> d1 + "\n" + d2)
                .orElse("Aucune"));
        diseases.setWrapText(true);


        box.getChildren().addAll(largeCreatureImage, typeLabel, name, age, height, weight, genderBox, moraleBox, diseases);

        DetailsCellView.show("Détails de la créature", box, 350, 450);
    }
}

