package com.fantasyhospital.view;

import com.fantasyhospital.config.StageManager;
import com.fantasyhospital.controller.DoomController;
import com.fantasyhospital.controller.GridMedicalServiceController;
import com.fantasyhospital.controller.MedicalServiceDetailsController;
import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.enums.RaceType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

/**
 * MedicalServiceCellView is a utility class that provides a method to create a view for a medical service in the hospital.
 * It displays the service's name, type, budget, beds, and doctors, and allows interaction to view more details.
 */
public class MedicalServiceCellView {

    /**
     * Creates a view for the given medical service.
     *
     * @param service                      the medical service to display
     * @param hospital                     the hospital where the service is located
     * @param stageManager                 the stage manager for managing stages
     * @param gridMedicalServiceController the controller for the grid of medical services
     * @param doomController               the controller for handling doom events
     * @return a Pane containing the view of the medical service
     */
    public static Pane createView(MedicalService service, Hospital hospital, StageManager stageManager, GridMedicalServiceController gridMedicalServiceController, DoomController doomController) {
        Pane pane = new Pane();
        pane.setStyle("""
                    -fx-background-color: #add8e6;
                    -fx-border-color: #000000;
                    -fx-border-width: 1;
                """);
        pane.setPrefSize(210.0, 290.0);
        pane.setCursor(Cursor.HAND);

        Label name = new Label(service.getName());
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        name.setMaxWidth(160);
        name.setPrefWidth(160);

        int creatureCount = service.getCreatures() != null ? service.getCreatures().size() : 0;
        StackPane creatureCounter = CounterCellView.create(creatureCount, 15, Color.DARKRED);

        HBox topRow = new HBox();
        topRow.setSpacing(10);
        topRow.setPadding(new Insets(10, 10, 0, 10));
        topRow.setPrefWidth(200);
        topRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        topRow.getChildren().addAll(name, creatureCounter);

        String raceString = service.getRoomType();
        if (raceString == null) {
            raceString = "Race inconnue";
        } else {
            raceString = RaceType.valueOf(raceString.toUpperCase()).getRace();
        }
        Label type = new Label("Type : " + raceString);
        type.setLayoutX(10);
        type.setLayoutY(60);
        type.setMaxWidth(160);

//        Label occupied = new Label("Docteurs : " + service.getDoctors().stream().map(Doctor::getFullName).collect(Collectors.joining(", ")));
//        occupied.setLayoutX(10);
//        occupied.setLayoutY(60);
//        occupied.setMaxWidth(160);

        Label budget = new Label("Budget : " + BudgetType.fromRatio(service.getBudget()) + " (" + service.getBudget() + ") ");
        budget.setLayoutX(10);
        budget.setLayoutY(80);
        budget.setMaxWidth(160);

        BudgetType budgetEnum = BudgetType.fromRatio(service.getBudget()) != null ? BudgetType.fromRatio(service.getBudget()) : BudgetType.INEXISTANT;

        FlowPane bedsFlow = createBedsView(service.getMAX_CREATURE(), budgetEnum, service);
        bedsFlow.setLayoutX(10);
        bedsFlow.setLayoutY(110);

        pane.setOnMouseClicked(event -> openDetailPanel(service, hospital, stageManager));

        pane.getChildren().addAll(topRow, type, budget, bedsFlow);

        // S'assurer que les éléments internes ne débordent pas
        pane.setClip(new javafx.scene.shape.Rectangle(
                pane.getPrefWidth(),
                pane.getPrefHeight()
        ));

        // Mise à jour du clip lorsque la taille change
        pane.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            pane.setClip(new javafx.scene.shape.Rectangle(
                    newBounds.getWidth(),
                    newBounds.getHeight()
            ));
        });


        List<Doctor> doctors = service.getDoctors() != null ? service.getDoctors() : Collections.emptyList();
        HBox doctorImageView = createDoctorImages(doctors);
        doctorImageView.setLayoutX(10);
        doctorImageView.setLayoutY(165);
        pane.getChildren().add(doctorImageView);

        // cas ou le service ferme
        if (service.isHasServiceToClose()) {
            Platform.runLater(() -> doomController.showServiceClose(service));

            Platform.runLater(() -> gridMedicalServiceController.showCloseDoor(pane));
        }

        return pane;
    }

    /**
     * Creates a view for the beds in the medical service.
     *
     * @param numberOfBeds the number of beds in the service
     * @param budgetType   the budget type of the service
     * @param service      the medical service to display
     * @return a FlowPane containing the bed images
     */
    public static FlowPane createBedsView(int numberOfBeds, BudgetType budgetType, MedicalService service) {
        generateBedImagePathsIfNeeded(numberOfBeds, budgetType, service);
        return buildBedsPane(service);
    }

    /**
     * Generates bed image paths based on the budget type and number of beds.
     * This method populates the service with the appropriate bed image paths.
     *
     * @param numberOfBeds the number of beds to generate images for
     * @param budgetType   the budget type of the medical service
     * @param service      the medical service to update with bed image paths
     */
    private static void generateBedImagePathsIfNeeded(int numberOfBeds, BudgetType budgetType, MedicalService service) {
        List<String> generated = new ArrayList<>();
        for (int i = 0; i < numberOfBeds; i++) {
            String bedImagePath = switch (budgetType) {
                case INSUFFISANT, EXCELLENT -> getRandomImage(new String[]{"/images/room/Bed.png"});
                case MEDIOCRE ->
                        getRandomImage(new String[]{"/images/room/Bed.png", "/images/room/BedBlood.png", "/images/room/BedBones.png"});
                case FAIBLE -> getRandomImage(new String[]{"/images/room/BedBlood.png", "/images/room/BedBones.png"});
                case CORRECT -> getRandomImage(new String[]{"/images/room/Bed.png", "/images/room/BedBlood.png"});
                case BON -> getRandomImage(new String[]{"/images/room/Bed.png"});
                case INEXISTANT -> "/images/room/Bed.png";
            };
            generated.add(bedImagePath);
        }
        service.setBedImagePaths(generated);
    }

    /**
     * Builds a FlowPane containing the bed images for the medical service.
     *
     * @param service the medical service containing the bed image paths
     * @return a FlowPane with bed images and creature images if available
     */
    private static FlowPane buildBedsPane(MedicalService service) {
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(5);
        flowPane.setVgap(5);
        flowPane.setPrefWrapLength(180);

        List<String> creatureImagePaths = List.of("/images/room/BedCreature.png");
        int creaturesCount = service.getCreatures() != null ? service.getCreatures().size() : 0;

        for (int i = 0; i < service.getBedImagePaths().size(); i++) {
            String bedImagePath = service.getBedImagePaths().get(i);

            Image bedImage = new Image(MedicalServiceCellView.class.getResourceAsStream(bedImagePath));
            ImageView bedView = new ImageView(bedImage);
            bedView.setFitWidth(30);
            bedView.setFitHeight(54);

            StackPane bedStack = new StackPane(bedView);

            if (i < creaturesCount) {
                String creatureImage = creatureImagePaths.get(i % creatureImagePaths.size());
                Image image = new Image(MedicalServiceCellView.class.getResourceAsStream(creatureImage));

                Image transparentImage = removePngBackground(image);
                Image croppedImage = cropImage(transparentImage);

                ImageView creatureView = new ImageView(croppedImage);
                creatureView.setFitWidth(30);
                creatureView.setFitHeight(30);
                StackPane.setAlignment(creatureView, javafx.geometry.Pos.CENTER);
                bedStack.getChildren().add(creatureView);
            }

            flowPane.getChildren().add(bedStack);
        }

        return flowPane;
    }

    /**
     * Returns a random image path from the provided options.
     *
     * @param options an array of image paths to choose from
     * @return a randomly selected image path
     */
    private static String getRandomImage(String[] options) {
        int randomIndex = (int) (Math.random() * options.length);
        return options[randomIndex];
    }

    /**
     * Opens a detail panel for the given medical service.
     *
     * @param service      the medical service to display details for
     * @param hospital     the hospital where the service is located
     * @param stageManager the stage manager for managing stages
     */
    private static void openDetailPanel(MedicalService service, Hospital hospital, StageManager stageManager) {
        VBox detailBox = new VBox(10);
        detailBox.setStyle("-fx-background-color: #ffffff;");

        Label title = new Label("🔍 Détails du service : " + service.getName());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        try {
            FXMLLoader loader = new FXMLLoader(MedicalServiceCellView.class.getResource("/fxml/medicalServiceDetailsListView.fxml"));
            Parent root = loader.load();

            MedicalServiceDetailsController controller = loader.getController();
            controller.setHospital(hospital);
            controller.setService(service); // IMPORTANT pour mettre à jour la vue avec le service

            // Crée la scène et la fenêtre modale
            Scene scene = new Scene(root);

            Stage detailStage = new Stage();
            detailStage.setTitle("Détails du service : " + service.getName());
            detailStage.initModality(Modality.APPLICATION_MODAL);
            detailStage.initOwner(stageManager.getPrimaryStage());

            double width = stageManager.getPrimaryStage().getWidth() * 0.5;
            double height = stageManager.getPrimaryStage().getHeight() * 0.5;

            detailStage.setWidth(width);
            detailStage.setHeight(height);
            detailStage.setScene(scene);
            detailStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a horizontal box containing images of doctors associated with the medical service.
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

            vbox.getChildren().addAll(imageView, nameLabel);
            hbox.getChildren().add(vbox);
        }

        return hbox;
    }
}
