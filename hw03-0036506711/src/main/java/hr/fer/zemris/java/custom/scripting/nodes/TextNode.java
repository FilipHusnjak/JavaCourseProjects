package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

/**
 * Representation of an {@code DocumentText}. It cannot
 * have children since {@code DocumentText} is not a TAG.
 * 
 * @author Filip Husnjak
 */
public class TextNode extends Node {
	
	/**
	 * Text of {@code this} {@code TextNode}.
	 */
	private final String text;
	
	/**
	 * Constructs {@code TextNode} with a given text.
	 * 
	 * @param text 
	 *        document text
	 * @throws NullPointerException if the given text {@code null}
	 */
	public TextNode(String text) {
		Objects.requireNonNull(text, "Given text cannot be null!");
		this.text = text;
	}
	
	/**
	 * Returns the text of this {@code TextNode}.
	 * 
	 * @return the text of this {@code TextNode}
	 */
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		// Replaces all single backslashes with double backslash and
		// adds one backslash to all possible TAG openings.
		return text.replaceAll("\\\\", "\\\\\\\\").replaceAll("[{]\\$", "\\\\{\\$");
	}
	
	/**
	 * @throws UnsupportedOperationException since {@code DocumentText} is not a TAG
	 */
	@Override
	public void addChildNode(Node child) {
		throw new UnsupportedOperationException("Echo TAG is an empty TAG"
				+ " so it cannot have children!");
	}

	/**
	 * {@inheritDoc}
	 * @return {@code ZERO} since {@code DocumentText} is not a TAG and it cannot have children
	 */
	@Override
	public int numberOfChildren() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @throws UnsupportedOperationException {@code DocumentText} is not a TAG
	 */
	@Override
	public Node getChild(int index) {
		throw new UnsupportedOperationException("Echo TAG is an empty TAG"
				+ " so it cannot have children!");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String accept(NodeVisitor visitor) {
		return visitor.visit(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof TextNode)) return false;
		TextNode textNode = (TextNode) obj;
		return textNode.getText().equals(text);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(text);
	}

}
