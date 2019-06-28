package hr.fer.zemris.java.hw17.model;

import hr.fer.zemris.java.hw17.objects.Circle;
import hr.fer.zemris.java.hw17.objects.FilledCircle;
import hr.fer.zemris.java.hw17.objects.Line;

public interface GeometricalObjectVisitor {

	void visit(Line line);
	
	void visit(Circle circle);
	
	void visit(FilledCircle filledCircle);
	
}
