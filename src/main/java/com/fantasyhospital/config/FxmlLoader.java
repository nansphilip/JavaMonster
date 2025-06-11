package com.fantasyhospital.config;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Loads FXML files and injects Spring beans as controllers.
 */
@Component
public class FxmlLoader {

    private final ApplicationContext context;

    /**
     * Constructs the FxmlLoader with the given Spring context.
     *
     * @param context the Spring application context
     */
    public FxmlLoader(ApplicationContext context) {
        this.context = context;
    }


    /**
     * Loads an FXML file and returns its root node.
     *
     * @param fxmlPath the path to the FXML file
     * @return the root node of the loaded FXML
     * @throws IOException if loading fails
     */
    public Parent load(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        loader.setLocation(getClass().getResource(fxmlPath));
        return loader.load();
    }
}
