package hr.fer.zemris.java.hw17.jvdraw.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

import hr.fer.zemris.java.hw17.jvdraw.listeners.ColorChangeListener;
import hr.fer.zemris.java.hw17.jvdraw.model.IColorProvider;

/**
 * Represents JComponent used to change background or foreground colors.
 * 
 * @author Filip Husnjak
 */
public class JColorArea extends JComponent implements IColorProvider {
	
	private static final long serialVersionUID = 3608043745283393775L;
	
	/** Currently selected color */
	private Color color;
	
	/** List of listeners to be notified upon each color change */
	private List<ColorChangeListener> listeners = new LinkedList<>();

	/**
	 * Constructs JColorArea with the given color.
	 * 
	 * @param color
	 *        initial color
	 */
	public JColorArea(Color color) {
		initGui(color);
	}
	
	/**
	 * Initializes GUI
	 */
	private void initGui(Color color) {
		setOpaque(true);
		setSelectedColor(color);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color newColor = JColorChooser.showDialog(
								JColorArea.this, 
								"Choose background color", 
								color);
				if (newColor == null) return;
				setSelectedColor(newColor);
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
        Dimension dim = getSize();
        Insets ins = getInsets();
        g2d.setColor(color);
        g2d.fillRect(ins.left, ins.top, dim.width, dim.height);
	}
	
	/**
	 * Notifies listeners about color change
	 * 
	 * @param oldColor
	 *        oldColor before the change
	 */
	private void notifyListeners(Color oldColor) {
		for (var l: listeners) {
			l.newColorSelected(this, oldColor, color);
		}
	}

	/**
	 * Sets the given color as new color of this area
	 * 
	 * @param color
	 *        new color
	 */
	public void setSelectedColor(Color color) {
		setBackground(color);
		repaint();
		Color oldColor = this.color;
		this.color = color;
		notifyListeners(oldColor);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(15, 15);
	}
	
	@Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

	@Override
	public Color getCurrentColor() {
		return color;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		listeners.add(l);
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}
	
}
