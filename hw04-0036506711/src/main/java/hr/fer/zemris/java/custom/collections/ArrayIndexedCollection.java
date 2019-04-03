package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implementation of {@code List} that stores elements in an array.
 * It can store duplicates but storage of null references is not allowed.
 * 
 * @author Filip Husnjak
 */
public class ArrayIndexedCollection<T> implements List<T> {

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
	private T[] elements;
	
	/**
	 * The number of times this {@code Collection} has been structurally modified.
     * Structural modifications are those that change the size of the list.
	 */
	private long modificationCount = 0;
	
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
	@SuppressWarnings("unchecked")
	public ArrayIndexedCollection(int initialCapacity) {
		requireGreaterThanZero(initialCapacity);
		elements = (T[]) new Object[initialCapacity];
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
	public ArrayIndexedCollection(Collection<T> other) {
		this(other, DEFAULT_CAPACITY);
	}
	
	/**
	 * Constructs an {@code ArrayIndexedCollection} containing the elements 
	 * of the specified collection. The capacity of newly created collection is 
	 * determined by size of a given collection and given capacity,
	 * <br>
	 * {@code newCapacity = max(other.size(), initialCapacity)}.
	 * 
	 * @param other
	 *        the collection whose elements are to be placed into this collection
	 * @param initialCapacity
	 *        wanted capacity of the new collection
	 * @throws NullPointerExceotion if the given collection is {@code null}
	 * @throws IllegalArgumentException if {@code initialCapacity} is less than {@code 1}
	 */       
	public ArrayIndexedCollection(Collection<T> other, int initialCapacity) {
		this(returnExpectedCapacity(other, requireGreaterThanZero(initialCapacity)));
		addAll(other);
	}
	
	/**
	 * Checks if given argument is greater than 0 and returns it. If not, IllegalArgumentException
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
	private static int returnExpectedCapacity(Collection<?> other, int initialCapacity) {
		other = Objects.requireNonNull(other, "Given Collection cannot be null!");
		return Math.max(other.size(), initialCapacity);
	}
	
	/**
	 * Checks if this array has enough capacity for 1 more element and if not
	 * it resizes array to the new capacity which is equal to {@link #CAPACITY_MULTIPLIER} * {@code oldCapacity}.
	 * 
	 * @return {@code this} array that is resized if it was needed
	 */
	private T[] checkAndGrow() {
		if (elements.length <= size) {
			elements = Arrays.copyOf(elements, elements.length * CAPACITY_MULTIPLIER);
		} 
		return elements;
	}
	
	/**
	 * {@inheritDoc} Capacity of an array that stores data doubles if its capacity is exceeded 
	 * when adding new element.
	 * @throws NullPointerException if the given object is {@code null}
	 */
	@Override
	public void add(T value) {
		Objects.requireNonNull(value, "Given object cannot be null!");
		elements = checkAndGrow();
		elements[size++] = value;
		modificationCount++;
	}
	
	/**
	 * {@inheritDoc}
	 * AverageComplexity of this method is 
	 * <br>{@code O(1)}.
	 */
	@Override
	public T get(int index) {
		Objects.checkIndex(index, size);
		return elements[index];
	}
	
	/**
	 * {@inheritDoc} 
	 * The allocated array is left at its current capacity.
	 */
	@Override
	public void clear() {
		Arrays.fill(elements, 0, size, null);
		size = 0;
		modificationCount++;
	}
	
	/**
	 * {@inheritDoc}
	 * AverageComplexity of this method is 
	 * <br>{@code O(size)}.
	 */
	@Override
	public void insert(T value, int position) {
		Objects.requireNonNull(value, "Given object cannot be null!");
		Objects.checkIndex(position, size + 1);
		elements = checkAndGrow();
		for (int i = size; i > position; --i) {
			elements[i] = elements[i - 1];
		}
		size++;
		modificationCount++;
		elements[position] = value;
	}
	
	/**
	 * {@inheritDoc}
	 * AverageComplexity of this method is {@code O(size)}.
	 */
	@Override
	public int indexOf(Object value) {
		for (int i = 0; i < size; ++i) {
			if (Objects.equals(elements[i], value)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 * The allocated array is left at its current capacity.
	 */
	public void remove(int index) {
		Objects.checkIndex(index, size);
		for (int i = index; i < size - 1; ++i) {
			elements[i] = elements[i + 1];
		}
		elements[--size] = null;
		modificationCount++;
	}
	
	/**
	 * Returns {@code true} if this collection contains given value, as determined by equals
	 * method and removes first occurrence of it. 
	 * The allocated array is left at its current capacity.
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
	 * {@inheritDoc}
	 */
	@Override
	public T[] toArray() {
		return Arrays.copyOf(elements, size);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ElementsGetter<T> createElementsGetter() {
		return new ArrayElementsGetter();
	}
	
	/**
	 * Specified representation of a {@code ElementsGetter} interface for this
	 * {@code Collection}. It uses {@code ArrayIndexedCollection} for internal
	 * data storage.
	 * 
	 * @author Filip Husnjak
	 */
	private class ArrayElementsGetter implements ElementsGetter<T> {
		
		/**
		 * Current position in {@link #collection}, method {@code next} returns element
		 * at this position if the index is valid.
		 */
		private int nextIndex = 0;
		
		/**
		 * Saved {@code modificationCount} of a given {@code Collection}, its immutable and initialized
		 * upon object creation.
		 */
		private final long savedModification = modificationCount;
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNextElement() {
			checkForModification();
			return nextIndex < size();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public T getNextElement() {
			if (!hasNextElement()) {
				throw new NoSuchElementException("This iteration contains no more elements!");
			}
			return elements[nextIndex++];
		}
		
		/**
		 * Checks whether the {@code Collection} has been modified by comparing {@code savedModification} 
		 * and {@code collection.modificationCount}. If they are not equal method
		 * throws {@code ConcurrentModificationException}.
		 * 
		 * @throws ConcurrentModificationException if the {@code savedModification} and 
		 *         {@code collection.modificationCount} are not equal
		 */
		private void checkForModification() {
			if (savedModification != modificationCount) {
				throw new ConcurrentModificationException("The collection was modified after this iterator was created!");
			}
		}
		
	}
	
}

