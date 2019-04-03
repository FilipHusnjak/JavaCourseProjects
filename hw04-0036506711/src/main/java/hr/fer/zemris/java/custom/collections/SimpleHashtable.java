package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * <p>
 * Represents hash table of key-value pairs. Each key is unique and can only have
 * one value mapped. Duplicate as well as {@code null} values are allowed, but storage
 * of {@code null} keys is prohibited. 
 * </p>
 * <p>
 * Position in the array is determined by
 * the method {@code hashCode} and capacity of the table. Each position in the table
 * represents the head of the linked list of the elements that ended up at that
 * index.
 * </p>
 * 
 * @author Filip Husnjak
 *
 * @param <K>
 *        type of the keys in this hash table
 * @param <V>
 *        type of the values in this hash table
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
	
	/**
	 * Default table capacity used if no capacity was provided.
	 */
	private static final int DEFAULT_TABLE_CAPACITY = 16;
	
	/**
	 * Array of table slots.
	 */
	private TableEntry<K, V>[] table; 
	
	/**
	 * Capacity of the table.
	 */
	private int capacity;
	
	/**
	 * Number or key-value pairs currently stored in {@code this} hash table.
	 */
	private int size;
	
	/**
	 * The number of times this {@code SimpleHashtable} has been structurally modified.
     * Structural modifications are those that change the size of the map.
	 */
	private long modificationCount;
	
	/**
	 * Constructs an object with table capacity set to {@link #DEFAULT_TABLE_CAPACITY}
	 */
	public SimpleHashtable() {
		this(DEFAULT_TABLE_CAPACITY);
	}
	
	/**
	 * Constructs an object with table capacity set to first number greater or equal
	 * to the specified capacity that is power of {@code 2}
	 * 
	 * @param initialCapacity
	 *        the specified capacity used to determine capacity of the table
	 * @throws IllegalArgumentException if the specified capacity is less than 1
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException("Initial capacity cannot be less than one!");
		}
		capacity = 1;
		while (capacity < initialCapacity) {
			capacity *= 2;
		}
		table = (TableEntry<K, V>[]) new TableEntry[capacity];
	}

	/**
	 * Returns the hash code of the given object modulo size of the table. It will
	 * never return indices less than 0 or greater than size of the table. If the
	 * given object is {@code null} this method will return 0.
	 * 
	 * @param of
	 *        object whose hash code it to be calculated
	 * @return the hash code of the given object modulo size of the table
	 */
	private int hash(Object of) {
		return Math.abs(Objects.hashCode(of) % capacity);
	}
	
	/**
	 * Resizes table capacity multiplying it by 2. Elements must either stay at
	 * the same index or move by an offset = oldCapacity. The method reuses already 
	 * created objects and does not create redundant instances. Capacity of the table 
	 * is doubled after this method returns.
	 */
	@SuppressWarnings("unchecked")
	private void resizeTable() {
		TableEntry<K, V>[] oldTable = table;
		int oldCapacity = capacity;
		table = (TableEntry<K, V>[]) new TableEntry[capacity *= 2];
		for (int i = 0; i < oldCapacity; ++i) {
			TableEntry<K, V> entry = oldTable[i];
			// List of elements that will end up at the same index
			TableEntry<K, V> lowerIndexHead = null, lowerIndexTail = null;
			// List of elements that will end up at the index = oldIndex + oldCapacity
			TableEntry<K, V> higherIndexHead = null, higherIndexTail = null;
			for (; entry != null; entry = entry.next) {
				// Checks whether this entry will end up at the same index after resize
				if ((hash(entry.getKey()) & oldCapacity) == 0) { 
					if (lowerIndexHead == null) {
						lowerIndexHead = entry;
					} else {
						lowerIndexTail.next = entry;
					}
					lowerIndexTail = entry;
				} else {
					if (higherIndexHead == null) {
						higherIndexHead = entry;
					} else {
						higherIndexTail.next = entry;
					}
					higherIndexTail = entry;
				}
			}
			if (lowerIndexTail != null) {
				lowerIndexTail.next = null;
			}
			if (higherIndexTail != null) {
				higherIndexTail.next = null;
			}
			table[i] = lowerIndexHead;
			table[i + oldCapacity] = higherIndexHead;
		}
	}
	
	/**
	 * Returns entry at specified {@code index} in the table with specified {@code key}. 
	 * If the element was not found this method returns {@code null}.
	 * 
	 * @param index
	 *        index in the table of an element to be found
	 * @param key
	 *        key of an element to be found
	 * @return entry at specified {@code index} in the table with specified {@code key}
	 * @throws NullpointerExcepton if the given key is {@code null}
	 * @throws IndexOutOfBoundsException if the given index is <= 0 or >= table.length
	 */
	private TableEntry<K, V> getEntry(int index, Object key) {
		Objects.requireNonNull(key, "Keys cannot be null!");
		Objects.checkIndex(index, table.length);
		for (TableEntry<K, V> entry = table[index]; entry != null; entry = entry.next) {
			if (entry.getKey().equals(key)) {
				return entry;
			}
		}
		return null;
	}
	
	/**
	 * Associates given key with the given value in this {@code SimpleHashTable} object. 
	 * {@code Null} values are allowed while the method throws {@code NullPointerException}
	 * if the given key is {@code null}. If the table previously contained the given
	 * key, old value mapped to that key is replaced with the given one.
	 * 
	 * @param key
	 *        key to which the value is to be mapped
	 * @param value
	 *        value to be mapped to the given key
	 * @throws NullPointerException if the given key is {@code null}
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key, "Given key cannot be null!");
		int index = hash(key);
		TableEntry<K, V> entry = table[index];
		for (; entry != null && !entry.getKey().equals(key) && entry.next != null; entry = entry.next);
		if (entry == null) {
			table[index] = new TableEntry<>(key, value);
			size++; modificationCount++;
		} else if (entry.getKey().equals(key)) {
			entry.setValue(value);
		} else {
			entry.next = new TableEntry<>(key, value);
			size++; modificationCount++;
		}
		if (3.0 / 4 * capacity <= size) resizeTable();
	}
	
	/**
	 * Returns the value mapped to the given key or {@code null} if the specified key 
	 * was not found. Since values can also be {@code null} it is not safe to rely
	 * on this method to check whether the map contains the given key. In that case 
	 * use the proper method {@link #containsKey(Object)}.
	 * 
	 * @param key
	 *        key whose mapped value is to be returned
	 * @return the value mapped to the given key or null if the specified key 
	 *         was not found
	 */
	public V get(Object key) {
		TableEntry<K, V> entry;
		return key == null || (entry = getEntry(hash(key), key)) == null ? null : entry.getValue();
	}
	
	/**
	 * Returns the number of key-value pairs stored in this {@code SimpleHashTable}.
	 * 
	 * @return the number of key-value pairs stored in this {@code SimpleHashTable}
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Returns {@code true} if this {@code SimpleHashTable} contains the specified key.
	 * 
	 * @param key
	 *        key to be checked
	 * @return {@code true} if this {@code SimpleHashTable} contains the specified key
	 */
	public boolean containsKey(Object key) {
		return key != null && getEntry(hash(key), key) != null;
	}
	
	/**
	 * Returns {@code true} if this {@code SimpleHashTable} contains the specified value.
	 * 
	 * @param value
	 *        value to be checked
	 * @return {@code true} if this {@code SimpleHashTable} contains the specified value
	 */
	public boolean containsValue(Object value) {
		if (size == 0) return false;
		for (TableEntry<K, V> entry: table) {
			for (; entry != null; entry = entry.next) {
				if (value == entry.getValue() || (value != null && value.equals(entry.getValue()))) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Removes the key-value pair with specified key from this {@code SimpleHashTable}. 
	 * The capacity of the table stays the same. If the specified key was not found
	 * nothing happens.
	 * 
	 * @param key
	 *        key to be removed from the table
	 */
	public void remove(Object key) {
		if (key == null) return;
		int index = hash(key);
		if (table[index] == null) return;
		if (table[index].getKey().equals(key)) {
			table[index] = table[index].next;
			size--; modificationCount++;
			return;
		}
		TableEntry<K, V> entry = table[index];
		for (; entry.next != null && !entry.next.getKey().equals(key); entry = entry.next);
		if (entry.next != null) {
			entry.next = entry.next.next;
			size--; modificationCount++;
		}
	}
	
	/**
	 * Returns {@code true} if there are no key-value pairs store in this {@code SimpleHashTable}.
	 * 
	 * @return {@code true} if there are no key-value pairs store in this {@code SimpleHashTable}
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Removes all key-value pairs stored in this {@code SimpleHashTable}. The
	 * table will be empty after this call returns. The table is left at its current
	 * capacity.
	 */
	public void clear() {
		Arrays.fill(table, null);
		size = 0; modificationCount++;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append("[");
		for (TableEntry<K, V> entry: this) {
			if (entry != null) {
				sb.append(entry.toString()).append(", ");
			}
		}
		return sb.append("]").toString().replaceAll(", ]", "]");
	}
	
	/**
	 * Returns an iterator over the elements in this {@code SimpleHashTable}.
	 * 
	 * @return an iterator over the elements in this {@code SimpleHashTable}
	 */
	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}
	
	/**
	 * 
	 * 
	 * @author Filip Husnjak
	 */
	private class IteratorImpl implements Iterator<TableEntry<K, V>> {

		/**
		 * Next table entry to return.
		 */
		private TableEntry<K, V> nextEntry;
		
		/**
		 * Last returned entry, used for remove method.
		 */
		private TableEntry<K, V> currentEntry;
		
		/**
		 * Table index of the next element to return.
		 */
		private int nextIndex;
		
		/**
		 * Saved modification count used to determine whether the {@code SimpleHashTable} was
		 * modified after this iterator was created.
		 */
		private long savedModificationCount;
		
		/**
		 * Constructs new object and saves the current modification count.
		 */
		public IteratorImpl() {
			savedModificationCount = modificationCount;
			progressToNextIndex();
		}
		
		/**
		 * {@inheritDoc} 
		 * @throws ConcurrentModificationException if the {@code SimpleHashTable} was
		 *         modified after this iterator was created
		 */
		@Override
		public boolean hasNext() {
			checkForModification();
			return nextEntry != null;
		}

		/**
		 * {@inheritDoc} 
		 * @throws NoSuchElementException if there are no more elements in this iteration
		 * @throws ConcurrentModificationException if the {@code SimpleHashTable} was
		 *         modified after this iterator was created
		 */
		@Override
		public TableEntry<K, V> next() {
			if (!hasNext()) {
				throw new NoSuchElementException("There are no more elements left in this iteration!");
			}
			currentEntry = nextEntry;
			if ((nextEntry = nextEntry.next) == null) {
				nextIndex++;
				progressToNextIndex();
			}
			return currentEntry;
		}
		
		/**
		 * Increments {@link #nextIndex} until it points to a valid element or it
		 * reaches the end of the table. {@link #nextEntry} points to the valid
		 * element or {@code null} if there are no elements to return, after this
		 * call returns.
		 */
		private void progressToNextIndex() {
			for (; nextIndex < table.length && (nextEntry = table[nextIndex]) == null; nextIndex++);
		}
		
		/**
		 * Check whether the {@code SimpleHashTable} was modified after this iterator
		 * was created and throws proper exception if thats the case.
		 * @throws ConcurrentModificationException if the {@code SimpleHashTable} was
		 *         modified after this iterator was created
		 */
		private void checkForModification() {
			if (savedModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Hashtable was modified after this iterator was crated!");
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @throws IllegalStateException if this method was called more than 1 time in a row
		 * @throws ConcurrentModificationException if the {@code SimpleHashTable} was
		 *         modified after this iterator was created
		 */
		@Override
		public void remove() {
			checkForModification();
			if (currentEntry == null) {
				throw new IllegalStateException("Method remove was already called "
						+ "or method next has not been called yet!");
			}
			savedModificationCount++;
			SimpleHashtable.this.remove(currentEntry.getKey());
			currentEntry = null;
		}
		
	}
	
	
	/**
	 * Represents each element of the table. It points to the next element at the
	 * same position in the table or {@code null} if this is the last element
	 * at its position.
	 * 
	 * @author Filip Husnjak
	 *
	 * @param <K>
	 *        type of the key
	 * @param <V>
	 *        type of the value
	 */
	public static class TableEntry<K, V> {
		
		/**
		 * Key of this {@code TableEntry}, its immutable.
		 */
		private final K key;
		
		/**
		 * Value of this {@code TableEntry}, it can be changed using the {@link #setValue(Object)}method.
		 */
		private V value;
		
		/**
		 * Next Table entry at the same position in the table as this one.
		 */
		TableEntry<K, V> next;

		/**
		 * Constructs an object with specified key and value.
		 * 
		 * @param key
		 *        key of this {@code TableEntry}
		 * @param value
		 *        value of this {@code TableEntry}
		 * @throws NullPointerException if the given key is null
		 */
		public TableEntry(K key, V value) {
			super();
			Objects.requireNonNull(key, "Keys cannot be null!");
			this.key = key;
			this.value = value;
		}

		/**
		 * Returns the value of this {@code TableEntry}. The return reference
		 * can be {@code null}.
		 * 
		 * @return the value of this table {@code TableEntry}
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Sets the value of this {@code TableEntry} to the specified one.
		 * {@code null} references are allowed.
		 * 
		 * @param value
		 *        new value of this {@code TableEntry}
		 */
		public void setValue(V value) {
			this.value = value;
		}

		/**
		 * Returns the key of this {@code TableEntry}.
		 * 
		 * @return the key of this {@code TableEntry}
		 */
		public K getKey() {
			return key;
		}
		
		@Override
		public String toString() {
			return key + "=" + value;
		}
		
	}
	
}
