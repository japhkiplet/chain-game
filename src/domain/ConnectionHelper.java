package domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ConnectionHelper {
	private Set<ConnectionObserver> connectionObservers = new HashSet<ConnectionObserver>();
	private Set<Connection> connections = new HashSet<Connection>();
	private static List<Point> connectionOffsets = new ArrayList<Point>();
	private final Board board;

	static {
		createConnectionOffsets();
	}

	// Constructor
	public ConnectionHelper(Board board) {
		this.board = board;
	}

	// Observer methods
	void registerConnectionObserver(ConnectionObserver observer) {
		connectionObservers.add(observer);
	}

	private void updateConnectionAddedObservers(Connection connection) {
		for (ConnectionObserver observer : connectionObservers) {
			observer.onConnectionAdded(connection);
		}
	}

	private void updateConnectionRemovedObservers(Connection connection) {
		for (ConnectionObserver observer : connectionObservers) {
			observer.onConnectionRemoved(connection);
		}
	}

	// Candidate connection methods
	private static void createConnectionOffsets() {
		connectionOffsets.add(new Point(1, 2));
		connectionOffsets.add(new Point(2, 1));
		connectionOffsets.add(new Point(2, -1));
		connectionOffsets.add(new Point(1, -2));
		connectionOffsets.add(new Point(-1, -2));
		connectionOffsets.add(new Point(-2, -1));
		connectionOffsets.add(new Point(-2, 1));
		connectionOffsets.add(new Point(-1, 2));
	}

	private List<Hole> fillCandidateHoles(Hole holeA, Hole holeB, PlayerID player) {
		List<Hole> candidateHoles = initCandidateHoles(holeA, holeB);
		filterOutCandidates(player, candidateHoles);

		return candidateHoles;
	}

	private List<Hole> initCandidateHoles(Hole holeA, Hole holeB) {
		List<Hole> candidateHoles = new ArrayList<Hole>();

		// add first two candidate holes by using x- or y- coord from other hole
		candidateHoles.add(board.getHole(holeA.getLocation().x, holeB.getLocation().y));
		candidateHoles.add(board.getHole(holeB.getLocation().x, holeA.getLocation().y));

		// add last two candidate holes
		addPossibleCandidates(holeA, holeB, candidateHoles);

		return candidateHoles;
	}

	private void addPossibleCandidates(Hole holeA, Hole holeB, List<Hole> candidateHoles) {
		int xIncrement = holeB.getLocation().x - holeA.getLocation().x;
		int yIncrement = holeB.getLocation().y - holeA.getLocation().y;

		Point firstDiagonal = initFirstDiagonalPoint(holeB, xIncrement, yIncrement);
		if (board.isWithinBounds(firstDiagonal)) {
			candidateHoles.add(board.getHole(firstDiagonal));
		}

		Point secondDiagonal = initSecondDiagonalPoint(holeB, xIncrement, yIncrement);
		if (board.isWithinBounds(secondDiagonal)) {
			candidateHoles.add(board.getHole(secondDiagonal));
		}
	}

	private Point initFirstDiagonalPoint(Hole hole, int xIncrement, int yIncrement) {
		int firstDiagonalX = hole.getLocation().x - Integer.signum(xIncrement);
		int firstDiagonalY = hole.getLocation().y - Integer.signum(yIncrement);
		Point firstDiagonal = new Point(firstDiagonalX, firstDiagonalY);
		return firstDiagonal;
	}

	private Point initSecondDiagonalPoint(Hole hole, int xIncrement, int yIncrement) {
		int secondDiagonalX = hole.getLocation().x;
		int secondDiagonalY = hole.getLocation().y;
		if (Math.abs(xIncrement) == 2) {
			secondDiagonalX -= Integer.signum(xIncrement);
		} else {
			secondDiagonalY -= Integer.signum(yIncrement);
		}
		Point secondDiagonal = new Point(secondDiagonalX, secondDiagonalY);
		return secondDiagonal;
	}

	private void filterOutCandidates(PlayerID player, List<Hole> candidateHoles) {
		Iterator<Hole> iter = candidateHoles.iterator();
		while (iter.hasNext()) {
			Hole hole = iter.next();
			if (hole.canPlacePeg()) {
				iter.remove();
			} else {
				Peg peg = hole.getPeg();
				if (peg.getOwningPlayer() == player) {
					iter.remove();
				}
			}
		}
	}

	// Checking methods
	private boolean isValidConnection(Hole hole, Point point) {
		if (hasNeighbour(hole, point)) {
			Hole neighbourHole = board.getHole(point);
			Connection connection = new Connection(hole, neighbourHole, hole.getPeg()
					.getOwningPlayer());

			if (connections.contains(connection)) {
				return false; // connection already exists
			}
			if (crossesOpponentConnection(connection)) {
				return false;
			}
			return true;
		}
		return false;
	}

	private Boolean hasNeighbour(Hole hole, Point point) {
		if (board.isWithinBounds(point)) {
			Hole neighbour = board.getHole(point);
			PlayerID owner = hole.getPeg().getOwningPlayer();
			PlayerID neighbourOwner = neighbour.getPeg().getOwningPlayer();
			return owner == neighbourOwner;
		}
		return false;
	}

	boolean crossesOpponentConnection(Connection connection) {
		PlayerID player = connection.getOwningPlayer();
		Hole holeA = connection.getHoleA();
		Hole holeB = connection.getHoleB();
		List<Hole> candidateHoles = fillCandidateHoles(holeA, holeB, player);

		for (Hole candidate : candidateHoles) {
			if (connectionCrossesHole(connection, candidate)) {
				return true;
			}
		}
		return false;
	}

	private boolean connectionCrossesHole(Connection connection, Hole candidate) {
		Set<Connection> candidateConnections = candidate.getNeighbours();

		for (Connection candidateConnection : candidateConnections) {
			if (connection.getOwningPlayer() == candidateConnection.getOwningPlayer()) {
				continue;
			} else if (connection.crosses(candidateConnection)) {
				return true;
			}
		}
		return false;
	}

	// Getters
	Set<Connection> getConnections() {
		return connections;
	}

	List<Point> getConnectionOffsets() {
		return connectionOffsets;
	}

	// Other methods
	void updateConnections(Point point) {
		for (Point connectionOffset : connectionOffsets) {
			int neighbourX = point.x + connectionOffset.x;
			int neighbourY = point.y + connectionOffset.y;
			Point neighbourPoint = new Point(neighbourX, neighbourY);

			Hole hole = board.getHole(point);
			if (isValidConnection(hole, neighbourPoint)) {
				addConnection(hole, neighbourPoint);
			}
		}
	}

	void updateRemovedConnections(Point point, PlayerID owningPlayer) {
		for (Point connectionOffset : connectionOffsets) {
			int neighbourX = point.x + connectionOffset.x;
			int neighbourY = point.y + connectionOffset.y;
			Point neighbourPoint = new Point(neighbourX, neighbourY);
			Hole holeA = board.getHole(point);
			Hole holeB = board.getHole(neighbourPoint);
			Connection connection = new Connection(holeA, holeB, owningPlayer);

			if (connections.contains(connection)) {
				removeConnection(connection);
			}
		}
	}

	private void addConnection(Hole hole, Point point) {
		Hole neighbourHole = board.getHole(point);
		Connection connection = new Connection(hole, neighbourHole, hole.getPeg()
				.getOwningPlayer());
		connections.add(connection);
		hole.getNeighbours().add(connection);
		neighbourHole.getNeighbours().add(connection);
		updateConnectionAddedObservers(connection);
	}

	void removeConnection(Connection connection) {
		connection.getHoleA().getNeighbours().remove(connection);
		connection.getHoleB().getNeighbours().remove(connection);
		if (connections.remove(connection)) {
			updateConnectionRemovedObservers(connection);
		}
	}

}
