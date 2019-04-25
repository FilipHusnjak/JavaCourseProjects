package hr.fer.zemris.java.hw07.observer1;

import java.util.Objects;

/*
 * Implementation of {@link #IntegerStorageObserver} which calculates and writes
 * square of the current value stored in subject.
 */
public class SquareValue implements IntegerStorageObserver {

	/**
	 * {@inheritDoc}
	 * Calculates and writes to standard output square of the current value stored
	 * in subject.
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		int value = Objects.requireNonNull(istorage, "Given description cannot be null!")
				             .getValue();
		System.out.println("Provided new value: " + value + 
				", square is " + value * value);
	}
	
}
