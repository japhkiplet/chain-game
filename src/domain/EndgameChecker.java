package domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import application.ChainGameFacade;
import application.EndgameObserver;
import application.TurnObserver;

public class EndgameChecker implements TurnObserver {
	private List<EndgameObserver> endObservers = new ArrayList<>();
	private static final EndgameChecker INSTANCE = new EndgameChecker();
	private Board boardToCheck;

	private EndgameChecker() {
		boardToCheck = ChainGameFacade.getBoard();
		ChainGameFacade.registerTurnObserver(this);
	}

	/**
	 * Checks if the game is over (i.e. a player has connected their opposing
	 * sides of the board successfully). This function relies on the fact that
	 * players cannot place pegs on the extreme edges of the board on their
	 * opponent's sides.
	 * 
	 * @return The winning player's ID
	 * 			UNDEFINED == no winner
	 * 			TIE == tie game
	 */
	public PlayerID isGameOver() {
		PlayerID winner = PlayerID.UNDEFINED;
		
		if(hasHorizontalEndConnection()) {
			winner = PlayerID.PLAYER_A;
		} else if (hasVerticalEndConnection()) {
			winner = PlayerID.PLAYER_B;
		} else if(boardToCheck.getNumHoles() <= boardToCheck.numPegsPlaced()) {
			winner = PlayerID.TIE;
		} else {
			winner = PlayerID.UNDEFINED;
		}
		if (winner != PlayerID.UNDEFINED) {
			updateEndgameObservers(winner);
		}
		return winner;
	}

	private boolean hasHorizontalEndConnection() {
		for(int y = 0; y < boardToCheck.getNumRows(); y++) {
			Hole hole = boardToCheck.getHole(0, y);
			if(hole.hasPeg()) {
				Set<Hole> allConnectedPegs = getConnectedPegs(hole);
				for(Hole scannedHole : allConnectedPegs) {
					if(hasHorizontalEndConnection(scannedHole)) {
						return true;
					}
				}	
			}
		}
		return false;
	}
	private boolean hasVerticalEndConnection() {
		for(int x = 0; x < boardToCheck.getNumColumns(); x++) {
			Hole hole = boardToCheck.getHole(x, 0);
			if(hole.hasPeg()) {
				Set<Hole> allConnectedPegs = getConnectedPegs(hole);
				for(Hole scannedHole : allConnectedPegs) {
					if(hasVerticalEndConnection(scannedHole)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	private Set<Hole> getConnectedPegs(Hole hole) {
		Set<Hole> allConnectedPegs = new HashSet<Hole>();
		boardToCheck.getAllConnectedPegs(hole, allConnectedPegs);
		return allConnectedPegs;
	}
	private boolean hasVerticalEndConnection(Hole scannedHole) {
		int numRows = boardToCheck.getNumRows() - 1;
		return scannedHole.getLocation().y == numRows;
	}
	private boolean hasHorizontalEndConnection(Hole scannedHole) {
		int numColumns = boardToCheck.getNumColumns() - 1;
		return scannedHole.getLocation().x == numColumns;
	}
	
	public void registerEndgameObserver(EndgameObserver observer) {
		endObservers.add(observer);
	}
	private void clearEndgameObservers() {
		endObservers.clear();
	}
	private void updateEndgameObservers(PlayerID winner) {
		for (EndgameObserver listener : endObservers) {
			listener.onGameWonEvent(winner);
		}
	}
	public static EndgameChecker getInstance() {
		return INSTANCE;
	}
	
	/**
	 * reset game state
	 */
	public void reset() {
		boardToCheck = ChainGameFacade.getBoard();
		clearEndgameObservers();
	}
	/**
	 * checks if game is over
	 */
	@Override
	public void onTurnEndEvent() {
		isGameOver();
	}
}