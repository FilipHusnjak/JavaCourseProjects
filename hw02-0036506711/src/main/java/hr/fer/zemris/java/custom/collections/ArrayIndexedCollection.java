package hr.fer.zemris.java.custom.collections;

/**
 * Implementation of {@code Collection} that stores elements in an array.
 * 
 * @author Filip Husnjak
 */
public class ArrayIndexedCollection extends Collection {

	/**
	 * Represents default size of an array when default constructor is called.
	 */
	private static int DEFAULT_CAPACITY;
	
	/**
	 * Represents current size of collection.
	 */
	private int size;
	
	/**
	 * An array of currently stored elements.
	 */
	private Object[] elements;
	
	public ArrayIndexedCollection(int capacity) {
		elements = new Object[capacity];
	}
	
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}
	
	
	
}
