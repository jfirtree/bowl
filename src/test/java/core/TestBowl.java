package core;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestBowl{

	@Test
	void testTrivialGame(){
		assertEquals(0, Bowl.score(Collections.nCopies(20, 0)).getScore());
		assertEquals(80, Bowl.score(Collections.nCopies(20, 4)).getScore());
	}

	@Test
	void testSpareScoring(){
		assertEquals(150, Bowl.score(Collections.nCopies(21, 5)).getScore());
		List<Integer> rolls = new ArrayList<>(Collections.nCopies(19, 4));
		rolls.add(6);
		rolls.add(2);
		assertEquals(84, Bowl.score(rolls).getScore());
	}

	@Test
	void testStrikeScoring(){
		assertEquals(300, Bowl.score(Collections.nCopies(12, 10)).getScore());
	}

	@Test
	void testNotEnoughRollsReported(){
		//Trivial game - need 20 balls.
		final List<Integer> rolls = new ArrayList<>(Collections.nCopies(15, 4));
		IndexOutOfBoundsException e = assertThrows(IndexOutOfBoundsException.class, () -> Bowl.score(rolls));
		assertEquals(String.format(Bowl.ERROR_NOT_ENOUGH_ROLLS, 8, 2), e.getMessage());

		//Still trivial, just testing that error message reports correct frame and ball number.
		rolls.add(4);
		e = assertThrows(IndexOutOfBoundsException.class, () -> Bowl.score(rolls));
		assertEquals(String.format(Bowl.ERROR_NOT_ENOUGH_ROLLS, 9, 1), e.getMessage());

		//Strike - need 3 balls in 10th frame
		rolls.clear();
		rolls.addAll(new ArrayList<>(Collections.nCopies(11, 10)));
		e = assertThrows(IndexOutOfBoundsException.class, () -> Bowl.score(rolls));
		assertEquals(String.format(Bowl.ERROR_NOT_ENOUGH_ROLLS, 10, 2), e.getMessage());

	}

	@Test
	void testTooManyRollsReported(){
		final List<Integer> rolls = new ArrayList<>(Collections.nCopies(13, 10));
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Bowl.score(rolls));
		assertEquals(String.format(Bowl.ERROR_TOO_MANY_ROLLS, 13, 12), e.getMessage());
	}

	@Test
	void testNoArgs(){
		String[] args = {};
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Bowl.parseArgs(args));
		assertEquals(Bowl.ERROR_EMPTY, e.getMessage());
	}

	@Test
	void testParseArgs(){
		String[] args = {"1,     2},3%%%%,^4,!!!5,8?"};
		List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 8);
		assertEquals(expected, Bowl.parseArgs(args));
	}

	@Test
	void testHighArg(){
		String arg = "12";
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Bowl.extractNumbers(arg));
		assertEquals(String.format(Bowl.ERROR_TOO_HIGH, 12, ScoreCard.NUM_PINS), e.getMessage());
	}

	@Test
	void testNegativeArg(){
		String arg = "-1";
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Bowl.extractNumbers(arg));
		assertEquals(String.format(Bowl.ERROR_NEGATIVE, -1), e.getMessage());
	}

	@Test
	void testNumberParse(){
		String arg = "8,2,1,3,5";
		List<Integer> expected = Arrays.asList(8, 2, 1, 3, 5);
		assertEquals(Bowl.extractNumbers(arg), expected);
	}

	@Test
	void testHighFrame(){
		List<Integer> expected = Arrays.asList(1, 2, 8, 4, 1, 3, 5, 6);
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Bowl.score(expected));
		assertEquals(String.format(ScoreCard.ERROR_SUM_PINS_HIGH, 2, 8, 4, 12, ScoreCard.NUM_PINS), e.getMessage());
	}

	@Test
	void testHighFinalFrame(){
		final List<Integer> rolls = new ArrayList<>(Collections.nCopies(19, 4));
		rolls.add(9);
		rolls.add(2);
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Bowl.score(rolls));
		assertEquals(String.format(ScoreCard.ERROR_SUM_PINS_HIGH, 10, 4, 9, 13, 10), e.getMessage());

		for(int i = 18; i < 21; i++){
			rolls.set(i, ScoreCard.NUM_PINS);
		}
		assertEquals(102, Bowl.score(rolls).getScore());
	}
}
