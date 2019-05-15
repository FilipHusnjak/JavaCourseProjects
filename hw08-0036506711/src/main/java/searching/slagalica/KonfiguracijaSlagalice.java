package searching.slagalica;

import java.util.Arrays;

/**
 * Represents the configuration of a puzzle. Configuration is stored in an int array.
 * Each element of the array is integer between [0, 8], 0 representing the space.
 * Each number has to appear once.
 * 
 * @author Filip Husnjak
 */
public class KonfiguracijaSlagalice {

	/**
	 * Array containing current configuration of a puzzle.
	 */
	private int[] polje;

	/**
	 * Constructs new {@link KonfiguracijaSlagalice} with specified parameter.
	 * 
	 * @param polje
	 *        array containing the initial configuration of a puzzle
	 */
	public KonfiguracijaSlagalice(int[] polje) {
		this.polje = polje;
	}
	
	/**
	 * Returns an array containing the configuration of a puzzle.
	 * 
	 * @return an array containing the configuration of a puzzle
	 */
	public int[] getPolje() {
		return polje;
	}
	
	/**
	 * Returns an index of 0 in {@link KonfiguracijaSlagalice#polje} that represents
	 * space in the configuration of a puzzle.
	 * 
	 * @return an index of 0 in {@link KonfiguracijaSlagalice#polje} that represents
	 *         space in the configuration of a puzzle
	 */
	public int indexOfSpace() {
		for (int i = 0; i < polje.length; ++i) {
			if (polje[i] == 0) return i;
		}
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < polje.length; ++i) {
			if (i != 0 && i % 3 == 0) result.append("\n");
			result.append(polje[i] == 0 ? "* " : polje[i] + " ");
		}
		return result.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(polje);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof KonfiguracijaSlagalice))
			return false;
		KonfiguracijaSlagalice other = (KonfiguracijaSlagalice) obj;
		return Arrays.equals(polje, other.polje);
	}
	
}
