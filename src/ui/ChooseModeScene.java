package ui;

import java.net.URL;
import java.util.ResourceBundle;

import domain.GameMode;
import domain.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class ChooseModeScene implements javafx.fxml.Initializable {

	@FXML private Pane chooseModePane;
	@FXML private Button pVpButton;
	@FXML private Button pVcButton;
	@FXML private Button cVrButton;
	@FXML private Button pVrButton;
	@FXML private Button backButton;
	@FXML private Button chooseModeTitle;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		modeCustomFont();
		setUpButtonClickEvents();
	}
	
	private void modeCustomFont(){
		CustomFonts.setCustomButtonFont(pVpButton, "PLAYER VS PLAYER");
		CustomFonts.setCustomButtonFont(pVrButton, "PLAYER VS REMOTE");
		CustomFonts.setCustomButtonFont(pVcButton, "PLAYER VS COMPUTER");
		CustomFonts.setCustomButtonFont(cVrButton, "COMPUTER VS REMOTE");
		CustomFonts.setCustomButtonFont(backButton, "BACK");
		CustomFonts.setCustomTitleButtonFont(chooseModeTitle, "CHOOSE MODE");
	}

	private void setUpButtonClickEvents(){
		modeClickEvents();
	}
	
	private void modeClickEvents(){
		pVpButtonClickEvent();
		pVrButtonClickEvent();
		pVcButtonClickEvent();
		cVrButtonClickEvent();
		backButtonClickEvent();
	}

	private void pVpButtonClickEvent() {
			pVpButton.setOnAction((event) -> {
				Settings.getInstance().setMode(GameMode.HUMAN_V_HUMAN);
				SceneLoader.loadScene(chooseModePane, "ChooseSizeScene.fxml");
			});
	}
	private void pVrButtonClickEvent() {
		pVrButton.setOnAction((event) -> {
			Settings.getInstance().setMode(GameMode.HUMAN_V_REMOTE);
			SceneLoader.loadScene(chooseModePane, "SelectNetworkScene.fxml");
		});
	}
	private void pVcButtonClickEvent() {
		pVcButton.setOnAction((event) -> {
			Settings.getInstance().setMode(GameMode.HUMAN_V_COMP);
			SceneLoader.loadScene(chooseModePane, "ChooseSizeScene.fxml");
		});
	}
	private void cVrButtonClickEvent() {
		cVrButton.setOnAction((event) -> {
			Settings.getInstance().setMode(GameMode.COMP_V_REMOTE);
			SceneLoader.loadScene(chooseModePane, "SelectNetworkScene.fxml");
		});
	}
	private void backButtonClickEvent() {
		backButton.setOnAction((event) -> {
			SceneLoader.loadScene(chooseModePane, "GameOptionsScene.fxml");
		});
	}

}