package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.components.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.model.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.model.Tool;
import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectPainter;

/**
 * Represents circle tool used to respond to mouse events.
 * 
 * @author Filip Husnjak
 */
public class CircleTool implements Tool {

	/** Model used by this tool */
	private DrawingModel model;
	
	/** Circle object created by this tool */
	private Circle circle;
	
	/** Flag which determines whether the first click occurred or not */
	private boolean firstClick = true;
	
	/** Foreground color provider */
	private IColorProvider fgColor;
	
	/** Canvas used to schedule repaint */
	private JDrawingCanvas canvas;
	
	/**
	 * Constructs new {@link CircleTool} with the given parameters.
	 * 
	 * @param model
	 *        model used by this circle tool
	 * @param fgColor
	 *        foreground color of the circle
	 * @param canvas
	 *        canvas which draws elements
	 */
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
	
	/**
	 * Sets radius of this circle determined from the mouse event object.
	 * 
	 * @param e
	 *        {@link MouseEvent} object
	 */
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
