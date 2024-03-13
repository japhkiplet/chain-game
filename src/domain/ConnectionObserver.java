package domain;


public interface ConnectionObserver {
	public void onConnectionAdded(Connection newConnection);
	
	public void onConnectionRemoved(Connection removedConnection);
}
