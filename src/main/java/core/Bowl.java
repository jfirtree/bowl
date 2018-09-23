package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Bowl{
	final static int NUM_PINS = 10;
	final static int NUM_FRAMES = 10;
	final static String ERROR_EMPTY = "Empty input.  Please input number of pins knocked down in each frame.";
	final static String ERROR_NEGATIVE = "Invalid pin number: %d.  Number of pins knocked down must be zero or greater.";
	final static String ERROR_TOO_HIGH = "Invalid pin number: %d.  Number of pins knocked down must be at most %d.";
	final static String ERROR_SUM_PINS_HIGH = "Frame %d reports %d and %d pins, for a total of %d pins.  Number of pins knocked down per frame must be at most %d.";

	final static String WARN_ALPHABET = "Warning: reading only numeric and separator input.  Ignoring letters.";

	enum Result{
		NORMAL(0), SPARE(1), STRIKE(2);
		final public int bonusRolls;

		Result(int bonusRolls){
			this.bonusRolls = bonusRolls;
		}

		static Result evaluate(int ballNum, int currRoll, int lastRoll){
			if(ballNum == 0 && currRoll == NUM_PINS) return STRIKE;
			else if(ballNum == 1 && currRoll == NUM_PINS - lastRoll) return SPARE;
			else return NORMAL;
		}
	}

	public static void main(String[] args){
		List<Integer> rolls = parseArgs(args);
		System.out.println(rolls);
		final List<Integer> scores = score(rolls);
		System.out.println(scores);
		System.out.println(scores.stream().mapToInt(Integer::intValue).sum());
	}

	static List<Integer> parseArgs(String[] args){
		if(args.length < 1){
			throw new IllegalArgumentException(ERROR_EMPTY);
		}
		String input = Arrays.stream(args).collect(Collectors.joining());
		if(input.matches("(.*)[a-zA-Z](.*)")){
			System.out.println(WARN_ALPHABET);
		}
		return extractNumbers(input);
	}

	static List<Integer> extractNumbers(String input){
		final Matcher matcher = Pattern.compile("([-]*\\d+)").matcher(input);
		List<Integer> toReturn = new ArrayList<>();
		while(matcher.find()){
			final int roll = Integer.parseInt(matcher.group());
			if(roll < 0) throw new IllegalArgumentException(String.format(ERROR_NEGATIVE, roll));
			if(roll > NUM_PINS) throw new IllegalArgumentException(String.format(ERROR_TOO_HIGH, roll, NUM_PINS));
			toReturn.add(roll);
		}

		return toReturn;
	}

	static List<Integer> generateRolls(){
		List<Integer> toReturn = new ArrayList<>();
		toReturn.add(randomInt(0, NUM_PINS));
		int ballNum = 0;
		for(int i = 1; i < 20; i++){
			final int prevBall = toReturn.get(i - 1);
			if(prevBall == NUM_PINS) ballNum = 0;
			else ballNum = (ballNum + 1) % 2;
			int upperBound = ballNum == 0 ? NUM_PINS : NUM_PINS - prevBall;
			toReturn.add(randomInt(0, upperBound));
		}

		return toReturn;
	}

	static int sumSubList(List<Integer> list, final int startIndex, final int additionalAddends){
		return IntStream.rangeClosed(startIndex, startIndex + additionalAddends).map(i -> list.get(i)).sum();
	}

	public static List<Integer> score(List<Integer> rolls){
		int ballNum = 0;
		int frameNum = 1;
		List<Integer> scores = new ArrayList<>();
		int i;
		for(i = 0; frameNum < NUM_FRAMES; i++){
			final int currRoll = rolls.get(i);
			int lastRoll = i > 0 ? rolls.get(i - 1) : -1;
			if(ballNum == 1 && (lastRoll + currRoll) > NUM_PINS){
				throw new IllegalArgumentException(String.format(ERROR_SUM_PINS_HIGH, frameNum, lastRoll, currRoll, lastRoll + currRoll, NUM_PINS));
			}
			Result result = Result.evaluate(ballNum, currRoll, lastRoll);
			scores.add(sumSubList(rolls, i, result.bonusRolls));

			if(result == Result.STRIKE || ballNum == 1){
				ballNum = 0;
				frameNum++;
			}else{
				ballNum++;
			}
		}

		//TODO: bonus balls from a strike or spare in the last frame.
		//TODO: varying pin max validation based on strike, spare, or nothing.
		int remainingBalls = 2;
		for(ballNum = 0; ballNum < remainingBalls; ballNum++){
			scores.add(rolls.get(i + ballNum));
		}

		return scores;
	}

	public static boolean isLastFrame(int frameNum){
		return frameNum == NUM_FRAMES;
	}

	private static int randomInt(int min, int max){
		Random rGen = new Random();
		int multiplier = max - min;
		return rGen.nextInt(multiplier + 1) + min;
	}
}
