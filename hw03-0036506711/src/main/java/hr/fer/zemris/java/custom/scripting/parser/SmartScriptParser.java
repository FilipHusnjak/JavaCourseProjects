package hr.fer.zemris.java.custom.scripting.parser;

import java.util.Arrays;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.lexer.Lexer;
import hr.fer.zemris.java.custom.scripting.lexer.LexerException;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;
import hr.fer.zemris.java.custom.scripting.nodes.*;

public class SmartScriptParser {

	private Lexer lexer;
	
	private DocumentNode document = new DocumentNode();
	
	public SmartScriptParser(String body) {
		lexer = new Lexer(body);
		ObjectStack nodes = new ObjectStack();
		nodes.push(document);
		try {
			for (Token token = lexer.nextToken(); token.getType() != TokenType.EOF; token = lexer.nextToken()) {
				if (token.getType() == TokenType.TEXT) {
					((Node) nodes.peek()).addChildNode(new TextNode((String)token.getValue()));
				} else if (token.getType() == TokenType.TAG) {
					if (token.getValue().equals("END")) {
						if (lexer.nextToken().getType() != TokenType.CLOSE_TAG) {
							throw new SmartScriptParserException("END tag without closing it!");
						}
						nodes.pop();
					} else if (token.getValue().equals("FOR")) {
						Token variable = lexer.nextToken();
						if (variable.getType() != TokenType.VARIABLE) {
							throw new SmartScriptParserException(String.format("%s is not a variable name!", variable.getValue()));
						}
						Token from = lexer.nextToken();
						checkVariableOrNumber(from);
						Token to = lexer.nextToken();
						checkVariableOrNumber(to);
						Token counter = lexer.nextToken();
						Element counterElem = null;
						if (counter.getType() != TokenType.CLOSE_TAG) {
							if (lexer.nextToken().getType() != TokenType.CLOSE_TAG) {
								throw new SmartScriptParserException("No closing tag provided!");
							}
							counterElem = checkVariableOrNumber(counter);
						}
						ForLoopNode forLoopNode = new ForLoopNode(new ElementVariable(variable.getValue().toString()),
								checkVariableOrNumber(from), checkVariableOrNumber(to), counterElem);
						((Node) nodes.peek()).addChildNode(forLoopNode);
						nodes.push(forLoopNode);
					} else if (token.getValue().equals("=")) {
						ArrayIndexedCollection elements = new ArrayIndexedCollection();
						for (Token elem = lexer.nextToken(); elem.getType() != TokenType.CLOSE_TAG; elem = lexer.nextToken()) {
							elements.add(getRightElement(elem));
						}
						((Node) nodes.peek()).addChildNode(new EchoNode(Arrays.copyOf(elements.toArray(), elements.toArray().length, Element[].class)));
					}
				} else {
					throw new SmartScriptParserException("Unexpected expression during parsing! : " + token.getType());
				}
			}
		} catch (LexerException ex) {
			throw new SmartScriptParserException(ex.getMessage());
		} catch (EmptyStackException ex) {
			throw new SmartScriptParserException("Expression contains more END tags than opened non-empty tags!");
		}
		if (nodes.size() != 1) {
			throw new SmartScriptParserException("Missing END tags!");
		}
	}
	
	private Element checkVariableOrNumber(Token check) {
		if (check.getType() == TokenType.VARIABLE) {
			return new ElementString(check.getValue().toString());
		}
		try {
			Integer num = Integer.parseInt(check.getValue().toString());
			return check.getType() == TokenType.TEXT ? new ElementString("\"" + check.getValue().toString() + "\"") : new ElementConstantInteger(num);
		} catch (NumberFormatException ex1) {
			try {
				Double num = Double.parseDouble(check.getValue().toString());
				return check.getType() == TokenType.TEXT ? new ElementString("\"" + check.getValue().toString() + "\"") : new ElementConstantDouble(num);
			} catch (NumberFormatException ex2) {
				throw new SmartScriptParserException(String.format("%s is not a number or variable!", check.getValue()));
			}
		}
	}
	
	private Element getRightElement(Token element) {
		if (element.getType() == TokenType.FUNCTION) {
			return new ElementFunction(element.getValue().toString());
		}
		if (element.getType() == TokenType.OPERATOR) {
			return new ElementOperator(element.getValue().toString());
		}
		return checkVariableOrNumber(element);
	}
	
	public DocumentNode getDocumentNode() {
		return document;
	}
	
}
