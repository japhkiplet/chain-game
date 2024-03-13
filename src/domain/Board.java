package domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.awt.Point;

import ui.DialogProvider;
import application.ChainGameController;
import domain.Grid;
import domain.Hole;

public class Board {

	private static final int CORNERS_OFFSET = 4;
	private Set<PegObserver> pegObservers = new HashSet<PegObserver>();	

	protected final Grid grid;
	private Map<Point, Hole> holes = new HashMap<>();
	private ConnectionHelper connectionHelper = new ConnectionHelper(this);
	private MoveTracker moveTracker = new MoveTracker();

	// Constructor
	public Board(int numColumns, int numRows) {
		this.grid = initGrid(numColumns, numRows);
		initHoles();
	}
	// Initiation Methods
	private Grid initGrid(int numColumns, int numRows) {
		return new Grid(numColumns, numRows);
	}
	private void initHoles() {
		for (Point point : grid) {
			holes.put(point, new Hole(point));
		}
	}

	// Observer Methods
	public void registerPegObserver(PegObserver observer) {
		pegObservers.add(observer);
	}
	private void updatePegObservers(Point pegPoint, PlayerID owningPlayerID) {
		for (PegObserver observer : pegObservers) {
			observer.onPegClicked(pegPoint, owningPlayerID);
		}
	}

	private void updatePegRemovedObservers(Point pegPoint, PlayerID owningPlayerID) {
		for(PegObserver observer : pegObservers) {
			observer.onPegRemoved(pegPoint, owningPlayerID);
		}
	}

	public void registerConnectionObserver(ConnectionObserver observer) {
		connectionHelper.registerConnectionObserver(observer);
	}

	// Getters
	public int getNumColumns() {
		return grid.getNumColumns();
	}
	public int getNumRows() {
		return grid.getNumRows();
	}
	/**
	 * 
	 * @return the number of Holes on the board
	 */
	public int getNumHoles() {
		int numHolesOnBoard = (getNumColumns() * getNumRows()) - CORNERS_OFFSET;
		return numHolesOnBoard;
	}
	/**
	 * 
	 * @param x
	 * @param y
	 * @return the hole from the given x and y coordinates
	 */
	public Hole getHole(int x, int y) {
		Point point = new Point(x, y);
		return getHole(point);
	}
	/**
	 * 
	 * @param point
	 * @return the hole from the given point
	 */
	public Hole getHole(Point point) {
		return holes.getOrDefault(point, Hole.offBoardInstance());
	}

	/**
	 * 
	 * @return the set of all connections on the board
	 */

	public Set<Connection> getConnections() {
		return connectionHelper.getConnections();
	}

	/**
	 * 
	 * @return the amount of moves/pegs placed on the board
	 */
	public int numPegsPlaced() {
		return moveTracker.numMoves();
	}

	// Checking Methods
	/**
	 * checks whether a move is possible on current board
	 * @param move
	 * @return true if move is possible
	 */
	public boolean isValidMove(PlayerMove move) {
		Point point = move.getCoordinates();
		Hole hole = getHole(point);
		PlayerID playerID = move.getPlayerId();

		if (!isOpponentEdge(point, playerID)) {
			Boolean isValidMove = hole.canPlacePeg();
			if (isValidMove) {
				return true;
			}
		}
		return false;
	}
	private boolean isOpponentEdge(Point point, PlayerID player) {
		int x = point.x;
		int y = point.y;
		if (player.getValue() == 0) {
			return isTopBottom(x, y);
		} else {
			return isLeftRight(x, y);
		}
	}
	private boolean isTopBottom(int x, int y) {
		return (x != 0 || x != (getNumColumns() - 1))
				&& (y == 0 || y == (getNumRows() - 1));
	}
	private boolean isLeftRight(int x, int y) {
		return (y != 0 || y != (getNumRows() - 1))
				&& (x == 0 || x == (getNumColumns() - 1));
	}
	/**
	 * checks whether a point is located within the board
	 * @param point
	 * @return true if the point is located within the board
	 */
	public boolean isWithinBounds(Point point) {
		int x = point.x;
		int y = point.y;
		boolean isWithinXBounds = x >= 0 && x < getNumColumns();
		boolean isWithinYBounds = y >= 0 && y < getNumRows();
		return isWithinXBounds && isWithinYBounds;
	}

