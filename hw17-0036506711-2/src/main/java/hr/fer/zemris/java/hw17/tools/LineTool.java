package hr.fer.zemris.java.hw17.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.components.JDrawingCanvas;
import hr.fer.zemris.java.hw17.model.DrawingModel;
import hr.fer.zemris.java.hw17.model.IColorProvider;
import hr.fer.zemris.java.hw17.model.Tool;
import hr.fer.zemris.java.hw17.objects.Line;
import hr.fer.zemris.java.hw17.visitors.GeometricalObjectPainter;

public class LineTool implements Tool {

	private DrawingModel model;
	
	private Line line;
	
	private boolean firstClick = true;
	
	private IColorProvider fgColor;
	
	private JDrawingCanvas canvas;
	
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
