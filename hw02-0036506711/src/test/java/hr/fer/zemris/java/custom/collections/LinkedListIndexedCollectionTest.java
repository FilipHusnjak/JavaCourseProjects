package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkedListIndexedCollectionTest {
	LinkedListIndexedCollection c1;
	LinkedListIndexedCollection c2;
	
	@BeforeEach
	public void setup() {
		c1 = new LinkedListIndexedCollection();
		c1.add(2);
		c1.add("Test");
		c1.add(2.0f);
	}

	
	// Constructor tests
	
	@Test
	public void testFirstConstructor() {
		c2 = new LinkedListIndexedCollection();

		assertEquals(0, c2.size());
	}
	
	@Test
	public void testSecondConstructor() {
		assertDoesNotThrow(() -> c2 = new LinkedListIndexedCollection(c1));
		
		assertArrayEquals(c1.toArray(), c2.toArray());
		
		assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection(null));
	}
	
	
	// Method tests

	@Test
	public void testToArray() {
		Object[] elements = c1.toArray();

		assertEquals(3, elements.length);
		
		assertArrayEquals(new Object[] {2, "Test", 2.0f}, elements);
	}

	@Test
	public void testAdd() {
		Object[] elements = c1.toArray();

		assertEquals(2, elements[0]);
		assertEquals("Test", elements[1]);
		assertEquals(2.0f, elements[2]);

		assertThrows(NullPointerException.class, () -> c1.add(null));
	}

	@Test
	public void testGet() {
		Object[] elements = c1.toArray();

		assertEquals(elements[0], c1.get(0));
		assertEquals(elements[1], c1.get(1));
		assertEquals(elements[2], c1.get(2));

		assertThrows(IndexOutOfBoundsException.class, () -> c1.get(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> c1.get(c1.size()));
	}

	@Test
	public void testClear() {
		c1.clear();

		assertEquals(0, c1.size());
	}

	@Test
	public void testSize() {
		c1.clear();
		assertEquals(0, c1.size());

		c1.add(0);
		c1.add(1);
		c1.add(2);

		assertEquals(3, c1.size());

		c1.clear();
	}

	@Test
	public void testIsEmpty() {
		c1.clear();

		assertTrue(c1.isEmpty());

		c1.add(0);
		c1.add(1);
		c1.add(2);

		assertFalse(c1.isEmpty());
	}

	@Test
	public void testInsert() {
		c1.insert(3, 1);
		assertEquals(3, c1.get(1));
		assertEquals("Test", c1.get(2));

		c1.insert("Java", 0);
		assertEquals("Java", c1.get(0));
		assertEquals(3, c1.get(2));

		assertThrows(IndexOutOfBoundsException.class, () -> c1.insert(1, -1));
		assertThrows(IndexOutOfBoundsException.class, () -> c1.insert(1, c1.size() + 1));

		assertThrows(NullPointerException.class, () -> c1.insert(null, 0));
	}

	@Test
	public void testIndexOf() {
		assertEquals(0, c1.indexOf(2));
		assertEquals(1, c1.indexOf("Test"));
		assertEquals(2, c1.indexOf(2.0f));
		assertEquals(-1, c1.indexOf(3));
	}

	@Test
	public void containsTest() {
		assertTrue(c1.contains(2));
		assertFalse(c1.contains(3));
	}

	@Test
	public void testRemoveAtIndex() {
		c1.remove(0);
		assertEquals(0, c1.indexOf("Test"));
		assertEquals(2, c1.size());

		c1.add(0);
		c1.add(1);
		c1.add(2);

		c1.remove(c1.size() - 1);
		assertEquals(4, c1.size());
		assertFalse(c1.contains(2));

		c1.remove(0);
		assertEquals(3, c1.size());
		assertFalse(c1.contains("Test"));

		assertThrows(IndexOutOfBoundsException.class, () -> c1.remove(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> c1.remove(c1.size()));
	}

	@Test
	public void testRemoveObject() {
		assertTrue(c1.contains("Test"));
		assertTrue(c1.remove("Test"));
		assertFalse(c1.contains("Test"));
		assertFalse(c1.remove("Test"));
	}

	@Test
	public void testForEach() {
		class TestProcessor extends Processor {
			int testIndex = 0;

			@Override
			public void process(Object value) {
				testIndex++;
			}
		}

		TestProcessor testProcessor = new TestProcessor();
		c1.forEach(testProcessor);
		assertEquals(3, testProcessor.testIndex);
	}
	
	@Test
	public void testAddAll() {
		c2 = new LinkedListIndexedCollection();
		c2.addAll(c1);
		
		assertArrayEquals(c1.toArray(), c2.toArray());
	}
}
