package com.fantasyhospital.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javafx.stage.Stage;

@Configuration
public class ApplicationConfig {

	@Bean
	@Lazy
	public StageManager stageManager(FxmlLoader fxmlLoader, Stage stage) {
		return new StageManager(fxmlLoader, stage);
	}
}
