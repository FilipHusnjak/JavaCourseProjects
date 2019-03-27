package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Represents tokenized function element of a tokenized expression.
 * 
 * @author Filip Husnjak
 */
public class ElementFunction extends Element {

	/**
	 * Name of {@code this} function.
	 */
	private final String name;

	/**
	 * Constructs {@code this} element with a given name.
	 * 
	 * @param name
	 *        name of {@code this} function element
	 */
	public ElementFunction(String name) {
		this.name = name;
	}

	/**
	 * Returns name of {@code this} function.
	 * 
	 * @return name of {@code this} function
	 */
	@Override
	public String asText() {
		return name;
	}
	
	@Override
	public String toString() {
		return "@" + name;
	}
	
	/**
	 * Returns the name of this function element.
	 * 
	 * @return name of this function element
	 */
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ElementFunction))
			return false;
		ElementFunction other = (ElementFunction) obj;
		return Objects.equals(name, other.name);
	}
	
}
