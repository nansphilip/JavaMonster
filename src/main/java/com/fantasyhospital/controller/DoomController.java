package com.fantasyhospital.controller;

import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.view.EventCellView;
import com.fantasyhospital.view.GifCellView;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Component;

/**
 * Controller for managing the Doom Box, which displays events like doctor harakiri and service closures.
 * It also handles showing GIFs in the Doom Box.
 */
@Component
public class DoomController {

    /**
     * The StackPane that serves as the Doom Box container.
     * It is used to display events and GIFs.
     */
    @FXML
    private StackPane doomContainer;

    /**
     * Initializes the DoomController by clearing the Doom Box and showing the initial GIF.
     * This method is called automatically by JavaFX after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        clearDoomBox();
        showGifInDoomBox();
    }

    /**
     * Returns the Doom Box container.
     *
     * @return the StackPane used as the Doom Box
     */
    public StackPane getDoomBox() {
        return doomContainer;
    }

    /**
     * Clears the Doom Box by removing all children from the doomContainer.
     * This is typically called before displaying new events or GIFs.
     */
    public void clearDoomBox() {
        doomContainer.getChildren().clear();
    }

    /**
     * Displays a harakiri event for a doctor in the Doom Box.
     * It shows the event and then starts a GIF loop after a delay.
     *
     * @param doctor the Doctor instance involved in the harakiri event
     */
    public void showHarakiriForDoctor(Doctor doctor) {
        EventCellView.showHarakiri(doctor, this);

        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
        delay.setOnFinished(event -> showGifInDoomBox());
        delay.play();
    }

    /**
     * Displays a service closure event in the Doom Box.
     * It shows the event and then starts a GIF loop after a delay.
     *
     * @param medicalService the MedicalService instance that is closing
     */
    public void showServiceClose(MedicalService medicalService) {
        EventCellView.showServiceClose(medicalService, this);

        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
        delay.setOnFinished(event -> showGifInDoomBox());
        delay.play();
    }

    /**
     * Starts a GIF loop in the Doom Box.
     * This method is called to display a looping GIF animation in the Doom Box.
     */
    public void showGifInDoomBox() {
        GifCellView.startLoop(this);
    }
}
