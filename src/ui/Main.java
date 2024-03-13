package ui;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

public class Main extends Application {
	private ChainGameUI gameUI = null;
	private ResizeCanvas canvas;

	@Override
	public void start(Stage primaryStage) {
		initUI(primaryStage);
		setupInputEvents(primaryStage, gameUI);
	}

	public void initUI(Stage primaryStage) {
		gameUI = new ChainGameUI();
		canvas = new ResizeCanvas();

		primaryStage.setTitle("Chain Game");
		StackPane root = new StackPane();
		root.getChildren().addAll(gameUI, canvas);

		canvas.widthProperty().bind(root.widthProperty());
		canvas.heightProperty().bind(root.heightProperty());

		primaryStage.setScene(SceneLoader.getScene());
		primaryStage.show();
	}

	private void setupInputEvents(Stage primaryStage, final ChainGameUI gameUI) {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				gameUI.onWindowCloseEvent(event);
			}
		});
	}

	class ResizeCanvas extends Canvas {
		public ResizeCanvas() {
			InvalidationListener listener = new InvalidationListener() {

				@Override
				public void invalidated(Observable observable) {
					reDraw();
				}
			};

			widthProperty().addListener(listener);
			heightProperty().addListener(listener);
		}

		private void reDraw() {
			GraphicsContext context = getGraphicsContext2D();
			context.fillRect(0, 0, getWidth(), getHeight());
		}

		@Override
		public boolean isResizable() {
			return true;
		}

		@Override
		public double prefWidth(double width) {
			return getWidth();
		}

		@Override
		public double prefHeight(double height) {
			return getHeight();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
