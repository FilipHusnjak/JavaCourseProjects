package hr.fer.zemris.java.hw17.jvdraw.visitors;

import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;

/**
 * {@link GeometricalObjectVisitor} implementation which creates String in proper
 * format visiting all object in model.
 * 
 * @author Filip Husnjak
 */
public class GeometricalObjectSaveVisitor implements GeometricalObjectVisitor {

	/** Final text to be returned */
	private StringBuilder text = new StringBuilder();
	
	@Override
	public void visit(Line line) {
		text.append(String.format("LINE %d %d %d %d %d %d %d%n", 
				line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY(),
				line.getFgColor().getRed(), line.getFgColor().getGreen(), line.getFgColor().getBlue()));
	}

	@Override
	public void visit(Circle circle) {
		text.append(String.format("CIRCLE %d %d %d %d %d %d%n",
				circle.getCenterX(), circle.getCenterY(), circle.getRadius(),
				circle.getFgColor().getRed(), circle.getFgColor().getGreen(), circle.getFgColor().getBlue()));
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		text.append(String.format("FCIRCLE %d %d %d %d %d %d %d %d %d%n",
				filledCircle.getCenterX(), filledCircle.getCenterY(), filledCircle.getRadius(),
				filledCircle.getFgColor().getRed(), filledCircle.getFgColor().getGreen(), filledCircle.getFgColor().getBlue(),
				filledCircle.getBgColor().getRed(), filledCircle.getBgColor().getGreen(), filledCircle.getBgColor().getBlue()));
	}
	
	/**
	 * Returns the created text.
	 * 
	 * @return created text
	 */
	public String getText() {
		return text.toString();
	}

}
