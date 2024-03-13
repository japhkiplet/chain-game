package network;

import domain.Board;
import domain.PlayerMove;

public interface NetworkAdapter {

	public Board initialize();
	public void sendMove(PlayerMove move);
	public PlayerMove receiveMove();
}
