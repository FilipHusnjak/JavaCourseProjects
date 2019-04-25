package hr.fer.zemris.java.hw07.observer2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class that stores one integer value and list of {@code ItegerStorageObserver}
 * instances whose methods onValueChanged are called whenever this integer value
 * changes.
 * 
 * @author Filip Husnjak
 */
public class IntegerStorage {

	/**
	 * Value stored in this {@code IntegerStorage} instance
	 */
	private int value;

	/**
	 * List of observers which are notified upon each change
	 */
	private List<IntegerStorageObserver> observers = new ArrayList<>();

	/**
	 * Constructs {@code IntegerStorage} instance with specified initial value.
	 * 
	 * @param initialValue
	 *        initial value stored in this instance
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}

	/**
	 * Adds the specified observer to this instance of {@code IntegerStorage} class.
	 * Whenever value is changed given observer will be notified. If there is the
	 * same instance already stored this method will not add another one.
	 * 
	 * @param observer
	 *        observer to be added
	 * @throws NullPointerException if the given observer is {@code null}
	 */
	public void addObserver(IntegerStorageObserver observer) {
		if (!observers.contains(Objects.requireNonNull(observer, "Given observer cannot be null!"))) {
			observers.add(observer);
		}
	}

	/**
	 * Removes the specified observer from this instance of {@code IntegerStorage}
	 * class. Observers are equals only if they represent the same instance of 
	 * {@code IntegerStorageObserver}. If the specified observer is not found this
	 * method does nothing.
	 * 
	 * @param observer
	 *        observer to be removed
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		observers.remove(observer);
	}

	/**
	 * Removes all observers from this {@code IntegerStorage} instance.
	 */
	public void clearObservers() {
		observers.clear();
	}

	/**
	 * Returns the value stored in this IntegerStorage instance.
	 * 
	 * @return the value stored in this IntegerStorage instance
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the value of stored in this IntegerStorage instance to the given one.
	 * 
	 * @param value
	 *        new value to be stored in this IntegerStorage instance
	 */
	public void setValue(int value) {
		if (this.value != value) {
			int oldValue = this.value;
			this.value = value;
			if (observers != null) {
				IntegerStorageChange desc = new IntegerStorageChange(this, oldValue, value);
				int size = observers.size();
				for (int i = 0; i < size; ++i) {
					observers.get(i).valueChanged(desc);
					if (observers.size() < size) {
						i -= size - observers.size(); 
						size = observers.size();
					}
				}
			}
		}
	}
	
}