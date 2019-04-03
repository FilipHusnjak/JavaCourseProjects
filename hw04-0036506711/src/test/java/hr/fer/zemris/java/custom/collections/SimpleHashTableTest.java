package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SimpleHashTableTest {

	SimpleHashtable<Integer, String> map;

	@BeforeEach
	public void setup() {
		map = new SimpleHashtable<>(2);
	}

	@Test
	public void testSize() {
		assertEquals(0, map.size());
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		assertEquals(3, map.size());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(map.isEmpty());
		map.put(1, "A");
		assertFalse(map.isEmpty());
	}

	@Test
	public void testPutNullKey() {
		assertThrows(NullPointerException.class, () -> map.put(null, "A"));
	}

	@Test
	public void testPutNullValue() {
		map.put(1, null);
		assertNull(map.get(1));
		;
		assertEquals(1, map.size());
	}

	@Test
	public void testPutMultipleValues() {
		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");
		map.put(4, "map");
		map.put(5, "E");
		assertEquals(5, map.size());
		assertEquals("A", map.get(1));
		assertEquals("B", map.get(2));
		assertEquals("C", map.get(3));
		assertEquals("map", map.get(4));
		assertEquals("E", map.get(5));
	}

	@Test
	public void testPutSameValue() {
		map.put(1, "A");
		map.put(2, "A");
		map.put(3, "A");
		map.put(4, "B");
		map.put(5, "B");
		assertEquals(5, map.size());
		assertEquals("A", map.get(1));
		assertEquals("A", map.get(2));
		assertEquals("A", map.get(3));
		assertEquals("B", map.get(4));
		assertEquals("B", map.get(5));
	}

	@Test
	public void testPutSameKey() {
		map.put(1, "A");
		map.put(1, "C");
		map.put(3, "A");
		map.put(3, "B");
		map.put(2, "B");
		assertEquals(3, map.size());
		assertEquals("C", map.get(1));
		assertEquals("B", map.get(2));
		assertEquals("B", map.get(3));
	}

	@Test
	public void testGetNullValue() {
		map.put(1, "A");
		assertNull(map.get(null));
	}

	@Test
	public void testGetNonExistingKey() {
		map.put(1, "A");
		map.put(1, "C");
		map.put(3, "A");
		map.put(3, "B");
		assertNull(map.get(2));
	}

	@Test
	public void testGetMultipleValues() {
		map.put(1, "A");
		assertEquals("A", map.get(1));
		map.put(1, "C");
		assertEquals("C", map.get(1));
		map.put(3, "A");
		assertEquals("A", map.get(3));
		map.put(3, "B");
		assertEquals("B", map.get(3));
		assertNull(map.get(2));
		map.put(2, "B");
		assertEquals("B", map.get(2));
		assertEquals(3, map.size());
	}

	@Test
	public void testClear() {
		assertEquals(0, map.size());
		map.put(1, "A");
		map.put(1, "C");
		map.put(3, "A");
		map.put(3, "B");
		map.put(2, "B");
		assertEquals(3, map.size());
		map.clear();
		assertEquals(0, map.size());
	}

	@Test
	public void testContainsKey() {
		for (int i = 0; i < 1000; ++i) {
			map.put(i, String.valueOf(i));
		}
		for (int i = 0; i < 10000; ++i) {
			if (i >= 1000) {
				assertFalse(map.containsKey(i));
			} else {
				assertTrue(map.containsKey(i));
			}
		}
	}

	@Test
	public void testContainsValue() {
		for (int i = 0; i < 1000; ++i) {
			map.put(i, String.valueOf(i));
		}
		for (int i = 0; i < 10000; ++i) {
			if (i >= 1000) {
				assertFalse(map.containsValue(String.valueOf(i)));
			} else {
				assertTrue(map.containsValue(String.valueOf(i)));
			}
		}
	}

	@Test
	public void remove() {
		for (int i = 0; i < 100000; ++i) {
			map.put(i, String.valueOf(i));
		}
		for (int i = 0; i < 50000; ++i) {
			assertTrue(map.containsKey(i));
		}
		for (int i = 0; i < 50000; ++i) {
			map.remove(i);
		}
		for (int i = 0; i < 50000; ++i) {
			assertFalse(map.containsKey(i));
		}
	}

	@Test
	public void testResizeTableLargeData() {
		for (int i = 0; i < 100000; ++i) {
			map.put(i, String.valueOf(i));
		}
		for (int i = 0; i < 100000; ++i) {
			assertNotNull(map.get(i));
		}
	}

	@Test
	public void testToString() {
		for (int i = 0; i < 10000; ++i) {
			map.put(i, String.valueOf(i));
		}
		String stringMap = map.toString();
		for (int i = 0; i < 10000; ++i) {
			stringMap.contains(String.valueOf(i));
		}
	}

	@Test
	public void testIteratorGet() {
		for (int i = 0; i < 100000; ++i) {
			map.put(i, String.valueOf(i));
		}
		Iterator<SimpleHashtable.TableEntry<Integer, String>> i = map.iterator();
		while (i.hasNext()) {
			if (i.next().getKey() % 2 == 0) {
				i.remove();
			}
		}
		i = map.iterator();
		while (i.hasNext()) {
			assertTrue(i.next().getKey() % 2 == 1);
		}
	}

	@Test
	public void testIteratorRemoveMultipleTimes() {
		for (int i = 0; i < 10000; ++i) {
			map.put(i, String.valueOf(i));
		}
		Iterator<SimpleHashtable.TableEntry<Integer, String>> i1 = map.iterator();
		while (i1.hasNext()) {
			if (i1.next().getKey() % 2 == 0) {
				i1.remove();
				assertThrows(IllegalStateException.class, () -> i1.remove());
			}
		}
	}

	@Test
	public void testConcurrentModification() {
		for (int i = 0; i < 10000; ++i) {
			map.put(i, String.valueOf(i));
		}
		Iterator<SimpleHashtable.TableEntry<Integer, String>> i1 = map.iterator();
		assertThrows(ConcurrentModificationException.class, () -> {
			while (i1.hasNext()) {
				SimpleHashtable.TableEntry<Integer, String> e = i1.next();
				if (e.getKey() % 2 == 0) {
					map.remove(e.getKey());
				}
			}
		});
	}

	@Test
	public void testExample1() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		String[] names = {
				"Ante",
				"Ivana",
				"Jasna",
				"Kristina"
		};
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		assertEquals(5, examMarks.get("Ivana"));
		int i = 0;
		for (SimpleHashtable.TableEntry<String, Integer> e: examMarks) {
			assertEquals(names[i++], e.getKey());
		}
	}

	@Test
	public void testExample2() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			if (pair.getKey().equals("Ivana")) {
				iter.remove(); // sam iterator kontrolirano uklanja trenutni element
			}
		}
		assertFalse(examMarks.containsKey("Ivana"));
	}

	@Test
	public void testExample3() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		assertThrows(IllegalStateException.class, () -> {
			while (iter.hasNext()) {
				SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
				if (pair.getKey().equals("Ivana")) {
					iter.remove();
					iter.remove();
				}
			}
		});
	}

	@Test
	public void testExample4() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		assertThrows(ConcurrentModificationException.class, () -> {
			while (iter.hasNext()) {
				while (iter.hasNext()) {
					SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
					if (pair.getKey().equals("Ivana")) {
						examMarks.remove("Ivana");
					}
				}
			}
		});
	}

	@Test
	public void testExample5() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			iter.next();
			iter.remove();
		}
		assertTrue(examMarks.isEmpty());
	}
}
