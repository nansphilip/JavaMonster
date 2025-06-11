package com.fantasyhospital.controller.ui;

import com.fantasyhospital.EvolutionGame;
import com.fantasyhospital.Simulation;
import com.fantasyhospital.config.StageManager;
import com.fantasyhospital.controller.ConsoleLogController;
import com.fantasyhospital.controller.CounterController;
import com.fantasyhospital.controller.HospitalStructureController;
import com.fantasyhospital.util.LogsUtils;
import com.fantasyhospital.util.Singleton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Controller for the main toolbar UI, handling simulation controls and log actions.
 */
@Component
public class ToolbarController implements Initializable {

	@FXML
	private Button restartButton;

	@FXML
	private Button startSimulationButton;

	@FXML
	private Button nextRoundButton;

	private final ConsoleLogController consoleLogController;
	private final StageManager stageManager;
	private final CounterController counterController;

	private ScheduledExecutorService scheduler;
	private final Simulation simulation;

	private final HospitalStructureController hospitalStructureController;


	/**
	 * Constructs the ToolbarController with required dependencies.
	 *
	 * @param stageManager the StageManager bean
	 * @param simulation the Simulation instance
	 * @param consoleLogController the ConsoleLogController
	 * @param hospitalStructureController the HospitalStructureController
	 * @param counterController the CounterController
	 */
	@Lazy
	public ToolbarController(StageManager stageManager, Simulation simulation, ConsoleLogController consoleLogController,
	                          HospitalStructureController hospitalStructureController, CounterController counterController) {
		this.simulation = simulation;
		this.stageManager = stageManager;
		this.consoleLogController = consoleLogController;
		this.hospitalStructureController = hospitalStructureController;
		this.counterController = counterController;
	}

	/**
	 * Initializes the toolbar UI components.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialize restart button
		if (restartButton != null) {
			restartButton.setDisable(false);
		}
	}

	/**
	 * Sets the stage and adds a close event handler.
	 *
	 * @param stage the primary Stage
	 */
	public void setStage(Stage stage) {
		stage.setOnCloseRequest(event -> stop()); // Ajoute un gestionnaire pour la fermeture

	}

	/**
	 * Restarts the simulation and resets UI/logs.
	 */
	@FXML
	private void restartSimulation() {
		consoleLogController.appendText("🔄 Restarting application...\n");
		
		// Clear console log
		consoleLogController.clearConsole();
		
		// Restart simulation in background thread
		new Thread(() -> {
			simulation.restartSimulation();
			Platform.runLater(() -> {
				// Re-enable start button for new simulation
				startSimulationButton.setDisable(false);
				
				// Reset all counters display
				counterController.resetCounters();
				
				consoleLogController.appendText("✅ Application reset to welcome screen!\n");
				consoleLogController.appendText("💡 Click 'Start' to begin a new simulation.\n");
			});
		}).start();
	}

	/**
	 * Stops the scheduler and exits the application.
	 */
	public void stop() {
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdown();
		}
		Platform.exit();  // Ferme toutes les ressources JavaFX
	}

	/**
	 * Starts the simulation if not already running.
	 */
	@FXML
	private void startSimulation() {
		if (simulation.isRunning()) {
			consoleLogController.appendText("⚠️ La simulation est déjà en cours.\n");
			return;
		}

		startSimulationButton.setDisable(true);
		consoleLogController.clearConsole();
		consoleLogController.appendText("✅ Démarrage de la simulation...\n");

		hospitalStructureController.startGame();

		new Thread(() -> {
			simulation.startSimulation();
			Platform.runLater(() -> startSimulationButton.setDisable(false));
		}).start();
	}

	/**
	 * Clears the console log and log file.
	 *
	 * @param actionEvent the action event
	 */
	public void clearLog(ActionEvent actionEvent) {
		consoleLogController.clearConsole();
		LogsUtils.clearLogFile();
	}

	/**
	 * Handles the next round action, updates counters and UI.
	 *
	 * @param actionEvent the action event
	 */
	public void handleNextRound(ActionEvent actionEvent) {
		EvolutionGame jeu = simulation.getJeu();
		if (jeu == null) {
			consoleLogController.appendText("La simulation n'est pas démarrée.\n");
			return;
		}

		// Incrémenter le compteur de tours
		counterController.incrementTurnCounter();

		if(jeu.runNextRound()){
			Singleton singleton = Singleton.getInstance();
			singleton.setEndGameSummary(singleton.buildEndGameSummary());
			jeu.showEndGame();
		}

		// Mettre à jour les compteurs de créatures soignées et décédées
		counterController.updateHealedCounter();
		counterController.updateDeathCounter();
		counterController.updateDeathDoctorsCounter();
		counterController.updateGlobalBudget(hospitalStructureController.getHospital());

		hospitalStructureController.updateWaitingRoom();
		hospitalStructureController.updateCrypt();
		hospitalStructureController.updateQuarantine();
	}
}
