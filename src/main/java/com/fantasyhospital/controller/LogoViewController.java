package com.fantasyhospital.controller;

import com.fantasyhospital.config.FxmlView;
import com.fantasyhospital.config.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

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
