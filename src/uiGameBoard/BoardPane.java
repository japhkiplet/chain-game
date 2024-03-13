package uiGameBoard;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.Pane;

/**
 * Base class for the layered panes that make up the playing board. This base
 * class is special in that it contains static fields for Grid width and height,
 * which allows all sub-classed panes to use one uniform grid for drawing.
 */
abstract class BoardPane extends Pane {
	protected static final int MARGIN = 25;
	private static int gridWidth;
	private static int gridHeight;

	final void bindSize(Pane parent) {
		this.prefHeightProperty().bind(parent.heightProperty());
		this.prefWidthProperty().bind(parent.widthProperty());
	}

	/**
	 * @return The screen spacing between grid coordinates on the x plane
	 */
	protected final double getSpacingX() {
		return (getWidth() - (BoardPane.MARGIN * 2.0d)) / (gridWidth - 1.0d);
	}

	/**
	 * @return The screen spacing between grid coordinates on the y plane
	 */
	protected final double getSpacingY() {
		return (getHeight() - (BoardPane.MARGIN * 2.0d)) / (gridHeight - 1.0d);
	}

	protected final Point getScreenPoint(Point gridPoint) {
		int screenX = getScreenX(gridPoint.x);
		int screenY = getScreenY(gridPoint.y);

		return new Point(screenX, screenY);
	}

	protected final int getScreenX(int gridX) {
		return (int) (gridX * getSpacingX() + MARGIN);
	}

	protected final int getScreenY(int gridY) {
		return (int) (gridY * getSpacingY() + MARGIN);
	}

	/**
	 * @return A List of all viable grid points
	 */
	protected final List<Point> getGridPoints() {
		List<Point> gridPoints = new ArrayList<Point>(gridWidth * gridHeight);
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				if (!isCornerPosition(x, y)) {
					gridPoints.add(new Point(x, y));
				}
			}
		}

		return gridPoints;
	}

	protected final boolean isCornerPosition(int x, int y) {
		return isVerticalEdge(x) && isHorizontalEdge(y);
	}

	protected final boolean isHorizontalEdge(int y) {
		return y == 0 || y == (gridHeight - 1);
	}

	protected final boolean isVerticalEdge(int x) {
		return x == 0 || x == (gridWidth - 1);
	}

	@Override
	public void resize(double width, double height) {
		super.resize(width, height);
		onResize();
	}

	/**
	 * All sub classes should rescale their contents on resizing.
	 */
	protected abstract void onResize();

	@Override
	public boolean isResizable() {
		return true;
	}

	static final int getGridWidth() {
		return gridWidth;
	}

	static final void setGridWidth(int gridWidth) {
		BoardPane.gridWidth = gridWidth;
	}

	static final int getGridHeight() {
		return gridHeight;
	}

	static final void setGridHeight(int gridHeight) {
		BoardPane.gridHeight = gridHeight;
	}

	@Override
	public String toString() {
		return "BoardPane [gridWidth=" + gridWidth + ", gridHeight=" + gridHeight + "]";
	}
}
