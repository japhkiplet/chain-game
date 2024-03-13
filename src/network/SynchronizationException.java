package network;

public class SynchronizationException extends RuntimeException{

	private static final long serialVersionUID = -7811811834809335043L;

	/**
	 * Used to signal a synchronization failure between a host and a client
	 */
	public SynchronizationException(){
		super("Loss of synchronization detected.");
	}
	
	/**
	 * @param message - a custom message to describe a cause of failure
	 */
	public SynchronizationException(String message){
		super(message);
	}
	
	public SynchronizationException(Exception e){
		super(e);
	}
}