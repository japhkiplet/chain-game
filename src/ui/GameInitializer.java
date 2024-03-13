package ui;

import java.awt.Point;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import application.ChainGameFacade;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import network.ConnectionEstablishmentException;
import network.SynchronizationException;
import network.TransmissionException;
import domain.Settings;


public class GameInitializer {

	private static final String GAME_BOARD_SCENE_ID = "GameBoardScene.fxml";
	private static final String SPLASH_SCENE_ID = "SplashScreen.fxml";
	private static final String CHOOSE_MODE_SCENE_ID = "ChooseModeScene.fxml";
	
	private static final Point boardSceneDimensions = new Point(800, 800);
	private static boolean ignoreErrors = false;
	
	private GameInitializer() {
	}

	/**
	 * Initializes Chain Game asynchronously while displaying a loading screen until the initialization is complete.
	 * On initialization failure, displayer an error message and return the user to menu.
	 * 
	 * @param stage - top level container stage
	 */
	public static void initializeGame(Stage stage) {
		ignoreErrors = false;
		
		Thread initThread = setupInitializationThread(stage);
		loadScene(SPLASH_SCENE_ID);
		initThread.start();
	}

	private static Thread setupInitializationThread(Stage stage) {
		Task<Integer> initTask = new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				initializationRoutine(stage);
				return 0;
			}
		};

		Thread initThread = new Thread(initTask);
		initThread.setDaemon(true);
		return initThread;
	}
	
	private static void initializationRoutine(Stage stage) {
		try {
			ChainGameFacade.startGame(Settings.getInstance().getMode(),
					Settings.getInstance().getNumColumns(), Settings
							.getInstance().getNumRows());
			GameInitializer.onSuccess(stage);
			
		} catch (SocketTimeoutException e) {
			GameInitializer.onFailure("Connection timeout has occurred. Returning to menu.");
		} catch (UnknownHostException e) {
			GameInitializer.onFailure("The specified host does not exist. Returning to menu.");
		} catch (ConnectionEstablishmentException | TransmissionException e) {
			GameInitializer.onFailure("Connection failure: "+ e.getMessage() + ".\n Returning to menu.");
		} catch (SynchronizationException e) {
			GameInitializer.onFailure("Protocol mismatch detected. Try changing the protocol type. Returning to menu.");
		}
	}
	
	/**
	 * Ends the game or cancels initialization
	 */
	public static void endGame(){
		ignoreErrors = true;
		ChainGameFacade.endGame();
	}
	
	private static void onSuccess(Stage stage) {
		loadSceneOfNewSize(GAME_BOARD_SCENE_ID, stage, boardSceneDimensions);
	}

	private static void onFailure(String message) {
		if(!ignoreErrors){
			DialogProvider.displayErrorMessage(message);
		}
		
		ChainGameFacade.endGame();
		loadScene(CHOOSE_MODE_SCENE_ID);
	}
	
	private static void loadScene(String sceneId){
		Platform.runLater(()->{
			SceneLoader.loadScene(new Pane(),sceneId);
		});
	}
	
	private static void loadSceneOfNewSize(String sceneId, Stage stage, Point newDimensions){
		Platform.runLater(()->{
			SceneLoader.loadScene(new Pane(), sceneId,stage, newDimensions.getX(), newDimensions.getY());
		});
	}

}
