package com.fantasyhospital.config;

public enum FxmlView {

	START {
		@Override
		public String getFxmlPath() {
			return "/fxml/logoView.fxml";
		}
	},

	LIST_CREATURE {
		@Override
		public String getFxmlPath() {
			return "/fxml/listCreatureView.fxml";
		}
	},

	LIST_DOCTORS {
		@Override
		public String getFxmlPath() {
			return "/fxml/listDoctorsView.fxml";
		}
	},

	CREATURE_DETAILS {
		@Override
		public String getFxmlPath() {
			return "/fxml/creatureDetails.fxml";
		}
	},

	END_GAME_LOG {
		@Override
		public String getFxmlPath() {
			return "/fxml/endGameLogView.fxml";
		}
	},

	MAIN {
		@Override
		public String getFxmlPath() {
			return "/fxml/mainView.fxml";
		}
	};

	public abstract String getFxmlPath();
}
