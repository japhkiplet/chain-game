package ai;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import application.UndoObserver;
import domain.Board;
import domain.PlayerID;
import domain.PlayerMove;

public class AIMoves implements UndoObserver {
	private List<PlayerMove> lastMoves = new ArrayList<PlayerMove>();

	public AIMoves() {
	}
	
	public AIMoves(PlayerID thisPlayer, Board myBoard) {
	}

	ListIterator<PlayerMove> getMovesIterator(int loc) {
		return lastMoves.listIterator(loc);
	}
	
	int getNumMoves() {
		return lastMoves.size();
	}
	
	PlayerMove getLastMove() {
		return getMove(getNumMoves()-1);
	}
	
	PlayerMove getMove(int index) {
		return lastMoves.get(index);
	}

	void addMove(PlayerMove move) {
		lastMoves.add(move);
	}
	
	void removeLastMove() {
		lastMoves.remove(getNumMoves()-1);
	}

	@Override
	public void onUndoEvent() {
		removeLastMove();		
	}
}