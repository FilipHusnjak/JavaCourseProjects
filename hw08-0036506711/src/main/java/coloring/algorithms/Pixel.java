package coloring.algorithms;

import java.util.Objects;

/**
 * Represents pixel on the screen. 
 * It has 2 properties: X and Y coordinates.
 * 
 * @author Filip Husnjak
 */
public class Pixel {

	/**
	 * X coordinate of the pixel
	 */
	public int x;
	
	/**
	 * Y coordinate of the pixel
	 */
	public int y;

	/**
	 * Constructs new Pixel object with specified x and y coordinates.
	 * 
	 * @param x
	 *        x coordinate of the pixel
	 * @param y
	 *        y coordinate of the pixel
	 */
	public Pixel(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
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
		if (!(obj instanceof Pixel))
			return false;
		Pixel other = (Pixel) obj;
		return x == other.x && y == other.y;
	}
	
}
