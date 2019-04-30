package searching.slagalica;

import java.util.Arrays;

public class KonfiguracijaSlagalice {

	private int[] polje;

	public KonfiguracijaSlagalice(int[] polje) {
		this.polje = polje;
	}
	
	public int[] getPolje() {
		return polje;
	}
	
	public int indexOfSpace() {
		for (int i = 0; i < polje.length; ++i) {
			if (polje[i] == 0) return i;
		}
		return -1;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < polje.length; ++i) {
			if (i != 0 && i % 3 == 0) result.append("\n");
			result.append(polje[i] == 0 ? "* " : polje[i] + " ");
		}
		return result.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(polje);
		return result;
	}

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
