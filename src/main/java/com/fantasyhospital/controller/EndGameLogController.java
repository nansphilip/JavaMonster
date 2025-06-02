package com.fantasyhospital.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class EndGameLogController {

    @FXML
    private TextArea summaryTextArea;

    @Setter
    private Stage dialogStage;

    public void setSummary(String summary) {
        summaryTextArea.setText(summary);
    }

    @FXML
    private void handleClose() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }
}
