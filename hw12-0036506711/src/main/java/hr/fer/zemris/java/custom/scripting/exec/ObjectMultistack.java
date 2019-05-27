package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents map that can map multiple values to the same key. Values that belong
 * to the same key are stored in stack-like abstraction.
 * 
 * @author Filip Husnjak
 */
public class ObjectMultistack {

	/**
	 * Maps each key to the specified node of a linked list representing stack of
	 * elements that belong to this key.
	 */
	private Map<String, MultistackEntry> map = new HashMap<>();
	
	/**
	 * Maps the given {@code ValueWrapper} object with the specified key. If mapping
	 * for the key already exists, given value wrapper is pushed on top of the stack
	 * of elements mapped with the specified key.
	 * 
	 * @param keyName
	 *        key which value wrapper should be mapped with
	 * @param valueWrapper
	 *        value wrapped which should be mapped with the specified key
	 * @throws NullPointerException if the given keyName or valueWrapper objects are {@code null}
	 */
	public void push(String keyName, ValueWrapper valueWrapper) {
		Objects.requireNonNull(keyName, "Given keyName cannot be null!");
		Objects.requireNonNull(valueWrapper, "Given value wrapper cannot be null!");
		map.put(keyName, new MultistackEntry(valueWrapper, map.get(keyName)));
	}
	
	/**
	 * Returns and removes the value wrapper that is on top of stack of the specified
	 * key. If there are no elements on the stack proper exception is thrown.
	 * 
	 * @param keyName
	 *        key whose element should be returned
	 * @return the value wrapper that is on top of stack of the specified key
	 * @throws NullPointerException if the given keyName is {@code null}
	 * @throws EmptyStackException if the stack of the given key is empty
	 */
	public ValueWrapper pop(String keyName) {
		ValueWrapper toReturn = peek(keyName);
		// If there are no more elements on the stack the key is removed from the map
		map.computeIfPresent(keyName, (k, v) -> v.next);
		return toReturn;
	}
	
	/**
	 * Returns the value wrapper that is on top of stack of the specified key. 
	 * If there are no elements on the stack proper exception is thrown.
	 * The element stays on the stack, in other words this method does not change
	 * stack structure.
	 * 
	 * @param keyName
	 *        key whose element should be returned
	 * @return the value wrapper that is on top of stack of the specified key
	 * @throws NullPointerException if the given keyName is {@code null}
	 * @throws EmptyStackException if the stack of the given key is empty
	 */
	public ValueWrapper peek(String keyName) {
		MultistackEntry entry = map.get(Objects.requireNonNull(keyName, 
				"Given keyName cannot be null!"));
		if (entry == null) {
			throw new EmptyStackException("Stack of the specified key is empty!");
		}
		return entry.getValue();
	}
	
	/**
	 * Returns {@code true} if the stack of the specified key is empty.
	 * 
	 * @param keyName
	 *        key whose stack is to be checked
	 * @return {@code true} if the stack of the specified key is empty
	 */
	public boolean isEmpty(String keyName) {
		return map.get(keyName) == null;
	}
	
	/**
	 * Represents stack implemented as linked list. It encapsulates value
	 * and reference to the next element or {@code null} if there are no more
	 * elements after this one.
	 * 
	 * @author Filip Husnjak
	 */
	private static class MultistackEntry {
		
		/**
		 * Value of this entry, its final
		 */
		private final ValueWrapper value;
		
		/**
		 * Next element on the stack, its final
		 */
		private final MultistackEntry next;
		
		/**
		 * Constructs new {@code MultistackEntry} with the specified parameters.
		 * Null values are prohibited.
		 * 
		 * @param value
		 *        value of this stack entry
		 * @param next
		 *        next stack entry object
		 * @throws NullPointerException if the given value is {@code null}
		 */
		public MultistackEntry(ValueWrapper value, MultistackEntry next) {
			this.value = Objects.requireNonNull(value, "Value of MultistackEntry cannot be null!");
			this.next = next;
		}
		
		/**
		 * Returns value of this {@code MultistackEntry}.
		 * 
		 * @return value of this {@code MultistackEntry}
		 */
		public ValueWrapper getValue() {
			return value;
		}
	}
	
}
