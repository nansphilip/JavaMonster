package com.fantasyhospital;

import javafx.scene.control.Button;

public class CustomButton extends Button {
    public CustomButton(String text) {
        super(text);
        this.setStyle(
            "-fx-background-color: #4CAF50; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-padding: 10 20 10 20; " +
            "-fx-background-radius: 8px;"
        );
    }
} 