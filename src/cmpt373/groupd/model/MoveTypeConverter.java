package cmpt373.groupd.model;

import domain.PlayerMoveType;

public class MoveTypeConverter {

	private MoveTypeConverter(){
		
	}
	
	public static PlayerMoveType convertToNative(MoveType type){
		switch(type){
			case AddPeg:
				return PlayerMoveType.L_MOVE;
			case RequestUndo:
				return PlayerMoveType.UNDO_REQUEST;
			case AcceptUndo:
				return PlayerMoveType.UNDO_ACCEPT;
			case DeclineUndo:
				return PlayerMoveType.UNDO_DECLINE;
			default:
				return PlayerMoveType.UNKNOWN;
		}
	}
	
	public static MoveType convertFromNative(PlayerMoveType type){
		switch(type){
		case L_MOVE:
			return MoveType.AddPeg;
		case UNDO_REQUEST:
			return MoveType.RequestUndo;
		case UNDO_ACCEPT:
			return MoveType.AcceptUndo;
		case UNDO_DECLINE:
			return MoveType.DeclineUndo;
		default:
			throw new RuntimeException("Unable to convert move type: "+type);
		}
	}
}
