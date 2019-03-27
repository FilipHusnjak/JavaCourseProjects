package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Represents tokenized operator element of a tokenized expression.
 * 
 * @author Filip Husnjak
 */
public class ElementOperator extends Element {

	/**
	 * Symbol of an operator.
	 */
	private final String symbol;

	/**
	 * Constructs {@code this} element with a given symbol.
	 * 
	 * @param symbol
	 *        symbol of {@code this} operator element
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Returns symbol of {@code this} operator.
	 * 
	 * @return symbol of {@code this} operator
	 */
	@Override
	public String asText() {
		return symbol;
	}
	
	@Override
	public String toString() {
		return symbol;
	}
	
	/**
	 * Returns the symbol of this element.
	 * 
	 * @return symbol of this element
	 */
	public String getSymbol() {
		return symbol;
	}

	@Override
	public int hashCode() {
		return Objects.hash(symbol);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ElementOperator))
			return false;
		ElementOperator other = (ElementOperator) obj;
		return Objects.equals(symbol, other.symbol);
	}
	
}
