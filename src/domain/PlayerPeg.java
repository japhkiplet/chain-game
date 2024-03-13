package domain;

public class PlayerPeg implements Peg {

	private PlayerID owningPlayer;

	public PlayerPeg() {
		this(PlayerID.UNDEFINED);
	}

	public PlayerPeg(PlayerID owningPlayer) {
		this.owningPlayer = owningPlayer;
	}

	public boolean isNull() {
		return false;
	}

	public PlayerID getOwningPlayer() {
		return owningPlayer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((owningPlayer == null) ? 0 : owningPlayer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerPeg other = (PlayerPeg) obj;
		if (owningPlayer != other.owningPlayer)
			return false;
		return true;
	}
}
