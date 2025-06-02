package com.fantasyhospital.config;

import com.fantasyhospital.model.Hospital;
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
	@Bean
	public Hospital hospital() {
		return new Hospital("Marseille");
	}
}
