package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class QueryFilterTest {

	@Test
	public void testAcceptsTrue() {
		QueryParser parser = new QueryParser("jmbag = \"00001\" and lastName LIKE \"AA*AA\"");
		QueryFilter query = new QueryFilter(parser.getQuery());
		assertTrue(query.accepts(new StudentRecord("00001", "AABAA", "Ana", 4)));
	}
	
	@Test
	public void testAcceptsFalse() {
		QueryParser parser = new QueryParser("jmbag = \"00001\" and lastName LIKE \"AA*AA\"");
		QueryFilter query = new QueryFilter(parser.getQuery());
		assertFalse(query.accepts(new StudentRecord("00001", "AAA", "Ana", 4)));
	}
	
}
