package network;

import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.ChainGameFacade;
import cmpt373.groupd.model.Move;
import cmpt373.groupd.model.MoveType;
import cmpt373.groupd.model.MoveTypeConverter;
import domain.Board;
import domain.PlayerMove;
import domain.Settings;
import domain.UndoPlayerMove;

public class CompatibilityAdapter implements NetworkAdapter{

	private Logger logger = NetworkLogger.getInstance();
	private NetworkManager networkManager = null;
	
	public CompatibilityAdapter(NetworkManager networkManager){
		if(networkManager == null){
			throw new IllegalArgumentException();
		}
		
		this.networkManager = networkManager;
	}
	
	@Override
	public Board initialize() {
		Settings.NetworkConfig config = Settings.getInstance().getNetworkConfig();
		if (config == Settings.NetworkConfig.CLIENT) {
			return initAsClient();
		} else if (config == Settings.NetworkConfig.HOST) {
			return initAsHost();
		} else {
			throw new RuntimeException("Unsupported network configuration.");
		}
	}
	
	private Board initAsHost() {
		logger.log(Level.INFO,"Waiting for a game request.");
		Move move = networkManager.receiveObjectUnwrapped(Move.class);
		
		if(move.getMoveType() != MoveType.NewGame){
			throw new SynchronizationException("Invalid Move Type.");
		}
		
		logger.log(Level.INFO,"New Game received.");
		
		Point boardSize = new Point(
				Settings.getInstance().getNumColumns(),
				Settings.getInstance().getNumRows()
				);
		
		networkManager.transmitObjectUnwrapped(new Move(MoveType.AcceptNewGame, boardSize));
		logger.log(Level.INFO,"Accept New Game sent.");
		logger.log(Level.INFO,"Initializing board.");
		return new VirtualBoard(
				boardSize.x,
				boardSize.y,
				this
				);
	}

	private Board initAsClient() {
		networkManager.transmitObjectUnwrapped(new Move(MoveType.NewGame));
		logger.log(Level.INFO,"New Game sent.");
		
		Move move = networkManager.receiveObjectUnwrapped(Move.class);
		Point boardSize = move.getData();
		if(move.getMoveType() != MoveType.AcceptNewGame || boardSize.x <= 0 || boardSize.y <= 0){
			throw new SynchronizationException("Invalid Move Type.");
		}
		logger.log(Level.INFO,"Accept New Game received.");
		Settings.getInstance().setNumColumns(boardSize.x);
		Settings.getInstance().setNumRows(boardSize.y);

		
		logger.log(Level.INFO,"Initializing board.");
		return new VirtualBoard(
				boardSize.x,
				boardSize.y,
				this
				);
	}

	@Override
	public void sendMove(PlayerMove move) {
		logger.log(Level.INFO,"Sending remote move: "+ move.toString());
		
		if(PlayerMove.isPlacementMove(move)){
			networkManager.transmitObjectUnwrapped(new Move(
				MoveTypeConverter.convertFromNative(move.getMoveType()),
				move.getCoordinates()
				));
		}else if(PlayerMove.isUndoMove(move)){
			networkManager.transmitObjectUnwrapped(new Move(
					MoveTypeConverter.convertFromNative(move.getMoveType()),
					new Point(0,0)
					));
		}else{
			throw new RuntimeException("Unsuppoorted move type");
		}
		
		
	}

	@Override
	public PlayerMove receiveMove() {
		Move move = networkManager.receiveObjectUnwrapped(Move.class);
		logger.log(Level.INFO,"Receiving remote move: "+ move.getData().toString());
		MoveType moveType = move.getMoveType();
		
		if(moveType == MoveType.AddPeg){
			return new PlayerMove(
				MoveTypeConverter.convertToNative(move.getMoveType()),
				move.getData(),
				ChainGameFacade.getCurrentPlayer().getID());
		}else if(moveType == MoveType.RequestUndo || moveType == MoveType.AcceptUndo || moveType == MoveType.DeclineUndo){
			return new UndoPlayerMove(
					MoveTypeConverter.convertToNative(move.getMoveType()),
					ChainGameFacade.getCurrentPlayer().getID());
		}else{
			throw new RuntimeException("Unsuppoorted move type");
		}
	}

}
