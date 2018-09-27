package core;

import java.util.ArrayList;
import java.util.List;

public class ScoreCard{
	final static String ERROR_SUM_PINS_HIGH = "Frame %d reports %d and %d pins, for a total of %d pins.  "
			+ "Number of pins knocked down per frame must be at most %d.";
	final static String ERROR_GAME_OVER = "Cannot add further rolls.  The game is already completed.";
	final static int NUM_PINS = 10;
	final static int NUM_FRAMES = 10;

	private int ballNum;
	private int frameNum;
	private int prevRoll;
	private int maxRoll;
	private final List<Integer> rollwiseScores;
	private final List<Result> results;

	public int getBallNum(){
		return ballNum;
	}

	public int getFrameNum(){
		return frameNum;
	}

	private ScoreCard(){
		ballNum = 0;
		frameNum = 1;
		prevRoll = 0;
		updateMaxRoll();
		rollwiseScores = new ArrayList<>();
		results = new ArrayList<>();
	}

	public boolean gameOver(){
		return isLastFrame() && lastFrameDone();
	}

	private boolean lastFrameDone(){
		int lastIndex = rollwiseScores.size() - 1;
		return ballNum == 3 || ballNum == 2 && (rollwiseScores.get(lastIndex) + rollwiseScores.get(lastIndex - 1)) < NUM_PINS;
	}

	private void updateMaxRoll(){
		if(ballNum == 0 || (isLastFrame() && prevRoll == NUM_PINS)){
			maxRoll = NUM_PINS;
		}else{
			maxRoll = NUM_PINS - prevRoll;
		}
	}

	public boolean isLastFrame(){
		return frameNum == NUM_FRAMES;
	}

	public static ScoreCard create(){
		return new ScoreCard();
	}

	private void finish(){
		final int totalRolls = results.size();
		for(int i = 0; i < totalRolls; i++){
			final Result currResult = results.get(i);
			int bonus = 0;
			for(int j = 1; j <= currResult.bonusRolls; j++){
				bonus += rollwiseScores.get(i + j);
			}
			if(bonus != 0)
				rollwiseScores.set(i, rollwiseScores.get(i) + bonus);
		}
	}

	public int getScore(){
		return rollwiseScores.stream().mapToInt(Integer::intValue).sum();
	}

	public Result add(int currRoll){
		if(currRoll > maxRoll)
			throw new IllegalArgumentException(String.format(ERROR_SUM_PINS_HIGH, frameNum, prevRoll, currRoll, prevRoll + currRoll, NUM_PINS));
		if(gameOver())
			throw new IllegalArgumentException(ERROR_GAME_OVER);

		final Result result = !isLastFrame() ? Result.evaluate(ballNum, currRoll, prevRoll) : Result.NORMAL;
		rollwiseScores.add(currRoll);
		results.add(result);

		if(!isLastFrame() && newFrameActivated(result)){
			ballNum = 0;
			frameNum++;
		}else{
			ballNum++;
		}

		prevRoll = currRoll;

		updateMaxRoll();

		if(gameOver()){
			finish();
		}
		return result;
	}

	private boolean newFrameActivated(Result result){
		return result == Result.STRIKE || ballNum == 1;
	}

}
