package hr.fer.zemris.java.hw03.prob1;

import java.util.Objects;

/**
 * Lexer with 2 states that tokenizes input text depending on the current state.
 * If the current state is set to {@code LexerState.BASIC} it tokenizes numbers, words
 * and symbols. If the current state is set to {@code LexerState.EXTENDED} it can only
 * tokenize words and symbols. LexerState can be set using the proper method.
 * 
 * @author Filip Husnjak
 */
public class Lexer {
	
	/**
	 * Given expression as char array
	 */
	private char[] data;
	
	/**
	 * Current token retrieved by {@code getToken} method
	 */
	private Token token;
	
	/**
	 * Index of the next character that was not tokenized.
	 */
	private int currentIndex;
    
	/**
	 * Current state of {@code this Lexer}. It can only be {@code LexerState.BASIC} or 
	 * {@code LexerState.EXTENDED}. By default its set to {@code LexerState.BASIC}.
	 */
	private LexerState currentState = LexerState.BASIC;
	
	/**
	 * Constructs a {@code Lexer} with specified text which will be tokenized using
	 * {@code this} object.
	 * 
	 * @param text
	 *        text to be tokenized
	 * @throws NullPointerException if the given {@code String} is {@code null}
	 */
	public Lexer(String text) {
		Objects.requireNonNull(text, "Given text cannot be null!");
		data = text.toCharArray();
	}
	
	/**
	 * Generates and returns next {@code Token} if its legal, throws
	 * {@code LexerException} otherwise.
	 * 
	 * @return next Token if it can be tokenized
	 * @throws LexerException if the next character or sequence of characters cannot
	 *         be tokenized or there are no characters left
	 */
	public Token nextToken() {
		ignoreSpaces();
		if (currentIndex > data.length) {
			throw new LexerException("This lexer has no more elements to tokenize!");
		}
		if (currentIndex == data.length) {
			currentIndex++;
			return token = Token.EOF_TOKEN;
		}
		if (currentState == LexerState.BASIC) {
			return token = basicLexerState();
		} else {
			return token = extendedLexerState();
		}
	}
	
	/**
	 * This method is used to tokenize text when {@code LexerState} is set to {@code LexerState.EXTENDED}.
	 * <br>
	 * It returns the tokenized element as {@code Token} or throws {@code LexerException} if the element
	 * cannot be tokenized.
	 * 
	 * @return the tokenized element as {@code Token}
	 * @throws LexerException if the next character or sequence of characters cannot
	 *         be tokenized or there are no characters left
	 */
	private Token extendedLexerState() {
		if (data[currentIndex] == '#') {
			return new Token(TokenType.SYMBOL, data[currentIndex++]);
		}
		StringBuilder word = new StringBuilder();
		while (currentIndex < data.length && !isIrrelevant(data[currentIndex]) && data[currentIndex] != '#') {
			word.append(data[currentIndex++]);
		}
		return new Token(TokenType.WORD, word.toString());
	}
	
	/**
	 * This method is used to tokenize text when {@code LexerState} is set to {@code LexerState.BASIC}.
	 * <br>
	 * It returns the tokenized element as {@code Token} or throws {@code LexerException} if the element
	 * cannot be tokenized.
	 * 
	 * @return the tokenized element as {@code Token}
	 * @throws LexerException if the next character or sequence of characters cannot
	 *         be tokenized or there are no characters left
	 */
	private Token basicLexerState() {
		if (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') {
			StringBuilder word = new StringBuilder();
			while (currentIndex < data.length && (data[currentIndex] == '\\' || Character.isLetter(data[currentIndex]))) {
				if (data[currentIndex] == '\\') {
					escapeNumberOrBackslash();
				}
				word.append(data[currentIndex++]);
			}
			return new Token(TokenType.WORD, word.toString());
		}
		if (Character.isDigit(data[currentIndex])) {
			StringBuilder number = new StringBuilder();
			while (currentIndex < data.length && Character.isDigit(data[currentIndex])) {
				number.append(data[currentIndex++]);
			}
			try {
				return new Token(TokenType.NUMBER, Long.parseLong(number.toString()));
			} catch (NumberFormatException ex) {
				throw new LexerException("Number is too big to be interpreted as long!");
			}
		}
		return new Token(TokenType.SYMBOL, data[currentIndex++]);
	}
	
	/**
	 * Increments {@link #currentIndex} until {@code data[currentIndex]} points to relevant
	 * character or {@link #currentIndex} goes out of range [0, data.length - 1].
	 */
	private void ignoreSpaces() {
		for ( ; currentIndex < data.length && isIrrelevant(data[currentIndex]); currentIndex++);
	}
	
	/**
	 * Returns {@code true} if the character is irrelevant, in other words if character is from set
	 * {'\r', '\n', '\t', ' '}.
	 * 
	 * @param character
	 *        character to be checked
	 * @return {@code true} if the character is irrelevant
	 */
	private boolean isIrrelevant(char character) {
		return character == '\r' || character == '\n' || character == '\t' || character == ' ';
	}
	
	/**
	 * Determines if backslash was used properly to escape either number or another backslash.
	 * If not, the method throws {@code LexerException}.
	 * 
	 * @throws LexerException if the backslash was not used properly
	 */
	private void escapeNumberOrBackslash() {
		currentIndex++;
		if (currentIndex >= data.length || (!Character.isDigit(data[currentIndex]) && data[currentIndex] != '\\')) {
			throw new LexerException("Escaping without folowing number!");
		}
	}
	
	/**
	 * Sets the state of {@code this Lexer} to the specified one. It cannot be
	 * {@code null}.
	 * 
	 * @param state
	 *        state to set {@code this Lexer} to
	 * @throws NullPointerException if the given state is {@code null}
	 */
	public void setState(LexerState state) {	
		Objects.requireNonNull(state, "Given state cannot be null!");
		currentState = state;
	}
	
	/**
	 * Returns the last generated {@code Token}. This method can be called multiple times
	 * and it will return the same {@code Token} as it does not trigger generating next one.
	 * If no Tokens were generated yet, this method will return null.
	 * 
	 * @return last generated {@code Token}
	 */
	public Token getToken() {
		return token;
	}
	
}
