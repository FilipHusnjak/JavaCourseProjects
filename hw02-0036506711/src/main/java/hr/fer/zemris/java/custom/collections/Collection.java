package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Represents a group of objects.
 * 
 * @author Filip Husnjak
 */
public class Collection {

	/**
	 * Returns {@code true} if this collection contains no elements.
	 * 
	 * @return {@code true} if there are no elements in this collection
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Returns the number of elements in this collection.
	 * 
	 * @return the number of elements in this collection
	 */
	public int size() {
		return 0;
	}
	
	/**
	 * Adds the given object into this collection.
	 * 
	 * @param value
	 *        object to be added into this collection
	 */
	public void add(Object value) {}
	
	/**
	 * Returns {@code true} if this collection contains given value, 
	 * as determined by equals method.
	 *
	 * @param value
	 *        element whose presence in this collection is to be tested
	 * @return {@code true} if this collection contains the specified element
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * Returns {@code true} if this collection contains given value, 
	 * as determined by equals method and removes the first occurrence of it.
	 * 
	 * @param value 
	 *        element to be removed from this collection, if present
	 * @return {@code true} if an element was removed as a result of this call
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Returns an array containing all of the elements in this collection.
	 * Size of an array is equal to the size of this collection.
	 * 
	 * @return array of elements in this collection
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Performs the given action for each element of the {@code Collection}
	 * until all elements have been processed or the action throws an exception.
	 * 
	 * @param processor 
	 *        the action to be performed for each element
	 */
	public void forEach(Processor processor) {}
	
	/**
	 * Adds all of the elements in the specified collection to this collection.
	 * 
	 * @param other
	 *        collection containing elements to be added into this collection
	 * @throws NullPointerException if the given Collection is {@code null}
	 */
	public void addAll(Collection other) {
		Objects.requireNonNull(other);
		
		/**
		 * Represents an operation that adds each given object to the 
		 * collection upon which {@code addAll(Collection)} method is called.
		 * 
		 * @author Filip Husnjak
		 */
		class AddToCollectionProcessor extends Processor {
			
			/**
			 * Adds given object to the collection upon which 
			 * {@code addAll(Collection)} method is called.
			 */
			@Override
			public void process(Object value) {
				Collection.this.add(value);
			}
			
		}
		other.forEach(new AddToCollectionProcessor());
	}
	
	/**
     * Removes all of the elements from this collection. The collection will
     * be empty after this call returns.
     */
	public void clear() {}
	
}
