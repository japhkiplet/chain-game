package domain;

public enum PlayerID {
	PLAYER_A(0), 
	PLAYER_B(1), 
	UNDEFINED(2),
	TIE(3);
	
	private int value;

	private PlayerID(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "PlayerID {"
				+ value
				+"}";
	}
}
