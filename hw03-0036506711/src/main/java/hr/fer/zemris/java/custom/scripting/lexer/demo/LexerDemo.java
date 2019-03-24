package hr.fer.zemris.java.custom.scripting.lexer.demo;

import hr.fer.zemris.java.custom.scripting.lexer.Lexer;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;

public class LexerDemo {

	public static void main(String[] args) {
		Lexer lexer = new Lexer("Example \\{$=1$}. Now actually write one {$=1$}");
		Token token;
		do {
			token = lexer.nextToken();
			System.out.println(token.getType() + " " + token.getValue());
		} while (token.getType() != TokenType.EOF);
	}

}
