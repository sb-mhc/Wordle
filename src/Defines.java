import java.util.HashMap;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Defines {
	
	final int APP_HEIGHT = 500;
    final int APP_WIDTH = 700;
    final int SCENE_HEIGHT = 570;
    final int SCENE_WIDTH = 400;
    
    final int CANVAS_HEIGHT = 170;
    final int CANVAS_WIDTH = 150;

    // coefficients related to the blob
    final int BLOB_WIDTH = 30;
    final int BLOB_HEIGHT = 30;
    final int BLOB_POS_X = 70;
    final int BLOB_POS_Y = 200;
    final int BLOB_DROP_TIME = 300000000;  	// the elapsed time threshold before the blob starts dropping
    final int BLOB_DROP_VEL = 300;    		// the blob drop velocity
    final int BLOB_FLY_VEL = -40;
    final int BLOB_IMG_LEN = 4;
    final int BLOB_IMG_PERIOD = 5;
    
    // coefficients related to the floors
    final int FLOOR_WIDTH = 400;
    final int FLOOR_HEIGHT = 100;
    final int FLOOR_COUNT = 2;
    
    // coefficients related to time
    final int SCENE_SHIFT_TIME = 5;
    final double SCENE_SHIFT_INCR = -0.4;
    final double NANOSEC_TO_SEC = 1.0 / 1000000000.0;
    final double TRANSITION_TIME = 0.1;
    final int TRANSITION_CYCLE = 2;
    
    // coefficients related to the bird
    final int BIRD_WIDTH = 50;
    final int BIRD_HEIGHT = 45;
    final int BIRD_POS_X = 0;
    final int BIRD_POS_Y = 0;
    final int BIRD_DROP_TIME = 300;   // controls the elapse time threshold before the bird starts dropping
    final int BIRD_DROP_ACC = 200;    // controls the bird drop acceleration
    final int BIRD_HIT_SPEED_X = -200;
    final int BIRD_HIT_SPEED_Y = 400;
    final int BIRD_ELEVATE_SPEED = -20;
    final int BIRD_ANIMATE_LEN = 4;
    final int MAX_LIVES = 3;
    
    final int PARACHUTE_POS_X = (int)(SCENE_WIDTH/2.5);
    final int PARACHUTE_POS_Y = BLOB_HEIGHT;
    final double PARACHUTE_FALL_VELOCITY = 0.01;
    final long PARACHUTE_TIME = 10;
    
    final String STAGE_TITLE = "Unlimited Wordle";
   
    private final String IMAGE_DIR = "resources/images/";
    final String[] IMAGE_FILES = {"sound-off-icon","sound-on-icon","confetti", "exit", "start","background"};
    private final String AUDIO_DIR = "resources/sounds/";
    private final String[] AUDIO_FILES = {"win", "lose", "game_play"};

    final HashMap<String, Sound> AUDIO = new HashMap<String, Sound>();
    final HashMap<String, ImageView> IMVIEW = new HashMap<String, ImageView>();
    final HashMap<String, Image> IMAGE = new HashMap<String, Image>();
    
    final int GUESS_LENGTH = 5;
    final int NO_OF_GUESSES = 6;
    
    
    Defines(){
    	
    	for(int i=0; i<AUDIO_FILES.length; i++) {
			if( i < 2)
				AUDIO.put(AUDIO_FILES[i], new Sound(AUDIO_DIR+AUDIO_FILES[i]+".wav"));
			else
				AUDIO.put(AUDIO_FILES[i], new Sound(AUDIO_DIR+AUDIO_FILES[i]+".mp3"));
    	}
    	
    	for(int i=0; i<IMAGE_FILES.length; i++) {
    		if(i<=1) {
    			Image img = new Image(pathImage(IMAGE_FILES[i]), BLOB_WIDTH, BLOB_HEIGHT, false, false);
    			IMAGE.put(IMAGE_FILES[i],img);
    		} else if (i==2) {
    			Image img = new Image(pathImage(IMAGE_FILES[i]), APP_WIDTH, 0.5*APP_HEIGHT, false, false);
    			IMAGE.put(IMAGE_FILES[i],img);
    		} else if (i<=4) {
    			Image img = new Image(pathImage(IMAGE_FILES[i]), BLOB_WIDTH, 0.8*BLOB_HEIGHT, false, false);
    			IMAGE.put(IMAGE_FILES[i],img);
    		} else {
    			Image img = new Image(pathImage(IMAGE_FILES[i]), SCENE_WIDTH, SCENE_HEIGHT, false, false);
    			IMAGE.put(IMAGE_FILES[i],img);
    		}
    	}
    	
    	for(int i=0; i<IMAGE_FILES.length; i++) {
    		ImageView imgView = new ImageView(IMAGE.get(IMAGE_FILES[i]));
    		IMVIEW.put(IMAGE_FILES[i],imgView);
    	}
    }
    
    public String pathImage(String filepath) {
    	String fullpath = getClass().getResource(IMAGE_DIR+filepath+".png").toExternalForm();
    	return fullpath;
    }
	
	public Image resizeImage(String filepath, int width, int height) {
    	IMAGE.put(filepath, new Image(pathImage(filepath), width, height, false, false));
    	return IMAGE.get(filepath);
    }

}
