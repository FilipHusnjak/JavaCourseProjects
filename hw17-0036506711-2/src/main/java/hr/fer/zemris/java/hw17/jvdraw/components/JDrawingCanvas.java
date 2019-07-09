package hr.fer.zemris.java.hw17.jvdraw.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw17.jvdraw.listeners.DrawingModelListener;
import hr.fer.zemris.java.hw17.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.model.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.model.Tool;
import hr.fer.zemris.java.hw17.jvdraw.tools.CircleTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.FilledCircleTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.LineTool;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectPainter;

/**
 * Represents JComponent used for drawing.
 * 
 * @author Filip Husnjak
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener {

	private static final long serialVersionUID = -8609331255155348602L;
	
	/** Current state of this component */
	private Tool currentState;
	
	/** Drawing model used to draw and store elements */
	private DrawingModel model;
	
	/** Tool used to draw lines */
	private Tool lineTool;
	
	/** Tool used to draw circles */
	private Tool circleTool;
	
	/** Tool used to draw filled circles */
	private Tool filledCircleTool;
	
	/**
	 * Constructs new {@link JDrawingCanvas} with the given model and color
	 * providers.
	 * 
	 * @param model
	 *        model used to draw elements
	 * @param fgColor
	 *        foreground color provider
	 * @param bgColor
	 *        background color provider
	 */
	public JDrawingCanvas(DrawingModel model, IColorProvider fgColor, IColorProvider bgColor) {
		this.model = model;
		setupTools(fgColor, bgColor);
		initGui();
	}
	
	/**
	 * Initializes tools and sets current state to default.
	 * 
	 * @param fgColor
	 *        foreground color provider
	 * @param bgColor
	 *        background color provider
	 */
	private void setupTools(IColorProvider fgColor, IColorProvider bgColor) {
		lineTool = new LineTool(model, fgColor, this);
		circleTool = new CircleTool(model, fgColor, this);
		filledCircleTool = new FilledCircleTool(model, fgColor, bgColor, this);
		currentState = lineTool;
	}

	/**
	 * Initializes GUI
	 */
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
	
	/**
	 * Sets current state to the specified one.
	 * 
	 * @param currentState
	 *        new current state
	 */
	public void setCurrentState(Tool currentState) {
		this.currentState = currentState;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
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
	
	/**
	 * Returns tool used for drawing lines.
	 * 
	 * @return tool used for drawing lines
	 */
	public Tool getLineTool() {
		return lineTool;
	}
	
	/**
	 * Returns tool used for drawing circles.
	 * 
	 * @return tool used for drawing circles
	 */
	public Tool getCircleTool() {
		return circleTool;
	}
	
	/**
	 * Returns tool used for drawing filled circles.
	 * 
	 * @return tool used for drawing filled circles
	 */
	public Tool getFilledCircleTool() {
		return filledCircleTool;
	}
	
}
