package com.fantasyhospital;

import com.fantasyhospital.controller.ui.ToolbarController;
import javafx.application.Platform;
import javafx.scene.image.Image;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.fantasyhospital.config.FxmlLoader;
import com.fantasyhospital.config.FxmlView;
import com.fantasyhospital.config.StageManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class FantasyHospitalApplication extends Application {

	private Stage stage;
	private ConfigurableApplicationContext applicationContext;
	private StageManager stageManager;

	@Override
	public void init() {
		applicationContext = new SpringApplicationBuilder(FantasyHospitalStarter.class).run();
	}

	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;

		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon/Icon.png")));

		stageManager = applicationContext.getBean(StageManager.class,
			applicationContext.getBean(FxmlLoader.class),
			primaryStage);
		showMainScene();

		primaryStage.setOnCloseRequest(event -> {
			event.consume(); // consume empeche fermeture immediate de l'app

			try {
				ToolbarController toolbarController = applicationContext.getBean(ToolbarController.class);
				if (toolbarController != null) {
					toolbarController.stop();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Appel de la méthode stop de l'application pour fermer le contexte Spring
			this.stop();

			Platform.exit();
			System.exit(0);
		});
	}

	@Override
	public void stop() {
		applicationContext.close();
		//stage.close();
	}

	private void showMainScene() {
		stageManager.switchScene(FxmlView.START);
	}
}
