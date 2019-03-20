package hr.fer.zemris.java.custom.collections.demo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class StackDemoTest {
	
	@Test
	public void testEvaluateValidExpression() {
		assertEquals(4, StackDemo.evaluate("8 2 /"));
		assertEquals(3, StackDemo.evaluate("-1 8 2 / +"));
		assertEquals(1, StackDemo.evaluate("3 2 /"));
		assertEquals(-16, StackDemo.evaluate("4 5 7 2 + - *"));
		assertEquals(2, StackDemo.evaluate("3 4 + 2  * 7 /"));
		assertEquals(48, StackDemo.evaluate("5 7 + 6 2 -  *"));
		assertEquals(18, StackDemo.evaluate("4 2 3 5 1 - + * +"));
		assertEquals(18, StackDemo.evaluate("4 2 + 3 5 1 -  * +"));
	}
	
	@Test
	public void testEvaluateInvalidExpression() {
		assertThrows(IllegalArgumentException.class, () -> StackDemo.evaluate("8 2 / /"));
		assertThrows(IllegalArgumentException.class, () -> StackDemo.evaluate("8 2 )"));
		assertThrows(IllegalArgumentException.class, () -> StackDemo.evaluate("3 3 4 + 2  * 7 /"));
		assertThrows(IllegalArgumentException.class, () -> StackDemo.evaluate("3 4 + + 2  * 7 /"));
		assertThrows(IllegalArgumentException.class, () -> StackDemo.evaluate("4 2 + 3 5 1 -  * + 32"));
	}
	
}
