package com.fantasyhospital.view;

import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.rooms.medicalservice.MedicalService;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;

import static com.fantasyhospital.enums.BudgetType.*;

public class MedicalServiceCellView {

	public static VBox createView(MedicalService service) {
		VBox box = new VBox(5);
		box.setStyle("""
            -fx-border-color: gray;
            -fx-padding: 10;
            -fx-background-color: #f4f4f4;
            -fx-background-radius: 5;
            -fx-border-radius: 5;
        """);
		box.setPrefWidth(200);

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

		Label creaturesLabel = new Label("Créatures :");
		VBox creatureList = new VBox();
		if (service.getCreatures().isEmpty()) {
			creatureList.getChildren().add(new Label("  Aucune."));
		} else {
			for (Creature creature : service.getCreatures()) {
				Label creatureLabel = new Label("  - " + creature.getFullName());
				creatureList.getChildren().add(creatureLabel);
			}
		}

		box.getChildren().addAll(name, type, occupied, budget, bedsHBox, creaturesLabel, creatureList);

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
}
