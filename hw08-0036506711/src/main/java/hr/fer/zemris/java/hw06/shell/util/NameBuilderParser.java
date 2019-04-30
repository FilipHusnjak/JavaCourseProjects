package hr.fer.zemris.java.hw06.shell.util;

public class NameBuilderParser {
	
	private Lexer lexer;
	
	private NameBuilder nameBuilder = (r, sb) -> {};

	public NameBuilderParser(String expression) {
		lexer = new Lexer(expression);
		Token token;
		while ((token = lexer.extractNextToken()) != Token.EOF_TOKEN) {
			if (token.getType() == TokenType.COMMAND) {
				String[] parts = token.getValue().split(",");
				if (parts.length > 2) {
					throw new IllegalArgumentException("Too many arguments in command tag provided!");
				}
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
			} else {
				nameBuilder = nameBuilder.text(token.getValue());
			}
		}
	}
	
	public NameBuilder getNameBuilder() {
		return nameBuilder;
	}
	
}
