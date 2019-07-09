package hr.fer.zemris.java.hw17.jvdraw.components;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

import hr.fer.zemris.java.hw17.jvdraw.model.IColorProvider;

/**
 * Represents bottom lable which shows currently selected foreground and background
 * colors.
 * 
 * @author Filip Husnjak
 */
public class JBottomLabel extends JLabel {

	private static final long serialVersionUID = -6036556022560726579L;
	
	/** Currently selected background color */
	private Color bgColor;
	
	/** Currently selected foreground color */
	private Color fgColor;
	
	/**
	 * Constructs new {@link JBottomLabel} with given foreground and background color
	 * providers.
	 * 
	 * @param fgColorProvider
	 *        color provider used to fetch foreground color
	 * @param bgColorProvider
	 *        color provider used to fetch background color
	 */
	public JBottomLabel(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		initGui(fgColorProvider, bgColorProvider);
		addListeners(fgColorProvider, bgColorProvider);
	}
	
	/**
	 * Initializes GUI
	 */
	private void initGui(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		Border padding = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(padding);
		fgColor = fgColorProvider.getCurrentColor();
		bgColor = bgColorProvider.getCurrentColor();
		setText();
	}

	/**
	 * Adds proper listeners to the given color providers.
	 * 
	 * @param fgColorProvider
	 *        foreground color provider
	 * @param bgColorProvider
	 *        background color provider 
	 */
	private void addListeners(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		fgColorProvider.addColorChangeListener((p, c1, c2) -> {
			fgColor = c2;
			setText();
		});
		bgColorProvider.addColorChangeListener((p, c1, c2) -> {
			bgColor = c2;
			setText();
		});
	}

	/**
	 * Sets text of this lable.
	 */
	private void setText() {
		setText(String.format("Foreground color: %s, "
				+ "background color: %s.", getRGB(fgColor), getRGB(bgColor)));
		repaint();
	}
	
	/**
	 * Returns formatted rgb values of the given color.
	 * 
	 * @param color
	 *        color whose rgb values are to be determined
	 * @return formatted rgb values of the given color
	 */
	private String getRGB(Color color) {
		return String.format("(%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
	}
	
}
