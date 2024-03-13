package ui;

import java.util.HashMap;
import java.util.Map;

import domain.PlayerID;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class UISettings {
	private static Map<PlayerID, Paint> playerColors;
	private static boolean isSoundMute;
	static {
        Map<PlayerID, Paint> map = new HashMap<>();
        map.put(PlayerID.PLAYER_A, Color.RED);
        map.put(PlayerID.PLAYER_B, Color.BLUE);
        playerColors = map;
    }
	
	private UISettings() {
	}

	public static Paint getPlayerColor(PlayerID playerID) {
		return playerColors.get(playerID);
	}

	public static void setPlayerColor(PlayerID playerID, Paint playerColor) {
		playerColors.put(playerID, playerColor);
	}
	
	public static boolean isSoundMute(){
		return isSoundMute;
	}
	
	public static void setIsSoundMute(boolean isMute){
		isSoundMute = isMute;
	}
	
	
}
