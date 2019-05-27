package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Represents tokenized variable element of a tokenized expression.
 * 
 * @author Filip Husnjak
 */
public class ElementVariable extends Element {

	/**
	 * Name of {@code this} variable.
	 */
	private final String name;
	
	/**
	 * Constructs {@code this} element with a given name.
	 * 
	 * @param name
	 *        name of {@code this} variable
	 */
	public ElementVariable(String name) {
		this.name = name;
	}
	
	@Override
	public String asText() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Returns the name of {@code this} variable.
	 * 
	 * @return the name of {@code this} variable
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
		if (!(obj instanceof ElementVariable))
			return false;
		ElementVariable other = (ElementVariable) obj;
		return Objects.equals(name, other.name);
	}
	
}
