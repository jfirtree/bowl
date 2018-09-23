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
	final static String ERROR_EMPTY = "Empty input.  Please input number of pins knocked down in each frame.";
	final static String ERROR_NEGATIVE = "Invalid pin number: %d.  Number of pins knocked down must be zero or greater.";
	final static String ERROR_TOO_HIGH = "Invalid pin number: %d.  Number of pins knocked down must be at most %d.";

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

	//TODO: bonus balls from a strike in the last frame.
	public static List<Integer> score(List<Integer> rolls){
		int ballNum = 0;
		List<Integer> scores = new ArrayList<>();
		for(int i = 0; i < rolls.size(); i++){
			final int currRoll = rolls.get(i);
			int points;
			int lastRoll = i > 0 ? rolls.get(i - 1) : -1;
			if(isStrike(ballNum, currRoll)){
				points = sumSubList(rolls, i, 2);
			}else if(isSpare(ballNum, currRoll, lastRoll)){
				points = sumSubList(rolls, i, 1);
			}else{
				points = rolls.get(i);
			}
			scores.add(points);
			ballNum = (currRoll != NUM_PINS) ? (ballNum + 1) % 2 : 0;
		}
		return scores;
	}

	public static boolean isSpare(int ballNum, int currRoll, int lastRoll){
		return ballNum == 1 && currRoll == NUM_PINS - lastRoll;
	}

	public static boolean isStrike(int ballNum, int currRoll){
		return ballNum == 0 && currRoll == NUM_PINS;
	}

	private static int randomInt(int min, int max){
		Random rGen = new Random();
		int multiplier = max - min;
		return rGen.nextInt(multiplier + 1) + min;
	}
}
