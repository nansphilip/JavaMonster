package com.fantasyhospital.view;

import com.fantasyhospital.controller.CryptViewController;
import com.fantasyhospital.controller.MedicalServiceDetailsController;
import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

/**
 * CryptCellView is a utility class that provides a method to create a graphical representation of a Crypt room.
 * It displays the room's title, budget, type, beds with creatures, temperature, and doctors in the room.
 */
public class CryptCellView {

    private final Crypt crypt;
    private final CryptViewController cryptViewController;
    private final List<String> bedImagePaths = new ArrayList<>();
    private final List<Doctor> doctors;
    private final Random random = new Random();

    /**
     * Constructs a CryptCellView with the specified Crypt, list of Doctors, and CryptViewController.
     *
     * @param crypt               the Crypt room to be displayed
     * @param doctors             the list of Doctors in the Crypt
     * @param cryptViewController the controller for handling interactions with the Crypt view
     */
    public CryptCellView(Crypt crypt, List<Doctor> doctors, CryptViewController cryptViewController) {
        this.crypt = crypt;
        this.doctors = doctors;
        this.cryptViewController = cryptViewController;
        generateBedImagePaths(crypt.getMAX_CREATURE());
    }

    /**
     * Generates a list of random bed image paths based on the maximum number of beds in the Crypt.
     *
     * @param max the maximum number of beds to generate image paths for
     */
    private void generateBedImagePaths(int max) {
        bedImagePaths.clear();
        for (int i = 0; i < max; i++) {
            bedImagePaths.add(getRandomCryptBedImage());
        }
    }

    /**
     * Returns a random bed image path from a predefined set of options.
     *
     * @return a random bed image path
     */
    private String getRandomCryptBedImage() {
        String[] options = {
                "/images/room/BedBones.png",
                "/images/room/BedBlood.png",
                "/images/room/Bed.png"
        };
        return options[random.nextInt(options.length)];
    }

