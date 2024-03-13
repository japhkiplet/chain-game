package uiGameBoard;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import ui.UISettings;
import domain.PegObserver;
import domain.PlayerID;
import application.ChainGameFacade;
import javafx.application.Platform;

final class PegPane extends BoardPane implements PegObserver {
	private Map<Point, PegCircle> pegs = new HashMap<>();

	PegPane() {
		super();
		initializePegs();
		ChainGameFacade.registerPegObserver(this);
	}

	private void initializePegs() {
		for (Point gridPoint : this.getGridPoints()) {
			PegCircle pegCircle = createPegCircle(gridPoint);
			pegs.put(gridPoint, pegCircle);
			this.getChildren().add(pegCircle);
		}
	}

	private PegCircle createPegCircle(Point gridPoint) {
		Point screenPoint = this.getScreenPoint(gridPoint);
		PegCircle pegCircle = new PegCircle(gridPoint, screenPoint);
		colorIfOnEdge(gridPoint, pegCircle);

		return pegCircle;
	}

	private void colorIfOnEdge(Point gridPoint, PegCircle peg) {
		if (isHorizontalEdge(gridPoint.y)) {
			peg.setStroke(UISettings.getPlayerColor(PlayerID.PLAYER_B));
		} else if (isVerticalEdge(gridPoint.x)) {
			peg.setStroke(UISettings.getPlayerColor(PlayerID.PLAYER_A));
		}
	}

	@Override
	protected void onResize() {
		double baseRadius = PegCircleRadius.calculatePegBaseRadius(getWidth(),
				getHeight(), getGridWidth(), getGridHeight());
		PegCircle.setBaseRadius(baseRadius);

		resizePegs();
	}

	private void resizePegs() {
		for (PegCircle peg : pegs.values()) {
			Point screenPoint = this.getScreenPoint(peg.getGridPoint());
			peg.onResize(screenPoint.x, screenPoint.y);
		}
	}

	@Override
	public void onPegClicked(Point pegPoint, PlayerID owningPlayerID) {
		Platform.runLater(() -> {
			PegCircle pegCircle = pegs.get(pegPoint);
			pegCircle.setPegClicked(UISettings.getPlayerColor(owningPlayerID));
		});
	}

	@Override
	public void onPegRemoved(Point pegPoint, PlayerID owningPlayerID) {
		Platform.runLater(() -> {
			PegCircle pegCircle = pegs.get(pegPoint);
			pegCircle.setPegUnclicked();
		});
	}

	@Override
	public String toString() {
		return "PegPane [pegs=" + pegs + "]";
	}
}