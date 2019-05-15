package hr.fer.zemris.java.hw06.shell.util;

import java.util.Objects;

/**
 * Simple lexer used for tokenizing expressions for renaming purposes.
 * Creates three types of tokens:
 * <ul>
 * <li> EOF - represents end of data
 * <li> COMMAND - represents command inside appropriate tags
 * <li> TEXT - represents the rest of the data
 * </ul>
 * 
 * @author Filip Husnjak
 */
public class Lexer {
	
	/**
	 * Data which is being tokenized.
	 */
	private char[] data;
	
	/**
	 * Last extracted token.
	 */
	private Token currentToken;
	
	/**
	 * Current position in data array.
	 */
	private int currentIndex;
	
	/**
	 * Constructs new {@code Lexer} with specified data to be tokenized.
	 * 
	 * @param data
	 *        data to be tokenized
	 * @throws NullPointerException if the given data is {@code null}
	 */
	public Lexer(String data) {
		this.data = Objects.requireNonNull(data, "Given data cannot be null!").toCharArray();
	}
	
	/**
	 * Generates and returns next {@code QeuryToken} if its legal, throws
	 * {@code QueryLexerException} otherwise.
	 * 
	 * @return next token if its legal
	 * @throws LexerException if there was an error extracting token
	 */
	public Token extractNextToken() {
		if (currentIndex > data.length) {
			throw new LexerException("This lexer has no more elements to tokenize!");
		}
		if (currentIndex == data.length) {
			currentIndex++;
			return currentToken = Token.EOF_TOKEN;
		}
		return currentToken = extract();
	}

	/**
	 * Returns the next tokenized element as {@code Token} or throws {@code LexerException} if the element
	 * cannot be tokenized.
	 * 
	 * @return next token in data array
	 * @throws LexerException if there was an error extracting token (i.e. expression
	 *         does not meet the requirements)
	 */
	private Token extract() {
		if (checkForOpenBrackets()) {
			StringBuilder command = new StringBuilder();
			for (currentIndex += 2; currentIndex < data.length && data[currentIndex] != '}'; currentIndex++) {
				command.append(data[currentIndex]);
			}
			if (currentIndex++ == data.length) {
				throw new LexerException("Missing closing bracket!");
			}
			return new Token(TokenType.COMMAND, command.toString());
		}
		StringBuilder text = new StringBuilder();
		for (; currentIndex < data.length && !checkForOpenBrackets(); currentIndex++) {
			text.append(data[currentIndex]);
		}
		return new Token(TokenType.TEXT, text.toString());
	}
	
	/**
	 * Returns {@code true} if next two symbols represent opening tag for command.
	 * 
	 * @return {@code true} if next two symbols represent opening tag for command
	 */
	private boolean checkForOpenBrackets() {
		return currentIndex < data.length - 1 && data[currentIndex] == '$' && data[currentIndex + 1] == '{';
	}
	
	/**
	 * Returns last generated token.
	 * 
	 * @return last generated token
	 */
	public Token getCurrentToken() {
		return currentToken;
	}

}
