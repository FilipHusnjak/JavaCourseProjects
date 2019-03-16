package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import static hr.fer.zemris.java.hw01.Factorial.*;

public class FactorialTest {
	
	@Test
	public void testFactorialCalc() {
		
		assertEquals(6, factorialCalc(3));
		assertEquals(24, factorialCalc(4));
		
		assertThrows(IllegalArgumentException.class, () -> factorialCalc(-4));
		assertThrows(IllegalArgumentException.class, () -> factorialCalc(21));
		
	}
}
