package hr.fer.zemris.java.hw17.objects;

import java.awt.Color;

import hr.fer.zemris.java.hw17.editors.LineEditor;
import hr.fer.zemris.java.hw17.model.GeometricalObject;
import hr.fer.zemris.java.hw17.model.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.model.GeometricalObjectVisitor;

public class Line extends GeometricalObject {
	
	private int startX;
	
	private int startY;
	
	private int endX;
	
	private int endY;
	
	private Color fgColor;

	public Line(int startX, int startY, int endX, int endY, Color fgColor) {
		super();
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.fgColor = fgColor;
	}
	
	public void setStart(int startX, int startY) {
		this.startX = startX;
		this.startY = startY;
	}
	
	public int getStartX() {
		return startX;
	}
	
	public int getStartY() {
		return startY;
	}
	
	public void setEnd(int endX, int endY) {
		this.endX = endX;
		this.endY = endY;
	}

	public int getEndX() {
		return endX;
	}

	public int getEndY() {
		return endY;
	}
	
	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
	}

	public Color getFgColor() {
		return fgColor;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}
	
	@Override
	public String toString() {
		return String.format("Line (%d,%d)-(%d,%d)", startX, startY, endX, endY);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new LineEditor(this);
	}

}
