package hr.fer.zemris.java.hw05.db;

/**
 * Represents a group of static {@code IFieldValueGetter} implementations. Each instance
 * returns the value of a different field in StudentRecord.
 * 
 * @author Filip Husnjak
 */
public class FieldValueGetters {

	/**
	 * Implementation of IFieldValueGetter which returns first name of a given StudentRecord.
	 */
	public static final IFieldValueGetter FIRST_NAME = StudentRecord::getFirstName;
	
	/**
	 * Implementation of IFieldValueGetter which returns last name of a given StudentRecord.
	 */
	public static final IFieldValueGetter LAST_NAME = StudentRecord::getLastName;
	
	/**
	 * Implementation of IFieldValueGetter which returns jmbag of a given StudentRecord.
	 */
	public static final IFieldValueGetter JMBAG = StudentRecord::getJmbag;
	
}
