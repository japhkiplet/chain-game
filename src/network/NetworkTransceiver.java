package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import nativeProtocol.NetworkPackage;

public class NetworkTransceiver {

	private Logger logger = NetworkLogger.getInstance();
	private ObjectInputStream inputStream = null;
	private ObjectOutputStream outputStream = null;

	/**
	 * Network transceiver handles object serialization over a given socket.
	 * 
	 * Objects can be serialized using the NetworkPackage frame allowing the recepient to read the class of the de-serialized object,
	 * or "Unwrapped" where the recepient always has to expect the received class.
	 * 
	 * @param socket
	 *            - a socket on which the connection has been established
	 * @throws ConnectionEstablishmentException
	 *             - failure to initialize object streams for serialization
	 * @throws IllegalArgumentException - socket is not initialized
	 */
	public NetworkTransceiver(Socket socket)
			throws ConnectionEstablishmentException {
		establishCommunication(socket);
	}

	private void establishCommunication(Socket socket)
			throws ConnectionEstablishmentException {
		if (socket == null) {
			throw new IllegalArgumentException();
		}
		logger.log(Level.INFO, "Attempting to create streams.");

		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.flush();
			inputStream = new ObjectInputStream(socket.getInputStream());

			logger.log(Level.INFO, "Object streams successfully initialized!");
		} catch (IOException e) {
			closeConnections();
			throw new ConnectionEstablishmentException(e);
		}
	}

	/**
	 * Transmits an object using the NetworkPackage frame
	 * 
	 * @param object
	 *            - the object to transmit
	 * @throws TransmissionException - failure to transmit object
	 */
	public <T extends Serializable> void transmitObject(T object) {
		serializeObject(wrapObject(object));
	}

	/**
	 * Receives an object encapsulated into a NetworkPackage frame
	 * 
	 * @param objClass - a class of the expected object
	 * @return - expected object
	 * 
	 * @throws TransmissionException - failure to receive object
	 * @throws SynchronizationException - unexpected object is received
	 */
	public <T extends Serializable> T receiveObject(Class<T> objClass) {
		try {
			NetworkPackage<?> pkg = (NetworkPackage<?>) deserializeObject();
			return unwrapObject(pkg, objClass);
		} catch (ClassCastException e) {
			throw new SynchronizationException(
					"Expected a package. Received an unwrapped object.");
		}
	}

	/**
	 * Transmits an object without the NetworkPackage frame (Unwrapped)
	 * 
	 * @param object - the object to transmit
	 * @throws TransmissionException - failure to transmit object
	 */
	public <T extends Serializable> void transmitObjectUnwrapped(T object) {
		serializeObject(object);
	}

	/**
	 * Receives an Unwrapped object
	 * 
	 * @param objClass - a class of the expected object
	 * @return - expected object
	 * 
	 * @throws TransmissionException - failure to receive object
	 * @throws SynchronizationException - unexpected object is received
	 */
	public <T extends Serializable> T receiveObjectUnwrapped(Class<T> objClass) {
		try {
			return objClass.cast(deserializeObject());
		} catch (ClassCastException e) {
			throw new SynchronizationException("Unexpected object.");
		}
	}

	/**
	 * Closes object streams
	 */
	public void closeConnections() {
		logger.log(Level.INFO, "Closing transceiver connections.");
		try {
			if (outputStream != null) {
				outputStream.close();
				outputStream = null;
			}
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to close streams.");
		}
	}

	private <T> T unwrapObject(NetworkPackage<?> pkg, Class<T> objClass) {
		if (containsClass(pkg, objClass)) {
			return objClass.cast(pkg.getData());
		}else{
			throw new SynchronizationException("Unexpected object");
		}
	}

	private <T> boolean containsClass(NetworkPackage<?> pkg, T objClass) {
		if (pkg.getData().getClass().equals(objClass)) {
			return true;
		} else {
			return false;
		}
	}

	private <T extends Serializable> NetworkPackage<T> wrapObject(T object) {
		return new NetworkPackage<T>(object.getClass(), object);
	}

	private void serializeObject(Object obj) {
		try {
			outputStream.writeObject(obj);
		} catch (IOException e) {
			throw new TransmissionException(e);
		}
	}

	private Object deserializeObject() {
		try {
			return inputStream.readObject();
		} catch (ClassNotFoundException e){
			throw new TransmissionException(e);
		}catch(IOException e){
			throw new TransmissionException(e);
		}
	}
}