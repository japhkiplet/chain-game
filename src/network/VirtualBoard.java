package network;

import java.util.logging.Level;
import java.util.logging.Logger;

import ui.DialogProvider;
import application.ChainGameController;
import application.ChainGameFacade;
import domain.Board;
import domain.PlayerMove;
import domain.PlayerMoveType;

public class VirtualBoard extends Board{

	private Logger logger = NetworkLogger.getInstance();
	private NetworkAdapter networkAdapter = null;
	
	/**
	 * A game board distributed between a host and a client
	 * @param width - board width
	 * @param height - board height
	 * @param networkManager - network manager reference
	 * @param config - player network configuration (Host or Client)
	 */
	public VirtualBoard(
			int width, 
			int height,
			NetworkAdapter networkAdapter
			){
		super(width,height);
		this.networkAdapter = networkAdapter;
	}
	
	@Override
	public void performMove(PlayerMove move) {
		if (PlayerMove.isPlacementMove(move)) {
			performPlacementMove(move);
		} else if (PlayerMove.isUndoMove(move)) {
			performUndoMove(move);
		} else {
			throw new RuntimeException("Unsupported move type.");
		}
	}
	
	private void performPlacementMove(PlayerMove move) {
		super.performMove(move);
		if(!ChainGameFacade.isNetworkPlayer(move.getPlayerId())){
			logger.log(Level.INFO, "Sending move" + move.getCoordinates());
			networkAdapter.sendMove(move);
		}
	}

	private void performUndoMove(PlayerMove move) {
		if (move.getMoveType() == PlayerMoveType.UNDO_REQUEST) {
			if(ChainGameFacade.isNetworkPlayer(move.getPlayerId())){
				ChainGameController.getInstance().getNextPlayer().queueUndoRequest();
			}else{
				DialogProvider
					.displayInformationDialog("Awaiting undo request processing.");
				logger.log(Level.INFO, "Sending remote undo request");
				networkAdapter.sendMove(move);
			}
			
		} else if (move.getMoveType() == PlayerMoveType.UNDO_ACCEPT) {
			if(ChainGameFacade.isNetworkPlayer(move.getPlayerId())){
				DialogProvider.displayInformationDialog("Undo request Accepted.");
			}else{
				logger.log(Level.INFO, "Sending remote undo acceptence");
				networkAdapter.sendMove(move);
			}
			System.out.println("Undo accepted.");
			undoMove();
			
		} else if (move.getMoveType() == PlayerMoveType.UNDO_DECLINE) {
			if(ChainGameFacade.isNetworkPlayer(move.getPlayerId())){
				DialogProvider.displayInformationDialog("Undo request Declined.");
			}else{
				logger.log(Level.INFO, "Sending remote undo decline");
				networkAdapter.sendMove(move);
			}
		}
	}
}