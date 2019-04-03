package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Object that maps keys to values. Dictionary cannot contain duplicate keys 
 * and each key can map to at most 1 value. {@code Null} keys are not allowed 
 * while {@code null} values are.
 * 
 * @author Filip Husnjak
 *
 * @param <K>
 *        The type of keys in this dictionary.
 * @param <V>
 *        The type of values in this dictionary
 */ 
public class Dictionary<K, V> {
	
	/**
	 * Collection used to store key-value pairs modeled by a Pair object.
	 */
	private final ArrayIndexedCollection<Pair> collection = new ArrayIndexedCollection<>();
	
	/**
	 * Returns {@code true} if {@code this} Dictionary is empty.
	 * 
	 * @return {@code true} if {@code this} Dictionary is empty
	 */
	public boolean isEmpty() {
		return collection.isEmpty();
	}
	
	/**
	 * Returns the size of {@code this Dictionary}.
	 * 
	 * @return the size of {@code this Dictionary}
	 */
	public int size() {
		return collection.size();
	}
	
	/**
	 * Removes all key-value pairs from {@code this Dictionary}. The Dictionary
	 * will be empty after this method returns.
	 */
	public void clear() {
		collection.clear();
	}
	
	/**
	 * Associates the specified value with the specified key in this Dictionary.
	 * If the dictionary contains specified key the old value is replaced with the
	 * specified value, and if not it adds new key-value pair into this Dictionary.
	 * 
	 * @param key
	 *        key of the new element
	 * @param value
	 *        value of the new element
	 * @throws NullPointerException if the given key is {@code null}
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key, "Keys cannot be null!");
		for (int i = 0; i < size(); ++i) {
			if (collection.get(i).getKey().equals(key)) {
				collection.get(i).setValue(value);
				return;
			}
		}
		collection.add(new Pair(key, value));
	}
	
	/**
	 * Returns the value to which the specified key is mapped,
     * or {@code null} if this dictionary contains no mapping for the key.
	 * 
	 * @param key
	 *        the key whose mapped value is to be returned
	 * @return the value mapped to the specified key
	 */
	public V get(Object key) {
		for (int i = 0; i < size(); ++i) {
			if (collection.get(i).getKey().equals(key)) {
				return collection.get(i).getValue();
			}
		}
		return null;
	}
	
	/**
	 * Key-value holder stored in internal list.
	 * 
	 * @author Filip Husnjak
	 */
	private class Pair {
		
		/**
		 * Key of this pair.
		 */
		private K key;
		
		/**
		 * Value of this pair.
		 */
		private V value;
		
		/**
		 * Constructs pair object with given {@code non-null} key and value.
		 * 
		 * @param key
		 *        key of {@code this} Pair
		 * @param value
		 *        value of {@code this} Pair
		 * @throws NullPointerException if the given key is null
		 */
		public Pair(K key, V value) {
			super();
			Objects.requireNonNull(key, "Keys cannot be null!");
			this.key = key;
			this.value = value;
		}

		/**
		 * Assigns value of this {@code Pair} to a given one. 
		 * 
		 * @param value
		 *        value to assign this pair to
		 */
		public void setValue(V value) {
			this.value = value;
		}

		/**
		 * Returns the key of this {@code Pair}.
		 * 
		 * @return the key of this {@code Pair}
		 */
		public K getKey() {
			return key;
		}

		/**
		 * Returns the value of this {@code Pair}.
		 * 
		 * @return the value of this {@code Pair}
		 */
		public V getValue() {
			return value;
		}
		
	}
	
}
