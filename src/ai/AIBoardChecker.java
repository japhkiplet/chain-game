package ai;

import domain.Board;
import domain.Connection;
import domain.PlayerMove;

public class AIBoardChecker {
	private Board myBoard;
	
	public AIBoardChecker(Board newBoard) {
		this.myBoard = newBoard;
	}

	boolean isPegOnGoalEdge(PlayerMove playerMove, DirectionOfTravel direction) {
		switch (direction) {
		case UP:
			if (playerMove.getY() == 0) return true;
			break;
		case DOWN:
			if (playerMove.getY() == myBoard.getNumRows()-1) return true;
			break;
		case LEFT:
			if (playerMove.getX() == 0) return true;
			break;
		case RIGHT:
			if (playerMove.getX() == myBoard.getNumColumns()-1) return true;
			break;
		}
		return false;
	}

	boolean checkMoveIsGood(PlayerMove lastMove, PlayerMove newMove) {
		Connection newConnection = new Connection(
				myBoard.getHole(newMove.getCoordinates()),
				myBoard.getHole(lastMove.getCoordinates()),
				lastMove.getPlayerId());
		if (myBoard.isValidMove(newMove) &&
				!myBoard.crossesOpponentConnection(newConnection)) {
			return true;
		}
		return false;
	}

	public int checkPegWeighting(PlayerMove move) {
		int pegWeight = 2;
		if (nearEdgeOfBoard(move)) {
			pegWeight = 3;
		} else if (inMiddleOfBoard(move)) {
			pegWeight = 1;
		}
		return pegWeight;
	}

	private boolean inMiddleOfBoard(PlayerMove move) {
		int boardWidth = myBoard.getNumColumns();
		int boardHeight = myBoard.getNumColumns();
		// Move is within the middle 1/3 of the board
		if (move.getX() < (boardWidth-(boardWidth/3)) &&
				move.getX() > (boardWidth/3) &&
				move.getY() < (boardHeight-(boardHeight/3)) &&
				move.getY() > (boardHeight/3)) {
			return true;
		}
		return false;
	}

	private boolean nearEdgeOfBoard(PlayerMove move) {
		int boardWidth = myBoard.getNumColumns();
		int boardHeight = myBoard.getNumRows();
		// Move is within the outer 1/4 of the board
		if ((move.getX() < (boardWidth/4) &&
				move.getX() > 0) ||
				(move.getX() < boardWidth-1) &&
				move.getX() > (boardWidth-(boardWidth/4))) {
			return true;
		} else if ((move.getY() < (boardHeight/4) &&
				move.getY() > 0) ||
				(move.getY() < boardHeight-1) &&
				move.getY() > (boardHeight-(boardHeight/4))) {
			return true;
		}
		return false;
	}
}