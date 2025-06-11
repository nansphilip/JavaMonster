package com.fantasyhospital.config;

/**
 * Enum representing available FXML views and their file paths.
 */
public enum FxmlView {



	/**
	 * Start view.
	 */
	START {
		@Override
		public String getFxmlPath() {
			return "/fxml/logoView.fxml";
		}
	},

	/**
	 * End game log view.
	 */
	END_GAME_LOG {
		@Override
		public String getFxmlPath() {
			return "/fxml/endGameLogView.fxml";
		}
	},

	/**
	 * Main view.
	 */
	MAIN {
		@Override
		public String getFxmlPath() {
			return "/fxml/mainView.fxml";
		}
	};

	/**
	 * Returns the FXML file path for the view.
	 *
	 * @return the FXML file path
	 */
	public abstract String getFxmlPath();
}
