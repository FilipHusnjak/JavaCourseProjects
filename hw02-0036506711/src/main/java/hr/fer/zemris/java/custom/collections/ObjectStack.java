package hr.fer.zemris.java.custom.collections;

/**
 * Objects of this class store data on a stack.
 * 
 * @author Filip Husnjak
 */
public class ObjectStack {

	/**
	 * Collection used to store data internally.
	 */
	private ArrayIndexedCollection collection;
	
	/**
	 * Default constructor, constructs empty stack.
	 */
	public ObjectStack() {
		collection = new ArrayIndexedCollection();
	}
	
	/**
	 * Returns {@code true} if this stack contains no elements.
	 * 
	 * @return {@code true} if there are no elements on this stack
	 */
	public boolean isEmpty() {
		return collection.isEmpty();
	}
	
	/**
	 * Returns the number of elements on this stack.
	 * 
	 * @return the number of elements on this stack
	 */
	public int size() {
		return collection.size();
	}
	
	/**
	 * Pushes the given object on this stack.
	 * 
	 * @param value
	 *        object to be pushed onto this stack
	 * @throws NullPointerException if the given object is {@code null}
	 */
	public void push(Object value) {
		collection.add(value);
	}
	
	/**
	 * Returns the element on the top of the stack and removes it.
	 * 
	 * @throws EmptyStackException if stack is empty
	 */
	public Object pop() {
		Object obj = peek();
		collection.remove(size() - 1);
		return obj;
	}
	
	/**
	 * Returns the element on the top of the stack.
	 * 
	 * @return the element on the top of the stack
	 * @throws EmptyStackException if stack is empty
	 */
	public Object peek() {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		return collection.get(size() - 1);
	}
	
	/**
     * Removes all of the elements from this stack. The stack will
     * be empty after this call returns.
     */
	public void clear() {
		collection.clear();
	}
}
