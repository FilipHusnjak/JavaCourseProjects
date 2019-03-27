package hr.fer.zemris.java.hw03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

public class SmartScriptTester {
	
	public static final NodeVisitor visitor = new NodeVisitor();

	/**
	 * Parses the given document and prints it. Document name is given as program
	 * parameter (i.e. examples\doc1.txt). It also parses the parsed document and checks whether the second
	 * parse creates the same document.
	 * 
	 * @param args
	 *        program parameters
	 * @throws IOException if an I/O error occurs reading from the stream
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Wrong number of arguments provided!");
			return;
		}
		String filepath = args[0];
		String docBody =  new String(
				 Files.readAllBytes(Paths.get(filepath)),
				 StandardCharsets.UTF_8
				);

		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
			DocumentNode document1 = parser.getDocumentNode();
			String original = createOriginalDocumentBody(document1);
			System.out.println("ORIGINAL DOCUMENT:\n");
			System.out.println(original + "\n");
			
			System.out.println("---------------------------------------------\n");
			
			SmartScriptParser parser2 = new SmartScriptParser(original);
			DocumentNode document2 = parser2.getDocumentNode();
			String original2 = createOriginalDocumentBody(document2);
			System.out.print("secondParse.equals(originalDocument): ");
			System.out.println(original2.equals(original));
			
			System.out.print("document1.equals(document2): ");
			System.out.println(document1.equals(document2));
		} catch (SmartScriptParserException e) {
			System.out.println(e.getMessage());
		} 
	}
	
	/**
	 * Creates and returns original document from given document tree.
	 * 
	 * @param document
	 *        root node of a document tree
	 * @return original document as {@code String} created from the given tree
	 * @throws NullPointerException if the given document is {@code null}
	 */
	public static String createOriginalDocumentBody(Node document) {
		// Visitor design pattern is used here.
		return Objects.requireNonNull(document, "Given document cannot be null!").accept(visitor);
	}

}
