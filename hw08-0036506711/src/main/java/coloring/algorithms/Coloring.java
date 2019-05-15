package coloring.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import marcupic.opjj.statespace.coloring.Picture;

/**
 * Represents a state of the current painting in progress.<br>
 * It has 4 properties:
 * <ul>
 * <li> Pixel reference - reference pixel used to determine which color is to be filled
 * <li> Picture picture - picture object used for drawing
 * <li> int FillColor - which color is used for filling
 * <li> int refColor - which color is to be filled
 * </ul>
 * 
 * @author Filip Husnjak
 */
public class Coloring implements Supplier<Pixel>, Consumer<Pixel>, Function<Pixel, List<Pixel>>, Predicate<Pixel> {

	/**
	 * Reference pixel used to determine which color is to be filled
	 */
	private Pixel reference;

	/**
	 * Picture object used for drawing
	 */
	private Picture picture;

	/**
	 * Which color is used for filling
	 */
	private int fillColor;

	/**
	 * Which color is to be filled
	 */
	private int refColor;

	/**
	 * Helper array used to check neighbours
	 */
	private final int[] movex = { 1, 0, 0, -1 };

	/**
	 * Helper array used to check neighbours
	 */
	private final int[] movey = { 0, 1, -1, 0 };

	/**
	 * Constructs new Coloring object with specified Pixel reference, Picture
	 * and fill color.
	 * 
	 * @param reference
	 *        pixel reference of this Coloring object
	 * @param picture
	 *        picture used for drawing
	 * @param fillColor
	 *        color used for filling
	 * @throws NullPointerException if any of the given objects are {@code null}
	 */
	public Coloring(Pixel reference, Picture picture, int fillColor) {
		this.reference = Objects.requireNonNull(reference, "Given pixel cannot be null!");
		this.picture = Objects.requireNonNull(picture, "Given picture cannot be null!");
		this.fillColor = fillColor;
		this.refColor = picture.getPixelColor(reference.x, reference.y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean test(Pixel t) {
		return refColor == picture.getPixelColor(t.x, t.y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Pixel> apply(Pixel t) {
		List<Pixel> neighbours = new ArrayList<>();
		for (int i = 0; i < movex.length; ++i) {
			int newx = t.x + movex[i];
			int newy = t.y + movey[i];
			if (newx < 0 || newx >= picture.getWidth() || newy < 0 || newy >= picture.getHeight()) {
				continue;
			}
			neighbours.add(new Pixel(newx, newy));
		}
		return neighbours;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(Pixel t) {
		picture.setPixelColor(t.x, t.y, fillColor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Pixel get() {
		return reference;
	}

}
