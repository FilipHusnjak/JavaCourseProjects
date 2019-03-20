package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class ObjectStackTest {
	
	@Test
	public void testPush() {
		ObjectStack s1 = new ObjectStack();
		s1.push(0);
		s1.push(1);
		s1.push(2);
		s1.push(3);
		s1.push(4);
		s1.push(5);
		
		assertEquals(6, s1.size());
		assertEquals(5, s1.pop());
		assertEquals(4, s1.pop());
		assertEquals(3, s1.pop());
		assertEquals(2, s1.pop());
		assertEquals(1, s1.pop());
		assertEquals(0, s1.pop());
	}
	
	public void testPop() {
		ObjectStack s1 = new ObjectStack();
		assertThrows(EmptyStackException.class, () -> s1.pop());
		s1.push(0);
		s1.push(1);
		
		assertEquals(1, s1.pop());
		assertEquals(0, s1.pop());
		
		assertThrows(EmptyStackException.class, () -> s1.pop());
	}
	
	@Test
	public void testSize() {
		ObjectStack s1 = new ObjectStack();
		assertEquals(0, s1.size());

		s1.push(0);
		s1.push(1);
		s1.push(2);

		assertEquals(3, s1.size());
	}
	
	@Test
	public void testClear() {
		ObjectStack s1 = new ObjectStack();
		
		s1.push(0);
		s1.push(1);
		s1.push(2);
		
		s1.clear();
		assertEquals(0, s1.size());
	}

	@Test
	public void testIsEmpty() {
		ObjectStack s1 = new ObjectStack();

		assertTrue(s1.isEmpty());

		s1.push(0);
		s1.push(1);
		s1.push(2);

		assertFalse(s1.isEmpty());
	}
	
	public void testPeek() {
		ObjectStack s1 = new ObjectStack();
		assertThrows(EmptyStackException.class, () -> s1.peek());
		
		s1.push(0);
		s1.push(1);
		
		assertEquals(1, s1.peek());
		assertEquals(1, s1.peek());
	}
	
}
