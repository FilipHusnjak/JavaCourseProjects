package hr.fer.zemris.java.hw05.db;

/**
 * Represents functional interface which returns the value of a field in StudentRecord.
 * 
 * @author Filip Husnjak
 */
@FunctionalInterface
public interface IFieldValueGetter {

	/**
	 * Returns the value of a field in StudentRecord.
	 * 
	 * @param record
	 *        StudentRecord whose field is to be returned
	 * @return he value of a field in StudentRecord.
	 * @throws NullPointerException if the given StudentRecord is {@code null}
	 */
	String get(StudentRecord record);
	
}
