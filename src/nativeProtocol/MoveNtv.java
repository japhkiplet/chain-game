package nativeProtocol;

import java.io.Serializable;

import domain.PlayerMove;

public class MoveNtv implements Serializable{

	private static final long serialVersionUID = -117495387931131428L;
	
	private PlayerMove playerMove = null;
	/**
	 * Used to transmit player move over the network
	 * @param type - the type of the move
	 * @param coords - the location of the move on the board
	 * @param playerId - ID of the player making the move
	 */
	public MoveNtv(PlayerMove playerMove){
		this.playerMove = playerMove;
	}
	
	/**
	 * @return the move type
	 */
	public PlayerMove getPlayerMove(){
		return playerMove;
	}
	
	@Override
	public String toString(){
		return "Move {"
				+ playerMove.toString()
				+ "}";
	}
}
