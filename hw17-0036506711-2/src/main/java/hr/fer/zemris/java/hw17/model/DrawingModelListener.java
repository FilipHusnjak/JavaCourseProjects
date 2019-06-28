package hr.fer.zemris.java.hw17.model;

public interface DrawingModelListener {

	void objectsAdded(DrawingModel source, int index0, int index1);

	void objectsRemoved(DrawingModel source, int index0, int index1);

	void objectsChanged(DrawingModel source, int index0, int index1);

}
