package hr.fer.zemris.java.hw07.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents collection of prime numbers. It calculates new prime number only
 * when requested. It can return only first {@code n} prime numbers. Parameter
 * {@code n} is given through constructor. This class implements interface
 * {@code Iterable} so it can be used in short notation of for-loops.
 * 
 * @author Filip Husnjak
 */
public class PrimesCollection implements Iterable<Integer> {
	
	/**
	 * How many prime numbers this collection can return
	 */
	private final int n;

	/**
	 * Constructs new {@code PrimesCollection} instance with specified parameter.
	 * 
	 * @param n
	 *        number of prime numbers this collection can return
	 */
	public PrimesCollection(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Given argument has to be posivite!");
		}
		this.n = n;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new PrimeIterator();
	}
	
	/**
	 * Represents iterator upon this instance of {@code PrimesCollection}. It
	 * implements interface {@code Iterator} so it provides all required methods.
	 * 
	 * @author Filip Husnjak
	 */
	private class PrimeIterator implements Iterator<Integer> {
		
		/**
		 * Counts how many prime numbers this iterator returned
		 */
		private int counter = 0;
		
		/**
		 * Represents last prime number this iterator returned
		 */
		private int lastPrime = 1;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return counter < n;
		}

		/**
		 * {@inheritDoc}
		 * @throws NoSuchElementException if there are no more elemnents in this
		 *         iteration
		 */
		@Override
		public Integer next() {
			if (!hasNext()) {
				throw new NoSuchElementException("There are no more prime numbers!");
			}
			return getNextPrime();
		}
		
		/**
		 * Returns next prime based on {@link #lastPrime}.
		 * 
		 * @return next prime based on {@link #lastPrime}
		 */
		private int getNextPrime() {
			for (++lastPrime; !isPrime(lastPrime); ++lastPrime);
			counter++;
			return lastPrime;
		}

		/**
		 * Returns {@code true} if the given number is prime.
		 * 
		 * @param prime
		 *        number to be checked
		 * @return {@code true} if the given number is prime
		 */
		private boolean isPrime(int prime) {
			int root = (int)Math.sqrt(prime);
			for (int i = 2; i <= root; ++i) {
				if (prime % i == 0) return false;
			}
			return true;
		}
		
	}
	
}
