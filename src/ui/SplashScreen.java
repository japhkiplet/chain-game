package ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SplashScreen implements Initializable{
	@FXML private Pane splashScreenPane;
	@FXML private Button splashButtonTitle;
	@FXML private Button cancelButton;
	@FXML private Rectangle leftRedRectangle;
	@FXML private Rectangle centerBlueRectangle;
	@FXML private Rectangle rightRedRectangle;
	
	private RotateTransition rotate;
	private final int CYCLE_COUNT = 100;
	private final int ROTATION_ANGLE = 280;
	private final int ARC_HEIGHT = 10;
	private final int ARC_WIDTH = 10;
	private final int ROTATION_DURATION_MS = 9000;
	private final int RECTANGLE_RATE = 10;

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		splashScreenCustomFont();
		rectanglesAnimation();
		initCancelButton();
	}

	private void splashScreenCustomFont() {
		CustomFonts.setCustomTitleButtonFont(splashButtonTitle, "LOADING..");
		CustomFonts.setCustomButtonFont(cancelButton, "CANCEL");
	}

	private void initCancelButton() {
		cancelButton.setOnAction((event) -> cancelButtonClickEvent());
	}

	private void cancelButtonClickEvent() {
		GameInitializer.endGame();
	}

	private void animateRectangle(Rectangle rectangle, Color color) {
		rectangle.setArcHeight(ARC_HEIGHT);
		rectangle.setArcWidth(ARC_WIDTH);
		rectangle.setFill(color);
		rotateRectangle(rectangle);
	}

	private void rotateRectangle(Rectangle rectangle) {
		rotate = new RotateTransition(Duration.millis(ROTATION_DURATION_MS),rectangle);
		rotate.setByAngle(ROTATION_ANGLE);
		rotate.setCycleCount(CYCLE_COUNT);
		rotate.setRate(RECTANGLE_RATE);
		rotate.setAutoReverse(true);
		rotate.play();
	}

	private void rectanglesAnimation() {
		animateRectangle(leftRedRectangle, Color.RED);
		animateRectangle(centerBlueRectangle, Color.BLUE);
		animateRectangle(rightRedRectangle, Color.RED);
	}
}