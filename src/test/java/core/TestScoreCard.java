package core;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class TestScoreCard{
	@Test
	void testSimpleGame(){
		final ScoreCard scoreCard = ScoreCard.create();

		assertEquals(1, scoreCard.getFrameNum());
		assertEquals(Result.STRIKE, scoreCard.add(10));
		assertEquals(2, scoreCard.getFrameNum());
		assertEquals(0, scoreCard.getBallNum());
		scoreCard.add(4);
		assertEquals(2, scoreCard.getFrameNum());
		assertEquals(1, scoreCard.getBallNum());

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> scoreCard.add(7));
		assertEquals(String.format(ScoreCard.ERROR_SUM_PINS_HIGH, 2, 4, 7, 11, ScoreCard.NUM_PINS), e.getMessage());
		assertEquals(Result.SPARE, scoreCard.add(6));
		assertFalse(scoreCard.isLastFrame());

		IntStream.range(0, 14).forEach(i -> scoreCard.add(3));
		assertTrue(scoreCard.isLastFrame());
		assertEquals(Result.NORMAL, scoreCard.add(3));
		scoreCard.add(3);
		assertTrue(scoreCard.gameOver());
		assertEquals(81, scoreCard.getScore());

		e = assertThrows(IllegalArgumentException.class, () -> scoreCard.add(5));
		assertEquals(ScoreCard.ERROR_GAME_OVER, e.getMessage());
	}

	@Test
	void testHighScoringGame(){
		final ScoreCard scoreCard = ScoreCard.create();

		IntStream.range(0, 8).forEach(i -> scoreCard.add(10));
		assertFalse(scoreCard.isLastFrame());
		assertEquals(Result.STRIKE, scoreCard.add(10));
		assertTrue(scoreCard.isLastFrame());
		assertFalse(scoreCard.gameOver());

		scoreCard.add(10);
		scoreCard.add(10);
		assertEquals(2, scoreCard.getBallNum());
		assertFalse(scoreCard.gameOver());
		scoreCard.add(10);
		assertTrue(scoreCard.gameOver());
		assertEquals(300, scoreCard.getScore());

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> scoreCard.add(5));
		assertEquals(ScoreCard.ERROR_GAME_OVER, e.getMessage());
	}

	@Test
	void testPredicatesTrivial(){
		final ScoreCard scoreCard = ScoreCard.create();
		assertFalse(scoreCard.isLastFrame());
		assertFalse(scoreCard.gameOver());
	}
}
