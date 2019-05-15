package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Layout manager that places components in a grid form with {@code 5} rows and {@code 7} columns. 
 * Component on the position (1, 1) is exception, it spans through 5 columns, so
 * this layout manager contains at max {@code 5 * 7 - 4} components.
 * 
 * @author Filip Husnjak
 */
public class CalcLayout implements LayoutManager2 {

	/**
	 * Space between rows and columns
	 */
	private final int space;
	
	/**
	 * Maps position defined with {@link RCPosition} class with components
	 */
	private final Map<RCPosition, Component> components = new HashMap<>();
	
	/**
	 * Number of rows in this layout
	 */
	private static final int rows = 5;
	
	/**
	 * Number of columns in this layout
	 */
	private static final int columns = 7;
	
	/**
	 * Constructs new {@link CalcLayout} with space between rows and columns
	 * set to {@code 0}.
	 */
	public CalcLayout() {
		space = 0;
	}
	
	/**
	 * Constructs new {@link CalcLayout} with given space between rows and columns.
	 * 
	 * @param space
	 *        space between rows and columns
	 * @throws IllegalArgumentException if the given space if negative
	 */
	public CalcLayout(int space) {
		if (space < 0) {
			throw new IllegalArgumentException("Space cannot be negative!");
		}
		this.space = space;
	}
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		components.values().remove(comp);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return calculateDimension(parent, Component::getPreferredSize);
	}
	
	@Override
	public Dimension maximumLayoutSize(Container target) {
		return calculateDimension(target, Component::getMaximumSize);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return calculateDimension(parent, Component::getMinimumSize);
	}
	
	/**
	 * Calculates proper size of a given parent {@link Container} 
	 * based on given getter function.
	 * 
	 * @param parent
	 *        parent whose dimension is to be calculated
	 * @param getter
	 *        function that extracts right dimension from components
	 * @return proper dimension
	 */
	private Dimension calculateDimension(Container parent, Function<Component, Dimension> getter) {
		int maxWidth = 0;
		int maxHeight = 0;
		for (Map.Entry<RCPosition, Component> entry: components.entrySet()) {
			Dimension dim = getter.apply(entry.getValue());
			if (dim == null) continue;
			if (entry.getKey().getRow() == 1 && entry.getKey().getColumn() == 1) {
				maxWidth = (int) Math.max(maxWidth, Math.ceil((dim.width - 4 * space) / 5));
			} else {
				maxWidth = Math.max(maxWidth, dim.width);
			}
			maxHeight = Math.max(maxHeight, dim.height);
		}
		Insets insets = parent.getInsets();
		return new Dimension(maxWidth * columns + space * (columns - 1) + insets.left + insets.right,
				maxHeight * rows + space * (rows - 1) + insets.top + insets.bottom);
	}

	// Linearly interpolates extra width and height as the result of component placement
	@Override
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		
		// Calculate width spent on gaps
		int gapWidth = space * (columns - 1);
		int insetsWidth = insets.left + insets.right;
		int componentWidth = (parent.getWidth() - gapWidth - insetsWidth) / columns;
		// Extra width to be interpolated
		int extraWidth = parent.getWidth() - gapWidth - insetsWidth - componentWidth * columns;
		
		// Calculate height spent on gaps
		int gapHeight = space * (rows - 1);
		int insetsHeight = insets.top + insets.bottom;
		int componentHeight = (parent.getHeight() - gapHeight - insetsHeight) / rows;
		// Extra height to be interpolated
		int extraHeight = parent.getHeight() - gapHeight - insetsHeight - componentHeight * rows;
		
		// Starting y value
		int y = insets.top;

		// Each how many x values next with should be incremented by 1
		int eachX = extraWidth > 0 ? (int) Math.floor(columns / extraWidth) - 1 : columns;
		// Each how many y values next height should be incremented by 1
		int eachY = extraHeight > 0 ? (int) Math.floor(rows / extraHeight) - 1 : rows;
		int tmpY = 0;
		for (int i = 1; i <= rows; ++i) {
			int x = insets.left;
			int tmpX = 0;
			// Height spent on next component
			int heightOnComponent = componentHeight;
			if (tmpY-- == 0) {
				tmpY = eachY;
				heightOnComponent += extraHeight > 0 ? 1 : 0;
				extraHeight--;
			}
			for (int j = 1, extraWidthtmp = extraWidth; j <= columns; ++j) {
				// Width spent on next component
				int widthOnCompnent = componentWidth;
				if (tmpX-- == 0) {
					tmpX = eachX;
					widthOnCompnent += extraWidthtmp > 0 ? 1 : 0;
					extraWidthtmp--;
				}
				// Find the component on the current position
				Component component = components.get(new RCPosition(i, j));
				// If component is found and positions are legal, place found component
				if (component != null && (i != 1 || j != 1)) {
					component.setBounds(x, y, widthOnCompnent, heightOnComponent);
				}
				// If we reached the point where we can calculate the top left component
				// width, we should calculate it and place it on right position
				if (i ==1 && j == 6) {
					component = components.get(new RCPosition(1, 1));
					if (component != null) {
						component.setBounds(insets.left, insets.top,
								x - space - insets.right, heightOnComponent);
					}
				}
				x += widthOnCompnent + space;
			}
			y += heightOnComponent + space;
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		RCPosition position;
		if (constraints instanceof RCPosition) {
			position = (RCPosition) constraints;
		} else if (constraints instanceof String) {
			try {
				int[] pos = Arrays.stream(((String) constraints).split(",")).map(String::trim)
						.mapToInt(Integer::parseInt).toArray();
				if (pos.length != 2) {
					throw new CalcLayoutException("Wrong number of arguments provided!");
				}
				position = new RCPosition(pos[0], pos[1]);
			} catch (NumberFormatException e) {
				throw new CalcLayoutException("Given argument cannot be intepreted as number!");
			}
		} else {
			throw new CalcLayoutException("Wrong type of constraints given!");
		}
		checkConstraints(position);
		if (components.containsKey(position)) {
			throw new CalcLayoutException("Element at the given position already exists!");
		}
		components.put(position, comp);
	}

	/**
	 * Checks whether position is legal. Legal positions are with {@code x, y}
	 * set to: {@code 1 <= x <= rows, 1 <= y < <= columns}.
	 * 
	 * @param position
	 *        position to be checked
	 * @throws CalcLayoutException if the given position is not legal     
	 */
	private void checkConstraints(RCPosition position) {
		if (position.getRow() < 1 || position.getRow() > rows) {
			throw new CalcLayoutException("Row has to be number between 1 and 5!");
		}
		if (position.getColumn() < 1 || position.getColumn() > columns) {
			throw new CalcLayoutException("Column has to be number between 1 and 7!");
		}
		if (position.getRow() == 1 && position.getColumn() > 1 && position.getColumn() < 6) {
			throw new CalcLayoutException("Element in the first row can be placed at "
					+ "column postions 1, 6 or 7!");
		}
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {}

}
