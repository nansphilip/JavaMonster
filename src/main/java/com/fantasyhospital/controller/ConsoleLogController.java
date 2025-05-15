package com.fantasyhospital.controller;

import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.fantasyhospital.Simulation;
import com.fantasyhospital.util.LogsUtils;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Contrôleur JavaFX chargé d'afficher le contenu du fichier de log
 * (logs/app.log) dans la zone de texte de la fenêtre graphique.
 * <p>
 * Ce contrôleur lit le fichier de log à l'initialisation de la vue et affiche
 * chaque ligne dans le composant TextArea nommé logConsole.
 * </p>
 */
@Component
public class ConsoleLogController implements Initializable {

	private final Path logFilePath = Path.of("logs/app.log");
	@FXML
	private TextArea logConsole;

	private ScheduledExecutorService scheduler;
	private final Simulation simulation;


	public ConsoleLogController(Simulation simulation) {
		this.simulation = simulation;
	}


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		scheduler = Executors.newSingleThreadScheduledExecutor();

		LogTailListener listener = new LogTailListener();
		scheduler.scheduleAtFixedRate(() -> {

			if (Files.exists(logFilePath)) {
				listener.listen();
			}

		}, 0, 1, TimeUnit.SECONDS);
	}

	class LogTailListener {

		long filePointer;

		void listen() {

			try {
				long len = Files.size(logFilePath);

				if (len < filePointer) {
					filePointer = len;
				} else if (len > filePointer) {

					try (RandomAccessFile raf = new RandomAccessFile(logFilePath.toFile(), "r")) {
						raf.seek(filePointer);
						String line;
						while ((line = raf.readLine()) != null) {
							logConsole.appendText(new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8) + "\n");
						}
						filePointer = raf.getFilePointer();
					}
				}
			} catch (Exception e) {
			}
		}
	}

	public void stop() {
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdown();
		}
		Platform.exit();  // Ferme toutes les ressources JavaFX
	}

	// Cette méthode permet d'écouter l'événement de fermeture de la fenêtre
	public void setStage(Stage stage) {
		stage.setOnCloseRequest(event -> stop()); // Ajoute un gestionnaire pour la fermeture

	}

	public void clearLog(ActionEvent actionEvent) {
		LogsUtils.clearLogFile(); // Appeler la méthode de nettoyage du fichier de log
		logConsole.setText(""); // Effacer également le contenu du TextArea
	}

	@FXML
	private void startSimulation() {
		simulation.startSimulation();
	}

}
