package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

/**
 * Lexer with 2 states that tokenizes input text depending on the current state.
 * If the current state is set to {@code SmartScriptLexerState.INSIDE_TAG} it tokenizes
 * numbers, strings, symbols and functions. If the current state is set to 
 * {@code SmartScriptLexerState.DOCUMENT_TEXT} it can only tokenize text. 
 * SmartScriptLexerState can be set using the proper method.
 * 
 * @author Filip Husnjak
 */
public class SmartScriptLexer {


	/**
	 * Given expression as char array.
	 */
	private char[] data;
	
	/**
	 * Current token retrieved by {@code getToken} method
	 */
	private SmartScriptToken token;
	
	/**
	 * Index of the next character that was not tokenized.
	 */
	private int currentIndex;
    
	/**
	 * Current state of {@code this Lexer}. It can only be {@code LexerState.BASIC} or 
	 * {@code LexerState.EXTENDED}. By default its set to {@code LexerState.BASIC}.
	 */
	private SmartScriptLexerState currentState = SmartScriptLexerState.DOCUMENT_TEXT;
	
	/**
	 * Constructs a {@code Lexer} with specified text which will be tokenized using
	 * {@code this} object.
	 * 
	 * @param text
	 *        text to be tokenized
	 * @throws NullPointerException if the given {@code String} is {@code null}
	 */
	public SmartScriptLexer(String text) {
		Objects.requireNonNull(text, "Given text cannot be null!");
		data = text.toCharArray();
	}
	
	/**
	 * Generates and returns next {@code Token} if its legal, throws
	 * {@code LexerException} otherwise.
	 * 
	 * @return next Token if it can be tokenized
	 * @throws SmartScriptLexerException if the next character or sequence of characters cannot
	 *         be tokenized or there are no characters left
	 */
	public SmartScriptToken nextToken() {
		if (token == SmartScriptToken.EOF_TOKEN) {
			throw new SmartScriptLexerException("This lexer has no more elements to tokenize!");
		}
		if (currentIndex == data.length) {
			return token = SmartScriptToken.EOF_TOKEN;
		}
		if (currentState == SmartScriptLexerState.DOCUMENT_TEXT) {
			return token = documentTextLexerState();
		} else {
			return token = insideTagLexerState();
		}
	}
	
	/**
	 * This method is used to tokenize expression when {@code SmartScriptLexerState} 
	 * is set to {@code SmartScriptLexerState.DOCUMENT_TEXT}.
	 * <br>
	 * Method returns the tokenized element as {@code SmartScriptToken} or throws 
	 * {@code SmartScriptLexerException} if the element cannot be tokenized. 
	 * It reads text until first valid open tag appears. Only backslash
	 * or open curly bracket escapes are possible in this state.
	 * 
	 * @return the tokenized element as {@code SmartScriptToken}
	 * @throws SmartScriptLexerException if the next character or sequence of characters cannot
	 *         be tokenized or there are no characters left
	 */
	private SmartScriptToken documentTextLexerState() {
		if (checkForOpenTag()) {
			return getOpenTag();
		}
		StringBuilder text = new StringBuilder();
		while (currentIndex < data.length && !checkForOpenTag()) {
			if (data[currentIndex] == '\\') {
				escapeBracketOrBackslash();
			}
			text.append(data[currentIndex++]);
		}
		return new SmartScriptToken(SmartScriptTokenType.TEXT, text.toString());
	}
	
	/**
	 * Returns {@code SmartScriptToken} containing parsed tag name. It can only be 
	 * variable name or '='.
	 * 
	 * @return {@code SmartScriptToken} containing parsed tag name
	 * @throws SmartScriptLexerException if the tag name is invalid
	 */
	private SmartScriptToken getOpenTag() {
		currentIndex += 2;
		ignoreIrrelevantCharacters();
		if (currentIndex >= data.length || 
				(!Character.isLetter(data[currentIndex]) && data[currentIndex] != '=')) {
			throw new SmartScriptLexerException("Tag name is invalid!");
		}
		if (data[currentIndex] == '=') {
			return new SmartScriptToken(SmartScriptTokenType.TAG, String.valueOf(data[currentIndex++]));
		}
		return new SmartScriptToken(SmartScriptTokenType.TAG, extractVariableName().toUpperCase());
	}
	
