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

import com.fantasyhospital.EvolutionGame;
import com.fantasyhospital.config.FxmlView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * JavaFX controller responsible for displaying the log file content
 * (`logs/app.log`) in the UI text area.
 * <p>
 * Reads the log file at view initialization and displays each line
 * in the `logConsole` TextArea component.
 * </p>
 */
@Component
public class ConsoleLogController implements Initializable {

	private final Path logFilePath = Path.of("logs/app.log");

	/**
	 * The TextArea displaying the log content.
	 */
	@FXML
    public TextArea logConsole;

	private ScheduledExecutorService scheduler;

	private final EvolutionGame evolutionGame;


	/**
	 * Constructs the controller with the game instance.
	 *
	 * @param evolutionGame the EvolutionGame instance
	 */
	public ConsoleLogController(EvolutionGame evolutionGame) {
		this.evolutionGame = evolutionGame;
	}


	/**
	 * Initializes the log console and starts the log file listener.
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			if (Files.exists(logFilePath) && Files.size(logFilePath) > 0) {
				clearConsole();
				Files.newBufferedWriter(logFilePath, StandardCharsets.UTF_8).close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		scheduler = Executors.newSingleThreadScheduledExecutor();

		LogTailListener listener = new LogTailListener();
		scheduler.scheduleAtFixedRate(() -> {

			if (Files.exists(logFilePath)) {
				listener.listen();
			}

		}, 0, 1, TimeUnit.SECONDS);
	}

	/**
	 * Listens for new lines in the log file and updates the console.
	 */
	class LogTailListener {

		long filePointer;

		private String cleanLogLineWithTime(String line) {
			String[] parts = line.split(" - ", 2);
			if (parts.length < 2) return line;

			String time = line.length() >= 8 ? line.substring(0, 19) : "";
			return time + " - " + parts[1].trim();
		}



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
							final String logLine = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

							String cleanedLine = cleanLogLineWithTime(logLine);
							javafx.application.Platform.runLater(() -> logConsole.appendText(cleanedLine + "\n"));

							//javafx.application.Platform.runLater(() -> logConsole.appendText(logLine + "\n"));

							if (cleanedLine.contains("FIN DU JEU")) {
								javafx.application.Platform.runLater(() -> {
									displayEndGameLog();
								});
							}
						}
						filePointer = raf.getFilePointer();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Clears the log console.
	 */
	public void clearConsole() {
		if (logConsole != null) {
			logConsole.setText("");
		}
	}

	/**
	 * Appends text to the log console.
	 *
	 * @param text the text to append
	 */
	public void appendText(String text) {
		if (logConsole != null) {
			javafx.application.Platform.runLater(() -> logConsole.appendText(text));
		}
	}

	/**
	 * Displays the end game log dialog.
	 */
	private void displayEndGameLog() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(FxmlView.END_GAME_LOG.getFxmlPath()));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.setTitle("Fin de partie");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(root));

			EndGameLogController controller = loader.getController();
			controller.setDialogStage(stage);
			controller.setSummary(evolutionGame.getEndGameSummary());

			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
