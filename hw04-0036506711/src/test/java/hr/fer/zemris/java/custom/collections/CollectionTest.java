package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

public class CollectionTest {

	@Test
	public void testAddAll() {
		List<Integer> c1 = new ArrayIndexedCollection<>();
		for (int i = 0; i < 10000; ++i) {
			c1.add(i);
		}
		Collection<Number> c2 = new LinkedListIndexedCollection<>();
		c2.addAll(c1);
		ElementsGetter<Number> el = c2.createElementsGetter();
		int i = 0;
		while (el.hasNextElement()) {
			assertEquals(c1.get(i++), el.getNextElement());
		}
	}
	
	@Test
	public void testAddAllSatisfying() {
		List<Integer> c1 = new ArrayIndexedCollection<>();
		for (int i = 0; i < 10000; ++i) {
			c1.add(i);
		}
		Collection<Integer> c2 = new LinkedListIndexedCollection<>();
		c2.addAllSatisfying(c1, (o) -> o % 2 == 0);
		ElementsGetter<Integer> el = c2.createElementsGetter();
		int i = 0;
		while (el.hasNextElement()) {
			assertEquals(c1.get(i), el.getNextElement());
			i += 2;
		}
	}
}
