package core;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestBowl{
	@Test
	void testStrike(){
		assertFalse(Bowl.isStrike(1, 10));
		assertFalse(Bowl.isStrike(0, 11));
		assertFalse(Bowl.isStrike(0, 9));

		assertTrue(Bowl.isStrike(0, 10));
	}

	@Test
	void testSpare(){
		assertFalse(Bowl.isSpare(0, 7, 2));
		assertFalse(Bowl.isSpare(0, 7, 3));
		assertFalse(Bowl.isSpare(0, 10, 0));

		assertTrue(Bowl.isSpare(1, 0, 10));
		assertTrue(Bowl.isSpare(1, 7, 3));
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
}
