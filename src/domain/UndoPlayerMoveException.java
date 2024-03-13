package domain;

public class UndoPlayerMoveException extends UnsupportedOperationException{

	private static final long serialVersionUID = -6117881119582045388L;

	public UndoPlayerMoveException(){
		super();
	}
	
	public UndoPlayerMoveException(String message){
		super(message);
	}
	
	public UndoPlayerMoveException(Exception e){
		super(e);
	}
}
