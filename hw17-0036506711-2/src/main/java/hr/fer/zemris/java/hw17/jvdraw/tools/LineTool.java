package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.components.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.model.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.model.Tool;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectPainter;

/**
 * Represents line tool used to respond to mouse events.
 * 
 * @author Filip Husnjak
 */
public class LineTool implements Tool {

	/** Model used by this tool */
	private DrawingModel model;
	
	/** Line object created by this tool */
	private Line line;
	
	/** Flag which determines whether the first click occurred or not */
	private boolean firstClick = true;
	
	/** Foreground color provider */
	private IColorProvider fgColor;
	
	/** Canvas used to schedule repaint */
	private JDrawingCanvas canvas;
	
	/**
	 * Constructs new {@link LineTool} with the given parameters.
	 * 
	 * @param model
	 *        model used by this line tool
	 * @param fgColor
	 *        foreground color of the line
	 * @param canvas
	 *        canvas which draws elements
	 */
	public LineTool(DrawingModel model, IColorProvider fgColor, JDrawingCanvas canvas) {
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
			line = new Line(e.getX(), e.getY(), e.getX(), e.getY(), fgColor.getCurrentColor());
		} else {
			setEndPoint(e);
			model.add(line);
		}
		firstClick = !firstClick;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!firstClick) {
			setEndPoint(e);
			canvas.repaint();
		}
	}
	
	/**
	 * Sets end point of the line calculated from given mouse event.
	 * 
	 * @param e
	 *        {@link MouseEvent} object
	 */
	private void setEndPoint(MouseEvent e) {
		line.setEnd(e.getX(), e.getY());
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (!firstClick) {
			line.accept(new GeometricalObjectPainter(g2d));
		}
	}
	
}
