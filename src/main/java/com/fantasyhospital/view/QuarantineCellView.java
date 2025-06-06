package com.fantasyhospital.view;

import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

public class QuarantineCellView {

    private final Quarantine quarantine;
    private final List<String> bedImagePaths = new ArrayList<>();
    private final List<Doctor> doctors;
    private final Random random = new Random();

    public QuarantineCellView(Quarantine quarantine, List<Doctor> doctors) {
        this.quarantine = quarantine;
        this.doctors = doctors;
        generateBedImagePaths(quarantine.getMAX_CREATURE());
    }

    private void generateBedImagePaths(int max) {
        bedImagePaths.clear();
        for (int i = 0; i < max; i++) {
            bedImagePaths.add(getRandomQuarantineBedImage());
        }
    }

    private String getRandomQuarantineBedImage() {
        String[] options = {
                "/images/room/BedBones.png",
                "/images/room/Bed.png"
        };
        return options[random.nextInt(options.length)];
    }

    public VBox render() {
        VBox container = new VBox(8);
        container.setPadding(new Insets(10));
        container.setAlignment(Pos.TOP_CENTER);

        // Titre
        Label titleLabel = new Label("QUARANTAINE");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #cccccc;");
        container.getChildren().add(titleLabel);

        // Budget
        Label budget = new Label("Budget : " + BudgetType.fromRatio(quarantine.getBudget()) + " (" + quarantine.getBudget() + ") ");
        budget.setLayoutX(10);
        budget.setLayoutY(80);
        budget.setMaxWidth(160);
        budget.setStyle("-fx-text-fill: #cccccc;");
        container.getChildren().add(budget);

        Label type = new Label("Type : " + quarantine.getRoomType());
        type.setLayoutX(10);
        type.setLayoutY(40);
        type.setMaxWidth(160);
        type.setStyle("-fx-text-fill: #cccccc;");
        container.getChildren().add(type);

//        // Créatures
//        Label creaturesLabel = new Label("Créatures en isolement: " + quarantine.getCreatures().size());
//        creaturesLabel.setStyle("-fx-text-fill: #cccccc;");
//        container.getChildren().add(creaturesLabel);

        // Vue des lits
        FlowPane beds = createBedsView();
        container.getChildren().add(beds);

        // Doctors
        HBox doctorImages = createDoctorImages(doctors);
        container.getChildren().add(doctorImages);

        return container;
    }

    /**
     * Crée une vue graphique avec les lits et les créatures dessus
     *
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

        while (bedImagePaths.size() < quarantine.getMAX_CREATURE()) {
            bedImagePaths.add(getRandomQuarantineBedImage());
        }

        List<Creature> creatures = new ArrayList<>(quarantine.getCreatures());
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
