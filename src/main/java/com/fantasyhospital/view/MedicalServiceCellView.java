package com.fantasyhospital.view;

import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.rooms.medicalservice.MedicalService;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
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

	public static Pane createView(MedicalService service, Hospital hospital) {
		Pane pane = new Pane();
		pane.setStyle("""
                -fx-background-color: #add8e6;
                -fx-border-color: #000000;
                -fx-border-width: 1;
            """);
		pane.setPrefSize(210.0, 290.0);
		pane.setCursor(Cursor.HAND);

		Label name = new Label("🩺 " + service.getName());
		name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
		name.setLayoutX(10);
		name.setLayoutY(10);
		name.setMaxWidth(160);

		Label type = new Label("Type : " + service.getRoomType());
		type.setLayoutX(10);
		type.setLayoutY(40);
		type.setMaxWidth(160);

		Label occupied = new Label("Docteurs : " + service.getDoctors());
		occupied.setLayoutX(10);
		occupied.setLayoutY(60);
		occupied.setMaxWidth(160);

		Label budget = new Label("Budget : " + service.getBudgetType());
		budget.setLayoutX(10);
		budget.setLayoutY(80);
		budget.setMaxWidth(160);

        BudgetType budgetEnum = service.getBudgetType() != null ? service.getBudgetType() : BudgetType.INEXISTANT;

        FlowPane bedsFlow = createBedsView(service.getMAX_CREATURE(), budgetEnum, service);
        bedsFlow.setLayoutX(10);
        bedsFlow.setLayoutY(110);

        pane.setOnMouseClicked(event -> openDetailPanel(service, hospital));

        pane.getChildren().addAll(topRow, type, occupied, budget, bedsFlow);
        return pane;
    }

    private static FlowPane createBedsView(int numberOfBeds, BudgetType budgetType, MedicalService service) {
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(5);
        flowPane.setVgap(5);
        flowPane.setPrefWrapLength(150);

        if (service.getBedImagePaths() == null || service.getBedImagePaths().isEmpty()) {
            List<String> generated = new ArrayList<>();
            for (int i = 0; i < numberOfBeds; i++) {
                String bedImagePath = switch (budgetType) {
                    case MEDIOCRE -> getRandomImage(new String[]{
                            "/images/room/Bed.png",
                            "/images/room/Bedblood.png",
                            "/images/room/Bedbones.png"
                    });
                    case FAIBLE -> getRandomImage(new String[]{
                            "/images/room/Bedblood.png",
                            "/images/room/Bedbones.png"
                    });
                    case CORRECT -> getRandomImage(new String[]{
                            "/images/room/Bed.png",
                            "/images/room/Bedblood.png"
                    });
                    case BON -> getRandomImage(new String[]{
                            "/images/room/Bed.png"
                    });
                    case INEXISTANT -> "/images/room/Bed.png";
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

	private static void openDetailPanel(MedicalService service, Hospital hospital) {
		Stage detailStage = new Stage();
		detailStage.setTitle("Détails du service : " + service.getName());

		VBox detailBox = new VBox(10);
		detailBox.setPadding(new Insets(20));
		detailBox.setStyle("-fx-background-color: #ffffff;");

		Label title = new Label("🔍 Détails du service : " + service.getName());
		title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		Label info = new Label("Type : " + service.getRoomType() +
				"\nBudget : " + service.getBudgetType() +
				"\nCréatures : " + service.getCreatures().size());

		// Tu pourras plus tard ajouter ici : liste de créatures, actions, etc.

		detailBox.getChildren().addAll(title, info);

		Scene scene = new Scene(detailBox, 300, 200);
		detailStage.setScene(scene);
		detailStage.initModality(Modality.APPLICATION_MODAL);
		detailStage.show();
	}
}
