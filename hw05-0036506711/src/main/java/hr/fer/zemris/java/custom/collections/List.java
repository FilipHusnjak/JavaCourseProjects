package hr.fer.zemris.java.custom.collections;

/**
 * An ordered {@code Collection}. The user of this
 * interface has precise control over where in the list each element is
 * inserted.
 * 
 * @author Filip Husnjak
 */
public interface List<T> extends Collection<T> {
	
	/**
	 * Returns the element at the specified position in this list.
	 * 
	 * @param index
	 *        index of the element to return
	 * @return the element at the specified position in this collection
	 * @throws IndexOutOfBoundsException if the specified index is out of range, 
	 *         ({@code index < 0 || index >= size()})
	 */
	T get(int index);

	/**
	 * Inserts the specified element at the specified position in this collection.
	 * 
	 * @param value
	 *        value to be inserted
	 * @param position
	 *        index at which the specified element is to be inserted
	 * @throws NullPointerException if given object is null
	 * @throws IndexOutOfBoundsException if the specified index is out of range,
	 *         ({@code index < 0 || index >= size()})
	 */
	void insert(T value, int position);

	/**
	 * Returns the position of the first occurrence of a specified element in this list,
	 * or {@code -1} if element was not found.
	 * 
	 * @param value
	 *        element whose index is to be found
	 * @return position of the first occurrence of an element in an array 
	 *         or -1 if the element is not found
	 */
	int indexOf(Object value);

	/**
	 * Removes an element at the specified index from the collection. Element that was
	 * previously at location {@code index + 1} after this operation is at
	 * location {@code index}.
	 * 
	 * @param index
	 *        position of an element to be removed
	 * @throws IndexOutOfBoundsException if the specified index is out of range,
	 *         ({@code index < 0 || index >= size()})
	 */
	void remove(int index);
}
