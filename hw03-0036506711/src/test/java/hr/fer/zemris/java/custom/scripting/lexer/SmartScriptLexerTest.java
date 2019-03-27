package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

public class SmartScriptLexerTest {
	
	@Test
	public void testNotNull() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		assertNotNull(lexer.nextToken(), "Token was expected but null was returned.");
	}

	@Test
	public void testNullArgument() {
		assertThrows(NullPointerException.class, () -> new SmartScriptLexer(null));
	}
	
	@Test
	public void testEmptyString() {
		assertEquals(SmartScriptTokenType.EOF, new SmartScriptLexer("").nextToken().getType());
	}
	
	@Test
	public void testGetReturnsLastNext() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		SmartScriptToken token = lexer.nextToken();
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
	}
	
	@Test
	public void testRadAfterEOF() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		lexer.nextToken();
		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	@Test
	public void testEscapingInsideDocumentText() {
		assertEquals(SmartScriptTokenType.TEXT, new SmartScriptLexer("\\{$").nextToken().getType());
		assertEquals("{$", new SmartScriptLexer("\\{$").nextToken().getValue());
		
		assertEquals(SmartScriptTokenType.TEXT, new SmartScriptLexer("\\\\$").nextToken().getType());
		assertEquals("\\$", new SmartScriptLexer("\\\\$").nextToken().getValue());
		
		assertThrows(SmartScriptLexerException.class, () -> new SmartScriptLexer("\\\"$").nextToken());
		assertThrows(SmartScriptLexerException.class, () -> new SmartScriptLexer("\\$$").nextToken());
		assertThrows(SmartScriptLexerException.class, () -> new SmartScriptLexer("\\\"$").nextToken());
	}
	
	@Test
	public void testEscapingInsideString() {
		SmartScriptLexer lexer1 = new SmartScriptLexer("\"\\\"\"");
		lexer1.setState(SmartScriptLexerState.INSIDE_TAG);
		lexer1.nextToken();
		assertEquals(SmartScriptTokenType.STRING, lexer1.getToken().getType());
		assertEquals("\"", lexer1.getToken().getValue());
		
		lexer1 = new SmartScriptLexer("\"\\\\\"");
		lexer1.setState(SmartScriptLexerState.INSIDE_TAG);
		lexer1.nextToken();
		assertEquals(SmartScriptTokenType.STRING, lexer1.getToken().getType());
		assertEquals("\\", lexer1.getToken().getValue());
		
		SmartScriptLexer lexer2 = new SmartScriptLexer("\"\\{\"");
		lexer2.setState(SmartScriptLexerState.INSIDE_TAG);
		assertThrows(SmartScriptLexerException.class, () -> lexer2.nextToken());
		
		SmartScriptLexer lexer3 = new SmartScriptLexer("\"\\g\"");
		lexer3.setState(SmartScriptLexerState.INSIDE_TAG);
		assertThrows(SmartScriptLexerException.class, () -> lexer3.nextToken());
		
		SmartScriptLexer lexer4 = new SmartScriptLexer("\"\\is\"");
		lexer4.setState(SmartScriptLexerState.INSIDE_TAG);
		assertThrows(SmartScriptLexerException.class, () -> lexer4.nextToken());
	}
	
	@Test
	public void testInvalidTagName() {
		assertThrows(SmartScriptLexerException.class, () -> new SmartScriptLexer("{$1d").nextToken());
		assertThrows(SmartScriptLexerException.class, () -> new SmartScriptLexer("{$1FOR").nextToken());
		assertThrows(SmartScriptLexerException.class, () -> new SmartScriptLexer("{$_12=").nextToken());
	}
	
	@Test
	public void testInvalidFunctionName() {
		SmartScriptLexer lexer1 = new SmartScriptLexer("@1ASD");
		lexer1.setState(SmartScriptLexerState.INSIDE_TAG);
		assertThrows(SmartScriptLexerException.class, () -> lexer1.nextToken());
		
		SmartScriptLexer lexer2 = new SmartScriptLexer("@12=");
		lexer2.setState(SmartScriptLexerState.INSIDE_TAG);
		assertThrows(SmartScriptLexerException.class, () -> lexer2.nextToken());
		
		SmartScriptLexer lexer3 = new SmartScriptLexer("@_asd");
		lexer3.setState(SmartScriptLexerState.INSIDE_TAG);
		assertThrows(SmartScriptLexerException.class, () -> lexer3.nextToken());
	}
	
	@Test
	public void testDoc1() {
		String document = loader("doc1.txt");
		SmartScriptLexer lexer = new SmartScriptLexer(document);
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.TEXT, lexer.getToken().getType());
		assertEquals("This is sample text.\r\n", lexer.getToken().getValue());
		
		// FOR tag
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG, lexer.getToken().getType());
		assertEquals("FOR", lexer.getToken().getValue());
		
		lexer.setState(SmartScriptLexerState.INSIDE_TAG);
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.VARIABLE, lexer.getToken().getType());
		assertEquals("i", lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.CONSTANT_INTEGER, lexer.getToken().getType());
		assertEquals(1, lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.CONSTANT_INTEGER, lexer.getToken().getType());
		assertEquals(10, lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.CONSTANT_INTEGER, lexer.getToken().getType());
		assertEquals(1, lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.CLOSE_TAG, lexer.getToken().getType());
		assertNull(lexer.getToken().getValue());
		
		lexer.setState(SmartScriptLexerState.DOCUMENT_TEXT);
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.TEXT, lexer.getToken().getType());
		assertEquals("\r\n This is ", lexer.getToken().getValue());
		
		// Echo tag
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG, lexer.getToken().getType());
		assertEquals("=", lexer.getToken().getValue());
		
		lexer.setState(SmartScriptLexerState.INSIDE_TAG);
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.VARIABLE, lexer.getToken().getType());
		assertEquals("i", lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.CLOSE_TAG, lexer.getToken().getType());
		assertNull(lexer.getToken().getValue());
		
		lexer.setState(SmartScriptLexerState.DOCUMENT_TEXT);
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.TEXT, lexer.getToken().getType());
		assertEquals("-th time this message is generated.\r\n", lexer.getToken().getValue());
		
		// END tag
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG, lexer.getToken().getType());
		assertEquals("END", lexer.getToken().getValue());
		
		lexer.setState(SmartScriptLexerState.INSIDE_TAG);
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.CLOSE_TAG, lexer.getToken().getType());
		assertNull(lexer.getToken().getValue());
		
		lexer.setState(SmartScriptLexerState.DOCUMENT_TEXT);
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.TEXT, lexer.getToken().getType());
		assertEquals("\r\n", lexer.getToken().getValue());
	
		// FOR tag
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG, lexer.getToken().getType());
		assertEquals("FOR", lexer.getToken().getValue());
		
		lexer.setState(SmartScriptLexerState.INSIDE_TAG);
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.VARIABLE, lexer.getToken().getType());
		assertEquals("i", lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.CONSTANT_INTEGER, lexer.getToken().getType());
		assertEquals(0, lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.CONSTANT_INTEGER, lexer.getToken().getType());
		assertEquals(10, lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.CONSTANT_INTEGER, lexer.getToken().getType());
		assertEquals(2, lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.CLOSE_TAG, lexer.getToken().getType());
		assertNull(lexer.getToken().getValue());
		
		lexer.setState(SmartScriptLexerState.DOCUMENT_TEXT);
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.TEXT, lexer.getToken().getType());
		assertEquals("\r\n sin(", lexer.getToken().getValue());
		
		// Echo tag
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG, lexer.getToken().getType());
		assertEquals("=", lexer.getToken().getValue());
		
		lexer.setState(SmartScriptLexerState.INSIDE_TAG);
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.VARIABLE, lexer.getToken().getType());
		assertEquals("i", lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.CLOSE_TAG, lexer.getToken().getType());
		assertNull(lexer.getToken().getValue());
		
		lexer.setState(SmartScriptLexerState.DOCUMENT_TEXT);
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.TEXT, lexer.getToken().getType());
		assertEquals("^2) = ", lexer.getToken().getValue());
		
		// Echo tag
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG, lexer.getToken().getType());
		assertEquals("=", lexer.getToken().getValue());
		
		lexer.setState(SmartScriptLexerState.INSIDE_TAG);
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.VARIABLE, lexer.getToken().getType());
		assertEquals("i", lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.VARIABLE, lexer.getToken().getType());
		assertEquals("i", lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.SYMBOL, lexer.getToken().getType());
		assertEquals('*', lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.FUNCTION, lexer.getToken().getType());
		assertEquals("sin", lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.STRING, lexer.getToken().getType());
		assertEquals("0.000", lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.FUNCTION, lexer.getToken().getType());
		assertEquals("decfmt", lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.CLOSE_TAG, lexer.getToken().getType());
		assertNull(lexer.getToken().getValue());
		
		lexer.setState(SmartScriptLexerState.DOCUMENT_TEXT);
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.TEXT, lexer.getToken().getType());
		assertEquals("\r\n", lexer.getToken().getValue());
		
		// END tag
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG, lexer.getToken().getType());
		assertEquals("END", lexer.getToken().getValue());
		
		lexer.setState(SmartScriptLexerState.INSIDE_TAG);
		
		lexer.nextToken();
		assertEquals(SmartScriptTokenType.CLOSE_TAG, lexer.getToken().getType());
		assertNull(lexer.getToken().getValue());
	}
	
	private String loader(String filename) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename)) {
			byte[] buffer = new byte[1024];
			while (true) {
				int read = is.read(buffer);
				if (read < 1)
					break;
				bos.write(buffer, 0, read);
			}
			return new String(bos.toByteArray(), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			return null;
		}
	}
	
}
