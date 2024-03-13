package domain;

import ui.GameBoardScene;

public class HumanPlayer extends Player {
	private boolean allowMoves = false;
	private Object syncObj = new Object();
	private PlayerMove nextMove = PlayerMove.nullInstance();

	/**
	 * Constructs Human Player
	 * 
	 * @param name - player name
	 * @param id - player ID
	 */
	public HumanPlayer(String name, PlayerID id) {
		super(name, id);
	}

	/**
	 * Aborts the wait for the next move
	 */
	@Override
	public void abortWait() {
		notifyOfMove();
	}

	/**
	 * Sets a move based on the game board mouse click location
	 * 
	 * @param point - (x,y) location on the game board
	 */
	@Override
	public void injectMove(PlayerMove move) {
		if (allowMoves) {
			nextMove = move;
			notifyOfMove();
		}
	}

	/**
	 * Waits until a player makes his move, and then returns it
	 */
	@Override
	public PlayerMove getNextMove() {
		if(this.containsRequest()){
			this.clearQueue();
			boolean acceptUndo = GameBoardScene.showUndoReqDialog();
			if(acceptUndo){
				System.out.println("Sending undo acceptance.");
				return new UndoPlayerMove(PlayerMoveType.UNDO_ACCEPT,getID());
			}else{
				System.out.println("Sending undo decline.");
				return new UndoPlayerMove(PlayerMoveType.UNDO_DECLINE,getID());
			}
		}
		nextMove = PlayerMove.nullInstance();
		allowMoves = true;
		waitForMove();
		allowMoves = false;

		return nextMove;
	}

	@Override
	public String toString() {
		return super.toString() + ", Allows Moves: " + allowMoves + ", Last move: "
				+ nextMove.toString();
	}

	private void waitForMove() {
		synchronized (syncObj) {
			try {
				syncObj.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void notifyOfMove() {
		synchronized (syncObj) {
			syncObj.notify();
		}
	}

}
