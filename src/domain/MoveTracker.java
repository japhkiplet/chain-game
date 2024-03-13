package domain;

import java.util.Deque;
import java.util.ArrayDeque;

public class MoveTracker {
	private Deque<PlayerMove> moves = new ArrayDeque<PlayerMove>();
	
	
	public void addMove(PlayerMove move) {
		moves.addFirst(move);
	}
	
	public PlayerMove removeMove() {
		return moves.pollFirst();
	}
	
	public int numMoves() {
		return moves.size();
	}

}
