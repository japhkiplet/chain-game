package network;

import java.util.logging.Level;
import java.util.logging.Logger;

import nativeProtocol.Ack;
import nativeProtocol.GameBoardParameters;
import nativeProtocol.MoveNtv;
import nativeProtocol.NewGameRequest;
import domain.Board;
import domain.PlayerMove;
import domain.Settings;

public class NativeAdapter implements NetworkAdapter{

	private Logger logger = NetworkLogger.getInstance();
	private NetworkManager networkManager = null;
	
	public NativeAdapter(NetworkManager networkManager){
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
		Board gameBoard = new VirtualBoard(Settings.getInstance().getNumColumns(),
				Settings.getInstance().getNumRows(), this);
		
		networkManager.receiveObject(NewGameRequest.class);
		networkManager.transmitObject(new GameBoardParameters(Settings
				.getInstance().getNumColumns(), Settings.getInstance()
				.getNumRows()));
		logger.log(Level.INFO,"Board Params Sent!");
		
		networkManager.receiveObject(Ack.class);

		logger.log(Level.INFO,"Ack Received!");
		return gameBoard;
	}

	private Board initAsClient() {
		networkManager.transmitObject(new NewGameRequest());
		
		GameBoardParameters params;
		params = networkManager
					.receiveObject(GameBoardParameters.class);
		
		logger.log(Level.INFO,"Board Params Received!");

		Settings.getInstance().setNumColumns(params.getWidth());
		Settings.getInstance().setNumRows(params.getHeight());

		Board gameBoard = new VirtualBoard(params.getWidth(), params.getHeight(),
				this);

		networkManager.transmitObject(new Ack());
		logger.log(Level.INFO,"Ack sent!");
		
		return gameBoard;
	}

	@Override
	public void sendMove(PlayerMove move) {
		networkManager.transmitObject(new MoveNtv(move));
	}

	@Override
	public PlayerMove receiveMove() {
		return networkManager.receiveObject(MoveNtv.class).getPlayerMove();
	}

}
