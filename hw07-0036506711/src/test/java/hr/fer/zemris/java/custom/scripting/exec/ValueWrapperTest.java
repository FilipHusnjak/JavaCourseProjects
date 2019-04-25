package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.junit.jupiter.api.*;

public class ValueWrapperTest {
	
	private static Map<String, BiFunction<ValueWrapper, Object, Object>> map = new HashMap<>();
	
	@BeforeAll
	public static void fillMap() {
		map.put("add", (v, o) -> {
			v.add(o);
			return v.getValue();
		});
		map.put("subtract", (v, o) -> {
			v.subtract(o);
			return v.getValue();
		});
		map.put("multiply", (v, o) -> {
			v.multiply(o);
			return v.getValue();
		});
		map.put("divide", (v, o) -> {
			v.divide(o);
			return v.getValue();
		});
		map.put("compare", (v, o) -> v.numCompare(o));
	}

	@Test
	public void testNullValue() {
		assertDoesNotThrow(() -> new ValueWrapper(null));
	}
	
	@Test
	public void testIllegalArguments() {
		assertThrows(RuntimeException.class, () -> testHelper(null, "aa", "add"));
		assertThrows(RuntimeException.class, () -> testHelper(null, "aa", "subtract"));
		assertThrows(RuntimeException.class, () -> testHelper(null, "aa", "multiply"));
		assertThrows(RuntimeException.class, () -> testHelper(null, "aa", "divide"));
		assertThrows(RuntimeException.class, () -> testHelper(null, "aa", "compare"));
		
		assertThrows(RuntimeException.class, () -> testHelper("aa", null, "add"));
		assertThrows(RuntimeException.class, () -> testHelper("aa", null, "subtract"));
		assertThrows(RuntimeException.class, () -> testHelper("aa", null, "multiply"));
		assertThrows(RuntimeException.class, () -> testHelper("aa", null, "divide"));
		assertThrows(RuntimeException.class, () -> testHelper("aa", null, "compare"));
		
		assertThrows(RuntimeException.class, () -> testHelper(null, true, "add"));
		assertThrows(RuntimeException.class, () -> testHelper(null, true, "subtract"));
		assertThrows(RuntimeException.class, () -> testHelper(null, true, "multiply"));
		assertThrows(RuntimeException.class, () -> testHelper(null, true, "divide"));
		assertThrows(RuntimeException.class, () -> testHelper(null, true, "compare"));
		
		assertThrows(RuntimeException.class, () -> testHelper(true, null, "add"));
		assertThrows(RuntimeException.class, () -> testHelper(true, null, "subtract"));
		assertThrows(RuntimeException.class, () -> testHelper(true, null, "multiply"));
		assertThrows(RuntimeException.class, () -> testHelper(true, null, "divide"));
		assertThrows(RuntimeException.class, () -> testHelper(true, null, "compare"));
	}
	
