package hr.fer.zemris.java.hw06.shell.util;

/**
 * Interface that represents NameBuilder used for creating new names based on
 * given expressions.
 * 
 * @author Filip Husnjak
 */
public interface NameBuilder {
	
	/**
	 * NameBuilder that does nothing.
	 */
	static NameBuilder blank = (r, sb) -> {};

	/**
	 * Generates part of the name by appending it at the end of the given 
	 * StringBuilder object.
	 * 
	 * @param result
	 *        FilterResult object used for creating new name
	 * @param sb
	 *        StringBuilder object updated with new name parts
	 */
	void execute(FilterResult result, StringBuilder sb);
	
	/**
	 * Returns new NameBuilder object that firstly executes {@code this} NameBuilder
	 * object and then appends the given text to the end of the given StringBuilder.
	 * 
	 * @param text
	 *        text to be added to the new name
	 * @return new NameBuilder object that firstly executes {@code this} NameBuilder
	 *         object and then appends the given text to the end of the given StringBuilder
	 */
	default NameBuilder text(String text) {
		return (r, sb) -> {
			execute(r, sb);
			sb.append(text);
		};
	}
	
	/**
	 * Returns new NameBuilder object that firstly executes {@code this} NameBuilder
	 * object and then appends the given group number at the end of the given StringBuilder.
	 * 
	 * @param groupNum
	 *        number of the group to be added to the new name
	 * @return new NameBuilder object that firstly executes {@code this} NameBuilder
	 *         object and then appends the given group number at the end of the given StringBuilder
	 */
	default NameBuilder group(int groupNum) {
		return (r, sb) -> {
			execute(r, sb);
			sb.append(r.group(groupNum));
		};
	}
	
	/**
	 * Returns new NameBuilder object that firstly executes {@code this} NameBuilder
	 * object and then appends the given group number at the end of the given StringBuilder.
	 * Appended group will have the minimum width {@link #minWidth} and if the specified group is
	 * shorter, padding char will be used to fill the rest.
	 * 
	 * @param groupNum
	 *        number of the group to be added to the new name
	 * @param padding
	 *        char to be used to fill the String to the size of minWidth if needed
	 * @param minWidth
	 *        minimum width of the group which will be appended
	 * @return new NameBuilder object that firstly executes {@code this} NameBuilder
	 *         object and then appends the given group number at the end of the given StringBuilder
	 *         with the width of {@link #minWidth} at least
	 */
	default NameBuilder group(int groupNum, char padding, int minWidth) {
		return (r, sb) -> {
			execute(r, sb);
			sb.append(String.format("%" + Math.max(minWidth, r.group(groupNum).length()) + "s", 
					r.group(groupNum)).replaceAll(" ", String.valueOf(padding)));
		};
	}
	
}
