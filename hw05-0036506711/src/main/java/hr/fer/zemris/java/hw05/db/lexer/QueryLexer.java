package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

/**
 * Representation of a lexer used by {@code QueryParser} which returns the proper
 * {@code QueryToken} with proper {@code QueryTokenType}. Tokens can be words, strings
 * or arrays of symbols. Token of type {@code EOF} represents the end of the process.
 * This lexer works in only one state.
 * 
 * @author Filip Husnjak
 *
 */
public class QueryLexer {

	/**
	 * Given expression as char array
	 */
	private char[] data;
	
	/**
	 * Current position in {@link #data} array
	 */
	private int currentIndex;
	
	/**
	 * Last returned token by {@link #extractNextToken()} method
	 */
	private QueryToken currentToken;
	
	/**
	 * Constructs {@code QueryLexer} object with given text.
	 * 
	 * @param text
	 *        text to be tokenized
	 * @throws NullPointerException if the given text is {@code null}
	 */
	public QueryLexer(String text) {
		this.data = Objects.requireNonNull(text, "Given text cannot be null!").toCharArray();
	}
	
	/**
	 * Generates and returns next {@code QeuryToken} if its legal, throws
	 * {@code QueryLexerException} otherwise.
	 * 
	 * @return next Token if it can be tokenized
	 * @throws LexerException if the next character or sequence of characters cannot
	 *         be tokenized or there are no characters left
	 */
	public QueryToken extractNextToken() {
		skipIrrelevantCharacters();
		if (currentIndex > data.length) {
			throw new QueryLexerException("This lexer has no more elements to tokenize!");
		} else if (currentIndex == data.length) {
			currentIndex++;
			return currentToken = QueryToken.EOF_TOKEN;
		}
		return currentToken = extract();
		
	}
	
	/**
	 * Returns the next tokenized element as {@code QueryToken} or throws {@code QueryLexerException} if the element
	 * cannot be tokenized.
	 * 
	 * @return the tokenized element as {@code QueryToken}
	 * @throws QueryLexerException if the next character or sequence of characters cannot
	 *         be tokenized or there are no characters left
	 */
	private QueryToken extract() {
		if (Character.isLetter(data[currentIndex])) {
			StringBuilder word = new StringBuilder();
			while (currentIndex < data.length && Character.isLetter(data[currentIndex])) {
				word.append(data[currentIndex++]);
			}
			return new QueryToken(QueryTokenType.WORD, word.toString());
		}
		if (data[currentIndex] == '"') {
			StringBuilder string = new StringBuilder();
			for (currentIndex++; currentIndex < data.length && data[currentIndex] != '"'; currentIndex++) {
				string.append(data[currentIndex]);
			}
			if (currentIndex++ >= data.length) {
				throw new QueryLexerException("String not properly closed!");
			}
			return new QueryToken(QueryTokenType.STRING, string.toString());
		}
		StringBuilder symbols = new StringBuilder();
		while (currentIndex < data.length && !Character.isLetter(data[currentIndex]) && data[currentIndex] != '"' && data[currentIndex] != ' ') {
			symbols.append(data[currentIndex++]);
		}
		return new QueryToken(QueryTokenType.SYMBOLS, symbols.toString());
	}
	
	/**
	 * Increments {@link #currentIndex} until {@code data[currentIndex]} points to relevant
	 * character or {@link #currentIndex} goes out of range [0, data.length - 1].
	 */
	private void skipIrrelevantCharacters() {
		for (; currentIndex < data.length && isIrrelevant(data[currentIndex]); currentIndex++);
	}

	/**
	 * Returns {@code true} if the character is irrelevant, in other words if character is from set
	 * {'\r', '\n', '\t', ' '}.
	 * 
	 * @param character
	 *        character to be checked
	 * @return {@code true} if the character is irrelevant
	 */
	private boolean isIrrelevant(char c) {
		return c == ' ' || c == '\n' || c == '\t' || c == '\r';
	}
	
	/**
	 * Returns the last generated {@code QueryToken}. This method can be called multiple times
	 * and it will return the same {@code QueryToken} as it does not trigger generating next one.
	 * If no Tokens were generated yet, this method will return null.
	 * 
	 * @return last generated {@code QueryToken}
	 */
	public QueryToken getCurrentToken() {
		return currentToken;
	}
	
}
