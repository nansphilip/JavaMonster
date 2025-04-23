package com.fantasyhospital;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.fantasyhospital.MessageCard;
import com.fantasyhospital.CustomButton;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        MessageCard card = new MessageCard();
        CustomButton btn = new CustomButton("Clique-moi !");
        
        btn.setOnAction(e -> {
            if (card.getMessage().isEmpty()) {
                card.setMessage("hello world");
            } else {
                card.setMessage("");
            }
        });

        VBox root = new VBox(20, btn, card);
        root.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 30px;");

        Scene scene = new Scene(root, 350, 250);
        primaryStage.setTitle("Fenêtre JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
