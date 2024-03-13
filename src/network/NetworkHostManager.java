package network;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import domain.Status;
import domain.StatusFail;
import domain.StatusOk;

public class NetworkHostManager implements NetworkManager{

	private Logger logger = NetworkLogger.getInstance();
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private NetworkTransceiver transceiver = null;
	
	private int portNumber = 0;
	private int connectionTimeoutMs = 0;
	private Status connected = new StatusFail();
	
	/**
	 * Network Manager for a game host
	 * @param portNumber - port number to host on
	 * @param connectionTimeoutMs - connection timeout in milliseconds
	 * @throws ConnectionEstablishmentException - failure to connect to host
	 * @throws SocketTimeoutException  - timeout has occurred
	 */
	public NetworkHostManager(int portNumber, int connectionTimeoutMs){
		this.portNumber = portNumber;
		this.connectionTimeoutMs = connectionTimeoutMs;
	}
	
	@Override
	public void connect() throws SocketTimeoutException, ConnectionEstablishmentException {
		hostGame();
		connected = new StatusOk();
	}
	
	private void hostGame() throws SocketTimeoutException, ConnectionEstablishmentException{
		logger.log(Level.INFO,"Attempting to host a game on port " + portNumber + ".");
		try {
			serverSocket = new ServerSocket(portNumber);
			serverSocket.setSoTimeout(connectionTimeoutMs);
			socket = serverSocket.accept();
			
			logger.log(Level.INFO,"Connection established!");
			transceiver = new NetworkTransceiver(socket);
			
		}catch(SocketTimeoutException e){ 
			closeConnections();
			throw e;
		}catch (ConnectionEstablishmentException e){
			closeConnections();
			throw e;
		}catch (IOException e) {
			closeConnections();
			throw new ConnectionEstablishmentException(e);
		}
	}
	
	
	@Override
	public void endSession() {
		connected = new StatusFail();
		closeConnections();
	}
	
	private void closeConnections(){
		logger.log(Level.INFO,"Closing network manager connections.");
			try {
				if(transceiver != null) {
					transceiver.closeConnections();
				}
				if(socket != null){
					socket.close();
				}
				if(serverSocket != null){
					serverSocket.close();
				}			
			} catch (IOException e) {
				logger.log(Level.SEVERE,"Failed to close the connection.");
			}
	}
	
	private void connectProtector(){
		if(connected.failed()){
			throw new RuntimeException("Network Manager must be connected.");
		}
	}

	@Override
	public <T extends Serializable> void transmitObject(T object) {
		connectProtector();
		transceiver.transmitObject(object);
	}

	@Override
	public <T extends Serializable> T receiveObject(Class<T> objClass) {
		connectProtector();
		return transceiver.receiveObject(objClass);
	}

	@Override
	public <T extends Serializable> void transmitObjectUnwrapped(
			T object) {
		connectProtector();
		transceiver.transmitObjectUnwrapped(object);
	}

	@Override
	public <T extends Serializable> T receiveObjectUnwrapped(Class<T> objClass) {
		connectProtector();
		return transceiver.receiveObjectUnwrapped(objClass);
	}

	

}