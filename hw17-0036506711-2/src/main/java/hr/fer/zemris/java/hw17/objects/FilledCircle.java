package hr.fer.zemris.java.hw17.objects;

import java.awt.Color;

import hr.fer.zemris.java.hw17.editors.FilledCircleEditor;
import hr.fer.zemris.java.hw17.model.GeometricalObject;
import hr.fer.zemris.java.hw17.model.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.model.GeometricalObjectVisitor;

public class FilledCircle extends GeometricalObject {

	private int centerX;

	private int centerY;

	private int radius;

	private Color fgColor;

	private Color bgColor;

	public FilledCircle(int centerX, int centerY, int radius, Color fgColor, 
			Color bgColor) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	public void setCenter(int centerX, int centerY) {
		this.centerX = centerX;
		this.centerY = centerY;
		notifyListeners();
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
		notifyListeners();
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public int getRadius() {
		return radius;
	}
	
	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
	}
	
	public Color getFgColor() {
		return fgColor;
	}
	
	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}
	
	public Color getBgColor() {
		return bgColor;
	}
	
	@Override
	public String toString() {
		return String.format("Filled Circle (%d,%d), %d, 0x%08X", 
				centerX, centerY, radius, bgColor.getRGB());
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledCircleEditor(this);
	}

}
