package ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import domain.PlayerID;

public class ChooseColorScene implements javafx.fxml.Initializable{
	private final ToggleGroup radioButtonGroup = new ToggleGroup();
	
	@FXML private Pane chooseColorPane;
	@FXML private RadioButton redPlayerRadioButton;
	@FXML private RadioButton bluePlayerRadioButton;
	@FXML private Button confirmColorButton;
	@FXML private Button backButton;
	@FXML private Button colorTitle;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		chooseSizeCustomFont();
		setupConfirmButton();
		getPlayerColors();
		backButtonClickEvent();
	}

	private void backButtonClickEvent() {
		backButton.setOnAction((event) -> {
			SceneLoader.loadScene(chooseColorPane, "ChooseSizeScene.fxml");
		});
	}

	private void setupConfirmButton() {
		confirmColorButton.setOnAction((event) -> {
			GameInitializer.initializeGame(SceneLoader
					.getEventSourceStage(event));
		});
	}

	private void getPlayerColors() {
		radioButtonToggleSetUp(radioButtonGroup);
		radioButtonGroup.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {
					@Override
					public void changed(
							ObservableValue<? extends Toggle> toggle,
							Toggle oldToggle, Toggle newToggle) {
						if (radioButtonGroup.getSelectedToggle().equals(
								redPlayerRadioButton)) {
							playerAIsRedColor();
						} else if (radioButtonGroup.getSelectedToggle().equals(
								bluePlayerRadioButton)) {
							playerAIsBlueColor();
						}
					}
				});
	}

	private void playerAIsRedColor() {
		UISettings.setPlayerColor(PlayerID.PLAYER_A, Color.RED);
		UISettings.setPlayerColor(PlayerID.PLAYER_B, Color.BLUE);
	}

	private void playerAIsBlueColor() {
		UISettings.setPlayerColor(PlayerID.PLAYER_A, Color.BLUE);
		UISettings.setPlayerColor(PlayerID.PLAYER_B, Color.RED);
	}

	private void radioButtonToggleSetUp(final ToggleGroup group) {
		redPlayerRadioButton.setToggleGroup(group);
		redPlayerRadioButton.setSelected(true);
		bluePlayerRadioButton.setToggleGroup(group);
	}

	private void chooseSizeCustomFont() {
		setSizeSceneCustomButtonsFont();
		setSizeSceneRadioButtonsFont();
	}

	private void setSizeSceneCustomButtonsFont() {
		CustomFonts.setCustomButtonFont(confirmColorButton, "CONFIRM");
		CustomFonts.setCustomButtonFont(backButton, "BACK");
		CustomFonts.setCustomTitleButtonFont(colorTitle, "PLAYER 1 COLOR");
	}

	private void setSizeSceneRadioButtonsFont() {
		CustomFonts.setRadioButtonFont(redPlayerRadioButton, "RED");
		CustomFonts.setRadioButtonFont(bluePlayerRadioButton, "BLUE");
	}
}