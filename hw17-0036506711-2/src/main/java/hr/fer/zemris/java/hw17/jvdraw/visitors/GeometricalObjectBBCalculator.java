package hr.fer.zemris.java.hw17.jvdraw.visitors;

import java.awt.Rectangle;

import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;

/**
 * {@link GeometricalObjectVisitor} implementation which calculates the bounding
 * box of the whole image.
 * 
 * @author Filip Husnjak
 */
public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {
	
	/** Coordinates of the bounding box */
	private Integer xMax, yMax, xMin, yMin;
	
	@Override
	public void visit(Line line) {
		adjust(line.getStartX(), line.getStartY());
		adjust(line.getEndX(), line.getEndY());
	}

	@Override
	public void visit(Circle circle) {
		adjust(circle.getCenterX() - circle.getRadius(), circle.getCenterY() - circle.getRadius());
		adjust(circle.getCenterX() + circle.getRadius(), circle.getCenterY() + circle.getRadius());
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		adjust(filledCircle.getCenterX() - filledCircle.getRadius(), 
				filledCircle.getCenterY() - filledCircle.getRadius());
		adjust(filledCircle.getCenterX() + filledCircle.getRadius(), 
				filledCircle.getCenterY() + filledCircle.getRadius());
	}
	
	/**
	 * Returns bounding box of the image.
	 * 
	 * @return bounding box of the image
	 */
	public Rectangle getRect() {
		if (xMin == null || yMin == null || xMax == null || xMin == null) {
			return null;
		}
		return new Rectangle(xMin, yMin, xMax - xMin, yMax - yMin);
	}
	
	/**
	 * Adjusts coordinates of the bounding box based on the given ones.
	 * 
	 * @param x
	 *        x coordinate of an object
	 * @param y
	 *        y coordinate of an object
	 */
	private void adjust(int x, int y) {
		if (xMax == null) {
			xMax = x;
		} else {
			xMax = Math.max(x, xMax);
		}
		if (yMax == null) {
			yMax = y;
		} else {
			yMax = Math.max(y, yMax);
		}
		if (xMin == null) {
			xMin = x;
		} else {
			xMin = Math.min(x, xMin);
		}
		if (yMin == null) {
			yMin = y;
		} else {
			yMin = Math.min(y, yMin);
		}
	}

}
