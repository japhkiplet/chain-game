package domain;

import java.awt.Point;


public interface PegObserver {
	public void onPegClicked(Point pegPoint, PlayerID owningPlayerID);
	
	public void onPegRemoved(Point pegPoint, PlayerID owningPlayerID);
}
