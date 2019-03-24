package hr.fer.zemris.java.custom.scripting.elems;

public class ElementOperator extends Element {

	private final String symbol;

	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String asText() {
		return symbol;
	}
	
}
