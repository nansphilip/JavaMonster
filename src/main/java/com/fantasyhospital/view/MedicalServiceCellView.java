package com.fantasyhospital.view;

import com.fantasyhospital.config.StageManager;
import com.fantasyhospital.controller.MedicalServiceDetailsController;
import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
import java.util.List;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

public class MedicalServiceCellView {

    public static Pane createView(MedicalService service, Hospital hospital, StageManager stageManager) {

		Pane pane = new Pane();
		pane.setStyle("""
                -fx-background-color: #add8e6;
                -fx-border-color: #000000;
                -fx-border-width: 1;
            """);
		pane.setPrefSize(210.0, 290.0);
		pane.setCursor(Cursor.HAND);

        Label name = new Label("Service Medical : " + service.getName());
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

		Label type = new Label("Type : " + service.getRoomType());
		type.setLayoutX(10);
		type.setLayoutY(40);
		type.setMaxWidth(160);

		Label occupied = new Label("Docteurs : " + service.getDoctors());
		occupied.setLayoutX(10);
		occupied.setLayoutY(60);
		occupied.setMaxWidth(160);

		Label budget = new Label("Budget : " + BudgetType.fromRatio(service.getBudget()) + " (" + service.getBudget() + ") ");
		budget.setLayoutX(10);
		budget.setLayoutY(80);
		budget.setMaxWidth(160);

        BudgetType budgetEnum = BudgetType.fromRatio(service.getBudget()) != null ? BudgetType.fromRatio(service.getBudget()) : BudgetType.INEXISTANT;

        FlowPane bedsFlow = createBedsView(service.getMAX_CREATURE(), budgetEnum, service);
        bedsFlow.setLayoutX(10);
        bedsFlow.setLayoutY(110);

        pane.setOnMouseClicked(event -> openDetailPanel(service, hospital, stageManager));

        pane.getChildren().addAll(topRow, type, occupied, budget, bedsFlow);
        return pane;
    }

    public static FlowPane createBedsView(int numberOfBeds, BudgetType budgetType, MedicalService service) {
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(5);
        flowPane.setVgap(5);
        flowPane.setPrefWrapLength(180);

        if (service.getBedImagePaths() == null || service.getBedImagePaths().isEmpty()) {
            List<String> generated = new ArrayList<>();
            for (int i = 0; i < numberOfBeds; i++) {
                String bedImagePath = switch (budgetType) {
                    case INSUFFISANT -> getRandomImage(new String[]{
                            "/images/room/Bed.png",
                    });
                    case MEDIOCRE -> getRandomImage(new String[]{
                            "/images/room/Bed.png",
                            "/images/room/BedBlood.png",
                            "/images/room/BedBones.png"
                    });
                    case FAIBLE -> getRandomImage(new String[]{
                            "/images/room/BedBlood.png",
                            "/images/room/BedBones.png"
                    });
                    case CORRECT -> getRandomImage(new String[]{
                            "/images/room/Bed.png",
                            "/images/room/BedBlood.png"
                    });
                    case BON -> getRandomImage(new String[]{
                            "/images/room/Bed.png"
                    });
                    case INEXISTANT -> "/images/room/Bed.png";
                    case EXCELLENT -> getRandomImage(new String[]{
                            "/images/room/Bed.png",
                    });
                };
                generated.add(bedImagePath);
            }
            service.setBedImagePaths(generated);
        }


        List<String> creatureImagePaths = List.of(
                "/images/room/BedCreature.png"
        );

        int creaturesCount = service.getCreatures() != null ? service.getCreatures().size() : 0;

        for (int i = 0; i < service.getBedImagePaths().size(); i++) {
            String bedImagePath = service.getBedImagePaths().get(i);

            ImageView bedView = new ImageView(new Image(MedicalServiceCellView.class.getResourceAsStream(bedImagePath)));
            bedView.setFitWidth(30);
            bedView.setFitHeight(54);

            StackPane bedStack = new StackPane();
            bedStack.getChildren().add(bedView);

            if (i < creaturesCount) {
                String creatureImage = creatureImagePaths.get(i % creatureImagePaths.size()); // pour varier
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

	private static String getRandomImage(String[] options) {
		int randomIndex = (int) (Math.random() * options.length);
		return options[randomIndex];
	}

    private static void openDetailPanel(MedicalService service, Hospital hospital, StageManager stageManager) {
        VBox detailBox = new VBox(10);
        detailBox.setStyle("-fx-background-color: #ffffff;");

        Label title = new Label("🔍 Détails du service : " + service.getName());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        try {
            FXMLLoader loader = new FXMLLoader(MedicalServiceCellView.class.getResource("/fxml/medicalServiceListCreatureView.fxml"));
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
}
