package domain;

public abstract class Player extends UndoRequestQueue{
	private String name = "";
	private PlayerID playerID;

	public Player(String name, PlayerID playerID) {
		this.name = name;
		this.playerID = playerID;
	}

	public abstract PlayerMove getNextMove();

	public abstract void injectMove(PlayerMove move);

	public abstract void abortWait();

	public String getName() {
		return name;
	}

	public PlayerID getID() {
		return playerID;
	}

	public String toString() {
		return name + "(" + playerID.toString() + ")";
	}
}