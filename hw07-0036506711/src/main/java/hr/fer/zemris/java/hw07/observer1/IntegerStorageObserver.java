package hr.fer.zemris.java.hw07.observer1;

/**
 * Represents observer that observes whether integer value in class 
 * {@link #IntegerStorage} has changed. If thats is the case the method 
 * valueChanged should be called.
 * 
 * @author Filip Husnjak
 */
public interface IntegerStorageObserver {
	
	/**
	 * This method is run whenever integer value changes in {@code IntegerStorage}
	 * class. It should be called with instance of the IntegerStorage whose value
	 * has changed.
	 * 
	 * @param istorage
	 *        instance of the IntegerStorage whose value has changed
	 * @throws NullPointerException if the given IntegerStorageChange is {@code null}
	 */
	public void valueChanged(IntegerStorage istorage);
	
}
