package hr.fer.zemris.java.custom.scripting.elems;

public class ElementVariable extends Element {

	private final String name;
	
	public ElementVariable(String name) {
		this.name = name;
	}
	
	@Override
	public String asText() {
		return name;
	}
	
}
