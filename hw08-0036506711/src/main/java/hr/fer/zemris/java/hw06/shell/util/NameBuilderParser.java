package hr.fer.zemris.java.hw06.shell.util;

import hr.fer.zemris.java.hw06.shell.commands.MassrenameShellCommand;

/**
 * Parser used to parse expressions for renaming purposes in {@link MassrenameShellCommand}.
 * It uses {@link Lexer} to tokenize data. Creates final NameBuilder whose execute
 * method fills the given StringBuilder object with appropriate data.
 * 
 * @author Filip Husnjak
 */
public class NameBuilderParser {
	
	/**
	 * Lexer used to tokenize data.
	 */
	private Lexer lexer;
	
	/**
	 * NameBuilder used to create new file names.
	 */
	private NameBuilder nameBuilder = NameBuilder.blank;

	/**
	 * Constructs new NameBuilderParser object with given expression.
	 * 
	 * @param expression
	 *        expression to be parsed.
	 * @throws NullPointerException if the given expression is {@code null}
	 * @throws LexerException if the expression is invalid
	 * @throws IllegalArgumentException if the given expression cannot be parsed
	 */
	public NameBuilderParser(String expression) {
		lexer = new Lexer(expression);
		Token token;
		while ((token = lexer.extractNextToken()) != Token.EOF_TOKEN) {
			if (token.getType() == TokenType.COMMAND) {
				String[] parts = token.getValue().split(",");
				if (parts.length > 2) {
					throw new IllegalArgumentException("Too many arguments in command tag provided!");
				}
				try {
					if (parts.length == 1) {
						nameBuilder = nameBuilder.group(Integer.parseInt(parts[0].strip()));
					} else {
						char padding = ' ';
						String fill = parts[1].strip();
						if (fill.startsWith("0") && fill.length() > 1) {
							padding = '0';
							fill = fill.substring(1);
						}
						nameBuilder = nameBuilder.group(Integer.parseInt(parts[0].strip()), padding, Integer.parseInt(fill));
					}
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("Number expected in command tag but not provided!");
				}
			} else {
				nameBuilder = nameBuilder.text(token.getValue());
			}
		}
	}
	
	/**
	 * Returns NameBuilder created by this parser.
	 * 
	 * @return NameBuilder created by this parser
	 */
	public NameBuilder getNameBuilder() {
		return nameBuilder;
	}
	
}
