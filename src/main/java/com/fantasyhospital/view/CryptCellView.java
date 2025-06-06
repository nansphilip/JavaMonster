package com.fantasyhospital.view;

import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

public class CryptCellView {

    private final Crypt crypt;
    private final List<String> bedImagePaths = new ArrayList<>();
    private final List<Doctor> doctors;
    private final Random random = new Random();

    public CryptCellView(Crypt crypt,List<Doctor> doctors) {
        this.crypt = crypt;
        this.doctors = doctors;
        generateBedImagePaths(crypt.getMAX_CREATURE());
    }

    private void generateBedImagePaths(int max) {
        bedImagePaths.clear();
        for (int i = 0; i < max; i++) {
            bedImagePaths.add(getRandomCryptBedImage());
        }
    }

    private String getRandomCryptBedImage() {
        String[] options = {
                "/images/room/BedBones.png",
                "/images/room/BedBlood.png",
                "/images/room/Bed.png"
        };
        return options[random.nextInt(options.length)];
    }

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

        return container;
    }


    /**
     * Crée une vue graphique avec les lits et les créatures dessus
     * @return un FlowPane contenant les images de lits avec les créatures
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


//    private VBox createCreatureProgress() {
//        VBox box = new VBox(5);
//        Map<Creature, Integer> waiting = crypt.getCreatureWaitNbTour();
//
//        if (waiting == null || waiting.isEmpty()) return box;
//
//        for (Map.Entry<Creature, Integer> entry : waiting.entrySet()) {
//            int tours = entry.getValue();
//
//            HBox hbox = new HBox(10);
//            hbox.setAlignment(Pos.CENTER_LEFT);
//
//            ProgressBar bar = new ProgressBar(tours / 3.0);
//            bar.setPrefWidth(30);
//            bar.setMinWidth(30);
//            bar.setPrefHeight(12);
//            bar.setMinHeight(12);
//            bar.setStyle("-fx-accent: #00ff77;");
//
//            hbox.getChildren().add(bar);
//            box.getChildren().add(hbox);
//        }
//
//        return box;
//    }

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

            vbox.getChildren().addAll(imageView, nameLabel);
            hbox.getChildren().add(vbox);
        }

        return hbox;
    }

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
}
