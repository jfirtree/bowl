package core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestResult{
	@Test
	void testStrike(){
		assertNotEquals(Result.STRIKE, Result.evaluate(1, 10, -1));
		assertNotEquals(Result.STRIKE, Result.evaluate(0, 11, -1));
		assertNotEquals(Result.STRIKE, Result.evaluate(0, 9, -1));

		assertEquals(Result.STRIKE, Result.evaluate(0, 10, -1));
	}

	@Test
	void testSpare(){
		assertNotEquals(Result.SPARE, Result.evaluate(0, 7, 2));
		assertNotEquals(Result.SPARE, Result.evaluate(0, 7, 3));
		assertNotEquals(Result.SPARE, Result.evaluate(0, 10, 0));

		assertEquals(Result.SPARE, Result.evaluate(1, 0, 10));
		assertEquals(Result.SPARE, Result.evaluate(1, 7, 3));
	}

	@Test
	void testSymbol(){
		assertEquals("-", Result.NORMAL.symbolForRoll(0));
		assertEquals("5", Result.NORMAL.symbolForRoll(5));
		assertEquals("/", Result.SPARE.symbolForRoll(5));
		assertEquals("X", Result.STRIKE.symbolForRoll(10));
	}
}
