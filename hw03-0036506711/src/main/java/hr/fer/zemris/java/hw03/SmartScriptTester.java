package hr.fer.zemris.java.hw03;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.lexer.Lexer;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

public class SmartScriptTester {

	public static void main(String[] args) {
		String docBody = "This is sample text.\r\n" + 
				"{$ FOR i 1 10 1 $}\r\n" + 
				" This is {$= i $}-th time this message is generated.\r\n" + 
				"{$END$}\r\n" + 
				"{$FOR i 0 10 2 $}\r\n" + 
				" sin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\r\n" + 
				"{$END$}\r\n";
		Lexer lexer = new Lexer(docBody);
		Token token;
		do {
			token = lexer.nextToken();
			System.out.println(token.getType() + " " + token.getValue());
		} while (token.getType() != TokenType.EOF);
		System.out.println("---------------------------");
		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
			createOriginalDocumentBody(parser.getDocumentNode());
		} catch (SmartScriptParserException e) {
			System.out.println(e.getMessage());
		} 
	}
	
	private static void createOriginalDocumentBody(Node document) {
		if (document instanceof ForLoopNode) {
			ForLoopNode forLoop = (ForLoopNode) document;
			System.out.println("FOR: " + forLoop.getVariable().asText() + " " + forLoop.getStartExpression().asText() + " " + forLoop.getEndExpression().asText() + " " + forLoop.getStepExpression().asText());
		} else if (document instanceof EchoNode) {
			EchoNode echo = (EchoNode) document;
			System.out.print("ECHO: ");
			for (Element e : echo.getElements()) {
				System.out.print(e.asText() + " ");
			}
			System.out.println();
		} else if (document instanceof TextNode) {
			TextNode text = (TextNode) document;
			System.out.println("TEXT: " + text.getText());
		}
		if (document.numberOfChildren() == 0) return;
		for (int i = 0, n = document.numberOfChildren(); i < n; ++i) {
			createOriginalDocumentBody(document.getChild(i));
		}
	}

}
