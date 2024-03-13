package ui;

import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SceneLoader {
	public static final String UI_SCENES_PATH = "/uiScenes/";
	static Scene scene = new Scene(new Parent() { });

	private SceneLoader() {}

	public static void loadScene(Pane pane, String fxmlFilename) {
		try {
			Parent root = loadFXMLResource(UI_SCENES_PATH + fxmlFilename);
			setScene(root);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void loadScene(Pane pane, String fxmlFilename, Stage stage,
			double STAGE_WIDTH, double STAGE_HEIGHT) {
		stage.setWidth(STAGE_WIDTH);
		stage.setHeight(STAGE_HEIGHT);
		Scene scene = stage.getScene();
		try {
			Parent root = loadFXMLResource(UI_SCENES_PATH + fxmlFilename);
			scene.setRoot(root);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void loadScene(Pane pane, String fxmlFilename,
			ActionEvent source, double STAGE_WIDTH, double STAGE_HEIGHT) {
		Stage stage = getEventSourceStage(source);
		stage.setWidth(STAGE_WIDTH);
		stage.setHeight(STAGE_HEIGHT);
		Scene scene = stage.getScene();
		try {
			Parent root = loadFXMLResource(UI_SCENES_PATH + fxmlFilename);
			scene.setRoot(root);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static Stage getEventSourceStage(ActionEvent event) {
		Node node = (Node) event.getSource();
		return (Stage) node.getScene().getWindow();
	}

	private static Parent loadFXMLResource(String fxmlFilename)
			throws IOException {
		final URL resource = SceneLoader.class.getResource(fxmlFilename);
		FXMLLoader loader = new FXMLLoader(resource);
		return loader.load();
	}

	public static void setScene(Parent root) {
		scene.setRoot(root);
	}

	public static Scene getScene() {
		return scene;
	}
}