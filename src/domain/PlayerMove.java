package domain;

import java.awt.Point;
import java.io.Serializable;

public class PlayerMove implements Serializable{

	private static final long serialVersionUID = -4052228490672046227L;

	private static PlayerMove NULL_INSTANCE = new PlayerMove(new Point(Integer.MIN_VALUE,
			Integer.MIN_VALUE), PlayerID.UNDEFINED);

	private PlayerMoveType moveType = PlayerMoveType.UNKNOWN;
	private Point coordinates = null;
	private PlayerID playerID;

	public PlayerMove(Point coordinates, PlayerID playerID) {
		this(PlayerMoveType.L_MOVE, coordinates, playerID);
	}

	public PlayerMove(PlayerMoveType moveType, Point coordinates, PlayerID playerID) {
		this.moveType = moveType;
		this.coordinates = coordinates;
		this.playerID = playerID;
	}
	
	public PlayerMoveType getMoveType(){
		return moveType;
	}
	
	public int getX() {
		return (int) coordinates.getX();
	}

	public int getY() {
		return (int) coordinates.getY();
	}

	public PlayerID getPlayerId() {
		return playerID;
	}

	public static PlayerMove nullInstance() {
		return NULL_INSTANCE;
	}

	public Point getCoordinates() {
		return coordinates;
	}

	
	@Override
	public String toString() {
		return "PlayerMove {"
				+ "PlayerID: " + playerID
				+ ", Coordinates: " + coordinates.toString()
				+ "}";
	}

	public static boolean isUndoMove(PlayerMove move){
		PlayerMoveType moveType = move.getMoveType();
		if(moveType == PlayerMoveType.UNDO_ACCEPT ||
				moveType == PlayerMoveType.UNDO_DECLINE ||
				moveType == PlayerMoveType.UNDO_REQUEST){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isPlacementMove(PlayerMove move){
		PlayerMoveType moveType = move.getMoveType();
		if(moveType == PlayerMoveType.L_MOVE){
			return true;
		}else{
			return false;
		}
	}
	
}