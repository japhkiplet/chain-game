package uiGameBoard;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ui.UISettings;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import application.ChainGameFacade;
import domain.Connection;
import domain.ConnectionObserver;

final class ConnectionPane extends BoardPane implements ConnectionObserver{
	private final List<ConnectionLine> connections = new ArrayList<ConnectionLine>();

	ConnectionPane(){
		super();
		ChainGameFacade.registerConnectionObserver(this);
	}

	@Override
	protected void onResize() {
		for (ConnectionLine line : connections) {
			Point startPoint = getScreenPoint(line.getGridStart());
			Point endPoint = getScreenPoint(line.getGridEnd());
			line.setStartX(startPoint.x);
			line.setStartY(startPoint.y);
			line.setEndX(endPoint.x);
			line.setEndY(endPoint.y);
		}
	}

	@Override
	public void onConnectionAdded(Connection newConnection) {
		Point holeALocation = newConnection.getHoleA().getLocation();
		Point holeBLocation = newConnection.getHoleB().getLocation();
		Point start = getScreenPoint(holeALocation);
		Point end = getScreenPoint(holeBLocation);
		Paint playerColor = UISettings.getPlayerColor(newConnection.getOwningPlayer());
		final ConnectionLine line = new ConnectionLine(holeALocation, holeBLocation,start, end,playerColor );
		connections.add(line);
		Platform.runLater(() -> updateConnectionPane(this, line));
	}
	
	private void updateConnectionPane(Pane pane, ConnectionLine line) {
		ObservableList<Node> children = pane.getChildren();
		children.addAll(line);
	}

	@Override
	public void onConnectionRemoved(Connection removedConnection) {
		Point holeALocation = removedConnection.getHoleA().getLocation();
		Point holeBLocation = removedConnection.getHoleB().getLocation();
		Point start = getScreenPoint(holeALocation);
		Point end = getScreenPoint(holeBLocation);
		Paint playerColor = UISettings.getPlayerColor(removedConnection.getOwningPlayer());
		final ConnectionLine line = new ConnectionLine(holeALocation, holeBLocation,start, end,playerColor );
		connections.remove(line);
		Platform.runLater(() -> updateConnectionRemovedPane(this, line));
		
	}
	
	private void updateConnectionRemovedPane(Pane pane, ConnectionLine line) {
		ObservableList<Node> children = pane.getChildren();
		children.removeAll(line);
	}
	
	@Override
	public String toString() {
		return "ConnectionPane [connections=" + connections + "]";
	}	
}
