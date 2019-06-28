package hr.fer.zemris.java.hw17.visitors;

import hr.fer.zemris.java.hw17.model.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.objects.Circle;
import hr.fer.zemris.java.hw17.objects.FilledCircle;
import hr.fer.zemris.java.hw17.objects.Line;

public class GeometricalObjectSaveVisitor implements GeometricalObjectVisitor {

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
	
	public String getText() {
		return text.toString();
	}

}
