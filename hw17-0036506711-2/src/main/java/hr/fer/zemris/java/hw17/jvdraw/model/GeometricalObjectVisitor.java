package hr.fer.zemris.java.hw17.jvdraw.model;

import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;

/**
 * Visitor used in visitor design pattern which defines proper methods for
 * lines, circles and filled circles.
 * 
 * @author Filip Husnjak
 */
public interface GeometricalObjectVisitor {

	/**
	 * Performs action upon the given line.
	 * 
	 * @param line
	 *        line upon which the action is performed
	 */
	void visit(Line line);
	
	/**
	 * Performs action upon the given circle.
	 * 
	 * @param circle
	 *        circle upon which the action is performed
	 */
	void visit(Circle circle);
	
	/**
	 * Performs action upon the given filled circle.
	 * 
	 * @param filledCircle
	 *        filled circle upon which the action is performed
	 */
	void visit(FilledCircle filledCircle);
	
}
