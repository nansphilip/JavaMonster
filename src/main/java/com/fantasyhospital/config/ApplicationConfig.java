package com.fantasyhospital.config;

import com.fantasyhospital.model.Hospital;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javafx.stage.Stage;

/**
 * Spring configuration class for application beans.
 */
@Configuration
public class ApplicationConfig {

	/**
	 * Provides the StageManager bean.
	 *
	 * @param fxmlLoader the FxmlLoader bean
	 * @param stage the primary Stage
	 * @return the StageManager bean
	 */
	@Bean
	@Lazy
	public StageManager stageManager(FxmlLoader fxmlLoader, Stage stage) {
		return new StageManager(fxmlLoader, stage);
	}

	/**
	 * Provides the Hospital bean.
	 *
	 * @return the Hospital instance
	 */
	@Bean
	public Hospital hospital() {
		return new Hospital("Marseille");
	}
}
