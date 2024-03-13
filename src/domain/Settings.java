package domain;

import javafx.scene.layout.Pane;
import domain.GameMode;

public class Settings {

	private static Settings instance = new Settings();
	private int numRows;
	private int numColumns;
	private int difficulty;
	
	public static enum NetworkConfig {HOST, CLIENT, NOT_SET}
	private NetworkConfig networkConfig = NetworkConfig.NOT_SET;
	public static enum ProtocolType {NATIVE, COMPATIBILITY}
	private ProtocolType protocolType = ProtocolType.NATIVE;
	
	private String hostIP;
	private int portNumber;
	private Pane centerPane;
	private GameMode mode;

	private Settings() {
	}

	public int getNumRows() {
		return numRows;
	}
	
	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public void setNumColumns(int numColumns) {
		this.numColumns = numColumns;
	}
	
	public void setNetworkConfig(NetworkConfig config){
		this.networkConfig = config;
	}
	
	public NetworkConfig getNetworkConfig(){
		return this.networkConfig;
	}
	
	public void setProtocolType(ProtocolType type){
		this.protocolType =type;
	}
	
	public ProtocolType getProtocolType(){
		return this.protocolType;
	}
	
	public void setPortNumber(int portNumber){
		this.portNumber = portNumber;
	}
	
	public int getPortNumber(){
		return portNumber;
	}
	
	public void setHostIP(String hostIP){
		this.hostIP = hostIP;
	}
	
	public String getHostIP(){
		return hostIP;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public GameMode getMode() {
		return mode;
	}

	public void setMode(GameMode mode) {
		this.mode = mode;
	}
	
	public void setCenterPane(Pane centerPane){
		this.centerPane = centerPane;
	}
	
	public Pane getCenterPane(){
		return centerPane;
	}

	public static Settings getInstance() {
		return instance;
	}

}
