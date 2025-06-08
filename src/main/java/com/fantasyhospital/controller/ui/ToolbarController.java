package com.fantasyhospital.controller.ui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledExecutorService;

import com.fantasyhospital.util.Singleton;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fantasyhospital.EvolutionGame;
import com.fantasyhospital.Simulation;
import com.fantasyhospital.config.FxmlView;
import com.fantasyhospital.config.StageManager;
import com.fantasyhospital.controller.ConsoleLogController;
import com.fantasyhospital.controller.CounterController;
import com.fantasyhospital.util.LogsUtils;

import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;
import com.fantasyhospital.controller.HospitalStructureController;

@Component
public class ToolbarController implements Initializable {

	public ToolBar toolPane;
	@FXML
	private Button homeButton;

	@FXML
	private Button fullScreenButton;

	@FXML
	private Button startSimulationButton;

	@FXML
	private Button exitButton;

	@FXML
	private Button nextRoundButton;

	@FXML
	private TextArea logConsole;

	private final ConsoleLogController consoleLogController;
	private final StageManager stageManager;
	private final CounterController counterController;

	private static final PseudoClass maximizeIcon = PseudoClass.getPseudoClass("max");
	private static final PseudoClass minimizeIcon = PseudoClass.getPseudoClass("min");

	private ScheduledExecutorService scheduler;
	private final Simulation simulation;
	private EvolutionGame jeu;

	private final HospitalStructureController hospitalStructureController;

	@Lazy
	public ToolbarController(StageManager stageManager, Simulation simulation, ConsoleLogController consoleLogController,
	                          HospitalStructureController hospitalStructureController, CounterController counterController) {
		this.simulation = simulation;
		this.stageManager = stageManager;
		this.consoleLogController = consoleLogController;
		this.hospitalStructureController = hospitalStructureController;
		this.counterController = counterController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (stageManager.isStageFullScreen()) {
			setWindowedGraphicsAndAction();
		} else {
			setFullScreenGraphicsAndAction();
		}
	}

	public void goFullScreen() {
		stageManager.switchToFullScreenMode();
		setWindowedGraphicsAndAction();
	}

	public void goWindowed() {
		stageManager.switchToWindowedMode();
		setFullScreenGraphicsAndAction();

	}

	void setFullScreenGraphicsAndAction() {

		fullScreenButton.pseudoClassStateChanged(minimizeIcon, false);
		fullScreenButton.pseudoClassStateChanged(maximizeIcon, true);

		fullScreenButton.setOnAction(e -> goFullScreen());

	}

	// Cette méthode permet d'écouter l'événement de fermeture de la fenêtre
	public void setStage(Stage stage) {
		stage.setOnCloseRequest(event -> stop()); // Ajoute un gestionnaire pour la fermeture

	}

	public void setWindowedGraphicsAndAction() {

		fullScreenButton.pseudoClassStateChanged(maximizeIcon, false);
		fullScreenButton.pseudoClassStateChanged(minimizeIcon, true);

		fullScreenButton.setOnAction(e -> goWindowed());
	}

	public void stop() {
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdown();
		}
		Platform.exit();  // Ferme toutes les ressources JavaFX
	}

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

	public void clearLog(ActionEvent actionEvent) {
		consoleLogController.clearConsole();
		LogsUtils.clearLogFile();
	}

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
