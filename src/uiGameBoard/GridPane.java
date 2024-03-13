package uiGameBoard;

import domain.PlayerID;
import ui.UISettings;
import javafx.scene.shape.Line;

final class GridPane extends BoardPane {
	private static final int LINE_WIDTH = 2;

	protected void onResize() {
		this.getChildren().clear();

		drawHorizontalLines();
		drawVerticalLines();
	}

	private void drawVerticalLines() {
		for (int gridX = 0; gridX < getGridWidth(); gridX++) {
			double screenX = getScreenX(gridX);
			
			Line line = isVerticalEdge(gridX) ? buildVerticalEdgeLine(screenX)
					: buildVerticalLine(screenX);
			line.setStrokeWidth(LINE_WIDTH);
			this.getChildren().add(line);
		}
	}

	private Line buildVerticalLine(double screenX) {
		double startX = screenX;
		double startY = BoardPane.MARGIN + LINE_WIDTH;
		double endX = screenX;
		double endY = this.getHeight() - BoardPane.MARGIN - LINE_WIDTH;

		return new Line(startX, startY, endX, endY);
	}

	private Line buildVerticalEdgeLine(double screenX) {
		double startX = screenX;
		double startY = BoardPane.MARGIN + getSpacingY();
		double endX = screenX;
		double endY = this.getHeight() - BoardPane.MARGIN - getSpacingY();

		Line edgeLine = new Line(startX, startY, endX, endY);
		edgeLine.setStroke(UISettings.getPlayerColor(PlayerID.PLAYER_A));
		return edgeLine;
	}

	private void drawHorizontalLines() {
		for (int gridY = 0; gridY < getGridHeight(); gridY++) {
			int screenY = getScreenY(gridY);

			Line line = isHorizontalEdge(gridY) ? buildHorizontalEdgeLine(screenY)
					: buildHorizontalLine(screenY);
			line.setStrokeWidth(LINE_WIDTH);
			this.getChildren().add(line);
		}
	}

	private Line buildHorizontalLine(int screenY) {
		double startX = BoardPane.MARGIN + LINE_WIDTH;
		double startY = screenY;
		double endX = this.getWidth() - BoardPane.MARGIN - LINE_WIDTH;
		double endY = screenY;

		return new Line(startX, startY, endX, endY);
	}

	private Line buildHorizontalEdgeLine(int screenY) {
		double startX = BoardPane.MARGIN + getSpacingX();
		double startY = screenY;
		double endX = this.getWidth() - BoardPane.MARGIN - getSpacingX();
		double endY = screenY;

		Line edgeLine = new Line(startX, startY, endX, endY);
		edgeLine.setStroke(UISettings.getPlayerColor(PlayerID.PLAYER_B));
		return edgeLine;
	}
}
