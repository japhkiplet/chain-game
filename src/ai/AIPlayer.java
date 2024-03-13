package ai;

import domain.Player;
import domain.PlayerID;
import domain.PlayerMove;
import domain.PlayerMoveType;
import domain.UndoPlayerMove;
import ai.AILogic;
import application.ChainGameFacade;;

public class AIPlayer extends Player {
	private PlayerMove nextMove = PlayerMove.nullInstance();
	private AILogic logic;

	public AIPlayer(String name, PlayerID id) {
		super(name, id);
		logic = new AILogic(ChainGameFacade.getBoard(), id);
		nextMove = PlayerMove.nullInstance();
	}

	@Override
	public PlayerMove getNextMove() {
		if(this.containsRequest()){
			this.clearQueue();
			logic.onUndoEvent();
			return new UndoPlayerMove(PlayerMoveType.UNDO_ACCEPT,getID());
		}
		nextMove = logic.calculateMove();
		return nextMove;
	}

	@Override
	public void injectMove(PlayerMove move) {
		// TODO Auto-generated method stub
	}

	@Override
	public void abortWait() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public String toString() {
		return super.toString() + ", Last move: "
				+ nextMove.toString();
	}
}