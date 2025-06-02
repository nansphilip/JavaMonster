package com.fantasyhospital.config;

import java.io.IOException;

import lombok.Getter;
import org.springframework.stereotype.Component;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

@Component
public class StageManager {

	@Getter
    private final Stage primaryStage;
	private final FxmlLoader fxmlLoader;

	public StageManager(FxmlLoader fxmlLoader,
		Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.fxmlLoader = fxmlLoader;
	}

	public void switchScene(final FxmlView view) {
		primaryStage.setMinWidth(1200);
		primaryStage.setMinHeight(800);
		primaryStage.setTitle("Fantasy Hospital - Bienvenue");

		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

		Parent rootNode = loadRootNode(view.getFxmlPath());

		Scene scene = new Scene(rootNode);
//		String stylesheet = Objects.requireNonNull(getClass()
//				.getResource("/styles/styles.css"))
//			.toExternalForm();
//
//		scene.getStylesheets().add(stylesheet);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void switchToNextScene(final FxmlView view) {

		Parent rootNode = loadRootNode(view.getFxmlPath());
		primaryStage.getScene().setRoot(rootNode);

		primaryStage.show();
	}

	private Parent loadRootNode(String fxmlPath) {
		Parent rootNode;
		try {
			rootNode = fxmlLoader.load(fxmlPath);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return rootNode;
	}

    public void switchToFullScreenMode() {
		primaryStage.setFullScreen(true);
	}

	public void switchToWindowedMode() {
		primaryStage.setFullScreen(false);
	}

	public boolean isStageFullScreen() {
		return primaryStage.isFullScreen();
	}

	public void exit() {
		primaryStage.close();
	}

}
