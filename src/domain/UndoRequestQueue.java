package domain;


public abstract class UndoRequestQueue {
	private boolean containsRequest = false;
	
	public final void queueUndoRequest() {
		containsRequest = true;
	}
	
	public final boolean containsRequest(){
		return containsRequest;
	}
	
	public final void clearQueue(){
		containsRequest = false;
	}

}
