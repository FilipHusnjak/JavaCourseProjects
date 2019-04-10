package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Instances of this class represent student record with 4 parameters:
 * <ul>
 *  <li>
 *  {@code String} jmbag 
 *  </li>
 *  <li>
 *  {@code String} firstName
 *  </li>
 *  <li>
 *  {@code String} lastName
 *  </li>
 *  <li>
 *  {@code int} grade
 *  </li>
 * </ul>
 * 
 * @author Filip Husnjak
 */
public class StudentRecord {

	/**
	 * Jmbag of the student
	 */
	private String jmbag;
	
	/**
	 * First name of the studen
	 */
	private String firstName;
	
	/**
	 * Last name of the studen
	 */
	private String lastName;
	
	/**
	 * Students grade
	 */
	private int grade;

	/**
	 * Constructs {@code StudentRecord} object with specified parameters.
	 * 
	 * @param jmbag
	 *        jmbag of the studen
	 * @param lastName
	 *        last name of the student
	 * @param firstName
	 *        first name of the student
	 * @param grade
	 *        students grade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int grade) {
		this.jmbag = Objects.requireNonNull(jmbag, "Given jmbag cannot be null!");
		this.firstName = Objects.requireNonNull(firstName, "Given firstName cannot be null!");
		this.lastName = Objects.requireNonNull(lastName, "Given lastName cannot be null!");
		this.grade = grade;
	}

	/**
	 * Returns jmbag of {@code this} StudentRecord.
	 * 
	 * @return jmbag of {@code this} StudentRecord
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Returns first name of {@code this} StudentRecord.
	 * 
	 * @return first name of {@code this} StudentRecord
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns last name of {@code this} StudentRecord.
	 * 
	 * @return last name of {@code this} StudentRecord
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns grade of {@code this} StudentRecord.
	 * 
	 * @return grade of {@code this} StudentRecord
	 */
	public int getGrade() {
		return grade;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof StudentRecord))
			return false;
		StudentRecord other = (StudentRecord) obj;
		return Objects.equals(jmbag, other.jmbag);
	}
	
}
