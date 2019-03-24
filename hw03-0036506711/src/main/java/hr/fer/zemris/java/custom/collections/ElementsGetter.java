package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Represents an iterator over a {@code Collection}.
 * 
 * @author Filip Husnjak
 */
public interface ElementsGetter {
	
	/**
	 * Returns {@code true} if iteration has more elements to return. 
	 * In other words returns {@code true} if {@code getNextElement} method would
	 * return an element rather than throwing an exception. 
	 * 
	 * @return {@code true} if iteration has more elements to return
	 * @throws ConcurrentModificationException if the collection was modified
     *         after {@code this} iterator was created
	 */
	boolean hasNextElement();
	
	/**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     * @throws ConcurrentModificationException if the collection was modified
     *         after {@code this} iterator was created
     */
	Object getNextElement();
	
	/**
	 * Performs an operation, specified with given {@code Processor} object,
	 * on each element left in this iteration. In other words it executes
	 * {@code processor.process(this.getNextElement())} while the iteration contains
	 * next element.
	 * 
	 * @param processor
	 *        {@code Processor} object which determines an operation to be performed
	 *        on each remaining element
	 * @throws NullPointerException if the given {@code Processor} is {@code null}
	 */
	default void processRemaining(Processor processor) {
		Objects.requireNonNull(processor, "Given processor cannot be null!");
		while (hasNextElement()) {
			processor.process(getNextElement());
		}
	}
}
