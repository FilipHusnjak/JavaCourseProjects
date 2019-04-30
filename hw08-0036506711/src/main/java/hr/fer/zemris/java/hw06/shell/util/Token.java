package hr.fer.zemris.java.hw06.shell.util;

import java.util.Objects;

/**
 * Token as the result of tokenizing character or sequence of characters 
 * with specified {@code TokenType} and {@code value}.
 * 
 * @author Filip Husnjak
 *
 */
public class Token {
	
	/**
	 * Type of {@code this Token} specified by enumeration {@code TokenType}.
	 */
	private TokenType type;
	
	/**
	 * Value of this {@code QueryToken}.
	 */
	private String value;
	
	public static final Token EOF_TOKEN = new Token(TokenType.EOF, null);
	
	/**
	 * Constructs a {@code Token} object with a given type and value.
	 * 
	 * @param type
	 *        type of {@code this Token}
	 * @param value
	 *        value of {@code this Token}
	 */
	public Token(TokenType type, String value) {
		Objects.requireNonNull(type, "Given type cannot be null!");
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Returns the value of {@code this Token}.
	 * 
	 * @return the value of {@code this Token} 
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Returns the type of {@code this Token}.
	 * 
	 * @return the type of {@code this Token} 
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(type, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Token))
			return false;
		Token other = (Token) obj;
		return type == other.type && Objects.equals(value, other.value);
	}
	
}

