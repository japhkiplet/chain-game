package ai;

import domain.Board;
import domain.PlayerID;
import domain.PlayerMove;
import ai.AIMoves;
import application.UndoObserver;

public class AILogic implements UndoObserver{
	private AIMoves moves;
	private AIBoardChecker moveChecker;
	private MoveFinder moveFinder;
	private MoveLookAhead lookAhead;
	private long MIN_MOVE_TIME_MS = 500;

	public AILogic(Board newBoard, PlayerID newPlayer) {
		moves = new AIMoves();
		moveChecker = new AIBoardChecker(newBoard);
		moveFinder = new MoveFinder(newBoard, newPlayer);
		lookAhead = new MoveLookAhead(moves, moveChecker, moveFinder);
	}
	
	/**
	 * Calculates the best move for the current player.
	 * Starts by picking a random peg along its edge of the board,
	 * then starts moving towards the opposite edge.
	 * @param playerID 
	 * 
	 * @return aiMove
	 */
	public PlayerMove calculateMove() {
		PlayerMove aiMove = PlayerMove.nullInstance();
		long timeToMakeMove = System.currentTimeMillis()+MIN_MOVE_TIME_MS;
		
		if (isFirstMove()) {
			aiMove = moveFinder.getValidPegOnEdge();
		} else {
			aiMove = lookAhead.moveLookahead();
		}
		moves.addMove(aiMove);
		
		waitForMinimumMoveTime(timeToMakeMove);
		
		return aiMove;
	}

	@Override
	public void onUndoEvent() {
		moves.onUndoEvent();
	}

	private void waitForMinimumMoveTime(long timeToMakeMove) {
		do {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (System.currentTimeMillis() < timeToMakeMove);
	}
	
	boolean isFirstMove() {
		return moves.getNumMoves() < 1;
	}
}