package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class QueryParserTest {

	@Test
	public void testNullArgument() {
		assertThrows(NullPointerException.class, () -> new QueryParser(null));
	}
	
	@Test
	public void testEmptyString() {
		assertThrows(QueryParserException.class, () -> new QueryParser(""));	
	}
	
	@Test
	public void testWrongAttributeName() {
		assertThrows(QueryParserException.class, () -> new QueryParser("jmbagg=\"1009\""));
	}
	
	@Test
	public void testMissingOperator() {
		assertThrows(QueryParserException.class, () -> new QueryParser("jmbag\"1009\""));
	}
	
	@Test
	public void testInvalidOperator() {
		assertThrows(QueryParserException.class, () -> new QueryParser("jmbagg==\"1009\""));
	}
	
	@Test
	public void testMissingStringLiteral() {
		assertThrows(QueryParserException.class, () -> new QueryParser("jmbagg=1009"));
	}
	
	@Test
	public void testMissingAndOperator() {
		assertThrows(QueryParserException.class, () -> new QueryParser("jmbagg=\"1009\" lastName=\"aaa\""));
	}
	
	@Test
	public void testIsDirectQuery() {
		QueryParser parser1 = new QueryParser("jmbag=\"00000\"");
		assertTrue(parser1.isDirectQuery());
		assertEquals(1, parser1.getQuery().size());
		assertEquals("00000", parser1.getQueriedJMBAG());
		
		QueryParser parser2 = new QueryParser("jmbag = \"00000\" and lastName=\"00000\"");
		assertFalse(parser2.isDirectQuery());
		assertEquals(2, parser2.getQuery().size());
		assertThrows(IllegalStateException.class, () -> parser2.getQueriedJMBAG());
	}
	
	@Test
	public void testExample1() throws IOException {
		StudentDatabase database = getData();
		QueryParser parser = new QueryParser("jmbag = \"0000000003\" AND lastName LIKE \"B*\"");
		assertEquals(new StudentRecord("0000000003", "Bosnić", "Andrea", 4), database.filter(new QueryFilter(parser.getQuery())).get(0));
	}
	
	@Test
	public void testExample2() throws IOException {
		StudentDatabase database = getData();
		QueryParser parser = new QueryParser("jmbag = \"0000000003\"");
		assertEquals(new StudentRecord("0000000003", "Bosnić", "Andrea", 4), database.filter(new QueryFilter(parser.getQuery())).get(0));
	}
	
	@Test
	public void testExample4() throws IOException {
		StudentDatabase database = getData();
		QueryParser parser = new QueryParser("lastName LIKE \"B*\"");
		
		assertArrayEquals(new StudentRecord[] { new StudentRecord("0000000002", "Bakamović", "Petra", 3), 
				new StudentRecord("0000000003", "Bosnić", "Andrea", 4),
				new StudentRecord("0000000004", "Bozić", "Marin", 5),
				new StudentRecord("0000000005", "Brezović", "Jusufadis", 2)}, database.filter(new QueryFilter(parser.getQuery())).toArray());
	}
	
	@Test
	public void testExample5() throws IOException {
		StudentDatabase database = getData();
		QueryParser parser = new QueryParser("jmbag = \"0000000003\" AND lastName LIKE \"L*\"");
		assertEquals(0, database.filter(new QueryFilter(parser.getQuery())).size());
	}
	
	@Test
	public void testExample6() throws IOException {
		StudentDatabase database = getData();
		QueryParser parser = new QueryParser("lastName LIKE \"Be*\"");
		assertEquals(0, database.filter(new QueryFilter(parser.getQuery())).size());
	}
	
	public static StudentDatabase getData() throws IOException {
		return new StudentDatabase(
				Files.readAllLines(Paths.get("./src/main/resources/database.txt"), StandardCharsets.UTF_8));
	}
	
}
