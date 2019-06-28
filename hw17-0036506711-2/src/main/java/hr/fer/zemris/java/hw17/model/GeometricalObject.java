package hr.fer.zemris.java.hw17.model;

import java.util.LinkedList;
import java.util.List;

public abstract class GeometricalObject {
	
	private List<GeometricalObjectListener> listeners = new LinkedList<>();

	public abstract void accept(GeometricalObjectVisitor v);

	public abstract GeometricalObjectEditor createGeometricalObjectEditor();

	public void addGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.add(l);
	}

	public void removeGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.remove(l);
	}

	protected void notifyListeners() {
		for (var l: listeners) {
			l.geometricalObjectChanged(this);
		}
	}
	
}
