package hr.fer.zemris.java.custom.scripting.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.hw03.SmartScriptTester;

/**
 * Some tests are used from 12th homework.
 * 
 * @author Filip Husnjak
 */
public class SmartScriptParserTest {
	
	@Test
	public void testNullArgument() {
		assertThrows(NullPointerException.class, () -> new SmartScriptParser(null));
	}
	
	@Test
	public void testEmptyString() {
		assertEquals("", SmartScriptTester.createOriginalDocumentBody(new SmartScriptParser("").getDocumentNode()));
	}
	
	@Test
	public void testTagNameNotRecognized() {
		String document1 = loader("wrongTagName1.txt");
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(document1));	
		
		String document2 = loader("wrongTagName2.txt");
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(document2));
		
		String document3 = loader("wrongTagName3.txt");
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(document3));	
	}
	
	@Test
	public void testUnclosedTag() {
		String document1 = loader("unclosedTag1.txt");
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(document1));	
		
		String document2 = loader("unclosedTag2.txt");
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(document2));
		
		String document3 = loader("unclosedTag3.txt");
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(document3));	
	}
	
	@Test
	public void testMissingEndTags() {
		String document1 = loader("missingEndTags1.txt");
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(document1));	
		
		String document2 = loader("missingEndTags2.txt");
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(document2));
	}
	
	@Test
	public void testTooManyEndTags() {
		String document1 = loader("tooManyEndTags1.txt");
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(document1));	
		
		String document2 = loader("tooManyEndTags2.txt");
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(document2));
	}

	@Test
	public void testForTagNotWrittenProperly() {
		String document1 = loader("forTagNotWrittenProperly1.txt");
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(document1));	
		
		String document2 = loader("forTagNotWrittenProperly2.txt");
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(document2));
		
		String document3 = loader("forTagNotWrittenProperly3.txt");
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(document3));	
	}
	
	@Test
	public void testDoc1() {
		checkFile("doc1.txt");
	}
	
	@Test
	public void testDoc2() {
		checkFile("doc2.txt");
	}
	
	@Test
	public void testDoc3() {
		checkFile("doc3.txt");
	}
	
	@Test
	public void testDoc4() {
		checkFile("doc4.txt");
	}
	
	@Test
	public void testDoc5() {
		checkFile("doc5.txt");
	}
	
	@Test
	public void testDoc6() {
		checkFile("doc6.txt");
	}

	private void checkFile(String filename) {
		String document = loader(filename);
		SmartScriptParser parser1 = new SmartScriptParser(document);
		DocumentNode documentNode1 = parser1.getDocumentNode();
		String originalDocument = SmartScriptTester.createOriginalDocumentBody(documentNode1);
		
		SmartScriptParser parser2 = new SmartScriptParser(originalDocument);
		DocumentNode documentNode2 = parser2.getDocumentNode();
		String createdFromOriginal = SmartScriptTester.createOriginalDocumentBody(documentNode2);
		assertEquals(originalDocument, createdFromOriginal);
		assertEquals(documentNode1, documentNode2);
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
