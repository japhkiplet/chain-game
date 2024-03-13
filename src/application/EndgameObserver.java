package application;

import domain.PlayerID;

public interface EndgameObserver {
	public void onGameWonEvent(PlayerID winner);
}
