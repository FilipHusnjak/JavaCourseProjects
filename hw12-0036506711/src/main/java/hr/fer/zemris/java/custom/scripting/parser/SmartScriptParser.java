package hr.fer.zemris.java.custom.scripting.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.EmptyStackException;
import java.util.List;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexerException;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexerState;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptToken;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptTokenType;
import hr.fer.zemris.java.custom.scripting.nodes.*;

public class SmartScriptParser {

	/**
	 * Lexer used to tokenize input.
	 */
	private SmartScriptLexer lexer;
	
	/**
	 * Root node of a document.
	 */
	private DocumentNode document = new DocumentNode();
	
	/**
	 * Stack used for creating document tree.
	 */
	private Deque<Node> nodes = new ArrayDeque<>();
	
	/**
	 * Represents list of possible operators.
	 */
	private static List<Character> operators = Arrays.asList('+', '-', '*', '/', '^');
	
	/**
	 * Constructs {@code SmartScriptParser} object, initializes {@link #lexer} and
	 * creates document tree.
	 * 
	 * @param body
	 *        expression to be parsed
	 * @throws SmartScriptParserException expression cannot be parsed
	 * @throws NullPointerException if the given {@code String} is {@code null}
	 */
	public SmartScriptParser(String body) {
		lexer = new SmartScriptLexer(body);
		try {
            parse();
        } catch (Exception e) {
            throw new SmartScriptParserException(e.getMessage());
        }
	}
	
	/**
	 * Parses the given expression with the help of {@link #lexer}. If the expression cannot
	 * be parsed {@code SmartScriptParserException} is thrown.
	 * 
	 * @throws SmartScriptParserException if the expression cannot be parsed
	 */
	private void parse() {
		nodes.push(document);
		try {
			for (SmartScriptToken token = lexer.nextToken(); 
					token.getType() != SmartScriptTokenType.EOF; token = lexer.nextToken()) {
				if (token.getType() == SmartScriptTokenType.TEXT) {
					nodes.peek().addChildNode(new TextNode((String)token.getValue()));
				} else if (token.getType() == SmartScriptTokenType.TAG) {
					lexer.setState(SmartScriptLexerState.INSIDE_TAG);
					if (token.getValue().equals("END")) {
						if (lexer.nextToken().getType() != SmartScriptTokenType.CLOSE_TAG) {
							throw new SmartScriptParserException("END tag without closing brackets!");
						}
						nodes.pop();
					} else if (token.getValue().equals("FOR")) {
						ForLoopNode forNode = parseFor();
						nodes.peek().addChildNode(forNode);
						nodes.push(forNode);
					} else if (token.getValue().equals("=")) {
						nodes.peek().addChildNode(parseEquals());
					} else {
						throw new SmartScriptParserException("Unexpected TAG name during parsing!");
					}
					lexer.setState(SmartScriptLexerState.DOCUMENT_TEXT);
				} else {
					throw new SmartScriptParserException("Unexpected expression during parsing!");
				}
			}
		} catch (SmartScriptLexerException ex) {
			throw new SmartScriptParserException(ex.getMessage());
		} catch (EmptyStackException ex) {
			throw new SmartScriptParserException(
					"Expression contains more END tags than opened non-empty tags!");
		}
		if (nodes.size() != 1) {
			throw new SmartScriptParserException("Missing END tags!");
		}
	}
	
	/**
	 * Parses {@code Echo TAG} and returns proper {@code EchoNode} with parsed elements.
	 * 
	 * @return proper {@code EchoNode} with parsed elements
	 * @throws SmartScriptLexerException if {@code SmartScriptLexer} cannot extract next token
	 */
	private EchoNode parseEquals() {
		List<Element> elements = new ArrayList<>();
		for (SmartScriptToken elem = lexer.nextToken(); 
				elem.getType() != SmartScriptTokenType.CLOSE_TAG; elem = lexer.nextToken()) {
			elements.add(getMatchingElementEqualsTag(elem));
		}
		return new EchoNode(elements.toArray(Element[]::new));
	}
	
	/**
	 * Parses {@code For TAG} and returns proper {@code ForLoopNode} with parsed elements.
	 * 
	 * @return proper {@code EchoNode} with parsed elements
	 * @throws SmartScriptLexerException if {@code SmartScriptLexer} cannot extract next token
	 * @throws SmartScriptParserException if unexpected type of {@code SmartScriptTokens} where tokenized,
	 *         or {@code For TAG} is not written properly
	 */
	private ForLoopNode parseFor() {
		SmartScriptToken variable = lexer.nextToken();
		if (variable.getType() != SmartScriptTokenType.VARIABLE) {
			throw new SmartScriptParserException(String.format(
					"%s is not a variable name!", variable.getValue()));
		}
		SmartScriptToken from = lexer.nextToken();
		SmartScriptToken to = lexer.nextToken();
		SmartScriptToken counter = lexer.nextToken();
		Element counterElem = null;
		if (counter.getType() != SmartScriptTokenType.CLOSE_TAG) {
			if (lexer.nextToken().getType() != SmartScriptTokenType.CLOSE_TAG) {
				throw new SmartScriptParserException("FOR tag not written properly!");
			}
			counterElem = getMatchingElementForTag(counter);
		}
		return new ForLoopNode(new ElementVariable(variable.getValue().toString()),
				getMatchingElementForTag(from), getMatchingElementForTag(to), counterElem);
		
	}
	
	/**
	 * Returns the matching element based on the type of a given {@code SmartScriptToken}.
	 * It can only return elements that can be present in the {@code For TAG}, otherwise
	 * {@code SmartScriptParserException} is thrown.
	 * 
	 * @param token
	 *        {@code SmartScriptToken} used to create the matching element
	 * @return the matching element based on the type of given {@code SmartScriptToken}
	 * @throws SmartScriptParserException if the type of the given {@code SmartScriptToken}
	 *         is not valid for {@code For TAG}
	 */
	private Element getMatchingElementForTag(SmartScriptToken token) {
		switch (token.getType()) {
		case VARIABLE:
			return new ElementVariable(token.getValue().toString());
		case STRING:
			return new ElementString(token.getValue().toString());	
		case CONSTANT_DOUBLE:
			return new ElementConstantDouble(Double.parseDouble(token.getValue().toString()));
		case CONSTANT_INTEGER:
			return new ElementConstantInteger(Integer.parseInt(token.getValue().toString()));
		default:
			throw new SmartScriptParserException("Invalid element type in FOR tag!");
		}
	}
	
	/**
	 * Returns the matching element based on the type of a given {@code SmartScriptToken}.
	 * It can only return elements that can be present in the {@code Echo TAG}, otherwise
	 * {@code SmartScriptParserException} is thrown.
	 * 
	 * @param token
	 *        {@code SmartScriptToken} used to create the matching element
	 * @return the matching element based on the type of given {@code SmartScriptToken}
	 * @throws SmartScriptParserException if the type of the given {@code SmartScriptToken}
	 *         is not valid for {@code Echo TAG}
	 */
	private Element getMatchingElementEqualsTag(SmartScriptToken token) {
		switch (token.getType()) {
		case FUNCTION:
			return new ElementFunction(token.getValue().toString());
		case SYMBOL:
			if (!operators.contains(token.getValue())) {
				throw new SmartScriptParserException("Unrecognized symbol: " + token.getValue());
			}
			return new ElementOperator(token.getValue().toString());
		default:
			return getMatchingElementForTag(token);
		}
	}
	
	/**
	 * Returns the root node of the created document tree.
	 * 
	 * @return the root node of the created document tree
	 */
	public DocumentNode getDocumentNode() {
		return document;
	}
	
}
