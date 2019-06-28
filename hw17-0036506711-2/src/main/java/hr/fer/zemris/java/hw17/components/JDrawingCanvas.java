package hr.fer.zemris.java.hw17.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw17.model.DrawingModel;
import hr.fer.zemris.java.hw17.model.DrawingModelListener;
import hr.fer.zemris.java.hw17.model.IColorProvider;
import hr.fer.zemris.java.hw17.model.Tool;
import hr.fer.zemris.java.hw17.tools.CircleTool;
import hr.fer.zemris.java.hw17.tools.FilledCircleTool;
import hr.fer.zemris.java.hw17.tools.LineTool;
import hr.fer.zemris.java.hw17.visitors.GeometricalObjectPainter;

public class JDrawingCanvas extends JComponent implements DrawingModelListener {

	private static final long serialVersionUID = -8609331255155348602L;
	
	private Tool currentState;
	
	private DrawingModel model;
	
	private Tool lineTool;
	
	private Tool circleTool;
	
	private Tool filledCircleTool;
	
	public JDrawingCanvas(DrawingModel model, IColorProvider fgColor, IColorProvider bgColor) {
		this.model = model;
		setupTools(fgColor, bgColor);
		initGui();
	}
	
	private void setupTools(IColorProvider fgColor, IColorProvider bgColor) {
		lineTool = new LineTool(model, fgColor, this);
		circleTool = new CircleTool(model, fgColor, this);
		filledCircleTool = new FilledCircleTool(model, fgColor, bgColor, this);
		currentState = lineTool;
	}

	private void initGui() {
		model.addDrawingModelListener(this);
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				currentState.mouseClicked(e);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				currentState.mousePressed(e);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				currentState.mouseReleased(e);
			}

		});
		
		addMouseMotionListener(new MouseAdapter() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				currentState.mouseMoved(e);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				currentState.mouseDragged(e);
			}
			
		});
	}
	
	public void setCurrentState(Tool currentState) {
		this.currentState = currentState;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		GeometricalObjectPainter painter = new GeometricalObjectPainter((Graphics2D) g);
 		for (int i = 0; i < model.getSize(); ++i) {
 			model.getObject(i).accept(painter);
 		}
 		currentState.paint((Graphics2D) g);
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		repaint();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1000, 1000);
	}
	
	public Tool getLineTool() {
		return lineTool;
	}
	
	public Tool getCircleTool() {
		return circleTool;
	}
	
	public Tool getFilledCircleTool() {
		return filledCircleTool;
	}
	
}
