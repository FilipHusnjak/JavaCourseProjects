package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

public class FieldValueGettersTest {

	@Test
	public void testFirstName() {
		assertEquals("Ana", FieldValueGetters.FIRST_NAME.get(new StudentRecord("0000", "Bakamović", "Ana", 4)));
	}
	
	@Test
	public void testLastName() {
		assertEquals("Bakamović", FieldValueGetters.LAST_NAME.get(new StudentRecord("0000", "Bakamović", "Ana", 4)));
	}
	
	@Test
	public void testJMBAG() {
		assertEquals("0000", FieldValueGetters.JMBAG.get(new StudentRecord("0000", "Bakamović", "Ana", 4)));
	}
	
}
