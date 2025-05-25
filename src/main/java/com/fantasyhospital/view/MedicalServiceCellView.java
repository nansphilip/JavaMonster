package com.fantasyhospital.view;

import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.rooms.medicalservice.MedicalService;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.Cursor;

import java.awt.*;

public class MedicalServiceCellView {

	public static VBox createView(MedicalService service, Hospital hospital) {
		VBox box = new VBox(5);
		box.setStyle("""
            -fx-border-color: gray;
            -fx-padding: 10;
            -fx-background-color: #f4f4f4;
            -fx-background-radius: 5;
            -fx-border-radius: 5;
        """);
		box.setPrefWidth(200);
		box.setCursor(Cursor.HAND);

		Label name = new Label("🩺 " + service.getName());
		name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
		Label type = new Label("Type du service : " + service.getRoomType());
		Label occupied = new Label("Docteurs du service : " + service.getDoctors());
		Label budget = new Label("Budget : " + service.getBudgetType());


		// a refaire plus propre en passant directement un budgetType et pas un valueOf string
		BudgetType budgetEnum = service.getBudgetType();
		if (budgetEnum == null) {
			budgetEnum = BudgetType.INEXISTANT;
		}

		HBox bedsHBox = createBedsView(service.getMAX_CREATURE(), budgetEnum);

		int creatureCount = service.getCreatures() != null ? service.getCreatures().size() : 0;
		Label creatureCountLabel = new Label("Nombre de créatures : " + creatureCount);
		creatureCountLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #555;");

		box.setOnMouseClicked(event -> openDetailPanel(service, hospital));

		box.getChildren().addAll(name, type, occupied, budget, bedsHBox, creatureCountLabel);

		return box;
	}

	private static HBox createBedsView(int numberOfBeds, BudgetType budgetType) {

		HBox bedsBox = new HBox(5);
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
				case BON -> "/images/room/Bed.png";
				case INEXISTANT -> "/images/room/Bed.png";
			};

			ImageView bedView = new ImageView(new Image(MedicalServiceCellView.class.getResourceAsStream(bedImagePath)));
			bedView.setFitWidth(30);
			bedView.setFitHeight(54);
			bedsBox.getChildren().add(bedView);
		}

		return bedsBox;
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
