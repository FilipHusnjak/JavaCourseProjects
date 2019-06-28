package hr.fer.zemris.java.hw17.visitors;

import java.awt.Graphics2D;

import hr.fer.zemris.java.hw17.model.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.objects.Circle;
import hr.fer.zemris.java.hw17.objects.FilledCircle;
import hr.fer.zemris.java.hw17.objects.Line;

public class GeometricalObjectPainter implements GeometricalObjectVisitor {
	
	private Graphics2D g2d;

	public GeometricalObjectPainter(Graphics2D g2d) {
		this.g2d = g2d;
	}

	@Override
	public void visit(Line line) {
		g2d.setColor(line.getFgColor());
		g2d.drawLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
	}

	@Override
	public void visit(Circle circle) {
		int size = circle.getRadius() * 2;
		int x = circle.getCenterX() - circle.getRadius();
		int y = circle.getCenterY() - circle.getRadius();
		g2d.setColor(circle.getFgColor());
		g2d.drawOval(x, y, size, size);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		int size = filledCircle.getRadius() * 2;
		int x = filledCircle.getCenterX() - filledCircle.getRadius();
		int y = filledCircle.getCenterY() - filledCircle.getRadius();
		g2d.setColor(filledCircle.getBgColor());
		g2d.fillOval(x, y, size, size);
		g2d.setColor(filledCircle.getFgColor());
		g2d.drawOval(x, y, size, size);
	}

}
