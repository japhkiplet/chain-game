package ui;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import domain.Settings;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class SelectNetworkScene implements Initializable {
	private static final String HOST_DIALOG = "Please enter a valid Host IP.";
	private static final String INVALID_PORT_NUMBER_DIALOG = "You have entered an invald Port Number." +"\n" + "Choose a port number between 1 and 65535.";
	private static final String INVALID_HOST_IP_DIALOG = "You have entered an invalid Host IP. Please try again,";
	private static final String INVALID_PORT_NUMBER_TITLE = "Invalid Port Number";

	private static final int MIN_PORT_NUMBER = 1;
	private static final int MAX_PORT_NUMBER = 65535;
	private static final  int DEFAULT_PORT = 45724;

	@FXML private Pane networkPane;
	@FXML private ComboBox<String> protocolTypeComboBox;
	@FXML private Button joinGameButton;
	@FXML private Button hostButton;
	@FXML private Button backButton;
	@FXML private Button networkTitle;
	@FXML private Label typeLabel;
	@FXML private Label portLabel;
	@FXML private TextField portTextField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		networkCustomFont();
		selectProtocolType();
		setJoinGameButtonEventHandler();
		setHostButtonEventHandler();
		backButtonClickEvent();
		setUpPortTextField();
	}

	private void setUpPortTextField() {
		portTextField.setText("" + DEFAULT_PORT);
		portTextField.textProperty().addListener((observable, oldValue, newValue)->{ 
			if(newValue.equals("")) {
				return;
			}try {
				Integer.parseInt(newValue);
			} catch (NumberFormatException ex) {
				portTextField.setText(oldValue);
			}
		});
	}

	private void backButtonClickEvent() {
		backButton.setOnAction((event) -> {
			SceneLoader.loadScene(networkPane, "ChooseModeScene.fxml");
		});
	}

	private void setJoinGameButtonEventHandler() {
		joinGameButton.setOnAction((event) -> {
			boolean portOK = checkPortNumber();
			if(portOK) {
				joinGame(event);
			}
		});
	}

	private void joinGame(ActionEvent event) {
		SwingUtilities.invokeLater(()->{
			String ipAddress = getIP();
			if(ipAddress != null) {
				Settings.getInstance().setHostIP(ipAddress);
				Settings.getInstance().setNetworkConfig(Settings.NetworkConfig.CLIENT);
				GameInitializer.initializeGame(SceneLoader.getEventSourceStage(event));
			}
		});
	}

	private void setHostButtonEventHandler() {
		hostButton.setOnAction((event) -> {
			boolean portOK = checkPortNumber();
			if(portOK) {
				Settings.getInstance().setNetworkConfig(Settings.NetworkConfig.HOST);
				Platform.runLater(() -> SceneLoader.loadScene(networkPane, "ChooseSizeScene.fxml"));
			}
		});
	}

	private String getIP() {
		String selectedHostIP = promptUserForIP(HOST_DIALOG);

		while(selectedHostIP != null && !isValidIP(selectedHostIP)) {
			selectedHostIP = promptUserForIP(INVALID_HOST_IP_DIALOG);
		}
		return selectedHostIP;
	}

	private String promptUserForIP(String message) {
		String ipAddress = JOptionPane.showInputDialog(null, message,"Host IP", JOptionPane.OK_CANCEL_OPTION);
		return ipAddress;
	}

	private boolean checkPortNumber() {
		if(portTextField.getText().equals("")) {
			showErrorMessageDialog(INVALID_PORT_NUMBER_DIALOG, INVALID_PORT_NUMBER_TITLE);
			return false;
		}

		int portNumber = Integer.parseInt(portTextField.getText());
		if(!isValidPortNumber(portNumber)) {
			showErrorMessageDialog(INVALID_PORT_NUMBER_DIALOG, INVALID_PORT_NUMBER_TITLE);
			return false;
		}
		Settings.getInstance().setPortNumber(portNumber);
		return true;
	}

	private void showErrorMessageDialog(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title,
				JOptionPane.OK_OPTION);
	}

	private boolean isValidIP(String ip) {
		String ipRegex = "\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";
		return ip != null && (ip.matches(ipRegex) || ip.equals("localhost"));
	}

	private boolean isValidPortNumber(int portNum) {
		return portNum >= MIN_PORT_NUMBER && portNum <= MAX_PORT_NUMBER;
	}

	private void selectProtocolType() {
		comboBoxSetUp();
		comboBoxChangeListener();
	}
	
	private void comboBoxSetUp(){
		protocolTypeComboBox.setStyle("-fx-font:15px\"Bold\";");
		protocolTypeComboBox.getItems().addAll("Native", "Compatibility");
		protocolTypeComboBox.setValue("Native");
	}
	
	private void comboBoxChangeListener(){
		protocolTypeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					ObservableValue<? extends String> selected,String oldSelected, String newSelected) {
				if(protocolTypeComboBox.getValue().toString()
						.equals("Native")) {
					Settings.getInstance().setProtocolType(
							Settings.ProtocolType.NATIVE);
				}else if(protocolTypeComboBox.getValue().toString()
						.equals("Compatibility")) {
					Settings.getInstance().setProtocolType(
							Settings.ProtocolType.COMPATIBILITY);
				}else{
					defaultNativeProtocolType();
				}
			}
		});
	}

	private void defaultNativeProtocolType() {
		Settings.getInstance().setProtocolType(Settings.ProtocolType.NATIVE);
	}

	private void networkCustomFont() {
		customButtonFonts();
		customLabelandTextFonts();
	}

	private void customButtonFonts(){
		CustomFonts.setCustomTitleButtonFont(networkTitle, "SELECT GAME");
		CustomFonts.setCustomButtonFont(joinGameButton, "JOIN GAME");
		CustomFonts.setCustomButtonFont(hostButton, "HOST GAME");
		CustomFonts.setCustomButtonFont(backButton, "BACK");
	}

	private void customLabelandTextFonts(){
		CustomFonts.setCustomLabelFont(typeLabel, "Protocol Type");
		CustomFonts.setCustomLabelFont(portLabel, "Port Number");
		CustomFonts.setCustomTextFieldFont(portTextField);
	}
}