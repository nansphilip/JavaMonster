package com.fantasyhospital;

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
	}

	@Override
	public void stop() {
		applicationContext.close();
		stage.close();
	}

	private void showMainScene() {
		stageManager.switchScene(FxmlView.START);
	}
}
