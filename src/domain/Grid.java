package domain;

import java.awt.Point;
import java.util.Iterator;

public class Grid implements Iterable<Point> {
	final int numColumns;
	final int numRows;

	public Grid(int numColumns, int numRows) {
		super();
		this.numColumns = numColumns;
		this.numRows = numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public int getNumRows() {
		return numRows;
	}

	public boolean isValid(Point point) {
		return isValidColumn(point.x) && isValidRow(point.y);
	}

	private boolean isValidColumn(int x) {
		return 0 <= x && x < numColumns;
	}

	private boolean isValidRow(int y) {
		return 0 <= y && y < numRows;
	}

	public Point firstPoint() {
		return new Point(0, 0);
	}

	public Point nextPoint(Point point) {
		if (point.x == numColumns - 1) {
			return new Point(0, point.y + 1);
		} else {
			return new Point(point.x + 1, point.y);
		}
	}

	@Override
	public Iterator<Point> iterator() {
		return new GridIterator();
	}

	private class GridIterator implements Iterator<Point> {
		private Point next = firstPoint();

		@Override
		public boolean hasNext() {
			return isValid(next);
		}

		@Override
		public Point next() {
			Point result = next;
			next = nextPoint(result);
			return result;
		}
	}
}
