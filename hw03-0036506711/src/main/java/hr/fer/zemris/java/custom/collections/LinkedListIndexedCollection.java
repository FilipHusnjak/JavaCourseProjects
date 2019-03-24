package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implementation of {@code List} that stores elements in a doubly linked list.
 * It can store duplicates but storage of null references is not allowed.
 * 
 * @author Filip Husnjak
 */
public class LinkedListIndexedCollection implements List {
	
	/**
	 * Represents node in the linked list.
	 * 
	 * @author Filip Husnjak
	 */
	private static class ListNode {
		
		/**
		 * next ListNode in linked list
		 */
		ListNode next;
		
		/**
		 * previous ListNode in linked list
		 */
		ListNode prev;
		
		/**
		 * value of this ListNode
		 */
		private Object value;
		
		/**
		 * Assigns value of this ListNode to the specified value when
		 * creating this object.
		 * 
		 * @param value
		 *        specified value of this object
		 */
		public ListNode(Object value) {
			this.value = value;
		}
		
		public Object getValue() {
			return value;
		}
	}
	
	/**
	 * The current size of this collection.
	 */
	private int size;
	
	/**
	 * Reference to the first node in this linked list.
	 */
	private ListNode first;
	
	/**
	 * Reference to the last node in this linked list.
	 */
	private ListNode last;
	
	/**
	 * The number of times this {@code Collection} has been structurally modified.
     * Structural modifications are those that change the size of the list.
	 */
	private long modificationCount = 0;

	/**
	 * Constructs an empty {@code LinkedListIndexedCollection}.
	 */
	public LinkedListIndexedCollection() {
		first = last = null;
	}
	
	/**
	 * Constructs an {@code LinkedListIndexedCollection} containing the elements 
	 * of the specified collection.
	 * 
	 * @param other
	 *        the collection whose elements are to be placed into this collection
	 * @throws NullPointerException if the given collection is {@code null}
	 */
	public LinkedListIndexedCollection(Collection other) {
		Objects.requireNonNull(other, "Given collection cannot be null!");
		addAll(other);
	}
	
	/**
	 * Returns node at specified index.
	 * <br>
	 * Average complexity of this method is {@code O(currentSize / 2 + 1)}.
	 * 
	 * @param index
	 *        index of the node to return
	 * @return node at specified index
	 * @throws IndexOutOfBoundsException if index is out of range of {@code this Collection}
	 */
	private ListNode getNode(int index) {
		Objects.checkIndex(index, size);
		// If index is less than {@code currentSize / 2} start from the beginning, otherwise start from the end.
		if (index < (size >> 1)) {
            ListNode node = first;
            for (int i = 0; i < index; i++)
                node = node.next;
            return node;
        } else {
            ListNode node = last;
            for (int i = size - 1; i > index; i--)
                node = node.prev;
            return node;
        }
	}
	
	/**
	 * Removes and unlinks specified node from this list. If given node is 
	 * {@code null} method does nothing.
	 * 
	 * @param node
	 * 		  node to be removed from this list
	 */
	private void removeNode(ListNode node) {
		if (node == null) return;
		
		ListNode prev = node.prev;
		ListNode next = node.next;
		if (prev == null) {
			first = next;
		} else {
			prev.next = next;
		}
		if (next == null) {
			last = prev;
		} else {
			next.prev = prev;
		}
		node.next = null;
		node.prev = null;
		
		size--;
		modificationCount++;
	}
	
	/**
	 * Links node with specified {@code value} before given {@code node}. If {@code null} is given 
	 * as node argument new node will be added to the end of the list.
	 * 
	 * @param node
	 *        node before which new node needs to be inserted
	 * @param value
	 *        value of a new node
	 * @throws NullPointerException if given {@code value} is {@code null}
	 */
	private void addBefore(ListNode node, Object value) {
		Objects.requireNonNull(value, "Given object cannot be null!");
		ListNode newNode = new ListNode(value);
		newNode.next = node;
		if (node == null) {
			if (last == null) {
				first = last = newNode;
			} else {
				last.next = newNode;
				newNode.prev = last;
				last = newNode;
			}
		} else {
			if (node.prev == null) {
				first = newNode;
			} else {
				node.prev.next = newNode;
				newNode.prev = node.prev;
			}
			node.prev = newNode;
		}
		size++;
		modificationCount++;
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
	 * AverageComplexity of this method is 
	 * <br>{@code O(1)}.
	 * @throws NullPointerException if the given value is null
	 */
	@Override
	public void add(Object value) {
		addBefore(null, value);
	}
	
	/**
	 * {@inheritDoc}
	 * AverageComplexity of this method is 
	 * <br>{@code O(size/2+1)}.
	 */
	@Override
	public Object get(int index) {
		return getNode(index).getValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		first = last = null;
		size = 0;
		modificationCount++;
	}
	
	/**
	 * {@inheritDoc}
	 * AverageComplexity of this method is 
	 * <br>{@code O(size/2+1)}.
	 */
	@Override
	public void insert(Object value, int position) {
		if (position == size) {
			addBefore(null, value);
		}
		else {
			addBefore(getNode(position), value);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * AverageComplexity of this method is 
	 * <br>{@code O(size)}.
	 */
	@Override
	public int indexOf(Object value) {
		int index = 0;
		for (ListNode node = first; node != null; node = node.next, index++) {
			if (node.getValue().equals(value)) {
				return index;
			}
		}
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(int index) {
		removeNode(getNode(index));
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
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new ArrayIndexedCollection(this).toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ElementsGetter createElementsGetter() {
		return new LinkedListElementsGetter(this);
	}
	
	/**
	 * Specified representation of a {@code ElementsGetter} interface for this
	 * {@code Collection}. It uses {@code LinkedListIndexedCollection} for internal
	 * data storage.
	 * 
	 * @author Filip Husnjak
	 */
	private static class LinkedListElementsGetter implements ElementsGetter {

		/**
		 * Represents {@code Collection} for internal data storage.
		 */
		private final LinkedListIndexedCollection collection;
		
		/**
		 * Current position in {@link #collection}, method {@code next} returns element
		 * at this position if the index is valid.
		 */
		private int nextIndex = 0;
		
		/**
		 * Saved {@code modificationCount} of a given {@code Collection}, its immutable and initialized
		 * upon object creation.
		 */
		private final long savedModification;
		
		/**
		 * Constructs an object with specified {@code Collection}. This instance of
		 * {@code LinkedListElementsGetter} will iterate over the specified {@code Collection}.
		 * 
		 * @param collection
		 *        collection you want to iterate over
		 * @throws NullPointerException if the given {@code Collection} is {@code null}
		 */
		public LinkedListElementsGetter(LinkedListIndexedCollection collection) {
			Objects.requireNonNull(collection, "Given collection cannot be null!");
			savedModification = collection.modificationCount;
			this.collection = collection;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNextElement() {
			checkForModification();
			return nextIndex < collection.size();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object getNextElement() {
			if (!hasNextElement()) {
				throw new NoSuchElementException("This iteration contains no more elements!");
			}
			return collection.get(nextIndex++);
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
			if (savedModification != collection.modificationCount) {
				throw new ConcurrentModificationException("The collection was modified after this iterator was created!");
			}
		}
		
	}
	
}





























