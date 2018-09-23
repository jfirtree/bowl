package core;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestBowl{
	@Test
	void testStrike(){
		assertNotEquals(Bowl.Result.STRIKE, Bowl.Result.evaluate(1, 10, -1));
		assertNotEquals(Bowl.Result.STRIKE, Bowl.Result.evaluate(0, 11, -1));
		assertNotEquals(Bowl.Result.STRIKE, Bowl.Result.evaluate(0, 9, -1));

		assertEquals(Bowl.Result.STRIKE, Bowl.Result.evaluate(0, 10, -1));
	}

	@Test
	void testSpare(){
		assertNotEquals(Bowl.Result.SPARE, Bowl.Result.evaluate(0, 7, 2));
		assertNotEquals(Bowl.Result.SPARE, Bowl.Result.evaluate(0, 7, 3));
		assertNotEquals(Bowl.Result.SPARE, Bowl.Result.evaluate(0, 10, 0));

		assertEquals(Bowl.Result.SPARE, Bowl.Result.evaluate(1, 0, 10));
		assertEquals(Bowl.Result.SPARE, Bowl.Result.evaluate(1, 7, 3));
	}

	@Test
	void testIsLastFrame(){
		assertFalse(Bowl.isLastFrame(8));
		assertFalse(Bowl.isLastFrame(1));
		assertTrue(Bowl.isLastFrame(10));
	}

	@Test
	void testSublistSum(){
		Integer arr[] = {3, 1, 4, 1, 5, 9, 5, 3, 5};
		List<Integer> list = Arrays.asList(arr);

		assertEquals(3, Bowl.sumSubList(list, 0, 0));
		assertEquals(8, Bowl.sumSubList(list, 0, 2));
		assertEquals(15, Bowl.sumSubList(list, 3, 2));
		assertEquals(8, Bowl.sumSubList(list, 7, 1));
	}

	@Test
	void testTrivialGame(){
		assertEquals(0, Bowl.score(Collections.nCopies(20, 0)).stream().mapToInt(Integer::intValue).sum());
		assertEquals(80, Bowl.score(Collections.nCopies(20, 4)).stream().mapToInt(Integer::intValue).sum());
	}

	@Test
	void testSpareScoring(){
		assertEquals(150, Bowl.score(Collections.nCopies(21, 5)).stream().mapToInt(Integer::intValue).sum());
		List<Integer> rolls = new ArrayList<>(Collections.nCopies(19, 4));
		rolls.add(6);
		rolls.add(2);
		assertEquals(84, Bowl.score(rolls).stream().mapToInt(Integer::intValue).sum());
	}

	@Test
	void testStrikeScoring(){
		assertEquals(300, Bowl.score(Collections.nCopies(12, 10)).stream().mapToInt(Integer::intValue).sum());
	}

	@Test
	void testNotEnoughRollsReported(){
		//Trivial game - need 20 balls.
		final List<Integer> rolls = new ArrayList<>(Collections.nCopies(15, 4));
		IndexOutOfBoundsException e = assertThrows(IndexOutOfBoundsException.class, () -> Bowl.score(rolls));
		assertEquals(String.format(Bowl.ERROR_NOT_ENOUGH_ROLLS, 8, 1), e.getMessage());

		//Still trivial, just testing that error message reports correct frame and ball number.
		rolls.add(4);
		e = assertThrows(IndexOutOfBoundsException.class, () -> Bowl.score(rolls));
		assertEquals(String.format(Bowl.ERROR_NOT_ENOUGH_ROLLS, 8, 2), e.getMessage());

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
	void testHighArg(){
		String arg = "12";
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Bowl.extractNumbers(arg));
		assertEquals(String.format(Bowl.ERROR_TOO_HIGH, 12, Bowl.NUM_PINS), e.getMessage());
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
		assertEquals(String.format(Bowl.ERROR_SUM_PINS_HIGH, 2, 8, 4, 12, Bowl.NUM_PINS), e.getMessage());
	}
}
