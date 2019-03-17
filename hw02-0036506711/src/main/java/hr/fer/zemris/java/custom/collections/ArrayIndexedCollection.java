package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.Objects;

/**
 * Implementation of {@code Collection} that stores elements in an array.
 * 
 * @author Filip Husnjak
 */
public class ArrayIndexedCollection extends Collection {

	/**
	 * The default capacity of the ArrayIndexedCollection when default
	 * constructor is called.
	 */
	private static final int DEFAULT_CAPACITY = 16;
	
	/**
	 * Defines the grow rate of the capacity of an array used to store elements.
	 */
	private static final int CAPACITY_MULTIPLIER = 2;
	
	/**
	 * The current size of this collection.
	 */
	private int size;
	
	/**
	 * An array of currently stored elements.
	 */
	private Object[] elements;
	
	/**
	 * Constructs an empty {@code ArrayIndexedCollection} with an initial 
	 * capacity of 16.
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Constructs an empty {@code ArrayIndexedCollection} with the specified
	 * initial capacity.
	 * 
	 * @param initialCapacity
	 *        the initial capacity
	 * @throws IllegalArgumentException if initial capacity is less than {@code 1}
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		requireGreaterThanZero(initialCapacity);
		elements = new Object[initialCapacity];
	}
	
	/**
	 * Constructs an {@code ArrayIndexedCollection} containing the elements 
	 * of the specified collection. If the size of the given collection is less
	 * than 16, capacity is set to 16, otherwise capacity is set to the size
	 * of a given collection.
	 * 
	 * @param other
	 *        the collection whose elements are to be placed into this collection
	 * @throws NullPointerExceotion if the given collection is {@code null}
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, DEFAULT_CAPACITY);
	}
	
	/**
	 * Constructs an {@code ArrayIndexedCollection} containing the elements 
	 * of the specified collection. The capacity of newly created collection is 
	 * determined by size of a given collection and given capacity, 
	 * newCapacity = max(other.size(), initialCapacity).
	 * 
	 * @param other
	 *        the collection whose elements are to be placed into this collection
	 * @param initialCapacity
	 *        wanted capacity of the new collection
	 * @throws NullPointerExceotion if the given collection is {@code null}
	 * @throws IllegalArgumentException if expected capacity is less than {@code 1}
	 */       
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		this(returnExpectedCapacity(other, initialCapacity));
		addAll(other);
	}
	
	/**
	 * Checks if given argument is greater than 0, if not IllegalArgumentException
	 * is thrown.
	 * 
	 * @param initialCapacity
	 *        argument to be checked
	 * @return given argument
	 * @throws IllegalArgumentException if the given argument is less than 1
	 */
	private static int requireGreaterThanZero(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException("Capacity must be greater "
					+ "than 0, provided capacity = " + initialCapacity);
		}
		
		return initialCapacity;
	}
	
	/**
	 * Returns expected capacity determined by given arguments. Capacity is set
	 * to max(other.size(), initialCapacity).
	 * 
	 * @param other
	 *        the collection whose size is used in calculation
	 * @param initialCapacity
	 *        initial capacity whose value is used in calculation
	 * @return expected capacity
	 * @throws NullPointerExceotion if the given collection is {@code null}
	 */
	private static int returnExpectedCapacity(Collection other, int initialCapacity) {
		other = Objects.requireNonNull(other, "Given Collection cannot be null!");
		initialCapacity = requireGreaterThanZero(initialCapacity);
		return Math.max(other.size(), initialCapacity);
	}
	
	/**
	 * Checks if this array has enough capacity for 1 more element and if not
	 * it resizes array to new capacity.
	 * 
	 * @param newCapacity
	 *        new capacity for array if needed
	 * @return this array that is resized if it was needed
	 */
	private Object[] checkAndGrow(int newCapacity) {
		if (elements.length <= size) {
			elements = Arrays.copyOf(elements, elements.length * CAPACITY_MULTIPLIER);
		} 
		return elements;
	}
	
	/**
	 * {@inheritDoc} Capacity doubles if its exceeded when adding new element.
	 * @throws NullPointerException if the given object is {@code null}
	 */
	@Override
	public void add(Object value) {
		Objects.requireNonNull(value, "Given object cannot be null!");
		elements = checkAndGrow(CAPACITY_MULTIPLIER * elements.length);
		elements[size++] = value;
	}
	
	/**
	 * Returns the element at the specified position in this collection.
	 * 
	 * @param index
	 *        index of the element to return
	 * @return the element at the specified position in this collection
	 * @throws IndexOutOfBoundsException if the specified index is out of range
	 */
	public Object get(int index) {
		Objects.checkIndex(index, size);
		return elements[index];
	}
	
	/**
	 * {@inheritDoc} The allocated array is left at its current capacity.
	 */
	@Override
	public void clear() {
		Arrays.fill(elements, 0, size, null);
		size = 0;
	}
	
	/**
	 * Inserts the specified element at the specified position in this array.
	 * 
	 * @param value
	 *        value to be inserted
	 * @param position
	 *        index at which the specified element is to be inserted
	 * @throws NullPointerException if given object is null
	 * @throws IndexOutOfBoundsException if the specified index is out of range
	 */
	// Average complexity of this method is O(size)
	public void insert(Object value, int position) {
		Objects.requireNonNull(value, "Given object cannot be null!");
		elements = checkAndGrow(CAPACITY_MULTIPLIER * elements.length);
		Objects.checkIndex(position, size + 1);
		for (int i = size; i > position; --i) {
			elements[i] = elements[i - 1];
		}
		elements[position] = value;
	}
	
	/**
	 * Returns the position of the first occurrence of a specified element
	 * or {@code -1} if element is not found.
	 * 
	 * @param value
	 *        element whose index is to be found
	 * @return position of the first occurrence of an element in an array 
	 *         or -1 if the element is not found
	 */
	// Average complexity of this method is O(size)
	public int indexOf(Object value) {
		for (int i = 0; i < size; ++i) {
			if (Objects.equals(elements[i], value)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Removes the element at specified index from collection. Element that was
	 * previously at location {@code index + 1} after this operation is at
	 * location {@code index}.
	 * 
	 * @param index
	 * @throws IndexOutOfBoundsException if the specified index is out of range
	 */
	public void remove(int index) {
		Objects.checkIndex(index, size);
		for (int i = index; i < size - 1; ++i) {
			elements[i] = elements[i + 1];
		}
		elements[--size] = null;
	}
	
	/**
	 * {@inheritDoc} Capacity of the allocated array stays the same.
	 */
	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		if (index == -1) {
			return false;
		}
		remove(index);
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}
	
	/**
	 * @throws NullPointerException if the given processor object is {@code null}
	 */
	@Override
	public void forEach(Processor processor) {
		Objects.requireNonNull(processor, "Processor object cannot be null!");
		for (int i = 0; i < size; ++i) {
			processor.process(get(i));
		}
	}
	
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);
	}
	
}

