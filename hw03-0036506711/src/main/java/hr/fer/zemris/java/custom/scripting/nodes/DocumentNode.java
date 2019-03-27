package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Represents the root Node of a document tree.
 * 
 * @author Filip Husnjak
 */
public class DocumentNode extends Node {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String accept(NodeVisitor visitor) {
		return visitor.visit(this);
	}
	
}
