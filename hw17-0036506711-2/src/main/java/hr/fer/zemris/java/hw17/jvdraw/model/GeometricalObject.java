package hr.fer.zemris.java.hw17.jvdraw.model;

import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw17.jvdraw.listeners.GeometricalObjectListener;

/**
 * Represents geometric object in this application. It provides default implementation
 * for listeners.
 * 
 * @author Filip Husnjak
 */
public abstract class GeometricalObject {
	
	/** Listeners which listen to changes made upon this geometric object */
	private List<GeometricalObjectListener> listeners = new LinkedList<>();

	/**
	 * Calls proper visit method upon the given visitor.
	 * 
	 * @param v
	 *        visitor used to call proper method
	 */
	public abstract void accept(GeometricalObjectVisitor v);

	/**
	 * Creates {@link GeometricalObjectEditor} for this type of geometric object. 
	 * 
	 * @return proper instance of {@link GeometricalObjectEditor}
	 */
	public abstract GeometricalObjectEditor createGeometricalObjectEditor();

	/**
	 * Adds the given listener to this object.
	 * 
	 * @param l
	 *        listener to be added
	 */
	public void addGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.add(l);
	}

	/**
	 * Removes the given listener from this object
	 * 
	 * @param l
	 *        listener to be removed
	 */
	public void removeGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.remove(l);
	}

	/**
	 * Notifies listeners about the change.
	 */
	protected void notifyListeners() {
		for (var l: listeners) {
			l.geometricalObjectChanged(this);
		}
	}
	
}