    /**
     * Renders the Crypt view as a VBox containing various UI elements.
     *
     * @return a VBox containing the rendered Crypt view
     */
    public VBox render() {
        VBox container = new VBox(8);
        container.setPadding(new Insets(10));
        container.setAlignment(Pos.TOP_CENTER);

        // Ligne du haut : titre à gauche + clim à droite
        HBox topLine = new HBox();
        topLine.setAlignment(Pos.CENTER_LEFT);
        topLine.setSpacing(10);

        Label titleLabel = new Label("CRYPTE");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #cccccc;");

        // Clim à droite, donc on utilise un HBox avec "title" à gauche et "clim" à droite via Region qui pousse
        Label climLabel = new Label("Clim : " + (crypt.isAirflow() ? "ON" : "OFF"));
        climLabel.setStyle("-fx-text-fill: " + (crypt.isAirflow() ? "#00ff00" : "#ff0000") + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topLine.getChildren().addAll(titleLabel, spacer, climLabel);
        container.getChildren().add(topLine);

        // Ligne Budget en dessous, à gauche
        Label budget = new Label("Budget : " + BudgetType.fromRatio(crypt.getCryptBudget()) + " (" + crypt.getCryptBudget() + ") ");
        budget.setStyle("-fx-text-fill: #cccccc;");
        budget.setAlignment(Pos.CENTER_LEFT);
        container.getChildren().add(budget);

        Label type = new Label("Type : " + crypt.getRoomType());
        type.setStyle("-fx-text-fill: #cccccc;");
        type.setLayoutX(10);
        type.setLayoutY(40);
        type.setMaxWidth(160);
        container.getChildren().add(type);

        // Ligne lits + température (image + valeur) à droite des lits
        HBox bedsAndTempLine = new HBox(10);
        bedsAndTempLine.setAlignment(Pos.CENTER_LEFT);

        FlowPane beds = createBedsView();
        bedsAndTempLine.getChildren().add(beds);

        // Température (image + valeur) à droite
        VBox tempBox = new VBox(2);
        tempBox.setAlignment(Pos.CENTER_LEFT);

        int temperature = crypt.getTemperature();
        ImageView tempImageView = new ImageView(getTemperatureImage(temperature));
        tempImageView.setFitWidth(20);
        tempImageView.setFitHeight(60);
        tempImageView.setPreserveRatio(true);

        Label tempLabel = new Label(temperature + "°C");
        String tempColor = temperature <= 20 ? "#00ffff"
                : temperature <= 30 ? "#00ff00"
                : temperature <= 40 ? "#ffff00"
                : "#ff0000";
        tempLabel.setStyle("-fx-text-fill: " + tempColor + ";");

        tempBox.getChildren().addAll(tempImageView, tempLabel);

        bedsAndTempLine.getChildren().add(tempBox);

        container.getChildren().add(bedsAndTempLine);

        // Doctors en bas
        HBox doctorImages = createDoctorImages(doctors);
        container.getChildren().add(doctorImages);

        // cas ou le service ferme
        if (crypt.isHasServiceToClose()) {
            Platform.runLater(() -> cryptViewController.showCloseDoor(container));
        }

        container.setCursor(Cursor.HAND);
        container.setOnMouseClicked(event -> openDetailPanel(crypt, container));

        return container;
    }


    /**
     * Creates a FlowPane to display the beds in the Crypt.
     */
    private FlowPane createBedsView() {
        FlowPane pane = new FlowPane(5, 5);

        int bedWidth = 30;
        int margin = 5;
        int padding = 10;

        int totalWidth = bedImagePaths.size() * (bedWidth + margin) + padding;

        pane.setPrefWidth(totalWidth);
        pane.setMaxWidth(totalWidth);
        pane.setMinWidth(totalWidth);
        pane.setPrefWrapLength(totalWidth);

        pane.setStyle("-fx-background-color: #333333; -fx-padding: 5; -fx-border-color: #555555;");

        while (bedImagePaths.size() < crypt.getMAX_CREATURE()) {
            bedImagePaths.add(getRandomCryptBedImage());
        }

        List<Creature> creatures = new ArrayList<>(crypt.getCreatures());
        Map<Creature, Integer> waiting = crypt.getCreatureWaitNbTour();

        String creatureImagePath = "/images/room/BedCreature.png";

        for (int i = 0; i < bedImagePaths.size(); i++) {
            ImageView bed = new ImageView(new Image(getClass().getResourceAsStream(bedImagePaths.get(i))));
            bed.setFitWidth(30);
            bed.setFitHeight(54);

            VBox bedWithProgress = new VBox(2);
            bedWithProgress.setAlignment(Pos.CENTER);

            StackPane bedStack = new StackPane(bed);

            if (i < creatures.size()) {
                Creature creature = creatures.get(i);

                Image img = new Image(getClass().getResourceAsStream(creatureImagePath));
                ImageView creatureImg = new ImageView(cropImage(removePngBackground(img)));
                creatureImg.setFitWidth(30);
                creatureImg.setFitHeight(30);
                bedStack.getChildren().add(creatureImg);

                if (waiting.containsKey(creature)) {
                    int tours = waiting.get(creature);
                    double progress = 1.0 - Math.min(tours / 3.0, 1.0);
                    if (progress <= 0.0) progress = 0.01;

                    ProgressBar bar = new ProgressBar(progress);
                    bar.setPrefWidth(30);
                    bar.setPrefHeight(12);
                    bar.setStyle("-fx-accent: #00ff77;");

                    bedWithProgress.getChildren().addAll(bedStack, bar);
                } else {
                    bedWithProgress.getChildren().add(bedStack);
                }

            } else {
                ProgressBar emptyBar = new ProgressBar(0.01);
                emptyBar.setPrefWidth(30);
                emptyBar.setPrefHeight(12);
                emptyBar.setStyle("-fx-accent: #222222;");

                bedWithProgress.getChildren().addAll(bedStack, emptyBar);
            }

            pane.getChildren().add(bedWithProgress);
        }

        return pane;
    }

    /**
     * Creates a horizontal box containing images of doctors and their names.
     *
     * @param doctors the list of doctors to display
     * @return an HBox containing the doctor images and names
     */
    private static HBox createDoctorImages(List<Doctor> doctors) {
        HBox hbox = new HBox(5);
        hbox.setPadding(new Insets(0));
        hbox.setAlignment(Pos.CENTER_LEFT);

        Image doctorImage = new Image(MedicalServiceCellView.class.getResourceAsStream("/images/room/DoctorInRoom.png"));

        for (Doctor doctor : doctors) {
            VBox vbox = new VBox(5);
            vbox.setAlignment(Pos.CENTER);

            ImageView imageView = new ImageView(doctorImage);
            imageView.setFitWidth(30);
            imageView.setFitHeight(60);
            imageView.setPreserveRatio(true);

            Label nameLabel = new Label(doctor.getFullName());
            nameLabel.setStyle("-fx-font-size: 10px;");
            nameLabel.setStyle("-fx-text-fill: white;");

            vbox.getChildren().addAll(imageView, nameLabel);
            hbox.getChildren().add(vbox);
        }

        return hbox;
    }

    /**
     * Returns an image representing the temperature based on the given temperature value.
     *
     * @param temperature the temperature value
     * @return an Image representing the temperature
     */
    private Image getTemperatureImage(int temperature) {
        String temperatureImagePath;

        if (temperature <= 0) {
            temperatureImagePath = "/images/crypt/CryptTemp1.png";
        } else if (temperature <= 21) {
            temperatureImagePath = "/images/crypt/CryptTemp2.png";
        } else if (temperature <= 25) {
            temperatureImagePath = "/images/crypt/CryptTemp3.png";
        } else if (temperature <= 30) {
            temperatureImagePath = "/images/crypt/CryptTemp4.png";
        } else if (temperature <= 35) {
            temperatureImagePath = "/images/crypt/CryptTemp5.png";
        } else if (temperature <= 37) {
            temperatureImagePath = "/images/crypt/CryptTemp6.png";
        } else if (temperature <= 40) {
            temperatureImagePath = "/images/crypt/CryptTemp7.png";
        } else if (temperature <= 45) {
            temperatureImagePath = "/images/crypt/CryptTemp8.png";
        } else {
            temperatureImagePath = "/images/crypt/CryptTemp9.png";
        }

        return new Image(getClass().getResourceAsStream(temperatureImagePath));
    }

    /**
     * Opens a detail panel for the Crypt, allowing users to view more information.
     *
     * @param crypt     the Crypt to display details for
     * @param container the VBox container where the detail panel will be opened
     */
    private void openDetailPanel(Crypt crypt, VBox container) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/medicalServiceDetailsListView.fxml"));
            Parent root = loader.load();

            // On suppose que le contrôleur accepte un setQuarantine(Quarantine q)
            MedicalServiceDetailsController controller = loader.getController();
            controller.setCrypt(crypt);

            Stage detailStage = new Stage();
            detailStage.setTitle("Détails de la crypte");
            detailStage.initModality(Modality.APPLICATION_MODAL);
            detailStage.initOwner(container.getScene().getWindow());

            double width = container.getScene().getWindow().getWidth() * 0.5;
            double height = container.getScene().getWindow().getHeight() * 0.5;

            detailStage.setWidth(width);
            detailStage.setHeight(height);
            detailStage.setScene(new Scene(root));
            detailStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
