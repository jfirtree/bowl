package core;

import org.junit.jupiter.api.Test;

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

		assertEquals(Bowl.sumSubList(list, 0, 0), 3);
		assertEquals(Bowl.sumSubList(list, 0, 2), 8);
		assertEquals(Bowl.sumSubList(list, 3, 2), 15);
		assertEquals(Bowl.sumSubList(list, 7, 1), 8);
	}

	@Test
	void testTrivialGame(){
		assertEquals(Bowl.score(Collections.nCopies(20, 0)).stream().mapToInt(Integer::intValue).sum(), 0);
		assertEquals(Bowl.score(Collections.nCopies(20, 4)).stream().mapToInt(Integer::intValue).sum(), 80);
	}

	@Test
	void testNoArgs(){
		String[] args = {};
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Bowl.parseArgs(args));
		assertEquals(e.getMessage(), Bowl.ERROR_EMPTY);
	}

	@Test
	void testHighArg(){
		String arg = "12";
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Bowl.extractNumbers(arg));
		assertEquals(e.getMessage(), String.format(Bowl.ERROR_TOO_HIGH, 12, Bowl.NUM_PINS));
	}

	@Test
	void testNegativeArg(){
		String arg = "-1";
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Bowl.extractNumbers(arg));
		assertEquals(e.getMessage(), String.format(Bowl.ERROR_NEGATIVE, -1));
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
		assertEquals(e.getMessage(), String.format(Bowl.ERROR_SUM_PINS_HIGH, 2, 8, 4, 12, Bowl.NUM_PINS));
	}
}
