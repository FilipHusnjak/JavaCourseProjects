package coloring.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import marcupic.opjj.statespace.coloring.Picture;

public class Coloring implements Supplier<Pixel>, Consumer<Pixel>, Function<Pixel, List<Pixel>>, Predicate<Pixel> {

	private Pixel reference;

	private Picture picture;

	private int fillColor;

	private int refColor;

	private final int[] movex = { 1, 0, 0, -1 };

	private final int[] movey = { 0, 1, -1, 0 };

	public Coloring(Pixel reference, Picture picture, int fillColor) {
		this.reference = reference;
		this.picture = picture;
		this.fillColor = fillColor;
		this.refColor = picture.getPixelColor(reference.x, reference.y);
	}

	@Override
	public boolean test(Pixel t) {
		return refColor == picture.getPixelColor(t.x, t.y);
	}

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

	@Override
	public void accept(Pixel t) {
		picture.setPixelColor(t.x, t.y, fillColor);
	}

	@Override
	public Pixel get() {
		return reference;
	}

}
