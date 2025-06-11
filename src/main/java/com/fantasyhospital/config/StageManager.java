package com.fantasyhospital.config;

import java.io.IOException;

import lombok.Getter;
import org.springframework.stereotype.Component;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * Manages the main application stage and scene switching.
 */
@Component
public class StageManager {

	/**
	 * Manages the main application stage and scene switching.
	 */
	@Getter
    private final Stage primaryStage;
	private final FxmlLoader fxmlLoader;

	/**
	 * Constructs the StageManager with the given FxmlLoader and primary Stage.
	 *
	 * @param fxmlLoader the FxmlLoader bean
	 * @param primaryStage the main application stage
	 */
	public StageManager(FxmlLoader fxmlLoader,
		Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.fxmlLoader = fxmlLoader;
	}

	/**
	 * Switches the stage to maximized mode.
	 */
	public void switchToMaximizedMode() {
		primaryStage.setMaximized(true);
	}

	/**
	 * Switches the scene to the specified FXML view.
	 *
	 * @param view the FxmlView to display
	 */
	public void switchScene(final FxmlView view) {
		primaryStage.setMinWidth(1200);
		primaryStage.setMinHeight(800);
		primaryStage.setTitle("Fantasy Hospital - Bienvenue");

		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

		Parent rootNode = loadRootNode(view.getFxmlPath());

		Scene scene = new Scene(rootNode);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Loads the root node from the given FXML path.
	 *
	 * @param fxmlPath the FXML file path
	 * @return the root node
	 * @throws RuntimeException if loading fails
	 */
	private Parent loadRootNode(String fxmlPath) {
		Parent rootNode;
		try {
			rootNode = fxmlLoader.load(fxmlPath);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return rootNode;
	}
}
