package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Represents tokenized Constant double element of a tokenized expression.
 * 
 * @author Filip Husnjak
 */
public class ElementConstantDouble extends Element {

	/**
	 * Value of this element.
	 */
	private final double value;

	/**
	 * Constructs {@code this} element with a given value.
	 * 
	 * @param value
	 *        value of {@code this} element
	 */
	public ElementConstantDouble(double value) {
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
	public double getValue() {
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
		if (!(obj instanceof ElementConstantDouble))
			return false;
		ElementConstantDouble other = (ElementConstantDouble) obj;
		return Double.doubleToLongBits(value) == Double.doubleToLongBits(other.value);
	}

}
