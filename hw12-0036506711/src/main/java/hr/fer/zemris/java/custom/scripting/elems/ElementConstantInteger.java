package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Represents tokenized Constant integer element of a tokenized expression.
 * 
 * @author Filip Husnjak
 */
public class ElementConstantInteger extends Element {

	/**
	 * Value of this element.
	 */
	private final int value;
	
	/**
	 * Constructs {@code this} element with a given value.
	 * 
	 * @param value
	 *        value of {@code this} element
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	
	@Override
	public String asText() {
		return String.valueOf(value);
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
	/**
	 * Returns the value of this element.
	 * 
	 * @return value of this element
	 */
	public int getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ElementConstantInteger))
			return false;
		ElementConstantInteger other = (ElementConstantInteger) obj;
		return value == other.value;
	}
	
}
