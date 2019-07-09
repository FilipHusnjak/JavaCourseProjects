package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.components.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.model.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.model.Tool;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectPainter;

/**
 * Represents filled circle tool used to respond to mouse events.
 * 
 * @author Filip Husnjak
 */
public class FilledCircleTool implements Tool {

	/** Model used by this tool */
	private DrawingModel model;
	
	/** Circle object created by this tool */
	private FilledCircle filledCircle;
	
	/** Flag which determines whether the first click occurred or not */
	private boolean firstClick = true;
	
	/** Foreground color provider */
	private IColorProvider fgColor;
	
	/** Background color provider */
	private IColorProvider bgColor;
	
	/** Canvas used to schedule repaint */
	private JDrawingCanvas canvas;
	
	/**
	 * Constructs new {@link FilledCircleTool} with the given parameters.
	 * 
	 * @param model
	 *        model used by this circle tool
	 * @param fgColor
	 *        foreground color of the circle
	 * @param bgColor
	 *        background color of the circle
	 * @param canvas
	 *        canvas which draws elements
	 */
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
	
	/**
	 * Sets radius of this circle determined from the mouse event object.
	 * 
	 * @param e
	 *        {@link MouseEvent} object
	 */
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