	@Test
	public void testNullNull() {
		Object result = testHelper(null, null, "add");
		assertEquals(0, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(null, null, "subtract");
		assertEquals(0, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(null, null, "multiply");
		assertEquals(0, result);
		assertTrue(result instanceof Integer);
		
		assertTrue(testHelper(null, null, "compare").equals(0));
	}
	
	@Test
	public void testNullInteger() {
		Object result = testHelper(null, 2, "add");
		assertEquals(2, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(null, 2, "subtract");
		assertEquals(-2, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(null, 2, "multiply");
		assertEquals(0, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(null, 2, "divide");
		assertEquals(0, result);
		assertTrue(result instanceof Integer);
		
		assertTrue(testHelper(null, 2, "compare").equals(-1));
	}
	
	@Test
	public void testNullDouble() {
		Object result = testHelper(null, 2.0, "add");
		assertEquals(2.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(null, 2.0, "subtract");
		assertEquals(-2.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(null, 2.0, "multiply");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(null, 2.0, "divide");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper(null, 2.0, "compare").equals(-1));
	}
	
	@Test
	public void testNullStringInteger() {
		Object result = testHelper(null, "2", "add");
		assertEquals(2, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(null, "2", "subtract");
		assertEquals(-2, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(null, "2", "multiply");
		assertEquals(0, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(null, "2", "divide");
		assertEquals(0, result);
		assertTrue(result instanceof Integer);
		
		assertTrue(testHelper(null, "2", "compare").equals(-1));
	}
	
	@Test
	public void testNullStringDoubleDot() {
		Object result = testHelper(null, "2.0", "add");
		assertEquals(2.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(null, "2.0", "subtract");
		assertEquals(-2.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(null, "2.0", "multiply");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(null, "2.0", "divide");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper(null, "2.0", "compare").equals(-1));
	}
	
	@Test
	public void testNullStringDoubleExp() {
		Object result = testHelper(null, "2E1", "add");
		assertEquals(20.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(null, "2E1", "subtract");
		assertEquals(-20.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(null, "2E1", "multiply");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(null, "2E1", "divide");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper(null, "2E1", "compare").equals(-1));
	}
	
	@Test
	public void testIntegerNull() {
		Object result = testHelper(2, null, "add");
		assertEquals(2, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(2, null, "subtract");
		assertEquals(2, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(2, null, "multiply");
		assertEquals(0, result);
		assertTrue(result instanceof Integer);
		
		assertTrue(testHelper(2, null, "compare").equals(1));
	}
	
	@Test
	public void testDoubleNull() {
		Object result = testHelper(2.0, null, "add");
		assertEquals(2.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2.0, null, "subtract");
		assertEquals(2.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2.0, null, "multiply");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper(2.0, null, "compare").equals(1));
	}
	
	@Test
	public void testStringIntegerNull() {
		Object result = testHelper("2", null, "add");
		assertEquals(2, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper("2", null, "subtract");
		assertEquals(2, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper("2", null, "multiply");
		assertEquals(0, result);
		assertTrue(result instanceof Integer);
		
		assertTrue(testHelper("2", null, "compare").equals(1));
	}
	
	@Test
	public void testStringDoubleDotNull() {
		Object result = testHelper("2.0", null, "add");
		assertEquals(2.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2.0", null, "subtract");
		assertEquals(2.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2.0", null, "multiply");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper("2.0", null, "compare").equals(1));
	}
	
	@Test
	public void testStringDoubleExpNull() {
		Object result = testHelper("2E1", null, "add");
		assertEquals(20.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2E1", null, "subtract");
		assertEquals(20.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2E1", null, "multiply");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper("2E1", null, "compare").equals(1));
	}
	
	@Test
	public void testIntegerInteger() {
		Object result = testHelper(2, 2, "add");
		assertEquals(4, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(2, 2, "subtract");
		assertEquals(0, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(2, 2, "multiply");
		assertEquals(4, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(2, 2, "divide");
		assertEquals(1, result);
		assertTrue(result instanceof Integer);
		
		assertTrue(testHelper(2, 2, "compare").equals(0));
	}
	
	@Test
	public void testIntegerDouble() {
		Object result = testHelper(2, 2.0, "add");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2, 2.0, "subtract");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2, 2.0, "multiply");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2, 2.0, "divide");
		assertEquals(1.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper(2, 2.0, "compare").equals(0));
	}
	
	@Test
	public void testIntegerStringInteger() {
		Object result = testHelper(2, "2", "add");
		assertEquals(4, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(2, "2", "subtract");
		assertEquals(0, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(2, "2", "multiply");
		assertEquals(4, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper(2, "2", "divide");
		assertEquals(1, result);
		assertTrue(result instanceof Integer);
		
		assertTrue(testHelper(2, "2", "compare").equals(0));
	}
	
	@Test
	public void testIntegerStringDouble() {
		Object result = testHelper(2, "2.0", "add");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2, "2.0", "subtract");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2, "2.0", "multiply");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2, "2.0", "divide");
		assertEquals(1.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper(2, "2.0", "compare").equals(0));
	}
	
	@Test
	public void testDoubleInteger() {
		Object result = testHelper(2.0, 2, "add");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2.0, 2, "subtract");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2.0, 2, "multiply");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2.0, 2, "divide");
		assertEquals(1.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper(2.0, 2, "compare").equals(0));
	}
	
	@Test
	public void testDoubleDouble() {
		Object result = testHelper(2.0, 2.0, "add");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2.0, 2.0, "subtract");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2.0, 2.0, "multiply");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2.0, 2.0, "divide");
		assertEquals(1.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper(2.0, 2.0, "compare").equals(0));
	}
	
	@Test
	public void testDoubleStringInteger() {
		Object result = testHelper(2.0, "2", "add");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2.0, "2", "subtract");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2.0, "2", "multiply");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2.0, "2", "divide");
		assertEquals(1.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper("2.0", 2, "compare").equals(0));
	}
	
	@Test
	public void testDoubleStringDouble() {
		Object result = testHelper(2.0, "2.0", "add");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2.0, "2.0", "subtract");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2.0, "2.0", "multiply");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper(2.0, "2.0", "divide");
		assertEquals(1.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper("2.0", 2.0, "compare").equals(0));
	}
	
	@Test
	public void testStringIntegerInteger() {
		Object result = testHelper("2", 2, "add");
		assertEquals(4, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper("2", 2, "subtract");
		assertEquals(0, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper("2", 2, "multiply");
		assertEquals(4, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper("2", 2, "divide");
		assertEquals(1, result);
		assertTrue(result instanceof Integer);
		
		assertTrue(testHelper("2", 2, "compare").equals(0));
	}
	
	@Test
	public void testStringIntegerDouble() {
		Object result = testHelper("2", 2.0, "add");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2", 2.0, "subtract");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2", 2.0, "multiply");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2", 2.0, "divide");
		assertEquals(1.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper("2", 2.0, "compare").equals(0));
	}
	
	@Test
	public void testStringIntegerStringInteger() {
		Object result = testHelper("2", "2", "add");
		assertEquals(4, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper("2", "2", "subtract");
		assertEquals(0, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper("2", "2", "multiply");
		assertEquals(4, result);
		assertTrue(result instanceof Integer);
		
		result = testHelper("2", "2", "divide");
		assertEquals(1, result);
		assertTrue(result instanceof Integer);
		
		assertTrue(testHelper("2", "2", "compare").equals(0));
	}
	
	@Test
	public void testStringIntegerStringDouble() {
		Object result = testHelper("2", "2.0", "add");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2", "2.0", "subtract");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2", "2.0", "multiply");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2", "2.0", "divide");
		assertEquals(1.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper("2", "2.0", "compare").equals(0));
	}
	
	@Test
	public void testStringDoubleInteger() {
		Object result = testHelper("2.0", 2, "add");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2.0", 2, "subtract");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2.0", 2, "multiply");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2.0", 2, "divide");
		assertEquals(1.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper("2.0", 2, "compare").equals(0));
	}
	
	@Test
	public void testStringDoubleDouble() {
		Object result = testHelper("2.0", 2.0, "add");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2.0", 2.0, "subtract");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2.0", 2.0, "multiply");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2.0", 2.0, "divide");
		assertEquals(1.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper("2.0", 2.0, "compare").equals(0));
	}
	
	@Test
	public void testStringDoubleStringInteger() {
		Object result = testHelper("2.0", "2", "add");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2.0", "2", "subtract");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2.0", "2", "multiply");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2.0", "2", "divide");
		assertEquals(1.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper("2.0", "2", "compare").equals(0));
	}
	
	@Test
	public void testStringDoubleStringDouble() {
		Object result = testHelper("2.0", "2.0", "add");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2.0", "2.0", "subtract");
		assertEquals(0.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2.0", "2.0", "multiply");
		assertEquals(4.0, result);
		assertTrue(result instanceof Double);
		
		result = testHelper("2.0", "2.0", "divide");
		assertEquals(1.0, result);
		assertTrue(result instanceof Double);
		
		assertTrue(testHelper("2.0", "2.0", "compare").equals(0));
	}
	
	private Object testHelper(Object v1, Object v2, String action) {
		return map.get(action).apply(new ValueWrapper(v1), v2);
	}
	
}
