package application;

import java.awt.Point;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ai.AIPlayer;
import javafx.concurrent.Task;
import network.CompatibilityAdapter;
import network.ConnectionEstablishmentException;
import network.NativeAdapter;
import network.NetworkAdapter;
import network.NetworkClientManager;
import network.NetworkHostManager;
import network.NetworkManager;
import network.NetworkPlayer;
import domain.Board;
import domain.GameMode;
import domain.HumanPlayer;
import domain.Player;
import domain.PlayerID;
import domain.PlayerMove;
import domain.PlayerMoveType;
import domain.Settings;

/**
 * To start the game: getInstance() followed by instance.startGame()
 */
public class ChainGameController {
	private static final int CONNECTION_TIMEOUT = 90000;
	private static final Logger LOGGER = Logger
			.getLogger(ChainGameController.class.getName());
	private static final ChainGameController INSTANCE = new ChainGameController();

	private Thread gameThread = null;
	private volatile Boolean runningState = false;
	private List<TurnObserver> turnObservers = new ArrayList<>();
	private PlayerController players;
	private Board gameBoard;

	private NetworkManager networkManager = null;
	private NetworkAdapter networkAdapter = null;

	private ChainGameController() {
		LOGGER.setLevel(Level.INFO);
	}

	private void initGameThread() {
		Task<Integer> gameTask = new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				gameLoop();
				return 0;
			}
		};
		gameThread = new Thread(gameTask);
		gameThread.setDaemon(false);
	}

	/**
	 * Starts a new game if a game is not already running. This will start the
	 * GameLoop thread.
	 * 
	 * @param mode
	 *            Defines what player types are used.
	 * @param width
	 *            The width of the board to create.
	 * @param height
	 *            The height of the board to create.
	 * @throws ConnectionEstablishmentException
	 * @throws UnknownHostException
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	synchronized void startGame(GameMode mode, int width, int height)
			throws SocketTimeoutException, UnknownHostException,
			ConnectionEstablishmentException {
		if (!isRunning()) {
			initGameThread();
			initGame();
			setRunning(true);
			gameThread.start();
		}
	}

	private void initGame() throws SocketTimeoutException,
			UnknownHostException, ConnectionEstablishmentException {
		switch (Settings.getInstance().getMode()) {
		case HUMAN_V_HUMAN:
			LOGGER.log(Level.INFO, "Initializing HVH");
			initHVH();
			break;

		case HUMAN_V_COMP:
			LOGGER.log(Level.INFO, "Initializing HVC");
			initHVC();
			break;

		case HUMAN_V_REMOTE:
			LOGGER.log(Level.INFO, "Initializing HVR");
			initHVR();
			break;
			
		case COMP_V_REMOTE:
			LOGGER.log(Level.INFO, "Initializing CVR");
			initCVR();
			break;

		default:
			throw new RuntimeException("Unsupported Game Mode.");
		}
	}

	private void initHVH() {
		gameBoard = new Board(Settings.getInstance().getNumColumns(), Settings
				.getInstance().getNumRows());
		players = new PlayerController(
				new HumanPlayer("Player 1", PlayerID.PLAYER_A), 
				new HumanPlayer("Player 2", PlayerID.PLAYER_B));
	}

	private void initHVC() {
		gameBoard = new Board(Settings.getInstance().getNumColumns(), Settings
				.getInstance().getNumRows());
		players = new PlayerController(
				new HumanPlayer("Player 1", PlayerID.PLAYER_A), 
				new AIPlayer("Player 2", PlayerID.PLAYER_B));
	}

	private void initHVR() throws UnknownHostException,
			ConnectionEstablishmentException, SocketTimeoutException {
		initNetworkManager();
		
		Settings.NetworkConfig config = Settings.getInstance().getNetworkConfig();
		if (config == Settings.NetworkConfig.CLIENT) {
			setupPlayers(
					new NetworkPlayer("Player 1", PlayerID.PLAYER_A, networkAdapter), 
					new HumanPlayer("Player 2", PlayerID.PLAYER_B));
		} else if (config == Settings.NetworkConfig.HOST) {
			setupPlayers(
					new HumanPlayer("Player 1", PlayerID.PLAYER_A),
					new NetworkPlayer("Player 2", PlayerID.PLAYER_B, networkAdapter));
		} else {
			throw new RuntimeException("Unsupported network configuration.");
		}
	}
	
	private void initCVR() throws UnknownHostException,
		ConnectionEstablishmentException, SocketTimeoutException {
		initNetworkManager();
		
		Settings.NetworkConfig config = Settings.getInstance().getNetworkConfig();
		if (config == Settings.NetworkConfig.CLIENT) {
			setupPlayers(
					new NetworkPlayer("Player 1", PlayerID.PLAYER_A, networkAdapter), 
					new AIPlayer("Player 2", PlayerID.PLAYER_B));
		} else if (config == Settings.NetworkConfig.HOST) {
			setupPlayers(
					new AIPlayer("Player 1", PlayerID.PLAYER_A),
					new NetworkPlayer("Player 2", PlayerID.PLAYER_B, networkAdapter));
		} else {
			throw new RuntimeException("Unsupported network configuration.");
		}
	}

	private void initNetworkManager() throws ConnectionEstablishmentException,
	UnknownHostException, SocketTimeoutException {
		Settings.NetworkConfig config = Settings.getInstance().getNetworkConfig();
		if (config == Settings.NetworkConfig.CLIENT) {
			networkManager = new NetworkClientManager(Settings.getInstance()
					.getHostIP(), Settings.getInstance().getPortNumber(),
					CONNECTION_TIMEOUT);
		} else if (config == Settings.NetworkConfig.HOST) {
			networkManager = new NetworkHostManager(Settings.getInstance()
					.getPortNumber(), CONNECTION_TIMEOUT);
		} else {
			throw new RuntimeException("Unsupported network configuration.");
		}
		
		networkManager.connect();
		networkAdapter = setupNetworkAdapter(networkManager);
		gameBoard = networkAdapter.initialize();
	}

	private NetworkAdapter setupNetworkAdapter(NetworkManager netManager) {
		Settings.ProtocolType protocolType = Settings.getInstance()
				.getProtocolType();
		if (protocolType == Settings.ProtocolType.NATIVE) {
			System.out.println("Running natively.");
			return new NativeAdapter(netManager);
		} else if (protocolType == Settings.ProtocolType.COMPATIBILITY) {
			System.out.println("Running in compatibility.");
			return new CompatibilityAdapter(netManager);
		} else {
			throw new RuntimeException("Unsupported protocol.");
		}
	}

	private void setupPlayers(Player player1, Player player2) {
		players = new PlayerController(player1, player2);
	}

	void endGame() {
		LOGGER.log(Level.INFO, "Attempting to end the game.");
		networkAdapter = null;
		if (networkManager != null) {
			networkManager.endSession();
			LOGGER.log(Level.INFO, "Released network manager.");
			networkManager = null;
		}
		if (isRunning()) {
			LOGGER.log(Level.INFO, "Ending game.");
			setRunning(false);

			wakeBlockingPlayer();
			joinThread();
		}
	}

	private void wakeBlockingPlayer() {
		Player player = getCurrentPlayer();
		player.abortWait();
	}

	private void joinThread() {
		try {
			gameThread.join();
		} catch (InterruptedException ex) {
			LOGGER.log(Level.SEVERE, ex.toString(), ex);
		}
	}

	private void gameLoop() {
		try {
			triedGameLoop();
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, ex.toString(), ex);
			endGame();
		}
	}

	private void triedGameLoop() throws InterruptedException {
		while (isRunning()) {
			attemptAMove();
		}
	}

	private void attemptAMove() {
		LOGGER.log(Level.INFO, "Getting current player");
		Player currentPlayer = getCurrentPlayer();
		LOGGER.log(Level.INFO, "Getting next move");
		PlayerMove move = currentPlayer.getNextMove();

		LOGGER.log(Level.INFO, "Validating move");
		if (validateMove(move)) {
			LOGGER.log(Level.INFO, "Performing move");
			performMove(move);
			updateTurnListeners();
		} else {
			LOGGER.log(Level.INFO, "Move is invalid");
		}

		LOGGER.log(Level.INFO, move.toString());
	}
	
	private boolean validateMove(PlayerMove move){
		if(PlayerMove.isPlacementMove(move)){
			return gameBoard.isValidMove(move);
		}else if(PlayerMove.isUndoMove(move)){
			System.out.println("Validating undo move.");
			PlayerMoveType moveType = move.getMoveType();
			if(moveType == PlayerMoveType.UNDO_REQUEST && !gameBoard.canUndoMove()){
				return false;
			}else{
				return true;
			}
		}else{
			throw new RuntimeException("Unsupported player move type.");
		}
	}
	
	private void performMove(PlayerMove move) {
		gameBoard.performMove(move);
		players.next();
	}

	private void updateTurnListeners() {
		for (TurnObserver listener : turnObservers) {
			listener.onTurnEndEvent();
		}
	}

	public void registerTurnObserver(TurnObserver observer) {
		turnObservers.add(observer);
	}

	/**
	 * Get the singleton instance
	 */
	public static ChainGameController getInstance() {
		return INSTANCE;
	}

	public Board getBoard() {
		return gameBoard;
	}

	public Player getCurrentPlayer() {
		return players.getCurrentPlayer();
	}
	
	public Player getNextPlayer(){
		PlayerID nextPlayerId = ChainGameFacade.getOtherPlayerID(getCurrentPlayer().getID());
		return players.getPlayer(nextPlayerId);
	}

	public boolean isRunning() {
		boolean isRunning = false;
		synchronized (runningState) {
			isRunning = runningState;
		}
		return isRunning;
	}

	private void setRunning(boolean value) {
		synchronized (runningState) {
			runningState = value;
		}
	}

	public void removeConnection(Point startPoint, Point endPoint, PlayerID id) {
		gameBoard.removeConnection(startPoint, endPoint, id);
	}

	boolean isNetworkPlayer(PlayerID playerID) {
		Player player = players.getPlayer(playerID);
		return (player instanceof NetworkPlayer);
	}
	
	public PlayerID getNetworkPlayerID(NetworkPlayer player) {
		return player.getID();
	}
	
}