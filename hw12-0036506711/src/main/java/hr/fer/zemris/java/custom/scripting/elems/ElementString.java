package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Represents tokenized String element of a tokenized expression.
 * 
 * @author Filip Husnjak
 */
public class ElementString extends Element {

	/**
	 * Value of this element.
	 */
	private final String value;

	/**
	 * Constructs {@code this} element with a given {@code String}.
	 * 
	 * @param value value of {@code this} {@code String} element
	 */
	public ElementString(String value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return value;
	}

	@Override
	public String toString() {
		// Replaces all single backslashes with double backslash and
		// adds one backslash to all quote marks.
		return "\"" + value.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"") + "\"";
	}

	/**
	 * Returns the value of this element.
	 * 
	 * @return value of this element
	 */
	public String getValue() {
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
		if (!(obj instanceof ElementString))
			return false;
		ElementString other = (ElementString) obj;
		return Objects.equals(value, other.value);
	}

}
