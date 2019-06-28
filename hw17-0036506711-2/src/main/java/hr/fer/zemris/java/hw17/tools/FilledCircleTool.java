package hr.fer.zemris.java.hw17.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.components.JDrawingCanvas;
import hr.fer.zemris.java.hw17.model.DrawingModel;
import hr.fer.zemris.java.hw17.model.IColorProvider;
import hr.fer.zemris.java.hw17.model.Tool;
import hr.fer.zemris.java.hw17.objects.FilledCircle;
import hr.fer.zemris.java.hw17.visitors.GeometricalObjectPainter;

public class FilledCircleTool implements Tool {

	private DrawingModel model;
	
	private FilledCircle filledCircle;
	
	private boolean firstClick = true;
	
	private IColorProvider fgColor;
	
	private IColorProvider bgColor;
	
	private JDrawingCanvas canvas;
	
	public FilledCircleTool(DrawingModel model, IColorProvider fgColor, 
			IColorProvider bgColor, JDrawingCanvas canvas) {
		this.model = model;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
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
			filledCircle = new FilledCircle(e.getX(), e.getY(), 0, fgColor.getCurrentColor(), bgColor.getCurrentColor());
		} else {
			setRadius(e);
			model.add(filledCircle);
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
		double xDist = e.getX() - filledCircle.getCenterX();
		double yDist = e.getY() - filledCircle.getCenterY();
		filledCircle.setRadius((int) Math.sqrt(xDist * xDist + yDist * yDist));
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (!firstClick) {
			filledCircle.accept(new GeometricalObjectPainter(g2d));
		}
	}

}
