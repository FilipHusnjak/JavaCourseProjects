package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

public class DictionaryTest {
	
	Dictionary<Integer, String> d;
	
	@BeforeEach
	public void setup() {
		d = new Dictionary<>();
	}
	
	@Test
	public void testSize() {
		assertEquals(0, d.size());
		d.put(1, "A");
		d.put(2, "B");
		d.put(3, "C");
		assertEquals(3, d.size());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(d.isEmpty());
		d.put(1, "A");
		assertFalse(d.isEmpty());
	}
	
	@Test
	public void testPutNullKey() {
		assertThrows(NullPointerException.class, () -> d.put(null, "A"));
	}
	
	@Test
	public void testPutNullValue() {
		d.put(1, null);
		assertNull(d.get(1));;
		assertEquals(1, d.size());
	}
	
	@Test
	public void testPutMultipleValues() {
		d.put(1, "A");
		d.put(2, "B");
		d.put(3, "C");
		d.put(4, "D");
		d.put(5, "E");
		assertEquals(5, d.size());
		assertEquals("A", d.get(1));
		assertEquals("B", d.get(2));
		assertEquals("C", d.get(3));
		assertEquals("D", d.get(4));
		assertEquals("E", d.get(5));
	}
	
	@Test
	public void testPutSameValue() {
		d.put(1, "A");
		d.put(2, "A");
		d.put(3, "A");
		d.put(4, "B");
		d.put(5, "B");
		assertEquals(5, d.size());
		assertEquals("A", d.get(1));
		assertEquals("A", d.get(2));
		assertEquals("A", d.get(3));
		assertEquals("B", d.get(4));
		assertEquals("B", d.get(5));
	}
	
	@Test
	public void testPutSameKey() {
		d.put(1, "A");
		d.put(1, "C");
		d.put(3, "A");
		d.put(3, "B");
		d.put(2, "B");
		assertEquals(3, d.size());
		assertEquals("C", d.get(1));
		assertEquals("B", d.get(2));
		assertEquals("B", d.get(3));
	}
	
	@Test
	public void testGetNullValue() {
		d.put(1, "A");
		assertNull(d.get(null));
	}
	
	@Test
	public void testGetNonExistingKey() {
		d.put(1, "A");
		d.put(1, "C");
		d.put(3, "A");
		d.put(3, "B");
		assertNull(d.get(2));
	}
	
	@Test
	public void testGetMultipleValues() {
		d.put(1, "A");
		assertEquals("A", d.get(1));
		d.put(1, "C");
		assertEquals("C", d.get(1));
		d.put(3, "A");
		assertEquals("A", d.get(3));
		d.put(3, "B");
		assertEquals("B", d.get(3));
		assertNull(d.get(2));
		d.put(2, "B");
		assertEquals("B", d.get(2));
		assertEquals(3, d.size());
	}
	
	@Test
	public void testClear() {
		assertEquals(0, d.size());
		d.put(1, "A");
		d.put(1, "C");
		d.put(3, "A");
		d.put(3, "B");
		d.put(2, "B");
		assertEquals(3, d.size());
		d.clear();
		assertEquals(0, d.size());
	}
	
	@Test
	public void testLargeData() {
		for (int i = 0; i < 10000; ++i) {
			d.put(i, String.valueOf(i));
		}
		assertEquals(10000, d.size());
		for (int i = 0; i < 10000; ++i) {
			assertEquals(String.valueOf(i), d.get(i));
		}
	}
	
}
