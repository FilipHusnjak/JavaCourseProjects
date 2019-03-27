package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

/**
 * Token as the result of tokenizing character or sequence of characters 
 * with specified {@code TokenType} and {@code value}.
 * 
 * @author Filip Husnjak
 *
 */
public class SmartScriptToken {
	
	/**
	 * Type of {@code this Token} specified by enumeration {@code TokenType}.
	 */
	private SmartScriptTokenType type;
	
	/**
	 * Value of this {@code Token}.
	 */
	private Object value;
	
	public static final SmartScriptToken EOF_TOKEN = new SmartScriptToken(SmartScriptTokenType.EOF, null);
	
	/**
	 * Constructs a {@code Token} object with a given type and value.
	 * 
	 * @param type
	 *        type of {@code this Token}
	 * @param value
	 *        value of {@code this Token}
	 */
	public SmartScriptToken(SmartScriptTokenType type, Object value) {
		Objects.requireNonNull(type, "Given type cannot be null!");
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Returns the value of {@code this Token}.
	 * 
	 * @return the value of {@code this Token} 
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Returns the type of {@code this Token}.
	 * 
	 * @return the type of {@code this Token} 
	 */
	public SmartScriptTokenType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SmartScriptToken))
			return false;
		SmartScriptToken other = (SmartScriptToken) obj;
		return type == other.type && Objects.equals(value, other.value);
	}
	
}
