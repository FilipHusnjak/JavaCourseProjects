package hr.fer.zemris.java.hw07.observer2;

import java.util.Objects;

/**
 * Implementation of {@link #IntegerStorageObserver} which writes to the standard
 * output double value (i.e. "value * 2") of the current value that is stored in
 * subject, but only first {@code n} times since its registration. After writing 
 * the double value for the n-th time, the observer automatically de-registers 
 * itself from the subject
 * 
 * @author Filip Husnjak
 */
public class DoubleValue implements IntegerStorageObserver {

	/**
	 * How many times the observer should calculate double value
	 */
	private int n;
	
	/**
	 * Constructs new {@code DoubleValue} instance with specified {@code n}, which
	 * represents how many times double value should be calculated and printed
	 * before observer removes itself.
	 * 
	 * @param n
	 *        number of times the double value should be calculated and printed
	 * @throws IllegalArgumentException if the given number is less than 1
	 */
	public DoubleValue(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("Specified number has to be greater than 0!");
		}
		this.n = n;
	}
	
	/**
	 * {@inheritDoc}
	 * Calculates and writes double value of the current value stored in subject.
	 * If it is the n-th time observer is calculating it removes itself from the
	 * subject.
	 */
	@Override
	public void valueChanged(IntegerStorageChange istorageChange) {
		Objects.requireNonNull(istorageChange, "Given description cannot be null!");
		if (--n <= 0) {
			istorageChange.getIstorage().removeObserver(this);
		}
		System.out.println("Double value: " + istorageChange.getIstorage().getValue() * 2);
	}

}
