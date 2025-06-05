package com.fantasyhospital.view;

import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
                "/images/room/Bed.png"
        };
        return options[random.nextInt(options.length)];
    }

    public VBox render() {
        VBox container = new VBox(8);
        container.setPadding(new Insets(10));

        // Titre
        Label titleLabel = new Label("CRYPTE");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #cccccc;");
        container.getChildren().add(titleLabel);

        // Ventilation
        boolean airflowStatus = crypt.isAirflow();
        Label airflowLabel = new Label("Ventilation: " + (airflowStatus ? "Fonctionnelle ✓" : "En panne ✗"));
        airflowLabel.setStyle("-fx-text-fill: " + (airflowStatus ? "#00ff00" : "#ff0000") + ";");
        container.getChildren().add(airflowLabel);

        // Température
        int temperature = crypt.getTemperature();
        Label tempLabel = new Label("Température: " + temperature + "°C");
        String tempColor = temperature <= 20 ? "#00ffff" : temperature <= 30 ? "#00ff00" : temperature <= 40 ? "#ffff00" : "#ff0000";
        tempLabel.setStyle("-fx-text-fill: " + tempColor + ";");
        container.getChildren().add(tempLabel);

        // Barre de progression pour la température
        ProgressBar tempProgressBar = new ProgressBar((double)temperature / 50);
        tempProgressBar.setPrefWidth(200);
        tempProgressBar.setStyle("-fx-accent: " + tempColor + ";");
        container.getChildren().add(tempProgressBar);

        // Créatures
        Label creaturesLabel = new Label("Créatures en repos: " + crypt.getCreatures().size());
        creaturesLabel.setStyle("-fx-text-fill: #cccccc;");
        container.getChildren().add(creaturesLabel);

        // Vue des lits
        FlowPane beds = createBedsView();
        container.getChildren().add(beds);

        // Progression
        VBox progress = createCreatureProgress();
        container.getChildren().add(progress);

        // Doctors
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
        pane.setPrefWrapLength(200);
        pane.setStyle("-fx-background-color: #333333; -fx-padding: 5; -fx-border-color: #555555;");

        while (bedImagePaths.size() < crypt.getMAX_CREATURE()) {
            bedImagePaths.add(getRandomCryptBedImage());
        }

        List<Creature> creatures = new ArrayList<>(crypt.getCreatures());
        String creatureImagePath = "/images/room/BedCreature.png";

        for (int i = 0; i < bedImagePaths.size(); i++) {
            ImageView bed = new ImageView(new Image(getClass().getResourceAsStream(bedImagePaths.get(i))));
            bed.setFitWidth(30);
            bed.setFitHeight(54);

            StackPane bedStack = new StackPane(bed);

            if (i < creatures.size()) {
                Image img = new Image(getClass().getResourceAsStream(creatureImagePath));
                ImageView creatureImg = new ImageView(cropImage(removePngBackground(img)));
                creatureImg.setFitWidth(30);
                creatureImg.setFitHeight(30);
                bedStack.getChildren().add(creatureImg);
            }

            pane.getChildren().add(bedStack);
        }

        return pane;
    }

    private VBox createCreatureProgress() {
        VBox box = new VBox(5);
        Map<Creature, Integer> waiting = crypt.getCreatureWaitNbTour();

        if (waiting == null || waiting.isEmpty()) return box;

        for (Map.Entry<Creature, Integer> entry : waiting.entrySet()) {
            Creature creature = entry.getKey();
            int tours = entry.getValue();

            HBox hbox = new HBox(10);
            hbox.setAlignment(Pos.CENTER_LEFT);

            Label name = new Label(creature.getFullName());
            name.setStyle("-fx-text-fill: #cccccc;");
            name.setPrefWidth(120);

            ProgressBar bar = new ProgressBar(tours / 3.0);
            bar.setPrefWidth(80);
            bar.setStyle("-fx-accent: #00ff77;");

            hbox.getChildren().addAll(name, bar);
            box.getChildren().add(hbox);
        }

        return box;
    }
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
}
