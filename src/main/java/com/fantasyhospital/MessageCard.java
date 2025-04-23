package com.fantasyhospital;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MessageCard extends VBox {
    private final Label label;

    public MessageCard() {
        label = new Label("");
        label.setStyle("-fx-font-size: 18px; -fx-text-fill: #333;");
        this.getChildren().add(label);
        this.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #cccccc; " +
            "-fx-border-radius: 10px; " +
            "-fx-background-radius: 10px; " +
            "-fx-padding: 20px; " +
            "-fx-effect: dropshadow(gaussian, #888888, 8, 0.5, 0, 2);"
        );
        this.setSpacing(10);
        this.setMinWidth(200);
        this.setMaxWidth(300);
    }

    public void setMessage(String message) {
        label.setText(message);
    }

    public String getMessage() {
        return label.getText();
    }
} 