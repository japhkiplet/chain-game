package ui;

import java.io.IOException;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class ChainGameUI extends Pane {

	public ChainGameUI() {
		initUIComponents();
	}

	public void onWindowCloseEvent(Event event) {
		// TODO: check if the user really wants to close game
		/**
		 * if not: use event.consume(); otherwise: let the game controller
		 * process closing event
		 */
		GameInitializer.endGame();
	}

	private void initUIComponents() {
		setGameOptionsScene();
	}

	public void setGameOptionsScene() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/uiScenes/GameOptionsScene.fxml"));
			root = loader.load();
			SceneLoader.setScene(root);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
