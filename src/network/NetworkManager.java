package network;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public interface NetworkManager {

	public void connect() throws UnknownHostException, ConnectionEstablishmentException, SocketTimeoutException;
	/**
	 * Transmits an object
	 * @param object - the object to transmit
	 * @return - the transmission status
	 */
	
	public <T extends Serializable> void transmitObject(T object);
	
	/**
	 * Receives an object
	 * Throws TransmissionException when transmission failure occurs
	 * Throws SynchronizationException when an unexpected object is received
	 * @param objInstance - an instance of the expected object
	 * @return - the received object
	 */
	public <T extends Serializable> T receiveObject(Class<T> objClass);
	
	public <T extends Serializable> void transmitObjectUnwrapped(T object);
	
	public <T extends Serializable> T receiveObjectUnwrapped(Class<T> objClass);
	/**
	 * Ends the current networking session
	 */
	public void endSession();
	
}