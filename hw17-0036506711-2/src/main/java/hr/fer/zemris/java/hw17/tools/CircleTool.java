package hr.fer.zemris.java.hw17.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.components.JDrawingCanvas;
import hr.fer.zemris.java.hw17.model.DrawingModel;
import hr.fer.zemris.java.hw17.model.IColorProvider;
import hr.fer.zemris.java.hw17.model.Tool;
import hr.fer.zemris.java.hw17.objects.Circle;
import hr.fer.zemris.java.hw17.visitors.GeometricalObjectPainter;

public class CircleTool implements Tool {

	private DrawingModel model;
		
	private Circle circle;
	
	private boolean firstClick = true;
	
	private IColorProvider fgColor;
	
	private JDrawingCanvas canvas;
	
	public CircleTool(DrawingModel model, IColorProvider fgColor, JDrawingCanvas canvas) {
		this.model = model;
		this.fgColor = fgColor;
		this.canvas = canvas;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (firstClick) {
			circle = new Circle(e.getX(), e.getY(), 0, fgColor.getCurrentColor());
		} else {
			setRadius(e);
			model.add(circle);
		}
		firstClick = !firstClick;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!firstClick) {
			setRadius(e);
			canvas.repaint();
		}
	}
	
	private void setRadius(MouseEvent e) {
		double xDist = e.getX() - circle.getCenterX();
		double yDist = e.getY() - circle.getCenterY();
		circle.setRadius((int) Math.sqrt(xDist * xDist + yDist * yDist));
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (!firstClick) {
			circle.accept(new GeometricalObjectPainter(g2d));
		}
	}

}
