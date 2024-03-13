package network;

public class ConnectionEstablishmentException extends Exception{

	private static final long serialVersionUID = 7817721382431815632L;
	
	/**
	 * Used to signal a failure to establish a network connection
	 */
	public ConnectionEstablishmentException(){
		super("Failed to establish connection.");
	}
	
	/**
	 * @param message - a custom message to describe a cause of failure
	 */
	public ConnectionEstablishmentException(String message){
		super(message);
	}
	
	public ConnectionEstablishmentException(Exception e){
		super(e);
	}
}
