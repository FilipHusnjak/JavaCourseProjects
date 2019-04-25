package hr.fer.zemris.java.hw07.observer2;

/**
 * Represents observer that observes whether integer value in class 
 * {@link #IntegerStorage} has changed. If thats is the case the method 
 * valueChanged should be called with proper description defined with
 * {@code IntegerStorageChange} class.
 * 
 * @author Filip Husnjak
 */
public interface IntegerStorageObserver {
	
	/**
	 * This method is run whenever integer value changes in {@code IntegerStorage}
	 * class. Proper description defined with {@code IntegerStorageChange} class
	 * should be supplied.
	 * 
	 * @param istorageChange
	 *        description that encapsulates IntegerStorage instance whose value was changed
	 *        old value and new value
	 * @throws NullPointerException if the given IntegerStorageChange is {@code null}
	 */
	public void valueChanged(IntegerStorageChange istorageChange);
}
