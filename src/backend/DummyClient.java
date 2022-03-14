package backend;

import java.util.Map;
import java.util.Scanner;

public class DummyClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		WordleLogic mechanics = new WordleLogic();
		
		Scanner in = new Scanner(System.in);
		
		for(int i = 0; i < 6; i++) {
			
			System.out.println("\n What is your next guess?");
			String guess = in.nextLine();
			mechanics.checkGuess(guess);
			
			if(mechanics.getCorrectPositionedCharacters().size() == 5) {
				System.out.println("\n Hurrah!! You win the game!");
				break;
			}
			
			for(Integer entry: mechanics.getCorrectPositionedCharacters().keySet())
				System.out.println("\nCorrect:"+mechanics.getCorrectPositionedCharacters().get(entry));
			
			for(Character c: mechanics.getIncorrectPositionedCharacters())
				System.out.println("\nIncorrect placed:"+c);
			
			for(Character c: mechanics.getAbsentCharacters())
				System.out.println("\nAbsent:"+c);
			
			
			
		}
		

	}

}
