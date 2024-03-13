package application;

import domain.Player;
import domain.PlayerID;

public class PlayerController {
	private static final int MAXIMUM_NUM_PLAYERS = 2;

	private Player[] players = new Player[MAXIMUM_NUM_PLAYERS];
	private int currentPlayerIndex = 0;

	public PlayerController(Player player1, Player player2) {
		players[0] = player1;
		players[1] = player2;

		assertPlayerIDs();
	}

	private void assertPlayerIDs() {
		for (int i = 0; i < MAXIMUM_NUM_PLAYERS; i++) {
			assert (players[i].getID().getValue() == i);
		}
	}

	/**
	 * Replaces a player in the set of players. The replaced player is the one
	 * that has the same ID as the argument. Use only for replacing a player
	 * mid-game; new games should get a new PlayerController.
	 * 
	 * @param player
	 */
	public void replacePlayer(Player player) {
		int indexToReplace = player.getID().getValue();
		assert isValidIndex(indexToReplace);
	}

	private boolean isValidIndex(int index) {
		return 0 <= index && index < MAXIMUM_NUM_PLAYERS;
	}

	public void next() {
		currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
	}

	public Player getCurrentPlayer() {
		return players[currentPlayerIndex];
	}
	
	public Player getPlayer(PlayerID playerID) {
		return players[playerID.ordinal()];
	}
}
