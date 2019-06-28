package hr.fer.zemris.java.hw17.visitors;

import java.awt.Rectangle;

import hr.fer.zemris.java.hw17.model.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.objects.Circle;
import hr.fer.zemris.java.hw17.objects.FilledCircle;
import hr.fer.zemris.java.hw17.objects.Line;

public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {
	
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
	
	public Rectangle getRect() {
		if (xMin == null || yMin == null || xMax == null || xMin == null) {
			return null;
		}
		return new Rectangle(xMin, yMin, xMax - xMin, yMax - yMin);
	}
	
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
