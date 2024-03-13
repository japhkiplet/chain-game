package ui;

import javax.swing.JOptionPane;

public class DialogProvider {

	public static final String GAME_OPTIONS_SCENE_FXML = "GameOptionsScene.fxml";

	/**
	 * Displays an error message dialog box 
	 * @param message - the message to display
	 */
			
	public static void displayErrorMessage(String message){
		JOptionPane.showMessageDialog(null, message, "Game Error",
				JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Displays an information message dialog box 
	 * @param message - the message to display
	 */
	public static void displayInformationDialog(String message){
		JOptionPane.showMessageDialog(null, message);
	}

}
