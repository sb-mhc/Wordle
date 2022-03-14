package backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;



public class WordleLogic {
	
	WordRepo repo;
	String word_of_the_day;
	
	HashMap<Integer,Character> correct_position_chars;
	Set<Character> incorrect_position_chars;
	Set<Character> absent_chars;
	Set<Character> unguessed_chars;
	
	
	/**
	 * Choose word of the day randomly
	 * and prepare a sublist of words that have same letters
	 */
	
	public WordleLogic(){
		
		repo = new WordRepo();
		int choose = (int)(Math.random()*repo.getSize());
		
		word_of_the_day = repo.getWord(choose);
		
		correct_position_chars = new HashMap<Integer,Character>();
		incorrect_position_chars = new HashSet<Character>();
		absent_chars = new HashSet<Character>();
		unguessed_chars = new HashSet<Character>(Arrays.asList(WordRepo.ALPHABET));
	}
	
	public void checkGuess(String guessed_word) {
		resetSets();
		for(int i = 0; i < WordRepo.LENGTH; i++) {
			Character letter = guessed_word.charAt(i);
			
			unguessed_chars.remove(letter);		
			if(word_of_the_day.charAt(i) == letter) {	
				correct_position_chars.put(i,letter);
			} else if(word_of_the_day.indexOf(letter) != -1 && word_of_the_day.indexOf(letter) != i) {
				incorrect_position_chars.add(letter);
			} else {
				absent_chars.add(letter);
			}
		}
	}
	
	public HashMap<Integer,Character> getCorrectPositionedCharacters(){
		return correct_position_chars;
	}
	
	public Set<Character> getIncorrectPositionedCharacters(){
		return incorrect_position_chars;
	}
	
	public Set<Character> getAbsentCharacters(){
		return absent_chars;
	}
	
	private void resetSets() {
		correct_position_chars.clear();
		incorrect_position_chars.clear();
	}
	
	public Boolean isValidGuess(String guess) {
		return repo.isValid(guess);
		
	}
	
	public String getWordOfTheDay() {
		return word_of_the_day;
	}

}
