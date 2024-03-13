package ai;

import java.awt.Point;

enum DirectionOfTravel {
	UP (0,-1),
	DOWN (0,1),
	LEFT (-1,0),
	RIGHT (1,0);
	
	private final Point directionMod;
	
	DirectionOfTravel(int hMod, int vMod) {
		directionMod = new Point(hMod, vMod);
	}
	
	public static DirectionOfTravel getDirectionToOppositeEdge(
			Point movePoint, int numRows, int numColumns) {
		if (movePoint.x == 0) {
			return RIGHT;
		} else if (movePoint.x == numColumns-1) {
			return LEFT;
		} else if (movePoint.y == 0) {
			return DOWN;
		} else if (movePoint.y == numRows-1) {
			return UP;
		} else {
			// default case
			return DOWN;
		}
	}
	
	public Point getAsPoint() {
		return directionMod;
	}
}