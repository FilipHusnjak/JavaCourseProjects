package hr.fer.zemris.java.hw07.observer2;

/**
 * Represents the description of a value change activity. It encapsulates 3 parameters:
 * <ul>
 * <li> iStorage - reference to the IntegerStorage instance whose value has changed
 * <li> oldValue - old value stored in IntegerStorage instance
 * <li> newValue - new value stored in IntegerStorage instance
 * </ul>
 * 
 * @author Filip Husnjak
 */
public class IntegerStorageChange {

	/**
	 * Reference to the IntegerStorage instance whose value has changed
	 */
	private final IntegerStorage istorage;
	
	/**
	 * Old value stored in IntegerStorage instance
	 */
	private final int oldValue;
	
	/**
	 * New value stored in IntegerStorage instance
	 */
	private final int newValue;
	
	/**
	 * Constructs new {@code IntegerStorageChange} instance with specified 
	 * parameters.
	 * 
	 * @param istorage
	 *        reference to the IntegerStorage instance whose value has changed
	 * @param oldValue
	 *        old value stored in IntegerStorage instance
	 * @param newValue
	 *        new value stored in IntegerStorage instance
	 */
	public IntegerStorageChange(IntegerStorage istorage, int oldValue, int newValue) {
		this.istorage = istorage;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	/**
	 * Returns the reference to the {@code IntegerStorage} instance whose value
	 * has changed.
	 * 
	 * @return the reference to the {@code IntegerStorage} instance whose value
	 *         has changed.
	 */
	public IntegerStorage getIstorage() {
		return istorage;
	}

	/**
	 * Returns old value stored in IntegerStorage instance.
	 * 
	 * @return old value stored in IntegerStorage instance
	 */
	public int getOldValue() {
		return oldValue;
	}

	/**
	 * Returns new value stored in IntegerStorage instance.
	 * 
	 * @return new value stored in IntegerStorage instance
	 */
	public int getNewValue() {
		return newValue;
	}
	
}
