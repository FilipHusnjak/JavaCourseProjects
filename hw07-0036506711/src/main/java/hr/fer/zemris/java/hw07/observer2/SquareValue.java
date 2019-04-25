package hr.fer.zemris.java.hw07.observer2;

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
	public void valueChanged(IntegerStorageChange istorageChange) {
		int value = Objects.requireNonNull(istorageChange, "Given description cannot be null!")
				            .getIstorage().getValue();
		System.out.println("Provided new value: " + value + 
				", square is " + value * value);
	}
	
}
