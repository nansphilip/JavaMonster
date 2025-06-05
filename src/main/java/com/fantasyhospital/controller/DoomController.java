package com.fantasyhospital.controller;

import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.view.GifCellView;
import com.fantasyhospital.view.HarakiriCellView;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Component;

@Component
public class DoomController {

    @FXML
    private StackPane doomContainer;

    @FXML
    private void initialize() {
        clearDoomBox();
        showGifInDoomBox();
    }


    public StackPane getDoomBox() {
        return doomContainer;
    }

    public void clearDoomBox() {
        doomContainer.getChildren().clear();
    }

    public void showHarakiriForDoctor(Doctor doctor) {
        HarakiriCellView.show(doctor, this);

        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
        delay.setOnFinished(event -> showGifInDoomBox());
        delay.play();
    }

    public void showGifInDoomBox() {
        GifCellView.startLoop(this);
    }
}
