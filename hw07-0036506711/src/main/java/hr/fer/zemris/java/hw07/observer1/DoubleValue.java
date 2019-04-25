package hr.fer.zemris.java.hw07.observer1;

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
		this.n = n;
	}
	
	/**
	 * {@inheritDoc}
	 * Calculates and writes double value of the current value stored in subject.
	 * If it is the n-th time observer is calculating it removes itself from the
	 * subject.
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		Objects.requireNonNull(istorage, "Given description cannot be null!");
		if (--n <= 0) {
			istorage.removeObserver(this);
		}
		System.out.println("Double value: " + istorage.getValue() * 2);
	}

}
