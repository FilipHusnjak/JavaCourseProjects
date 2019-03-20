package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Implementation of {@code Collection} that stores elements in a doubly linked list.
 * 
 * @author Filip Husnjak
 */
public class LinkedListIndexedCollection extends Collection {
	
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
	 * @throws NullPointerException if the given value is null
	 */
	@Override
	public void add(Object value) {
		addBefore(null, value);
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
		return getNode(index).getValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		first = last = null;
		size = 0;
	}
	
	/**
	 * Inserts the specified element at the specified position in this collection.
	 * <br>
	 * Average complexity of this method is O(currentSize / 2 + 1)
	 * 
	 * @param value
	 *        value to be inserted
	 * @param position
	 *        index at which the specified element is to be inserted
	 * @throws NullPointerException if given object is null
	 * @throws IndexOutOfBoundsException if the specified index is out of range
	 */
	public void insert(Object value, int position) {
		if (position == size) {
			addBefore(null, value);
		}
		else {
			addBefore(getNode(position), value);
		}
	}
	
	/**
	 * Returns the position of the first occurrence of a specified element
	 * or {@code -1} if element is not found. 
	 * Average complexity of this method is O(size)
	 * 
	 * @param value
	 *        element whose index is to be found
	 * @return position of the first occurrence of an element in an array 
	 *         or -1 if the element is not found
	 */
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
	 * Removes element at specified index from collection. Element that was
	 * previously at location {@code index + 1} after this operation is at
	 * location {@code index}.
	 * 
	 * @param index
	 * @throws IndexOutOfBoundsException if the specified index is out of range
	 */
	public void remove(int index) {
		removeNode(getNode(index));
	}
	
	/**
	 * {@inheritDoc}
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
	public void forEach(Processor processor) {
		Objects.requireNonNull(processor, "Processor object cannot be null!");
		for (int i = 0; i < size; ++i) {
			processor.process(get(i));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new ArrayIndexedCollection(this).toArray();
	}
}





























