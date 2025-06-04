package com.fantasyhospital.controller;

import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.view.HarakiriCellView;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Component;

@Component
public class DoomController {

    @FXML
    private StackPane harakiriContainer;

    @FXML
    private void initialize() {
        clearHarakiri();
    }


    public StackPane getHarakiriContainer() {
        return harakiriContainer;
    }

    public void clearHarakiri() {
        harakiriContainer.getChildren().clear();
    }

    public void showHarakiriForDoctor(Doctor doctor) {
        HarakiriCellView.show(doctor, this);
    }
}
