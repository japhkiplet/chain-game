package uiGameBoard;

import java.awt.Point;

import ui.UISettings;
import domain.Player;
import application.ChainGameFacade;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.effect.Glow;

final class ConnectionLine extends Line{

	private Point gridStart, gridEnd; 
	private static final float CONNECTION_WIDTH = 4.0f;
	private static final double MIN_GLOW_LEVEL = 0.0f;
	private static final double MAX_GLOW_LEVEL = 1.0f;

	ConnectionLine(Point gridStart, Point gridEnd, Point startPoint, Point endPoint, Paint playerColor){
		super(startPoint.x, startPoint.y,endPoint.x, endPoint.y);
		this.gridStart = gridStart;
		this.gridEnd = gridEnd;
		setDefaults(playerColor);
		registerMouseEvents();
	}

	private void setDefaults(Paint playerColor) {
		setStrokeWidth(CONNECTION_WIDTH);
		setStroke(playerColor);
	}

	private void registerMouseEvents() {
		registerMouseClick();
	}

	private void registerMouseClick() {
		this.addEventHandler(MouseEvent.MOUSE_ENTERED,
				(mouseEvent) -> {
					if (this.getStroke().equals(getPlayerColor())){
						this.setEffect(new Glow(MAX_GLOW_LEVEL));
					}
				});
		this.addEventHandler(MouseEvent.MOUSE_EXITED,
				(mouseEvent) -> {
					this.setEffect(new Glow(MIN_GLOW_LEVEL));
				});
	}

	private Paint getPlayerColor() {
		Player currentPlayer = ChainGameFacade.getCurrentPlayer();
		Paint playerColor = UISettings.getPlayerColor(currentPlayer.getID());
		return playerColor;
	}

	Point getGridStart(){
		return  gridStart;
	}
	Point getGridEnd(){
		return  gridEnd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gridEnd == null) ? 0 : gridEnd.hashCode());
		result = prime * result + ((gridStart == null) ? 0 : gridStart.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConnectionLine other = (ConnectionLine) obj;
		if (gridEnd == null) {
			if (other.gridEnd != null)
				return false;
		} else if (!gridEnd.equals(other.gridEnd))
			return false;
		if (gridStart == null) {
			if (other.gridStart != null)
				return false;
		} else if (!gridStart.equals(other.gridStart))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConnectionLine [gridStart=" + gridStart + ", gridEnd=" + gridEnd
				+ "]";
	}

}