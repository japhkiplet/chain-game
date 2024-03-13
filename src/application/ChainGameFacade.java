package application;

import java.awt.Point;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import network.ConnectionEstablishmentException;
import network.NetworkPlayer;
import domain.Board;
import domain.ConnectionObserver;
import domain.GameMode;
import domain.HumanPlayer;
import domain.PegObserver;
import domain.Player;
import domain.PlayerID;
import domain.PlayerMove;
import domain.PlayerMoveType;
import domain.UndoPlayerMove;

public final class ChainGameFacade {
	// Constructor is private to block instantiation of this class
	private ChainGameFacade() {
	}

	public static void clickPeg(Point point) {
		Player player = getCurrentPlayer();
		PlayerMove playerMove = new PlayerMove(point, player.getID());
		player.injectMove(playerMove);
	}

	public static boolean isValidMove(Point point) {
		Player currentPlayer = ChainGameController.getInstance()
				.getCurrentPlayer();
		PlayerMove testMove = new PlayerMove(point, currentPlayer.getID());
		return getBoard().isValidMove(testMove);
	}

	public static boolean isHumanPlayerTurn() {
		Player currentPlayer = getCurrentPlayer();
		return (currentPlayer instanceof HumanPlayer);
	}
	
	public static boolean isNetworkPlayer(PlayerID playerID) {
		return ChainGameController.getInstance().isNetworkPlayer(playerID);
	}
	
	public static PlayerID getNetworkPlayerID(NetworkPlayer player) {
		return ChainGameController.getInstance().getNetworkPlayerID(player);
	}
	
	public static Player getCurrentPlayer() {
		return ChainGameController.getInstance().getCurrentPlayer();
	}
	
	public static PlayerID getOtherPlayerID(PlayerID id){
		if(id == PlayerID.PLAYER_A){
			return PlayerID.PLAYER_B;
		}else if(id == PlayerID.PLAYER_B){
			return PlayerID.PLAYER_A;
		}else{
			throw new RuntimeException("Unsupported player ID.");
		}
	}

	public static void endGame() {
		ChainGameController.getInstance().endGame();
	}

	public static void startGame(GameMode mode, int width, int height)
			throws SocketTimeoutException, UnknownHostException,
			ConnectionEstablishmentException {
		ChainGameController.getInstance().startGame(mode, width, height);
	}

	public static Board getBoard() {
		return ChainGameController.getInstance().getBoard();
	}

	/**
	 * TurnObservers will be called when a turn ends.
	 * 
	 * @param observer
	 */
	public static void registerTurnObserver(TurnObserver observer) {
		ChainGameController.getInstance().registerTurnObserver(observer);
	}

	public static void registerConnectionObserver(ConnectionObserver observer) {
		getBoard().registerConnectionObserver(observer);
	}

	public static void registerPegObserver(PegObserver observer) {
		getBoard().registerPegObserver(observer);
	}
	
	public static void removeConnection(Point startPoint, Point endPoint) {
		ChainGameController.getInstance().removeConnection(startPoint, endPoint, getCurrentPlayer().getID());
	}
	
	public static void requestUndoMove() {
		Player player = getCurrentPlayer();
		UndoPlayerMove undoMove = new UndoPlayerMove(PlayerMoveType.UNDO_REQUEST, player.getID());
		player.injectMove(undoMove);
	}

}