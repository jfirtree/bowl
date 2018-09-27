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
	private int lastFrameIndex;

	public List<Integer> getRollwiseScores(){
		return rollwiseScores;
	}

	private final List<Integer> rollwiseScores;
	private final List<Result> results;
	private final List<Integer> rollingTotal;

	public String getScorePrintout(){
		return scorePrintout;
	}

	private String scorePrintout;

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
		rollingTotal = new ArrayList<>();
		scorePrintout = "";
		lastFrameIndex = -1;
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

	public List<Integer> getRollingTotal(){
		return rollingTotal;
	}

	private void finish(){
		String frameBuffer = "";
		StringBuilder individualRollBuilder = new StringBuilder("|");
		StringBuilder cumulativeBuilder = new StringBuilder("|");
		final int totalRolls = results.size();
		int total = 0;
		int ballCount = 0;

		for(int i = 0; i < totalRolls; i++){
			final Result currResult = results.get(i);
			final int pureRoll = rollwiseScores.get(i);
			frameBuffer += currResult.symbolForRoll(pureRoll);

			//Bonuses applied in every frame but the final one.
			if(i < lastFrameIndex){
				int bonus = 0;
				for(int j = 1; j <= currResult.bonusRolls; j++){
					bonus += rollwiseScores.get(i + j);
				}
				if(bonus != 0)
					rollwiseScores.set(i, pureRoll + bonus);
			}
			total += rollwiseScores.get(i);
			rollingTotal.add(total);

			if(i < lastFrameIndex && newFrameActivated(currResult, ballCount)){
				individualRollBuilder.append(String.format("%1$4s", frameBuffer));
				frameBuffer = "";
				individualRollBuilder.append('|');
				ballCount = 0;
				cumulativeBuilder.append(String.format("%1$4s", rollingTotal.get(i)));
				cumulativeBuilder.append('|');
			}else{
				frameBuffer += ' ';
				ballCount++;
			}
		}
		individualRollBuilder.append(String.format(" %1$6s", frameBuffer));
		individualRollBuilder.append('|');
		cumulativeBuilder.append(String.format("%1$7s", rollingTotal.get(rollingTotal.size() - 1)));
		cumulativeBuilder.append('|');

		scorePrintout = individualRollBuilder.toString() + "\n" + cumulativeBuilder.toString();
	}

	public int getScore(){
		return rollwiseScores.stream().mapToInt(Integer::intValue).sum();
	}

	public Result add(int currRoll){
		if(currRoll > maxRoll)
			throw new IllegalArgumentException(String.format(ERROR_SUM_PINS_HIGH, frameNum, prevRoll, currRoll, prevRoll + currRoll, NUM_PINS));
		if(gameOver())
			throw new IllegalArgumentException(ERROR_GAME_OVER);

		final Result result = Result.evaluate(ballNum, currRoll, prevRoll);
		rollwiseScores.add(currRoll);
		results.add(result);

		if(!isLastFrame() && newFrameActivated(result, ballNum)){
			ballNum = 0;
			frameNum++;
			if(isLastFrame()) lastFrameIndex = rollwiseScores.size();
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

	private static boolean newFrameActivated(Result result, int ballNum){
		return result == Result.STRIKE || ballNum == 1;
	}

}
