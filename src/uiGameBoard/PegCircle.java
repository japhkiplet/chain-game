package uiGameBoard;

import java.awt.Point;
import ui.UISettings;
import domain.Player;
import application.ChainGameFacade;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

final class PegCircle extends Circle {
	private static final Paint DEFAULT_FILL = new Color(0.7, 0.7, 0.7, 0.2);
	private static final Paint DEFAULT_STROKE = Color.BLACK;
	private static final double BORDER_WIDTH = 1.5;
	private static final double GHOST_OPACITY = 0.6;
	private static final double CLICKED_OPACITY = 1.0;
	private static final double CLICKED_RADIUS_MULTIPLIER = 1.4;
	
	private static double baseRadius;
	private Point gridPoint;
	private Paint baseFill;
	private double baseOpacity;
	private boolean isClicked = false;
	private static final SoundManager soundManager = new SoundManager();


	PegCircle(Point gridPoint, Point screenPoint) {
		super(screenPoint.x, screenPoint.y, 0.0d);
		this.gridPoint = gridPoint;

		setDefaults();
		registerMouseEvents();
	}

	private void setDefaults() {
		setStrokeWidth(BORDER_WIDTH);
		setStroke(DEFAULT_STROKE);
		setFill(DEFAULT_FILL);
		setBaseFill(DEFAULT_FILL);
		setBaseOpacity(getOpacity());
	}

	private void registerMouseEvents() {
		registerMouseEntered();
		registerMouseExited();
		registerMouseClick();
	}

	private void registerMouseEntered() {
		this.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event) -> {
			if (!isInteractable()) {
				return;
			}

			if (ChainGameFacade.isValidMove(gridPoint)) {
				Paint playerColor = getPlayerColor();
				this.setFill(playerColor);
				this.setOpacity(GHOST_OPACITY);
				soundManager.playPegHoverSound();
			}
		});
	}

	private void registerMouseExited() {
		this.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent event) -> {
			if (!isInteractable()) {
				return;
			}

			this.setFill(getBaseFill());
			this.setOpacity(getBaseOpacity());
		});
	}

	private void registerMouseClick() {
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
			if (!isInteractable()) {
				return;
			}

			if (ChainGameFacade.isValidMove(gridPoint)) {
				ChainGameFacade.clickPeg(gridPoint);
				soundManager.playPegPlacedSound();
			}
		});
	}

	private boolean isInteractable() {
		return ChainGameFacade.isHumanPlayerTurn() && GameBoard.isClickable();
	}

	void setPegClicked(Paint playerColor) {
		isClicked = true;
		updateRadius();
		setFill(playerColor);
		setBaseFill(playerColor);
		setStroke(DEFAULT_STROKE);
		setBaseOpacity(CLICKED_OPACITY);
		setOpacity(CLICKED_OPACITY);
	}
	
	void setPegUnclicked() {
		isClicked = false;
		updateRadius();
		setFill(DEFAULT_FILL);
		setBaseFill(DEFAULT_FILL);
		setStroke(DEFAULT_STROKE);
		setBaseOpacity(getOpacity());
		setOpacity(getOpacity());
	}

	private Paint getPlayerColor() {
		Player currentPlayer = ChainGameFacade.getCurrentPlayer();
		Paint playerColor = UISettings.getPlayerColor(currentPlayer.getID());
		return playerColor;
	}

	Point getGridPoint() {
		return gridPoint;
	}

	private void setBaseFill(Paint color) {
		baseFill = color;
	}

	private Paint getBaseFill() {
		return baseFill;
	}

	private double getBaseOpacity() {
		return baseOpacity;
	}

	private void setBaseOpacity(double baseOpacity) {
		this.baseOpacity = baseOpacity;
	}
	
	static void setBaseRadius(double baseRadius) {
		PegCircle.baseRadius = baseRadius;
	}

	boolean isClicked() {
		return isClicked;
	}

	void onResize(int x, int y) {
		setCenterX(x);
		setCenterY(y);

		updateRadius();
	}

	void updateRadius() {
		double radius = baseRadius;
		if (isClicked()) {
			radius *= CLICKED_RADIUS_MULTIPLIER;
		}
		setRadius(radius);
	}

	@Override
	public String toString() {
		return "PegCircle [gridPoint=" + gridPoint + ", baseFill=" + baseFill
				+ ", baseOpacity=" + baseOpacity + ", isClicked=" + isClicked
				+ "]";
	}
}