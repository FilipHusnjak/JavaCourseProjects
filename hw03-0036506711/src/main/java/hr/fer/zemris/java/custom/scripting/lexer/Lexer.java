package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Lexer with 2 states that tokenizes input text depending on the current state.
 * If the current state is set to {@code LexerState.BASIC} it tokenizes numbers, words
 * and symbols. If the current state is set to {@code LexerState.EXTENDED} it can only
 * tokenize words and symbols. If Lexer reads character '#' it switches state. State
 * can also be set using the proper method.
 * 
 * @author Filip Husnjak
 */
public class Lexer {


	/**
	 * Given expression as char array.
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
	private LexerState currentState = LexerState.DOCUMENT_TEXT;
	
	private boolean firstTimeInside;
	
	private static ArrayIndexedCollection operators = new ArrayIndexedCollection();
	
	static {
		operators.add('+');
		operators.add('-');
		operators.add('*');
		operators.add('/');
		operators.add('^');
	}
	
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
		if (currentIndex > data.length) {
			throw new LexerException("This lexer has no more elements to tokenize!");
		}
		if (currentIndex == data.length) {
			currentIndex++;
			return token = Token.EOF_TOKEN;
		}
		if (currentState == LexerState.DOCUMENT_TEXT) {
			return token = documentTextLexerState();
		} else if (currentState == LexerState.INSIDE_TAG) {
			return token = insideTagLexerState();
		} else {
			return token = stringLexerState();
		}
	}
	
	private Token documentTextLexerState() {
		StringBuilder text = new StringBuilder();
		while (currentIndex < data.length) {
			if (data[currentIndex] == '\\') {
				escapeBracketOrBackslash();
			} else if (data[currentIndex] == '{') {
				if (checkForOpenTag()) {
					currentState = LexerState.INSIDE_TAG;
					firstTimeInside = true;
					break;
				}
			}
			text.append(data[currentIndex++]);
		}
		return new Token(TokenType.TEXT, text.toString());
	}
	
	private Token insideTagLexerState() {
		ignoreSpaces();
		if (firstTimeInside) {
			currentIndex += 2;
			ignoreSpaces();
			// TODO ne radi dobro za tag ime =asda 
			if (currentIndex >= data.length || (!Character.isLetter(data[currentIndex]) && data[currentIndex] != '=')) {
				throw new LexerException("Tag name is invalid!");
			}
			firstTimeInside = false;
			if (data[currentIndex] == '=') {
				return new Token(TokenType.TAG, String.valueOf(data[currentIndex++]));
			}
			return new Token(TokenType.TAG, getName().toUpperCase());
		}
		if (data[currentIndex] == '$') {
			if (!checkForClosingTag()) {
				throw new LexerException("Closing tag is not written properly!");
			}
			currentState = LexerState.DOCUMENT_TEXT;
			currentIndex += 2;
			firstTimeInside = false;
			return new Token(TokenType.CLOSE_TAG, null);
		}
		if (data[currentIndex] == '"') {
			currentIndex++;
			currentState = LexerState.STRING_INSIDE_TAG;
			return nextToken();
		}
		// TODO brojevi koji pocinju s - ili + trenutno ne rade
		if (Character.isDigit(data[currentIndex])) {
			return new Token(TokenType.NUMBER, getNumber());
		}
		if (Character.isLetter(data[currentIndex])) {
			return new Token(TokenType.VARIABLE, getName());
		}
		if (data[currentIndex] == '@') {
			if (!Character.isLetter(data[++currentIndex])) {
				throw new LexerException("Invalid function name! Function names should start with a letter!");
			}
			return new Token(TokenType.FUNCTION, getName());
		}
		if (operators.contains(data[currentIndex])) {
			return new Token(TokenType.OPERATOR, data[currentIndex++]);
		}
		throw new LexerException("Invalid symbol inside a TAG: \"" + data[currentIndex] + "\"");
	}
	
	private Token stringLexerState() {
		StringBuilder string = new StringBuilder();
		while (currentIndex < data.length && data[currentIndex] != '"') {
			if (data[currentIndex] == '\\') {
				escapeQuotationsOrBackslash();
			}
			string.append(data[currentIndex++]);
		}
		currentIndex = currentIndex < data.length ? currentIndex + 1 : currentIndex;
		currentState = LexerState.INSIDE_TAG;
		return new Token(TokenType.TEXT, string.toString());
	}
	
	private boolean checkForOpenTag() {
		if (currentIndex == data.length - 1) return false;
		return data[currentIndex] == '{' && data[currentIndex + 1] == '$';
	}
	
	private boolean checkForClosingTag() {
		if (currentIndex == data.length - 1) return false;
		return data[currentIndex] == '$' && data[currentIndex + 1] == '}';
	}
	
	private void escapeBracketOrBackslash() {
		currentIndex++;
		if (currentIndex >= data.length || (data[currentIndex] != '{' && data[currentIndex] != '\\')) {
			throw new LexerException("Escaping without folowing number!");
		}
	}
	
	private void escapeQuotationsOrBackslash() {
		currentIndex++;
		if (currentIndex >= data.length || (data[currentIndex] != '"' && data[currentIndex] != '\\')) {
			throw new LexerException("Escaping without quotations!");
		}
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
	
	private String getName() {
		StringBuilder name = new StringBuilder();
		while (currentIndex < data.length && isValidName(data[currentIndex])) {
			name.append(data[currentIndex++]);
		}
		return name.toString();
	}
	
	private Double getNumber() {
		StringBuilder number = new StringBuilder();
		while (currentIndex < data.length && (Character.isDigit(data[currentIndex]) || data[currentIndex] == '.')) {
			number.append(data[currentIndex++]);
		}
		try {
			return Double.parseDouble(number.toString());
		} catch (NumberFormatException ex) {
			throw new LexerException("Number is in wrong format!");
		}
	}
	
	private boolean isValidName(char c) {
		return Character.isLetter(c) || Character.isDigit(c) || c == '_';
	}
	
	public Token getToken() {
		return token;
	}
	
}
