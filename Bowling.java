package bowling;
/*
 * Author: Kevin Ruffin
 * 
 * Enter data to simulate a bowling game.
 * XXXXXXXXXXXX	(10+10+10) + (10+10+10) + (10+10+10) + (10+10+10) + (10+10+10) + (10+10+10) + (10+10+10) + (10+10+10) + (10+10+10) + (10+10+10)	300
 * 9-9-9-9-9-9-9-9-9-9-	9 + 9 + 9 + 9 + 9 + 9 + 9 + 9 + 9 + 9	90
 * 5/5/5/5/5/5/5/5/5/5/5	(10+5) + (10+5) + (10+5) + (10+5) + (10+5) + (10+5) + (10+5) + (10+5) + (10+5) + (10+5)	150
 * X7/9-X-88/-6XXX81	(10+7+3) + (7+3+9) + 9 + (10+0+8) + 8 + (8+2+0) + 6 + (10+10+10) + (10+10+8) + (10+8+1)	167
 */

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Bowling {
	private static final int perfectGame = 300;
	private static final int frames = 10;
	private static final int pins = 10;
	private static final int keepSize = 2;
	private static final char spare = '/';
	private static final char strike = 'X';
	private static final char miss = '-';
	private static final int strikeScore = 10;
	private String gameRolls = "";
	private Scanner input = null;
	public static boolean quit = false;
	private boolean gotLastSpare = false;
	private int spareRoll = 0;	
	private int frameCount = 0;
	private int set = 0;
	private int score = 0;
	private LinkedList<Integer> keeper = null;

	Bowling(){		
		input = new Scanner(System.in);
		keeper = new LinkedList<Integer>();
	}

	public void readInputLine(){
		try{
			System.out.print("Enter test case or empty line to exit. ");
			gameRolls = input.nextLine();
			input.reset();

			quit = (gameRolls.length() == 0) ? true : false;
			if(quit){
				try{
					input.close();
				}
				catch(IllegalStateException err){
					System.err.println("Error closing Scanner");
				};

				return;
			}

			scoreGame();
		}
		catch(NoSuchElementException e){
			try{
				input.close();
			}
			catch(IllegalStateException err){
				System.err.println("Error closing Scanner");
			}
		}
		catch(IllegalStateException e){
			try{
				input.close();
			}
			catch(IllegalStateException err){
				System.err.println("Error closing Scanner");
			}
		}
	}


	private void trackRolls(Integer x){
		keeper.addLast(x);
		if(keeper.size() > keepSize){
			//Drop head entry
			keeper.pop();
		}
	}

	private int addToScore(int amount ) throws NoSuchElementException {
		score += amount;
		if(frameCount > frames){
			if(keeper.size() > 0){
				keeper.pop();
			}
		}

		if(!keeper.isEmpty()){
			if(keeper.size() == keepSize){
				if(keeper.peekFirst().intValue() == strikeScore){
					score += amount;
				}

				if(keeper.peekLast().intValue() == strikeScore){
					score += amount;
				}
			}
			else{
				if(keeper.element().intValue() == strikeScore){
					score += amount;
				}								
			}
		}

		return score;
	}

	private void scoreGame(){
		char roll = '\0';		
		gotLastSpare = false;
		int pinsDowned = frameCount = score = set = spareRoll = 0;
		keeper.clear();

		for(int index = 0; index < gameRolls.length(); index++){
			try{
				roll = gameRolls.charAt(index);	
				if(set == 0){
					frameCount++;
					set = 1;
				}

				switch(roll){
				case strike:
					addToScore(strikeScore);
					
					//Don't add spare here after final frame
					if(gotLastSpare && (frameCount <= frames)){
						addToScore(strikeScore);
						gotLastSpare = false;
					}

					if(frameCount <= frames){
						trackRolls(new Integer(strikeScore));
					}
					set = 0;
					break;
				case miss:
					pinsDowned = 0;
					if(frameCount <= frames){
						trackRolls(new Integer(0));
					}

					if(set == 1){
						set++;
					}
					else{
						set = 0;
					}

					gotLastSpare = false;					
					break;
				case spare:
					//Calculate spare pins.
					spareRoll = Math.abs(pinsDowned - pins);

					addToScore(spareRoll);
					gotLastSpare = true;
					pinsDowned = 0;	
					if(frameCount <= frames){
						trackRolls(new Integer(spareRoll));
					}
					set = 0;
					break;
				default:
					pinsDowned = Character.getNumericValue(roll);
					addToScore(pinsDowned);

					//Don't add spare here after final frame
					if(gotLastSpare && (frameCount <= frames)){	
						addToScore(Character.getNumericValue(roll));
						gotLastSpare = false;
					}

					if(frameCount <= frames){
						trackRolls(new Integer(pinsDowned));
					}

					if(set == 1){
						set++;
					}
					else{
						set = 0;
					}				
				}
			}
			catch(NoSuchElementException e){
				System.err.println("NoSuchElementException" + e.getMessage());
				break;
			}
			catch(Exception e){
				System.err.println("Exception" + e.getMessage());
				break;
			}
		}

		if(score == perfectGame)
			System.out.println("You ROCK!!!");
		System.out.println("Game: " + gameRolls + " scored: " + score + "\n");			
	}

	public static void main(String[] args) {
		Bowling game = new Bowling();
		while(!Bowling.quit){
			game.readInputLine();
		}

		System.out.println("Normal End of Job");
	}
}
