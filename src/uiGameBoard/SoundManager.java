package uiGameBoard;

import ui.UISettings;
import javafx.scene.media.AudioClip;

public class SoundManager {

	public  final  AudioClip PEG_HOVER_SOUND = new AudioClip(getClass().getResource("/sounds/PEG_HOVER_SOUND.mp3").toString());
	public  final  AudioClip PEG_PLACED_SOUND = new AudioClip(getClass().getResource("/sounds/PEG_PLACED_SOUND.mp3").toString());
	public final  AudioClip FIREWORKS_SOUND = new AudioClip(getClass().getResource("/sounds/FIREWORKS_SOUND.mp3").toString());
    private static final int DEFAULT_VOLUME_LEVEL = 50;

	public SoundManager(){

	}

	public void playPegHoverSound(){
		if(!UISettings.isSoundMute()){
			PEG_HOVER_SOUND.play(DEFAULT_VOLUME_LEVEL);
		}
	}

	public void playPegPlacedSound(){
		if(!UISettings.isSoundMute()){
			PEG_PLACED_SOUND.play(DEFAULT_VOLUME_LEVEL);
		}
	}
	
	public void playFireworksSound(){
		if(!UISettings.isSoundMute()){
			FIREWORKS_SOUND.play(DEFAULT_VOLUME_LEVEL);
		}
	}
	
	public void stopFireworksSound(){
		if(FIREWORKS_SOUND.isPlaying()){
			FIREWORKS_SOUND.stop();
		}
	}
	
}
