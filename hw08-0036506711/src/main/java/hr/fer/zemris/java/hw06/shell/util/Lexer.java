package hr.fer.zemris.java.hw06.shell.util;

public class Lexer {
	
	private char[] data;
	
	private Token currentToken;
	
	private int currentIndex;
	
	public Lexer(String data) {
		this.data = data.toCharArray();
	}
	
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
	
	private boolean checkForOpenBrackets() {
		return currentIndex < data.length - 1 && data[currentIndex] == '$' && data[currentIndex + 1] == '{';
	}
	
	public Token getCurrentToken() {
		return currentToken;
	}

}
