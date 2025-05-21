package com.fantasyhospital.controller;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fantasyhospital.config.FxmlView;
import com.fantasyhospital.config.StageManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

@Component
public class LogoViewController {

    private final StageManager stageManager;

    @Lazy
	public LogoViewController(StageManager stageManager) {this.stageManager = stageManager;}

	@FXML
    private void startGame(ActionEvent event) {
        stageManager.switchScene(FxmlView.MAIN);
    }
}
