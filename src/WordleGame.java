import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import backend.WordleLogic;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class WordleGame extends Application {
	
	private Scene overallScene;
	private Stage stage;
	private Group gameScene;
	private VBox gameControl;
	private VBox keyBoard;
	private VBox guesses;
	
	private Button startButton;
	private Button soundButton;
	private Button nextGameButton;
	private Button exitButton;
	private String soundButtonGraphic;
	private Label scoreLabel;
	private ComboBox<String> difficultyLevelMenu;
	
	private ArrayList<HBox> keyRows;
	private ArrayList<ArrayList<Button>> letterKeys;
	
	private ArrayList<HBox> guessRows;
	private ArrayList<ArrayList<TextField>> letterGuesses;
	
	
	private Defines DEF = new Defines();
	private WordleLogic LOGIC;
	
	private Integer guessRowNumber = 0;
	private Integer guessLetterNumber = 0;
	private Boolean notGuessedYet = true;
	private String soundChoice = "on";
	private String difficultyChoice = "Intermediate";
	
	private boolean firstEntry = false;
	private Integer score = 0;
	
	
	
	public static void main(String[] args) {
		
		launch(args);
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// initialize scene graphs and UIs
        resetGameControl();    // resets the gameControl
        resetGameScene();  // resets the gameScene
    	resetKeyBoard(); // resets the keyboard
    	
    	VBox gamePart = new VBox();
    	HBox root = new HBox();
    	
    	VBox.setMargin(gameScene,  new Insets(10,0,0,10));
    	gamePart.setSpacing(10);
    	gamePart.getChildren().addAll(gameScene, keyBoard);
    	gamePart.setAlignment(Pos.BASELINE_CENTER);
    	root.setStyle("-fx-background-color: #F5F1C8; ");
		HBox.setMargin(gamePart, new Insets(0,0,0,15));
		root.getChildren().addAll(gamePart, gameControl);
		
		
		// add scene graphs to scene
		overallScene = new Scene(root, DEF.APP_WIDTH, DEF.APP_HEIGHT);
		overallScene.getStylesheets().add("resources/style.css");
        
		
		
        // finalize and show the stage
        primaryStage.setScene(overallScene);
        primaryStage.setTitle(DEF.STAGE_TITLE);
        primaryStage.setResizable(false);
		primaryStage.show();
		
		overallScene.setOnKeyPressed(this::handlePhysicalKeyboard);
		
	}
	

	public void resetGameControl() {
	 
		scoreLabel = new Label("Score:" + score.toString());
		soundButton = new Button();
		//Setting the size of the button
		soundButton.setPrefSize(30, 30);
		
	    //Setting a graphic to the button
		soundButton.setGraphic(DEF.IMVIEW.get("sound-"+soundChoice+"-icon"));
		soundButtonGraphic = "sound-"+soundChoice+"-icon";
		DEF.AUDIO.get("game_play").playClip();
	    soundButton.setOnMouseClicked(this::toggleSound);
		
		nextGameButton = new Button("New Game");
		exitButton = new Button("Exit");
		nextGameButton.setOnMouseClicked(this::nextGameRound);
		exitButton.setOnMouseClicked(this::exitGame);
		
		difficultyLevelMenu = new ComboBox();
		difficultyLevelMenu.getItems().addAll("Easy","Intermediate","Difficult");
		difficultyLevelMenu.setValue(difficultyLevelMenu.getItems().get(1));
		
        gameControl = new VBox();
        gameControl.setPadding(new Insets(20,0,0,0));
        gameControl.setSpacing(10);
        gameControl.setAlignment(Pos.TOP_CENTER);
        HBox abtGames = new HBox();
        abtGames.setSpacing(10);
        abtGames.getChildren().addAll(nextGameButton, exitButton);
        gameControl.getChildren().addAll(scoreLabel, soundButton, abtGames, difficultyLevelMenu);
	}
	
	
	//reset the keyboard every game round
	public void resetKeyBoard() {
	
        keyBoard = new VBox();
        keyRows = new ArrayList<HBox>();
        letterKeys = new ArrayList<ArrayList<Button>>();
        
        // get the keyboard letters row-wise
        ArrayList<ArrayList<Character>> letters = new ArrayList<ArrayList<Character>>();
        letters.add(new ArrayList<Character>(Arrays.asList('Q','W','E','R','T','Y','U','I','O','P')));
        letters.add(new ArrayList<Character>(Arrays.asList('A','S','D','F','G','H','J','K','L')));
        letters.add(new ArrayList<Character>(Arrays.asList('Z','X','C','V','B','N','M')));

        
       
        for(int i = 0; i < 3; i++) {
        	
        	// create buttons for each letter in a given row
        	ArrayList<Button> buttons_per_row = new ArrayList<Button>();
        	
        	if(i==1) {
        		Button letterButton = new Button("Backspace");
        		letterButton.setOnMouseClicked(this::keysClickHandler);
        		buttons_per_row.add(letterButton);
        	}
        	
        	for(Character c : letters.get(i)) {
        		Button letterButton = new Button(c.toString());
        		letterButton.setOnMouseClicked(this::keysClickHandler);
        		buttons_per_row.add(letterButton);
        	}
        	
        	if(i==1) {
        		Button letterButton = new Button("Enter");
        		letterButton.setOnMouseClicked(this::keysClickHandler);
        		buttons_per_row.add(letterButton);
        	}
        	
        	//store those keys
        	letterKeys.add(buttons_per_row);
        	// arrange the keys horizontally
        	HBox row = new HBox();
        	row.setSpacing(5);
        	row.getChildren().addAll(buttons_per_row);
        	keyRows.add(row);
        	
        }
        
        keyRows.get(0).setAlignment(Pos.CENTER);
        keyRows.get(2).setAlignment(Pos.CENTER);
        // add all rows vertically
        keyBoard.setSpacing(5);
        keyBoard.getChildren().addAll(keyRows);
        
	}
	
	
	public void resetGameScene() {
		
		 LOGIC = new WordleLogic();
         gameScene = new Group();
         guesses = new VBox();
         guessRows = new ArrayList<HBox>();
         letterGuesses = new ArrayList<ArrayList<TextField>>();
         
         if(firstEntry) {
        	 // create a stack for background, buttons & difficulty-levels
        	 StackPane canvas = new StackPane();
        	 
        	 startButton = new Button("Start Game");
        	 startButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-text-font-family:Courier; -fx-font-size: 30");
     		  
             // create a background
             ImageView background = DEF.IMVIEW.get("background");
             
             canvas.getChildren().addAll(background, startButton);
             
             // create the game scene
             gameScene = new Group();
             gameScene.getChildren().addAll(canvas);
             
         } else {

        	 for(int i = 0; i < DEF.NO_OF_GUESSES; i++) {

        		 // create textfields for each letter in a given row
        		 ArrayList<TextField> fields_per_row = new ArrayList<TextField>();

        		 for(int j = 0; j < DEF.GUESS_LENGTH; j++) {
        			 TextField tf = new TextField();
        			 tf.setEditable(false);
        			 fields_per_row.add(tf);
        		 }

        		 //store the rows of textfields
        		 letterGuesses.add(fields_per_row);

        		 // arrange the keys horizontally
        		 HBox row = new HBox();
        		 row.getChildren().addAll(fields_per_row);
        		 guessRows.add(row);	

        	 }

        	 guesses.getChildren().addAll(guessRows);

        	 gameScene.getChildren().add(guesses);
         }
         
        // initialize timer
     
		
	}
	
	public void toggleSound(MouseEvent e) {
		
		if(soundButtonGraphic.equals("sound-on-icon")) {
			soundButtonGraphic = "sound-off-icon";
			soundButton.setGraphic(DEF.IMVIEW.get("sound-off-icon"));
			soundChoice="off";
			DEF.AUDIO.get("game_play").stopClip();
		} else {
			soundButtonGraphic = "sound-on-icon";
			soundButton.setGraphic(DEF.IMVIEW.get("sound-on-icon"));
			soundChoice="on";
			DEF.AUDIO.get("game_play").playClip();
		}
	}
	
	public void handlePhysicalKeyboard(KeyEvent e) {
		    if (e.getCode() == KeyCode.ALPHANUMERIC) {
		        System.out.println("A key was pressed"+((KeyCode)e.getSource()).getChar());
		    }
	}
	
	public void keysClickHandler(MouseEvent e) {
		difficultyLevelMenu.setDisable(true);
		
		Button clickedKey = (Button)e.getSource();
		
		if(clickedKey.getText() == "Enter") {
			
			StringBuilder currentGuess = new StringBuilder();
			
			for(int i = 0; i < DEF.GUESS_LENGTH; i++) {
				currentGuess.append(letterGuesses.get(guessRowNumber).get(i).getText());
			}
			
			if(!letterGuesses.get(guessRowNumber).get(DEF.GUESS_LENGTH-1).getText().isEmpty()) {
				
				if(LOGIC.isValidGuess(currentGuess.toString())) {
					
					LOGIC.checkGuess(currentGuess.toString());
					colorTheBoxes(LOGIC.getCorrectPositionedCharacters(),LOGIC.getIncorrectPositionedCharacters(),LOGIC.getAbsentCharacters());
					guessLetterNumber = 0;
					guessRowNumber++;
					if(notGuessedYet && guessRowNumber < DEF.NO_OF_GUESSES)
						letterGuesses.get(guessRowNumber).get(guessLetterNumber).requestFocus();
					
					if(notGuessedYet && guessRowNumber >= DEF.NO_OF_GUESSES)
						updateLoseScore();
					
				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning Dialog");
					alert.setHeaderText("Not a valid word!");
					alert.showAndWait();
				}
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning Dialog");
				alert.setHeaderText("Word is not complete!");
				
				alert.showAndWait();
				
			}
			
		} else if(clickedKey.getText() == "Backspace") {
			if(guessLetterNumber >= 1 && guessLetterNumber <= DEF.GUESS_LENGTH) {
				guessLetterNumber--;
				letterGuesses.get(guessRowNumber).get(guessLetterNumber).setText("");
				if(guessLetterNumber==-1)guessLetterNumber=0;
			} 
			
		} else {
			
			if(guessLetterNumber < DEF.GUESS_LENGTH) {
				letterGuesses.get(guessRowNumber).get(guessLetterNumber).setText(clickedKey.getText());
				guessLetterNumber++;
			}
		}
		
		
		if(!notGuessedYet) {
			updateWinScore();
		}
	}
	
	
	public void colorTheBoxes(HashMap<Integer,Character> colorGreen, Set<Character> colorYellow, Set<Character> colorGrey) {
		
		//color the guesses
		int colored = 0;
		for(int i = 0; i < DEF.GUESS_LENGTH; i++) {
			
			String keyText = letterGuesses.get(guessRowNumber).get(i).getText();
			
			if(keyText.length() == 1) {
			
				if(colorGreen.containsKey(i) && colorGreen.get(i).equals((keyText.charAt(0)))){
					letterGuesses.get(guessRowNumber).get(i).setStyle("-fx-background-color:#82E0AA");
					colored++;
				} else if(colorYellow.contains(keyText.charAt(0))){
					letterGuesses.get(guessRowNumber).get(i).setStyle("-fx-background-color:#F9E79F");
				} else {
					letterGuesses.get(guessRowNumber).get(i).setStyle("-fx-background-color:#F5B7B1");
				}
			}
		}
		
		if(colored == DEF.GUESS_LENGTH) notGuessedYet = false;
		
		//disable absent keys
		for(int i = 0; i < letterKeys.size(); i++) {
			
			for(int j = 0; j < letterKeys.get(i).size(); j++) {
				
				String keyText = letterKeys.get(i).get(j).getText();
				if(keyText.length() == 1 && colorGrey.contains(keyText.charAt(0))) {
					letterKeys.get(i).get(j).setDisable(true);
				}
			
			}
		}
		
	}
	
	public void updateWinScore() {
		score++;
		scoreLabel.setText("Score:"+score);
		if(soundChoice == "on") {
			DEF.AUDIO.get("game_play").stopClip();
			DEF.AUDIO.get("win").slowPlay();
		}
		
		winThisGameRound();
		
	}
	
	public void updateLoseScore() {
		scoreLabel.setText("Score:"+score);
		if(soundChoice=="on") {
			DEF.AUDIO.get("game_play").stopClip();
			DEF.AUDIO.get("lose").playClip();
		}
		loseThisGameRound();
	}

	public void nextGameRound(MouseEvent e) {
		DEF.AUDIO.get("game_play").playClip();
		difficultyLevelMenu.setDisable(false);
		stage = (Stage)(gameControl.getScene().getWindow());
		resetGameVariables();
		
		resetGameScene();
		resetKeyBoard();
		
		VBox gamePart = new VBox();
    	HBox root = new HBox();
    	
    	VBox.setMargin(gameScene, new Insets(10,0,0,10));
    	gamePart.setSpacing(10);
    	gamePart.getChildren().addAll(gameScene, keyBoard);
    	gamePart.setAlignment(Pos.BASELINE_CENTER);
    	root.setStyle("-fx-background-color: #F5F1C8; ");
		HBox.setMargin(gamePart, new Insets(0,0,0,15));
		root.getChildren().addAll(gamePart, gameControl);
		overallScene = new Scene(root, DEF.APP_WIDTH, DEF.APP_HEIGHT);
		overallScene.getStylesheets().add("resources/style.css");
		
		stage.setScene(overallScene);
		stage.show();
	}
	
	public void exitGame(MouseEvent e) {
		stage = (Stage)exitButton.getScene().getWindow();
		stage.close();
	}
	
	public void resetGameVariables() {
		guessRowNumber = 0;
		guessLetterNumber = 0;
		notGuessedYet = true;
	}
	
	
	
	private Random random = new Random();
	public void winThisGameRound() {
		
		stage = (Stage)(gameControl.getScene().getWindow());
		
		
		VBox gamePart = new VBox();
		StackPane sp = new StackPane();
    	HBox root = new HBox();
    	
    	VBox.setMargin(gameScene, new Insets(10,0,0,10));
    	gamePart.setSpacing(10);
    	gamePart.getChildren().addAll(gameScene, keyBoard);
    	gamePart.setAlignment(Pos.BASELINE_CENTER);
    	StackPane.setMargin(gamePart, new Insets(0,0,0,15));
    	sp.getChildren().addAll(gamePart);
    	
    	Circle c[] = new Circle[2000];
		for (int i = 0; i < 2000; i++) {
            c[i] = new Circle(-DEF.SCENE_WIDTH,1,1);
            c[i].setRadius(random.nextDouble() * 3);
            Color color = Color.rgb((int)Math.ceil((new Random()).nextFloat()*255),(int) Math.ceil((new Random()).nextFloat()*255), (int)Math.ceil((new Random()).nextFloat()*255), 0.5);
            c[i].setFill(color);
            //StackPane.setMargin(c[i], new Insets(0,0,0,30));
            sp.getChildren().add(c[i]);
            Raining(c[i]);
        }
    	
    	root.setStyle("-fx-background-color: #F5F1C8; ");
		HBox.setMargin(sp, new Insets(0,0,0,15));
		root.getChildren().addAll(sp, gameControl);
		overallScene = new Scene(root, DEF.APP_WIDTH, DEF.APP_HEIGHT);
		overallScene.getStylesheets().add("resources/style.css");
		
		stage.setScene(overallScene);
		stage.show();
		
	}
	
	public void loseThisGameRound() {
		
		stage = (Stage)(gameControl.getScene().getWindow());
		
		
		VBox gamePart = new VBox();
		StackPane sp = new StackPane();
    	HBox root = new HBox();
    	Label answer = new Label(LOGIC.getWordOfTheDay());
    	
    	
    	VBox.setMargin(gameScene, new Insets(10,0,0,10));
    	gamePart.setSpacing(10);
    	gamePart.getChildren().addAll(gameScene, keyBoard, answer);
    	
    	gamePart.setAlignment(Pos.BASELINE_CENTER);
    	root.setStyle("-fx-background-color: #F5F1C8;");
    	
		HBox.setMargin(sp, new Insets(0,0,0,15));
		root.getChildren().addAll(gamePart, gameControl);
		overallScene = new Scene(root, DEF.APP_WIDTH, DEF.APP_HEIGHT);
		overallScene.getStylesheets().add("resources/style.css");
		
		stage.setScene(overallScene);
		stage.show();
		
	}
	
	
	public void Raining(Circle c) {
        c.setCenterX(Math.pow(-1,random.nextInt(2))*(random.nextInt(DEF.SCENE_WIDTH) - DEF.SCENE_WIDTH));//Window width = 950
        int time = 5000 + random.nextInt(500);
        TranslateTransition walk = new TranslateTransition(Duration.millis(time),c);
        walk.setFromY(-(DEF.SCENE_HEIGHT)/2);
        walk.setToY(DEF.SCENE_HEIGHT); //WIndow height = 534
        walk.setToX(random.nextDouble() * c.getCenterX() *Math.pow(-1,random.nextInt(1)));
        walk.play();
    }

}
