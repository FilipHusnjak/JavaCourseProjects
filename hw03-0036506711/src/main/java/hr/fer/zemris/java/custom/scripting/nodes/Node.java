package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

public class Node {
	
	private ArrayIndexedCollection collection;
	
	public void addChildNode(Node child) {
		if (collection == null) {
			collection = new ArrayIndexedCollection();
		}
		collection.add(child);
	}
	
	public int numberOfChildren() {
		if (collection == null) {
			return 0;
		}
		return collection.size();
	}
	
	public Node getChild(int index) {
		Objects.requireNonNull(collection, "Collection is not created yet, method addChildNode has not been called yet!");
		return (Node)collection.get(index);
	}
	
}
