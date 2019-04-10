package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Represents a group of objects.
 * 
 * @author Filip Husnjak
 */
public interface Collection<T> {

	/**
	 * Returns {@code true} if this collection contains no elements.
	 * 
	 * @return {@code true} if there are no elements in this collection
	 */
	default boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Returns the number of elements in this collection.
	 * 
	 * @return the number of elements in this collection
	 */
	int size();
	
	/**
	 * Adds the given object into this collection.
	 * 
	 * @param value
	 *        object to be added into this collection
	 */
	void add(T value);
	
	/**
	 * Returns {@code true} if this collection contains given value, 
	 * as determined by equals method.
	 *
	 * @param value
	 *        element whose presence in this collection is to be tested
	 * @return {@code true} if this collection contains the specified element
	 */
	boolean contains(Object value);
	
	/**
	 * Returns {@code true} if this collection contains given value, 
	 * as determined by equals method and removes one occurrence of it 
	 * (not specified which one).
	 * 
	 * @param value 
	 *        element to be removed from this collection, if present
	 * @return {@code true} if an element was removed as a result of this call
	 */
	boolean remove(Object value);
	
	/**
	 * Returns an array containing all of the elements in this collection.
	 * Size of an array is equal to the size of this collection.
	 * 
	 * @return array of elements in this collection
	 */
	Object[] toArray();
	
	/**
	 * Performs the given action for each element of the {@code Collection}
	 * until all elements have been processed or the action throws an exception.
	 * 
	 * @param processor 
	 *        the action to be performed for each element
	 * @throws NullPointerException if the given processor object is {@code null}
	 */
	default void forEach(Processor<? super T> processor) {
		Objects.requireNonNull(processor, "Processor object cannot be null!");
		createElementsGetter().processRemaining(processor);
	}
	
	/**
	 * Adds all of the elements in the specified collection to this collection.
	 * 
	 * @param other
	 *        collection containing elements to be added into this collection
	 * @throws NullPointerException if the given Collection is {@code null}
	 */
	default void addAll(Collection<? extends T> other) {
		Objects.requireNonNull(other, "Given collection cannot be null!");
		other.forEach(this::add);
	}
	
	/**
     * Removes all of the elements from this collection. The collection will
     * be empty after this call returns.
     */
	void clear();
	
	/**
	 * Returns a non {@code null} reference to a specified ElementsGetter which
	 * represents an iterator over the {@code Collection} upon which this method is called.
	 * 
	 * @return ElementsGetter that represents an iterator over {@code this} Collection
	 */
	ElementsGetter<T> createElementsGetter();
	
	/**
	 * Adds all elements, from the given collection, that satisfy the condition specified by {@code Tester} object.
	 * <br>
	 * In other words if condition {@code tester.test(element)} is {@code true} 
	 * it adds that element into {@code this Collection}.
	 * 
	 * @param col
	 *        {@code Collection} whose elements are tested and added into {@code this} collection
	 *        accordingly
	 * @param tester
	 *        functional interface that is used to test elements of the given {@code Collection}
	 * @throws NullPointerException if given collection or tester is {@code null}
	 */
	default void addAllSatisfying(Collection<? extends T> col, Tester<? super T> tester) {
		Objects.requireNonNull(col, "Given collection cannot be null!");
		Objects.requireNonNull(tester, "Given tester cannot be null!");
		col.createElementsGetter().processRemaining(o -> {
			if (tester.test(o)) {
				add(o);
			}
		});
	}
	
}
