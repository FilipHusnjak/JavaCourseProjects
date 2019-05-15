package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

/**
 * Represents position of a component in {@link CalcLayout} layout manager.
 * 
 * @author Filip Husnjak
 */
public class RCPosition {

	/**
	 * Row number
	 */
	private final int row;
	
	/**
	 * Column number
	 */
	private final int column;

	/**
	 * Constructs new {@link RCPosition} with specified row and column.
	 * 
	 * @param row
	 *        row of the components position
	 * @param column
	 *        column of the components position
	 */
	public RCPosition(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

	/**
	 * Returns row of this position.
	 * 
	 * @return row of this position
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns column of this position.
	 * 
	 * @return column of this position
	 */
	public int getColumn() {
		return column;
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RCPosition))
			return false;
		RCPosition other = (RCPosition) obj;
		return column == other.column && row == other.row;
	}
	
}
