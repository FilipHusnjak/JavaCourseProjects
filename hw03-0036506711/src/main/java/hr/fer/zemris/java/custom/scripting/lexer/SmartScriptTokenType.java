package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Represents the possible token types created by {@code SmartScriptLexer}.
 * 
 * @author Filip Husnjak
 */
public enum SmartScriptTokenType {
	
	EOF,
	
	TAG,
	
	VARIABLE,
	
	TEXT,
	
	STRING,
	
	CONSTANT_DOUBLE,
	
	CONSTANT_INTEGER,
	
	CLOSE_TAG,
	
	FUNCTION,
	
	SYMBOL
	
}