	/**
	 * checks if the current move will create a connection that
	 * will intersect with an existing opponent's connection
	 * @param connection
	 * @return true is the move will create an intersecting connection
	 */

	public boolean crossesOpponentConnection(Connection connection) {
		return connectionHelper.crossesOpponentConnection(connection);
	}

	/**
	 * 
	 * @return a list of knight's position coordinates
	 */

	public List<Point> getConnectionOffsets() {
		return connectionHelper.getConnectionOffsets();
	}

	// Other
	/**
	 * execute and update the board/observers with the given move 
	 * @param move
	 */
	public void performMove(PlayerMove move) {
		if (PlayerMove.isPlacementMove(move)) {
			performPlacementMove(move);
		} else if (PlayerMove.isUndoMove(move)) {
			performUndoMove(move);
		} else {
			throw new RuntimeException("Unsupported move type.");
		}
	}

	private void performPlacementMove(PlayerMove move) {
		Point point = move.getCoordinates();
		PlayerID playerID = move.getPlayerId();
		Hole hole = getHole(point);
		hole.placePeg(move.getPlayerId());
		connectionHelper.updateConnections(point);
		moveTracker.addMove(move);
		updatePegObservers(point, playerID);
	}
	/**
	 * undo the last turn on the board
	 * remove the most recent move from both players and update board/observers
	 */
	private void performUndoMove(PlayerMove move) {
		if (move.getMoveType() == PlayerMoveType.UNDO_REQUEST) {
			DialogProvider
					.displayInformationDialog("Awaiting undo request processing.");
			ChainGameController.getInstance().getNextPlayer().queueUndoRequest();
		} else if (move.getMoveType() == PlayerMoveType.UNDO_ACCEPT) {
			System.out.println("Undo accepted.");
			DialogProvider.displayInformationDialog("Undo request Accepted.");
			undoMove();
		} else if (move.getMoveType() == PlayerMoveType.UNDO_DECLINE) {
			DialogProvider.displayInformationDialog("Undo request Declined.");
		}
	}

	protected void undoMove() {
		for (int i = 0; i < 2; ++i) {
			PlayerMove moveToUndo = moveTracker.removeMove();
			if (moveToUndo != null) {
				Point point = moveToUndo.getCoordinates();
				PlayerID playerID = moveToUndo.getPlayerId();

				Hole hole = getHole(point);
				hole.removePeg();
				connectionHelper.updateRemovedConnections(point, playerID);
				updatePegRemovedObservers(point, playerID);
			}
		}
	}

	/**
	 * checks if the most recent move can be undone
	 * @return whether there exists a most recent move
	 */
	public boolean canUndoMove() {
		return (numPegsPlaced() > 1);
	}

	/**
	 * 
	 * takes a set as param and uses DFS to fill the set 
	 * with all the pegs the given hole is interconnected with
	 * @param hole
	 * @param allConnectedPegs
	 */

	public void getAllConnectedPegs(Hole hole, Set<Hole> allConnectedPegs) {
		allConnectedPegs.add(hole);
		for (Connection connection : hole.getNeighbours()) {
			Hole holeA = connection.getHoleA();
			Hole holeB = connection.getHoleB();
			if (!allConnectedPegs.contains(holeA)) {
				allConnectedPegs.add(holeA);
				getAllConnectedPegs(holeA, allConnectedPegs);
			}
			if (!allConnectedPegs.contains(holeB)) {
				allConnectedPegs.add(holeB);
				getAllConnectedPegs(holeB, allConnectedPegs);
			}
		}
	}

	/**
	 * proposed easter egg to allow players to remove their own connections
	 * permanently from the board (incomplete)
	 * @param startPoint
	 * @param endPoint
	 * @param owningPlayer
	 */
	public void removeConnection(Point startPoint, Point endPoint, PlayerID owningPlayer) {
		//Connection connection = new Connection(startP, endPoint, owningPlayer);
		//connectionHelper.removeConnection(connection);
		
	}	

}