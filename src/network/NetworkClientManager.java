package network;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import domain.Status;
import domain.StatusFail;
import domain.StatusOk;

public class NetworkClientManager implements NetworkManager {

	private final int RECONNECTION_INTERVAL_MS = 5000; 
	
	private Logger logger = NetworkLogger.getInstance();
	private Socket socket = null;
	private NetworkTransceiver transceiver = null;
	
	private String hostName = "";
	private int portNumber = 0;
	private int connectionTimeoutMs = 0;
	
	private Object sleepObject = new Object();
	private Status connected = new StatusFail();
	private boolean reconnectionAllowed = true;

	/**
	 * Network Manager for a game client
	 * @param hostName - the name of the host to connect to
	 * @param portNumber - the port number of the host to connect to
	 * @throws ConnectionEstablishmentException - failure to establish connection with host
	 * @throws UnknownHostException - the host with the specified name could not be identified
	 */
	public NetworkClientManager(String hostName, int portNumber, int connectionTimeoutMs){
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.connectionTimeoutMs = connectionTimeoutMs;
	}

	@Override
	public void connect() throws UnknownHostException, ConnectionEstablishmentException {
		joinGame();
		connected = new StatusOk();
	}
	
	private void joinGame() throws UnknownHostException, ConnectionEstablishmentException {
		logger.log(Level.INFO, "Attempting to join a game on host " + hostName
				+ ", port " + portNumber + ".");
		try {
			connectToServer(hostName, portNumber, connectionTimeoutMs);
			
		} catch (UnknownHostException e) {
			closeConnections();
			throw e;
		} catch (ConnectionEstablishmentException e){
			closeConnections();
			throw e;
		}catch (IOException e) {
			closeConnections();
			throw new ConnectionEstablishmentException(e);
		}
	}

	private void connectToServer(String hostName, int portNumber, int connectionTimeoutMs)
			throws UnknownHostException, IOException,
			ConnectionEstablishmentException {
		
		int numReconnects = connectionTimeoutMs / RECONNECTION_INTERVAL_MS;
		for(int i = 0; i < numReconnects; i++){
			if(!reconnectionAllowed){
				throw new ConnectionEstablishmentException("Unable to establish connection.");
			}
			
			logger.log(Level.INFO,"Retrying connection.");
			if(i == numReconnects - 1){
				socket = new Socket(hostName, portNumber);
			}else{
				try {
					socket = new Socket(hostName, portNumber);
					break;
				} catch (IOException e) {
					sleep(RECONNECTION_INTERVAL_MS);
				}
			}	
		}
		logger.log(Level.INFO, "Connection established!");
		transceiver = new NetworkTransceiver(socket);
	}

	private void sleep(int interval) {
		try {
			synchronized(sleepObject){
				sleepObject.wait(interval);
			}	
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
	}

	@Override
	public void endSession() {
		connected = new StatusFail();
		interruptReconnection();
		closeConnections();
	}
	
	private void interruptReconnection(){
		reconnectionAllowed = false;
		synchronized(sleepObject){
			sleepObject.notify();
		}
	}

	private void closeConnections() {
		logger.log(Level.INFO,"Closing network manager connections.");
		try {
			if(transceiver != null) {
				transceiver.closeConnections();
				transceiver = null;
			}
			if (socket != null) {
				socket.close();
				socket = null;
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to close the link.");
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