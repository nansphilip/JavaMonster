package com.fantasyhospital.config;

public enum FxmlView {

	START {
		@Override
		public String getFxmlPath() {
			return "/fxml/LogoView.fxml";
		}
	},

	MINI_LOGO {
		@Override
		public String getFxmlPath() {
			return "/fxml/LogoMiniView.fxml";
		}
	},

	LIST_CREATURE {
		@Override
		public String getFxmlPath() {
			return "/fxml/ListCreatureView.fxml";
		}
	},

	MAIN {
		@Override
		public String getFxmlPath() {
			return "/fxml/MainView.fxml";
		}
	};

	public abstract String getFxmlPath();
}
