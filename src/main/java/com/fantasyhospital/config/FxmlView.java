package com.fantasyhospital.config;

public enum FxmlView {

	START {
		@Override
		public String getFxmlPath() {
			return "/fxml/logoView.fxml";
		}
	},

	MINI_LOGO {
		@Override
		public String getFxmlPath() {
			return "/fxml/logoMiniView.fxml";
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

	MAIN {
		@Override
		public String getFxmlPath() {
			return "/fxml/mainView.fxml";
		}
	};

	public abstract String getFxmlPath();
}
