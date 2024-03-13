package nativeProtocol;

import java.io.Serializable;

public class GameBoardParameters implements Serializable {

	private static final long serialVersionUID = 7418845912233674795L;
	private int width = 0;
	private int height = 0;

	/**
	 * Used to transmit game board parameters over the network
	 * @param width - board width
	 * @param height - board height
	 */
	public GameBoardParameters(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * @return board width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return board height
	 */
	public int getHeight() {
		return height;
	}
	
	@Override
	public String toString(){
		return "Game Board {"
				+ "width: " + width
				+ ", height: " + height
				+"}";
	}

}
