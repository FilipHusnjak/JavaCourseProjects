package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Arrays;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Representation of an {@code Echo TAG} that stores all elements in an array. It cannot
 * have children since {@code Echo TAGs} are empty tags.
 * 
 * @author Filip Husnjak
 *
 */
public class EchoNode extends Node {

	/**
	 * Elements of {@code this} Echo TAG.
	 */
	private final Element[] elements;

	/**
	 * Constructs {@code EchoNode} with given elements.
	 * 
	 * @param elements
	 */
	public EchoNode(Element[] elements) {
		super();
		this.elements = elements;
	}

	/**
	 * @throws UnsupportedOperationException since Echo TAGs cannot have children
	 */
	@Override
	public void addChildNode(Node child) {
		throw new UnsupportedOperationException("Echo TAG is an empty TAG"
				+ " so it cannot have children!");
	}

	/**
	 * 
	 * @return {@code ZERO} since Echo TAGs cannot have children
	 */
	@Override
	public int numberOfChildren() {
		return 0;
	}

	/**
	 * @throws UnsupportedOperationException since Echo TAGs cannot have children
	 */
	@Override
	public Node getChild(int index) {
		throw new UnsupportedOperationException("Echo TAG is an empty TAG"
				+ " so it cannot have children!");
	}

	/**
	 * Returns elements of this {@code EchoNode}.
	 * 
	 * @return elements of this {@code EchoNode}
	 */
	public Element[] getElements() {
		return elements;
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
		if (!(obj instanceof EchoNode)) return false;
		EchoNode echoNode = (EchoNode) obj;
		return Arrays.equals(elements, echoNode.getElements());
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(elements);
	}

}
