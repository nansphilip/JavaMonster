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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

		VBox bedsContainer = new VBox(5);
		bedsContainer.setLayoutX(10);
		bedsContainer.setLayoutY(110);
		bedsContainer.setMaxWidth(160);

		// Création des lits avec une meilleure organisation (2 par ligne)
		HBox row1 = new HBox(10);
		HBox row2 = new HBox(10);

		int numberOfBeds = service.getMAX_CREATURE();
		for (int i = 0; i < numberOfBeds; i++) {
			String bedImagePath = getBedImagePath(budgetEnum);
			ImageView bedView = new ImageView(new Image(MedicalServiceCellView.class.getResourceAsStream(bedImagePath)));
			bedView.setFitWidth(30);
			bedView.setFitHeight(50);

			if (i < numberOfBeds/2) {
				row1.getChildren().add(bedView);
			} else {
				row2.getChildren().add(bedView);
			}
		}

		bedsContainer.getChildren().addAll(row1, row2);

		int creatureCount = service.getCreatures() != null ? service.getCreatures().size() : 0;
		Label creatureCountLabel = new Label("Créatures : " + creatureCount);
		creatureCountLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #555;");
		creatureCountLabel.setLayoutX(10);
		creatureCountLabel.setLayoutY(230);

        pane.setOnMouseClicked(event -> openDetailPanel(service, hospital));

		pane.getChildren().addAll(name, type, occupied, budget, bedsContainer, creatureCountLabel);
		return pane;
	}

	private static String getBedImagePath(BudgetType budgetType) {
		return switch (budgetType) {
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
			case BON -> "/images/room/Bed.png";
			case INEXISTANT -> "/images/room/Bed.png";
		};
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
