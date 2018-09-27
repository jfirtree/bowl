package core;

enum Result{
	NORMAL(0), SPARE(1), STRIKE(2);
	final public int bonusRolls;

	Result(int bonusRolls){
		this.bonusRolls = bonusRolls;
	}

	static Result evaluate(int ballNum, int currRoll, int lastRoll){
		if(ballNum == 0 && currRoll == ScoreCard.NUM_PINS) return STRIKE;
		else if(ballNum == 1 && currRoll == ScoreCard.NUM_PINS - lastRoll) return SPARE;
		else return NORMAL;
	}
}
