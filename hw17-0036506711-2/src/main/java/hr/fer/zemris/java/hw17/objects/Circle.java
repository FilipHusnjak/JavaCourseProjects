package hr.fer.zemris.java.hw17.objects;

import java.awt.Color;

import hr.fer.zemris.java.hw17.editors.CircleEditor;
import hr.fer.zemris.java.hw17.model.GeometricalObject;
import hr.fer.zemris.java.hw17.model.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.model.GeometricalObjectVisitor;

public class Circle extends GeometricalObject {
	
	private int centerX;
	
	private int centerY;
	
	private int radius;
	
	private Color fgColor;
		
	public Circle(int centerX, int centerY, int radius, Color fgColor) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.fgColor = fgColor;
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
	
	@Override
	public String toString() {
		return String.format("Circle (%d,%d), %d", centerX, centerY, radius);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new CircleEditor(this);
	}

}
