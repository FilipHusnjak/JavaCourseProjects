package hr.fer.zemris.java.hw07.observer1;

import java.util.Objects;

/**
 * Implementation of {@link #IntegerStorageObserver} which counts and writes to
 * standard output the number of times the value had been change since this
 * observers registration.
 * 
 * @author Filip Husnjak
 */
public class ChangeCounter implements IntegerStorageObserver {

	/**
	 * Number of times the value has been changed
	 */
	private int counter = 0;
	
	/**
	 * {@inheritDoc}
	 * Writes to standard output the number of times the value had been change since this
	 * observers registration and increments {@link ChangeCounter#counter}.
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		Objects.requireNonNull(istorage, "Given description cannot be null!");
		System.out.println("Number of value changes since tracking: " + ++counter);
	}

}
