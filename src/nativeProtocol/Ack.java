package nativeProtocol;

import java.io.Serializable;

public class Ack implements Serializable{

	private static final long serialVersionUID = -5462325059349951502L;

	/**
	 * Used to communicate acknowledgement over the network
	 */
	public Ack(){
	}
	
	@Override
	public String toString(){
		return "Aknowledged.";
	}
}
