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

	MAIN {
		@Override
		public String getFxmlPath() {
			return "/fxml/mainView.fxml";
		}
	};

	public abstract String getFxmlPath();
}
