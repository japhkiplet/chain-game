package uiGameBoard;

public class PegCircleRadius {
	private static final double MIN_RADIUS = 7.5;
	private static final double MAX_RADIUS_FACTOR = 225.0;
	private static final double RADIUS_SCALE = 50.0;

	private PegCircleRadius() {
	}

	static double calculatePegBaseRadius(double width, double height,
			double gridWidth, double gridHeight) {
		double smallestDimension = Math.min(width, height);
		double baseRadius = smallestDimension / RADIUS_SCALE;
		baseRadius = clampPegRadius(gridWidth, gridHeight, baseRadius);

		return baseRadius;
	}

	private static double clampPegRadius(double gridWidth, double gridHeight,
			double radius) {
		double maxRadius = MAX_RADIUS_FACTOR / Math.max(gridWidth, gridHeight);
		return Math.min(maxRadius, Math.max(MIN_RADIUS, radius));
	}
}
