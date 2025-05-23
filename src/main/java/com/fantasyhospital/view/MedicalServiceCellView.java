package com.fantasyhospital.view;

import com.fantasyhospital.rooms.medicalservice.MedicalService;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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
		Label budget = new Label("Budget : " + service.getBudget());

		box.getChildren().addAll(name, type, occupied, budget);

		return box;
	}
}
