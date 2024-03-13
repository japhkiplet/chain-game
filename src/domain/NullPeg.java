package domain;

public class NullPeg implements Peg {

	public boolean isNull() {
		return true;
	}

	public PlayerID getOwningPlayer() {
		return PlayerID.UNDEFINED;
	}
}
