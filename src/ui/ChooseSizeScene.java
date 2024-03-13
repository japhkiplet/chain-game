package ui;

import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import domain.Settings;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class ChooseSizeScene implements javafx.fxml.Initializable {
	private static final int MINIMUM_BOARD_SIZE = 2;
	private static final int MAXIMUM_BOARD_SIZE = 40;
	private static final int SMALL_BOARD_SIZE = 9;
	private static final int MEDIUM_BOARD_SIZE = 13;
	private static final int LARGE_BOARD_SIZE = 19;
	
	@FXML private Pane boardSizePane;
	@FXML private Button smallButton;
	@FXML private Button mediumButton;
	@FXML private Button largeButton;
	@FXML private Button customSizeButton;
	@FXML private Button backButton;
	@FXML private Button boardSizeTitle;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		boardSizeCustomFont();
		smallButtonClickEvent(smallButton);
		mediumButtonClickEvent(mediumButton);
		largeButtonClickEvent(largeButton);
		customButtonClickEvent(customSizeButton);
		backButtonClickEvent();
	}

	private void boardSizeCustomFont(){
		CustomFonts.setCustomButtonFont(smallButton,"9X9");
		CustomFonts.setCustomButtonFont(mediumButton,"13X13");
		CustomFonts.setCustomButtonFont(largeButton,"19X19");
		CustomFonts.setCustomButtonFont(customSizeButton,"CUSTOM SIZE");
		CustomFonts.setCustomButtonFont(backButton,"BACK");
		CustomFonts.setCustomTitleButtonFont(boardSizeTitle,"BOARD SIZE");
	}
	
	private void smallButtonClickEvent(Button button) {
		button.setOnAction((event) -> {
			Settings.getInstance().setNumColumns(SMALL_BOARD_SIZE);
			Settings.getInstance().setNumRows(SMALL_BOARD_SIZE);
			SceneLoader.loadScene(boardSizePane, "ChooseColorScene.fxml");
		});
	}
	private void mediumButtonClickEvent(Button button) {
		button.setOnAction((event) -> {
			Settings.getInstance().setNumColumns(MEDIUM_BOARD_SIZE);
			Settings.getInstance().setNumRows(MEDIUM_BOARD_SIZE);
			SceneLoader.loadScene(boardSizePane, "ChooseColorScene.fxml");
		});
	}
	private void largeButtonClickEvent(Button button) {
		button.setOnAction((event) -> {
			Settings.getInstance().setNumColumns(LARGE_BOARD_SIZE);
			Settings.getInstance().setNumRows(LARGE_BOARD_SIZE);
			SceneLoader.loadScene(boardSizePane, "ChooseColorScene.fxml");	
		});
	}
	private void customButtonClickEvent(Button button) {
		button.setOnAction((event) -> {
			SwingUtilities.invokeLater(()-> customSizeSelection());
		});
	}
	private void backButtonClickEvent() {
		backButton.setOnAction((event) -> {
			SceneLoader.loadScene(boardSizePane, "ChooseModeScene.fxml");
		});
	}
	
	private void customSizeSelection() {
		JTextField widthText = new JTextField();
		JTextField heightText = new JTextField();
		Object[] message = {
				"Width:", widthText,
				"Height:", heightText
		};
		int option = JOptionPane.showConfirmDialog(null, message, "Custom Size Selection", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION){
			try{
				int width = Integer.parseInt(widthText.getText());
				int height = Integer.parseInt(heightText.getText());
				if (isInRange(width) && isInRange(height)) {
					Settings.getInstance().setNumColumns(width);
					Settings.getInstance().setNumRows(height);
					Platform.runLater(()->SceneLoader.loadScene(boardSizePane, "ChooseColorScene.fxml"));
				} else {
					JOptionPane.showMessageDialog(null, "You have entered invalid size (Size should be greater than 2 and less than 40). "
							+ "\nSize did not change.", "Invalid Size", JOptionPane.OK_OPTION);
				}	
			} catch (NumberFormatException e){
				JOptionPane.showMessageDialog(null, "You have entered invalid size (Size should be number). "
						+ "\nSize did not change.", "Invalid Number", JOptionPane.OK_OPTION);
			}
		}
	}
	private boolean isInRange(int size) {
		return (size > MINIMUM_BOARD_SIZE) && (size < MAXIMUM_BOARD_SIZE);
	}
}
