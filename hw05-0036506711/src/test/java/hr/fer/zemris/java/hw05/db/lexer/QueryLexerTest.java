package hr.fer.zemris.java.hw05.db.lexer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class QueryLexerTest {

	@Test
	public void testNotNull() {
		QueryLexer lexer = new QueryLexer("");
		assertNotNull(lexer.extractNextToken(), "Token was expected but null was returned.");
	}

	@Test
	public void testNullArgument() {
		assertThrows(NullPointerException.class, () -> new QueryLexer(null));
	}
	
	@Test
	public void testEmptyString() {
		assertEquals(QueryTokenType.EOF, new QueryLexer("").extractNextToken().getType());
	}
	
	@Test
	public void testGetReturnsLastNext() {
		QueryLexer lexer = new QueryLexer("");
		QueryToken token = lexer.extractNextToken();
		assertEquals(token, lexer.getCurrentToken(), "getToken returned different token than nextToken.");
		assertEquals(token, lexer.getCurrentToken(), "getToken returned different token than nextToken.");
	}
	
	@Test
	public void testExtractAfterEOF() {
		QueryLexer lexer = new QueryLexer("");
		assertEquals(QueryTokenType.EOF, lexer.extractNextToken().getType());
		assertThrows(QueryLexerException.class, () -> lexer.extractNextToken());
	}
	
	@Test
	public void testStringNotProperlyClosed() {
		QueryLexer lexer = new QueryLexer("\"0000003");
		assertThrows(QueryLexerException.class, () -> lexer.extractNextToken());
	}
	
	@Test
	public void testNormalInput() {
		QueryLexer lexer = new QueryLexer("\"0000003\"jmbag???aaa   <<= >>");
		assertEquals(QueryTokenType.STRING, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.WORD, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.SYMBOLS, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.WORD, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.SYMBOLS, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.SYMBOLS, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.EOF, lexer.extractNextToken().getType());
	}
	
	@Test
	public void testDataFomHomework1() {
		QueryLexer lexer = new QueryLexer("jmbag = \"0000000003\"");
		assertEquals(QueryTokenType.WORD, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.SYMBOLS, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.STRING, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.EOF, lexer.extractNextToken().getType());
	}
	
	@Test
	public void testDataFomHomework2() {
		QueryLexer lexer = new QueryLexer(" jmbag = \"0000000003\" AND lastName LIKE \"B*\"");
		assertEquals(QueryTokenType.WORD, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.SYMBOLS, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.STRING, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.WORD, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.WORD, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.WORD, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.STRING, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.EOF, lexer.extractNextToken().getType());
	}
	
	@Test
	public void testDataFomHomework3() {
		QueryLexer lexer = new QueryLexer(" jmbag = \"0000000003\" AND lastName LIKE \"L*\"");
		assertEquals(QueryTokenType.WORD, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.SYMBOLS, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.STRING, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.WORD, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.WORD, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.WORD, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.STRING, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.EOF, lexer.extractNextToken().getType());
	}
	
	@Test
	public void testDataFomHomework4() {
		QueryLexer lexer = new QueryLexer(" lastName LIKE \"B*\"");
		assertEquals(QueryTokenType.WORD, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.WORD, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.STRING, lexer.extractNextToken().getType());
		assertEquals(QueryTokenType.EOF, lexer.extractNextToken().getType());
	}
	
}
