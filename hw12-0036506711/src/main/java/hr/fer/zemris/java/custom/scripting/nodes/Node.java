package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * General representation of a {@code Node} in a document tree. More specific implementations
 * required.
 * 
 * @author Filip Husnjak
 */
public abstract class Node {
	
	/**
	 * Collection of children of {@code this} {@code Node}.
	 */
	private List<Node> collection;
	
	/**
	 * Adds given {@code Node} as child of {@code this} {@code Node}.
	 * 
	 * @param child
	 *        child to be added
	 */
	public void addChildNode(Node child) {
		if (collection == null) {
			collection = new ArrayList<>();
		}
		collection.add(child);
	}
	
	/**
	 * Returns the number of children this {@code Node} contains.
	 * 
	 * @return the number of children this {@code Node} contains
	 */
	public int numberOfChildren() {
		if (collection == null) {
			return 0;
		}
		return collection.size();
	}
	
	/***
	 * Returns the child of {@code this} {@code Node} at specified index.
	 * 
	 * @param index
	 *        index of the wanted child {@code Node}
	 * @return the child of {@code this} {@code Node} at specified index
	 * @throws NullPointerException if {@code this} {@code Node} contains no children
	 * @throws IndexOutOfBoundsException if the given index is
	 *         <br>{@code index < ZERO} or {@code index >= numberOfChildren()}
	 */
	public Node getChild(int index) {
		Objects.requireNonNull(collection, 
				"Collection is not created yet, method addChildNode has not been called yet!");
		return collection.get(index);
	}
	
	/**
	 * Support for visitor design pattern. Calls visit method upon given visitor object.
	 * 
	 * @param visitor
	 *        visitor whose method gets called
	 * @return {@code String} result of the visit method
	 */
	public abstract void accept(INodeVisitor visitor);
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Node)) return false;
		Node node = (Node) obj;
		if (node.numberOfChildren() != this.numberOfChildren()) return false;
		for (int i = 0; i < numberOfChildren(); ++i) {
			if (!collection.get(i).equals(node.getChild(i))) return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		Long sum = 0L;
		for (int i = 0; i < numberOfChildren(); ++i) {
			sum += collection.get(i).hashCode();
		}
		return Objects.hash(sum);
	}
	
}
