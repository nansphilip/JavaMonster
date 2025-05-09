package com.fantasyhospital.controller;

import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fantasyhospital.util.LogUtils;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Contrôleur JavaFX chargé d'afficher le contenu du fichier de log
 * (logs/app.log) dans la zone de texte de la fenêtre graphique.
 * <p>
 * Ce contrôleur lit le fichier de log à l'initialisation de la vue et affiche
 * chaque ligne dans le composant TextArea nommé logConsole.
 * </p>
 */
public class MainController {

	@FXML
	private TextArea logConsole;

	@FXML
	private ImageView creatureImage;

	private final Path logFilePath = Path.of("logs/app.log");
	private ScheduledExecutorService scheduler;

	@FXML
	public void initialize() {
		scheduler = Executors.newSingleThreadScheduledExecutor();

		LogTailListener listener = new LogTailListener();
		scheduler.scheduleAtFixedRate(() -> {

			if (Files.exists(logFilePath)) {
				listener.listen();
			}

		}, 0, 1, TimeUnit.SECONDS); // met à jour toutes les secondes
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
							logConsole.appendText(new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
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
		LogUtils.clearLogFile(); // Appeler la méthode de nettoyage du fichier de log
		logConsole.setText(""); // Effacer également le contenu du TextArea
	}

	private void creatureImageLog(String logContent) {
		if (logContent.contains("Zombie")) {
			creatureImage.setImage(new Image(getClass().getResourceAsStream("/images/Zombie.PNG")));
		} else if (logContent.contains("Orque")) {
			creatureImage.setImage(new Image(getClass().getResourceAsStream("/images/Orque.PNG")));
			//        } else {
			//            // Une image par défaut ou vide
			//            creatureImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Nain.png"))));
		}
	}
}
