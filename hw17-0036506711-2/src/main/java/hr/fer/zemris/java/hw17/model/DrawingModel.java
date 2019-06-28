package hr.fer.zemris.java.hw17.model;

import java.nio.file.Path;

public interface DrawingModel extends GeometricalObjectListener {

	int getSize();

	GeometricalObject getObject(int index);

	void add(GeometricalObject object);

	void remove(GeometricalObject object);

	void changeOrder(GeometricalObject object, int offset);

	int indexOf(GeometricalObject object);

	void clear();

	void clearModifiedFlag();

	boolean isModified();
	
	void save(Path path);
	
	void load(Path path);
	
	void export(Path path);
		
	Path getCurrentPath();

	void addDrawingModelListener(DrawingModelListener l);

	void removeDrawingModelListener(DrawingModelListener l);

}
