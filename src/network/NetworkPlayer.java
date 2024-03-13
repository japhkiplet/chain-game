package network;

import java.util.logging.Level;
import java.util.logging.Logger;
import domain.Player;
import domain.PlayerID;
import domain.PlayerMove;

public class NetworkPlayer extends Player{
	
	private Logger logger = NetworkLogger.getInstance();
	private NetworkAdapter networkAdapter = null;
	
	/**
	 * Constructs a Network Player
	 * @param name - player name
	 * @param playerID - player ID
	 * @param networkManager - reference to a network manager
	 */
	public NetworkPlayer(String name, PlayerID playerID, NetworkAdapter networkAdapter) {
		super(name, playerID);
		this.networkAdapter = networkAdapter;
	}

	@Override
	public PlayerMove getNextMove() throws TransmissionException {
		PlayerMove move = networkAdapter.receiveMove();
		logger.log(Level.INFO, "Move Received!" + move.toString());
		return move;
	}

	@Override
	public void injectMove(PlayerMove move) {
		//Do nothing
	}

	@Override
	public void abortWait() {
		//Do nothing
	}

}