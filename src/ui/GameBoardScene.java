package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import uiFireworks.Fireworks;
import uiGameBoard.GameBoard;
import uiGameBoard.SoundManager;
import application.ChainGameFacade;
import application.EndgameObserver;
import application.TurnObserver;
import domain.EndgameChecker;
import domain.Player;
import domain.PlayerID;
import domain.Settings;

public class GameBoardScene implements javafx.fxml.Initializable,
EndgameObserver, TurnObserver {
	@FXML private ScrollPane scrollPane;
	@FXML private AnchorPane container;
	@FXML private BorderPane boardContainer;
	@FXML private Button undoButton;
	@FXML private Button helpButton;
	@FXML private Button restartButton;
	@FXML private Button endButton;
	private static Label topStatusLabel= new Label();;
	private Player currentPlayer;
	private static final String UNDO_BUTTON_TEXT = "UNDO";
	private static final String HELP_BUTTON_TEXT = "HELP";
	private static final String RESTART_BUTTON_TEXT = "Restart";
	private static final String EXIT_BUTTON_TEXT = "EXIT";
	private static final int BORDER_WIDTH = 55;
	private static final int REGULAR_BOARD_SIZE = 19;
	private static final int CONTAINER_MIN_WIDTH = 800;
	private static final int CONTAINER_MIN_HEIGHT = 700;
	private static final int GAME_OPTION_SCENE_WIDTH = 500;
	private static final int GAME_OPTION_SCENE_HEIGHT = GAME_OPTION_SCENE_WIDTH;
	private static final int FONT_SIZE = 20;
	private static final String GAME_OPTIONS_SCENE_FXML = "GameOptionsScene.fxml";
	private static final String SOUND_CONTROL_BUTTON_ID = "SoundControlButton";
	private static final String IMAGE_VOLUME_UP_PATH = "/images/volume-up.png";
	private static final String IMAGE_VOLUME_MUTE_PATH = "/images/volume-mute.png";
	private final Fireworks fireworks = new Fireworks();
	private static final SoundManager soundManager = new SoundManager();

	@Override
	public void initialize(URL location, ResourceBundle resource1) {
		gameBoardButtonsCustomFont();
		setupButtons();
		setupScrollPaneSettings();
		setupPanes();
		ChainGameFacade.registerTurnObserver(this);
		EndgameChecker.getInstance().reset();
		EndgameChecker.getInstance().registerEndgameObserver(this);
	}

	private void setupScrollPaneSettings() {
		if(Settings.getInstance().getNumColumns() > REGULAR_BOARD_SIZE){
			boardContainer.setMinWidth(CONTAINER_MIN_WIDTH);
			boardContainer.setMinHeight(CONTAINER_MIN_HEIGHT);
			scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
			scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		}
	}

	private void setupPanes() {
		boardContainer.setTop(createTopPane());
		boardContainer.setCenter(createCenterPane());
		boardContainer.setLeft(createLeftPane());
		boardContainer.setRight(createRightPane());
		setupFireworksPane();
	}

	private void setupFireworksPane() {
		Pane fireworksPane = new Pane(fireworks);
		fireworksPane.setMouseTransparent(true);
		container.getChildren().add(fireworksPane);
	}

	private Pane createTopPane() {
		FlowPane topPane = new FlowPane();
		topStatusLabel.setFont(new Font(FONT_SIZE));
		updateStatusMessage();
		topPane.getChildren().add(topStatusLabel);
		topPane.setAlignment(Pos.TOP_CENTER);
		return topPane;
	}

	private Pane createLeftPane() {
		Pane leftPane = new Pane();
		leftPane.setMinWidth(BORDER_WIDTH);
		leftPane.setMaxWidth(BORDER_WIDTH);
		return leftPane;
	}

	private Pane createRightPane() {
		Pane rightPane = new Pane();
		ToggleButton soundControlButton = setupSoundControlButton();
		rightPane.setMinWidth(BORDER_WIDTH);
		rightPane.setMaxWidth(BORDER_WIDTH);
		rightPane.getChildren().add(soundControlButton);
		return rightPane;
	}

	private ToggleButton setupSoundControlButton() {
		Image volumeMuteImage = new Image(getClass().getResourceAsStream(IMAGE_VOLUME_MUTE_PATH));
		Image volumeUpImage = new Image(getClass().getResourceAsStream(IMAGE_VOLUME_UP_PATH));
		ToggleButton soundControlButton = new ToggleButton("",new ImageView(volumeUpImage));
		soundControlButton.setId(SOUND_CONTROL_BUTTON_ID);
		soundControlButton.setOnAction((event) ->{
			if(soundControlButton.isSelected()){
				Platform.runLater(()->soundControlButton.setGraphic(new ImageView(volumeMuteImage)));
				UISettings.setIsSoundMute(true);
				soundManager.stopFireworksSound();
			}else{
				Platform.runLater(()->soundControlButton.setGraphic(new ImageView(volumeUpImage)));
				UISettings.setIsSoundMute(false);
				if(fireworks.isFireWorksPlaying()){
					soundManager.playFireworksSound();
				}
			}
		});
		return soundControlButton;
	}

	private Pane createCenterPane() {
		int gridWidth = Settings.getInstance().getNumColumns();
		int gridHeight = Settings.getInstance().getNumRows();
		Pane centerPane = new GameBoard(gridWidth, gridHeight);
		Settings.getInstance().setCenterPane(centerPane);
		return centerPane;
	}

	@Override
	public void onGameWonEvent(PlayerID winner) {
		String message = winner.name() + " Wins!";
		Platform.runLater(()->{
			updateStatusSettings(message, Color.WHITE);
			undoButton.setDisable(true);
		});
		fireworks.play();
		soundManager.playFireworksSound();
		//fireworksPlayTimer(); //TODO: should we stop fireworks after 30sec 
		ChainGameFacade.endGame();
	}

	@SuppressWarnings("unused")
	private void fireworksPlayTimer() {
		Timer timer = new Timer(30000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetFireworksAndSound();
			}
		});
		timer.setRepeats(false); 
		timer.start();
	}

	@Override
	public void onTurnEndEvent() {
		updateStatusMessage();
	}

	private void updateStatusMessage() {
		currentPlayer = ChainGameFacade.getCurrentPlayer();
		Paint playerColor = UISettings.getPlayerColor(currentPlayer.getID());
		String message = currentPlayer.getName() + " turn";
		Platform.runLater(() ->updateStatusSettings(message, playerColor));
	}

	private void updateStatusSettings(String message, Paint color) {
		topStatusLabel.setText(message);
		topStatusLabel.setTextFill(color);
	}

	private void setupButtons() {
		helpButton.setOnAction((event) -> {
			String message = "<html><span style='font-size:25'>Chain Game generated by Group A, 2014. <br> Project for CMPT 373 <br><br> </span>"
					+ "<span style='font-size:20'> Rules: <br> 1. Players will take turns placing pegs onto the board <br> </span>"
					+ "<span style='font-size:20'> 2. Pegs that are knight's position away from each other will form a connection <br> </span>"
					+ "<span style='font-size:20'> 3. Connections that are intercepted by an opponent's connection will not form <br> </span>"
					+ "<span style='font-size:20'> 4. Players are not allowed to place a peg onto the opponents edges <br> </span>"
					+ "<span style='font-size:20'> 5. First player to be able to form a connection from edge to edge wins the game <br><br> </span>"
					+ "<span style='font-size:20'> How to Play: <br> </span>"
					+ "<span style='font-size:20'> 1. Select a hole that does not contain a peg to place your peg <br> </span>"
					+ "<span style='font-size:20'> 2. Player's turn will end when a peg is placed and other player's turn will begin</span></html>";
			SwingUtilities.invokeLater(()->JOptionPane.showMessageDialog(null, message, "Help",JOptionPane.INFORMATION_MESSAGE));
		});
		endButton.setOnAction((event) -> {
			ChainGameFacade.endGame();
			System.exit(0);
		});
		undoButton.setOnAction((event) -> {
			ChainGameFacade.requestUndoMove();
		});
		restartButton.setOnAction((event)->{
			ChainGameFacade.endGame();
			resetFireworksAndSound();
			SceneLoader.loadScene(new Pane(),GAME_OPTIONS_SCENE_FXML, event, GAME_OPTION_SCENE_WIDTH,GAME_OPTION_SCENE_HEIGHT);
		});
	}

	public static boolean showUndoReqDialog() {
		GameBoard.isClickable(false);
		int result = JOptionPane.showConfirmDialog(null, "Opponent has requested to undo last move. Would you accept?", 
				"Request for Undo", JOptionPane.YES_NO_OPTION);

		GameBoard.isClickable(true);
		if (result == JOptionPane.YES_OPTION){
			return true;
		} else {
			return false;
		}
	}

	private void gameBoardButtonsCustomFont(){
		CustomFonts.setCustomButtonFont(undoButton,UNDO_BUTTON_TEXT);
		CustomFonts.setCustomButtonFont(helpButton,HELP_BUTTON_TEXT);
		CustomFonts.setCustomButtonFont(endButton,EXIT_BUTTON_TEXT);
		CustomFonts.setCustomButtonFont(restartButton,RESTART_BUTTON_TEXT);
	}

	private void resetFireworksAndSound(){
		fireworks.stop();
		soundManager.stopFireworksSound();
	}
}