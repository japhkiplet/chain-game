package network;

public class TransmissionException extends RuntimeException{
	
	private static final long serialVersionUID = 2542251510904052829L;

	/**
	 * Used to indicate a network transmission failure
	 */
	public TransmissionException(){
		super("Transmission failure.");
	}
	
	/**
	 * @param message - a custom message to describe a cause of failure
	 */
	public TransmissionException(String message){
		super(message);
	}
	
	public TransmissionException(Exception e){
		super(e);
	}
}
