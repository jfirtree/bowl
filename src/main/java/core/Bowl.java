package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Bowl{
	final private static int NUM_PINS = 10;

	public static void main(String[] args){
		List<Integer> rolls = generateRolls();
		final Integer[] ints = {10, 7, 3, 7, 2, 9, 1, 10, 10, 10, 2, 3, 6, 4, 7, 3, 3};
		//rolls = Arrays.asList(ints);
		System.out.println(rolls);
		final List<Integer> scores = score(rolls);
		System.out.println(scores);
		System.out.println(scores.stream().mapToInt(Integer::intValue).sum());
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