	/**
	 * This method is used to tokenize expression when {@code SmartScriptLexerState} 
	 * is set to {@code SmartScriptLexerState.INSIDE_TAG}.
	 * <br>
	 * It returns the tokenized element as {@code SmartScriptToken} or throws 
	 * {@code SmartScriptLexerException} if the element cannot be tokenized. 
	 * In this state as next element it can return: variable, string, function, symbol,
	 * constant double or constant integer.
	 * 
	 * @return the tokenized element as {@code SmartScriptToken}
	 * @throws SmartScriptLexerException if the next character or sequence of 
	 * 		   characters cannot be tokenized or there are no characters left
	 */
	private SmartScriptToken insideTagLexerState() {
		ignoreIrrelevantCharacters();
		if (checkForClosingTag()) {
			currentIndex += 2;
			return new SmartScriptToken(SmartScriptTokenType.CLOSE_TAG, null);
		}
		if (Character.isDigit(data[currentIndex]) || checkNegativeNumber()) {
			return extractNumber();
		}
		if (Character.isLetter(data[currentIndex])) {
			return new SmartScriptToken(SmartScriptTokenType.VARIABLE, extractVariableName());
		}
		if (data[currentIndex] == '@') {
			if (!Character.isLetter(data[++currentIndex])) {
				throw new SmartScriptLexerException("Invalid function name!");
			}
			return new SmartScriptToken(SmartScriptTokenType.FUNCTION, extractVariableName());
		}
		if (data[currentIndex] == '"') {
			currentIndex++;
			return extractString();
		}
		return new SmartScriptToken(SmartScriptTokenType.SYMBOL, data[currentIndex++]);
	}
	
	/**
	 * Returns tokenized text inside quotation marks. It reads text until non escaped
	 * quotation mark is read. Only quotation marks, backslash or ('\n', '\t', '\r') 
	 * escapes are possible, otherwise this method throws {@code SmartScriptLexerException}.
	 * 
	 * @return {@code SmartScriptToken} that contains tokenized string
	 * @throws SmartScriptLexerException if invalid escaping is provided or string
	 *         was not closed correctly.
	 */
	private SmartScriptToken extractString() {
		StringBuilder string = new StringBuilder();
		while (currentIndex < data.length && data[currentIndex] != '"') {
			if (data[currentIndex] == '\\') {
				escapeQuotationsOrBackslash();
				switch (data[currentIndex]) {
				case 'n':
					string.append("\n");
					break;
				case 'r':
					string.append("\r");
					break;
				case 't':
					string.append("\t");
					break;
				default:
					string.append(data[currentIndex]);
				}
				currentIndex++;
			} else {
				string.append(data[currentIndex++]);
			}
		}
		if (currentIndex++ == data.length) {
			throw new SmartScriptLexerException("String constants have to end with \"!");
		}
		return new SmartScriptToken(SmartScriptTokenType.STRING, string.toString());
	}
	
	/**
	 * Returns {@code true} if currently read curly bracket is used for opening a tag.
	 * 
	 * @return {@code true} if currently read curly bracket is used for opening a tag
	 */
	private boolean checkForOpenTag() {
		if (currentIndex == data.length - 1) return false;
		return data[currentIndex] == '{' && data[currentIndex + 1] == '$';
	}
	
	/**
	 * Returns {@code true} if currently read curly bracket is used for closing a tag.
	 * 
	 * @return {@code true} if currently read curly bracket is used for closing a tag
	 */
	private boolean checkForClosingTag() {
		if (currentIndex == data.length - 1) return false;
		return data[currentIndex] == '$' && data[currentIndex + 1] == '}';
	}
	
	/**
	 * Checks whether backslash is used to escape another backslash or curly bracket
	 * or throws exception if thats not the case.
	 * 
	 * @throws SmartScriptLexerException if backslash was not used to escape 
	 *         curly bracket or another backslash
	 */
	private void escapeBracketOrBackslash() {
		currentIndex++;
		if (currentIndex >= data.length || (data[currentIndex] != '{' && data[currentIndex] != '\\')) {
			throw new SmartScriptLexerException(
					"Escaping this character in document text is prohibited! Char: " + 
							data[currentIndex]);
		}
	}
	
