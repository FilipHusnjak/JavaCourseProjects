package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

public class ObjectMultistackTest {

	@Test
	public void testPushNull() {
		assertThrows(NullPointerException.class, () -> new ObjectMultistack().push(null, new ValueWrapper(null)));
		assertThrows(NullPointerException.class, () -> new ObjectMultistack().push("test", null));
	}
	
	@Test
	public void testPushNonExistingKey() {
		ObjectMultistack ms = new ObjectMultistack();
		for (int i = 0; i < 1000; ++i) {
			ms.push(String.valueOf(i), new ValueWrapper(i));
		}
		for (int i = 0; i < 1000; ++i) {
			assertEquals(i, ms.peek(String.valueOf(i)).getValue());
		}
	}
	
	@Test
	public void testPushExistingKey() {
		ObjectMultistack ms = new ObjectMultistack();
		for (int i = 0; i < 1000; ++i) {
			ms.push("test", new ValueWrapper(i));
		}
		for (int i = 999; i >= 0; --i) {
			assertEquals(i, ms.pop("test").getValue());
		}
	}
	
	@Test
	public void testPushMixedKeys() {
		ObjectMultistack ms = new ObjectMultistack();
		for (int j = 0; j < 4; ++j) {
			for (int i = 0; i < 1000; ++i) {
				ms.push(String.valueOf(j), new ValueWrapper(i));
			}
		}
		for (int j = 0; j < 4; ++j) {
			for (int i = 999; i >= 0; --i) {
				assertEquals(i, ms.pop(String.valueOf(j)).getValue());
			}
		}
	}
	
	@Test
	public void testIsEmpty() {
		ObjectMultistack ms = new ObjectMultistack();
		for (int j = 0; j < 4; ++j) {
			for (int i = 0; i < 1000; ++i) {
				ms.push(String.valueOf(j), new ValueWrapper(i));
			}
		}
		for (int j = 0; j < 4; ++j) {
			assertFalse(ms.isEmpty(String.valueOf(j)));
			for (int i = 999; i >= 0; --i) {
				assertEquals(i, ms.pop(String.valueOf(j)).getValue());
			}
			assertTrue(ms.isEmpty(String.valueOf(j)));
		}
	}
	
	@Test
	public void testPeekNullKey() {
		assertThrows(NullPointerException.class, () -> new ObjectMultistack().peek(null));
	}
	
	@Test
	public void testPeekNormal() {
		ObjectMultistack ms = new ObjectMultistack();
		for (int i = 0; i < 1000; ++i) {
			ms.push(String.valueOf(i), new ValueWrapper(i));
		}
		for (int i = 0; i < 1000; ++i) {
			assertEquals(i, ms.peek(String.valueOf(i)).getValue());
		}
	}
	
	@Test
	public void testPeekEmptyStack() {
		ObjectMultistack ms = new ObjectMultistack();
		assertThrows(EmptyStackException.class, () -> ms.peek("test"));
		ms.push("test", new ValueWrapper(null));
		ms.pop("test");
		assertThrows(EmptyStackException.class, () -> ms.peek("test"));
	}
	
	@Test
	public void testPopNullKey() {
		assertThrows(NullPointerException.class, () -> new ObjectMultistack().pop(null));
	}
	
	@Test
	public void testPopNormal() {
		ObjectMultistack ms = new ObjectMultistack();
		for (int i = 0; i < 1000; ++i) {
			ms.push("test", new ValueWrapper(i));
		}
		for (int i = 999; i >= 0; --i) {
			assertEquals(i, ms.pop("test").getValue());
		}
		assertTrue(ms.isEmpty("test"));
	}
	
	@Test
	public void testPopEmptyStack() {
		ObjectMultistack ms = new ObjectMultistack();
		assertThrows(EmptyStackException.class, () -> ms.pop("test"));
		ms.push("test", new ValueWrapper(null));
		ms.pop("test");
		assertThrows(EmptyStackException.class, () -> ms.pop("test"));
	}
	
}
