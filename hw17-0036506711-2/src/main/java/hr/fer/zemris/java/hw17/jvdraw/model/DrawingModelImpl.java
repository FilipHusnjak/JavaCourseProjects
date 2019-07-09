package hr.fer.zemris.java.hw17.jvdraw.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.hw17.jvdraw.listeners.DrawingModelListener;
import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectSaveVisitor;

/**
 * Implementation of the {@link DrawingModel} interface.
 * 
 * @author Filip Husnjak
 */
public class DrawingModelImpl implements DrawingModel {
	
	/** Flag which determines whether this model was modified or not */
	private boolean isModified;
	
	/** Current path of this model */
	private Path currentPath;
	
	/** Listeners of this model */
	private List<DrawingModelListener> listeners = new LinkedList<>();
	
	/** Objects in this model */
	private List<GeometricalObject> objects = new ArrayList<>();

	@Override
	public void geometricalObjectChanged(GeometricalObject o) {
		int index = indexOf(o);
		notifyChanged(index, index);
	}

	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		objects.add(object);
		object.addGeometricalObjectListener(this);
		notifyAdded(objects.size() - 1, objects.size() - 1);
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notifies listeners that objects are added.
	 * 
	 * @param indexFrom
	 *        index of the first added object
	 * @param indexTo
	 *        index of the last added object
	 */
	private void notifyAdded(int indexFrom, int indexTo) {
		isModified = true;
		for (var l: listeners) {
			l.objectsAdded(this, indexFrom, indexTo);
		}
	}
	
	/**
	 * Notifies listeners that objects are removed.
	 * 
	 * @param indexFrom
	 *        index of the first removed object
	 * @param indexTo
	 *        index of the last removed object
	 */
	private void notifyRemoved(int indexFrom, int indexTo) {
		isModified = true;
		for (var l: listeners) {
			l.objectsRemoved(this, indexFrom, indexTo);
		}
	}
	
	/**
	 * Notifies listeners that objects are changed.
	 * 
	 * @param indexFrom
	 *        index of the first changed object
	 * @param indexTo
	 *        index of the last changed object
	 */
	private void notifyChanged(int indexFrom, int indexTo) {
		isModified = true;
		for (var l: listeners) {
			l.objectsChanged(this, indexFrom, indexTo);
		}
	}

	@Override
	public void remove(GeometricalObject object) {
		int index = indexOf(object);
		if (index < 0) return;
		object.removeGeometricalObjectListener(this);
		objects.remove(object);
		notifyRemoved(index, index);
	}

	@Override
	public void changeOrder(GeometricalObject object, int offset) {
		int index = indexOf(object);
		int fromIndex = Math.min(index, index + offset);
		int toIndex = Math.max(index, index + offset);
		if (!checkIndex(fromIndex) || !checkIndex(toIndex)) return;
		Collections.rotate(objects.subList(fromIndex, toIndex + 1), offset < 0 ? 1 : -1);
		notifyChanged(fromIndex, toIndex);
	}

	/**
	 * Checks whether given index is legal.
	 * 
	 * @param index
	 *        index to be checked
	 * @return true if the given index is legal
	 */
	private boolean checkIndex(int index) {
		return index >= 0 && index < objects.size();
	}

	@Override
	public int indexOf(GeometricalObject object) {
		return objects.indexOf(object);
	}

	@Override
	public void clear() {
		int size = objects.size();
		if (size == 0) return;
		objects.clear();
		notifyRemoved(0, size - 1);
	}

	@Override
	public void clearModifiedFlag() {
		isModified = false;
	}

	@Override
	public boolean isModified() {
		return isModified;
	}

	@Override
	public void save(Path path) {
		if (!path.toString().endsWith(".jvd")) {
			throw new IllegalArgumentException("Invalid file name! Allowed extensions: jvd!");
		}
		GeometricalObjectSaveVisitor saveVisitor = new GeometricalObjectSaveVisitor();
		for (var o: objects) {
			o.accept(saveVisitor);
		}
		try {
			Files.writeString(path, saveVisitor.getText());
			currentPath = path;
			clearModifiedFlag();
		} catch (IOException e) {
			throw new IllegalArgumentException("File cannot be created!");
		}
	}

	@Override
	public Path getCurrentPath() {
		return currentPath;
	}

	@Override
	public void load(Path path) {
		if (!Files.exists(path) || !path.toString().endsWith(".jvd")) {
			throw new IllegalArgumentException("Invalid File!");
		}
		try {
			List<String> lines = Files.readAllLines(path);
			clear();
			fillModel(lines);
			currentPath = path;
			notifyAdded(0, getSize() - 1);
			clearModifiedFlag();
		} catch (IOException e) {
			throw new IllegalArgumentException("File cannot be opened!");
		}
		
	}
	
	@Override
	public void export(Path path) {
		String extension = getExtension(path.toString());
		if (extension == null || !extension.equals(".jpg") && !extension.equals(".png") 
				&& !extension.equals(".gif")) {
			throw new IllegalArgumentException("Allowed extensions: jpg, png, gif!");
		}
		GeometricalObjectBBCalculator bbcalc = new GeometricalObjectBBCalculator();
		for (var o: objects) {
			o.accept(bbcalc);
		}
		Rectangle rect = bbcalc.getRect();
		if (rect == null) {
			throw new IllegalArgumentException("Document is empty!");
		}
		BufferedImage image = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
		g2d.translate(-rect.x, -rect.y);
		GeometricalObjectPainter painter = new GeometricalObjectPainter(g2d);
		for (var o: objects) {
			o.accept(painter);
		}
		g2d.dispose();
		try {
			ImageIO.write(image, extension.substring(1), path.toFile());
		} catch (IOException e) {
			throw new IllegalArgumentException("Image could not be saved!");
		}
	}

	private String getExtension(String pathName) {
		int index = pathName.lastIndexOf(".");
		if (index < 0) {
			return null;
		}
		return pathName.substring(index);
	}

	/**
	 * Fills this model from the given list of lines with proper format.
	 * 
	 * @param lines
	 *        lines used to fill this model
	 */
	private void fillModel(List<String> lines) {
		for (String line: lines) {
			String[] parts = line.split("\\s+");
			switch (parts[0]) {
			case "LINE":
				objects.add(new Line(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), 
						Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), new Color(
								Integer.parseInt(parts[5]), 
								Integer.parseInt(parts[6]), 
								Integer.parseInt(parts[7]))));
				break;
			case "CIRCLE":
				objects.add(new Circle(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), 
						Integer.parseInt(parts[3]), new Color(
								Integer.parseInt(parts[4]), 
								Integer.parseInt(parts[5]), 
								Integer.parseInt(parts[6]))));
				break;
			case "FCIRCLE":
				objects.add(new FilledCircle(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), 
						Integer.parseInt(parts[3]), new Color(
								Integer.parseInt(parts[4]), 
								Integer.parseInt(parts[5]), 
								Integer.parseInt(parts[6])),
						new Color(
								Integer.parseInt(parts[7]), 
								Integer.parseInt(parts[8]), 
								Integer.parseInt(parts[9]))));
				break;
			}
		}
	}
	
}
