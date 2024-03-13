package domain;

import java.awt.Point;

public class UndoPlayerMove extends PlayerMove{

	private static final long serialVersionUID = -3193224797503226752L;

	/**
	 * The move generated during undo operations
	 * @param moveType - the type of undo move
	 * @param playerId - the ID of the player making the move
	 * @throws UndoPlayerMoveException - on all coordinate related operations
	 */
	public UndoPlayerMove(PlayerMoveType moveType, PlayerID playerId){
		super(moveType, new Point(0,0), playerId);
	}

	@Override
	public int getX() {
		throw new UndoPlayerMoveException();
	}

	@Override
	public int getY() {
		throw new UndoPlayerMoveException();
	}

	@Override
	public Point getCoordinates() {
		throw new UndoPlayerMoveException();
	}
}
