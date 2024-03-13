package uiGameBoard;

import javafx.scene.layout.Pane;

public final class GameBoard extends Pane {
	private static boolean isClickable = true;
	
	public GameBoard(int gridWidth, int gridHeight) {
		this.setPickOnBounds(false);
		BoardPane.setGridWidth(gridWidth);
		BoardPane.setGridHeight(gridHeight);

		initPane(new GridPane());
		initPane(new ConnectionPane());
		initPane(new PegPane());
	}

	private void initPane(BoardPane pane) {
		this.getChildren().add(pane);
		pane.bindSize(this);
		
		// Setting pickOnBounds to false allows mouse clicks to 
		// 'pass through' the pane to underlying panes.
		pane.setPickOnBounds(false);
	}
	
	public static boolean isClickable() {
		return isClickable;
	}
	
	public static void isClickable(boolean value) {
		isClickable = value;
	}
}