	/**
	 * Checks whether backslash is used to escape another backslash or quotation mark
	 * or throws exception if thats not the case.
	 * 
	 * @throws SmartScriptLexerException if backslash was not used to escape 
	 *         curly bracket or another backslash
	 */
	private void escapeQuotationsOrBackslash() {
		currentIndex++;
		if (currentIndex >= data.length || 
				(data[currentIndex] != '"' && data[currentIndex] != '\\' 
				&& data[currentIndex] != 'n' && data[currentIndex] != 'r' 
				&& data[currentIndex] != 't')) {
			throw new SmartScriptLexerException(
					"Escaping this character in string is prohibited! Char: " + data[currentIndex]);
		}
	}
	
	/**
	 * Increments {@link #currentIndex} until {@code data[currentIndex]} points to relevant
	 * character or {@link #currentIndex} goes out of range [0, data.length - 1].
	 */
	private void ignoreIrrelevantCharacters() {
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
	public void setState(SmartScriptLexerState state) {	
		Objects.requireNonNull(state, "Given state cannot be null!");
		currentState = state;
	}
	
	/**
	 * Tokenizes text while valid characters are read and returns variable name
	 * as (@code String}.
	 * 
	 * @return variable name as (@code String}
	 */
	private String extractVariableName() {
		StringBuilder name = new StringBuilder();
		while (currentIndex < data.length && isValidName(data[currentIndex])) {
			name.append(data[currentIndex++]);
		}
		return name.toString();
	}
	
	/**
	 * Tokenizes text while valid digits are read and returns {@code SmartScriptToken}
	 * with right number format (Double or Integer) and value.
	 * 
	 * @return {@code SmartScriptToken} with right number format (Double or Integer) and value
	 * @throws SmartScriptLexerException if it is not possible to convert the number to Integer or Double
	 */
	private SmartScriptToken extractNumber() {
		StringBuilder number = new StringBuilder();
		if (checkNegativeNumber()) {
			number.append(data[currentIndex++]);
		}
		boolean dot = false;
		while (currentIndex < data.length && 
				(Character.isDigit(data[currentIndex]) || (!dot && data[currentIndex] == '.'))) {
			if (data[currentIndex] == '.') {
				dot = true;
				if (currentIndex == data.length - 1 || !Character.isDigit(data[currentIndex + 1])) break;
			}
			number.append(data[currentIndex++]);
		}
		String num = number.toString();
		try {
			return new SmartScriptToken(
					SmartScriptTokenType.CONSTANT_INTEGER, 
					Integer.parseInt(num));
		} catch (NumberFormatException ex1) {
			try {
				return new SmartScriptToken(
						SmartScriptTokenType.CONSTANT_DOUBLE, 
						Double.parseDouble(num));
			} catch (NumberFormatException ex2) {
				throw new SmartScriptLexerException(
						"Given number is not possible to convert to Integer or Double! Number: " + num);
			}
		}
	}
	
	/**
	 * Returns {@code true} if minus sign is used to form a negative number.
	 * 
	 * @return {@code true} if minus sign is used to form a negative number
	 */
	private boolean checkNegativeNumber() {
		return currentIndex < data.length - 1 && data[currentIndex] == '-' && 
				Character.isDigit(data[currentIndex + 1]);
	}
	
	/**
	 * Returns {@code true} if the given character can form a variable name.
	 * 
	 * @param c
	 *        character to be tested
	 * @return {@code true} if the given character can form a variable name
	 */
	private boolean isValidName(char c) {
		return Character.isLetter(c) || Character.isDigit(c) || c == '_';
	}
	
	/**
	 * Returns the last generated {@code SmartScriptToken} or {@code null} if no
	 * tokens are generated yet.
	 * 
	 * @return last generated {@code SmartScriptToken} or {@code null} if no
	 *         tokens are generated yet
	 */
	public SmartScriptToken getToken() {
		return token;
	}
	
}
