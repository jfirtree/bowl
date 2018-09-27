package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Bowl{
	final static String ERROR_EMPTY = "Empty input.  Please input number of pins knocked down in each frame.";
	final static String ERROR_NEGATIVE = "Invalid pin number: %d.  Number of pins knocked down must be zero or greater.";
	final static String ERROR_TOO_HIGH = "Invalid pin number: %d.  Number of pins knocked down must be at most %d.";
	final static String ERROR_NOT_ENOUGH_ROLLS = "Not enough rolls provided.  Missing starting at frame %d, ball %d.";
	final static String ERROR_TOO_MANY_ROLLS = "Too many rolls provided.  You provided %d, but there should only be %d.";

	final static String WARN_ALPHABET = "Warning: reading only numeric and separator input.  Ignoring letters.";

	final static String PATTERN_ANY_LETTER = "(.*)[a-zA-Z](.*)";
	final static String PATTERN_ANY_NUMBER = "([-]*\\d+)";

	public static void main(String[] args){
		try{
			List<Integer> rolls = parseArgs(args);
			final ScoreCard scoreCard = score(rolls);
			System.out.println(scoreCard.getScorePrintout());
			System.out.printf("Your total score: %d\n", scoreCard.getScore());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	/* Join all user arguments into a single string, and return a list of distinct numbers from that list. */
	static List<Integer> parseArgs(String[] args){
		if(args.length < 1){
			throw new IllegalArgumentException(ERROR_EMPTY);
		}
		String input = Arrays.stream(args).collect(Collectors.joining(" "));
		if(input.matches(PATTERN_ANY_LETTER)){
			System.out.println(WARN_ALPHABET);
		}
		return extractNumbers(input);
	}

	/* Using a basic regular expression, find each nonnegative number.
	@throws IllegalArgumentException on encountering any negative number.
	 */
	static List<Integer> extractNumbers(String input){
		final Matcher matcher = Pattern.compile(PATTERN_ANY_NUMBER).matcher(input);
		List<Integer> toReturn = new ArrayList<>();
		while(matcher.find()){
			final int roll = Integer.parseInt(matcher.group());
			if(roll < 0) throw new IllegalArgumentException(String.format(ERROR_NEGATIVE, roll));
			if(roll > ScoreCard.NUM_PINS)
				throw new IllegalArgumentException(String.format(ERROR_TOO_HIGH, roll, ScoreCard.NUM_PINS));
			toReturn.add(roll);
		}

		return toReturn;
	}

	/* Core method to validate user's inputted rolls, and build a corresponding ScoreCard. */
	public static ScoreCard score(List<Integer> rolls){
		final int totalRolls = rolls.size();
		int i = 0;
		final ScoreCard scoreCard = ScoreCard.create();

		while(!scoreCard.isLastFrame()){
			scoreCard.add(rolls.get(i));
			if((++i) >= totalRolls)
				throw new IndexOutOfBoundsException(String.format(ERROR_NOT_ENOUGH_ROLLS, scoreCard.getFrameNum(), scoreCard.getBallNum() + 1));
		}

		int remainingBalls = rolls.get(i) + rolls.get(i + 1) >= ScoreCard.NUM_PINS ? 3 : 2;
		int inputRemaining = totalRolls - i;
		if(inputRemaining < remainingBalls)
			throw new IndexOutOfBoundsException(String.format(ERROR_NOT_ENOUGH_ROLLS, ScoreCard.NUM_PINS, inputRemaining));
		else if(inputRemaining > remainingBalls)
			throw new IllegalArgumentException(String.format(ERROR_TOO_MANY_ROLLS, totalRolls, i + remainingBalls));

		for(int ballNum = 0; ballNum < remainingBalls; ballNum++){
			scoreCard.add(rolls.get(i + ballNum));
		}

		return scoreCard;
	}

}
