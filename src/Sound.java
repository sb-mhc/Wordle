import javafx.scene.media.AudioClip;

public class Sound {
    private AudioClip soundEffect;

    public Sound(String filePath) {
        soundEffect = new AudioClip(getClass().getResource(filePath).toExternalForm());
    	 
    }

    public void playClip() {
        soundEffect.play();
    }
    
    public void stopClip() {
        if(soundEffect.isPlaying()) soundEffect.stop();
    }
    
    public void slowPlay() {
        soundEffect.setRate(0.4);
        soundEffect.play();
    }
 
    public boolean isPlayingClip() {
        return soundEffect.isPlaying();
    }
    
}