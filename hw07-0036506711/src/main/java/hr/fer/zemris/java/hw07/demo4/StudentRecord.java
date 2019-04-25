package hr.fer.zemris.java.hw07.demo4;

import java.util.Objects;

/**
 * Instances of this class represent student record with 7 parameters:
 * <ul>
 *  <li>
 *  {@code String} jmbag
 *  </li>
 *  <li>
 *  {@code String} lastName
 *  </li>
 *  <li>
 *  {@code String} firstName
 *  </li>
 *  <li>
 *  {@code double} pointsMI
 *  </li>
 *  <li>
 *  {@code double} pointsZI
 *  </li>
 *  <li>
 *  {@code double} pointsLAB
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
	 * Students jmbag
	 */
	private String jmbag;
	
	/**
	 * Students lastName
	 */
	private String prezime;
	
	/**
	 * Students firstName
	 */
	private String ime;
	
	/**
	 * Students pointsMI
	 */
	private double brojBodovaMI;
	
	/**
	 * Students pointsZI
	 */
	private double brojBodovaZI;
	
	/**
	 * Students pointsLAB
	 */
	private double brojBodovaLab;
	
	/**
	 * Students grade
	 */
	private int ocjena;

	/**
	 * Constructs {@code StudentRecord} object with specified parameters.
	 * 
	 * @param jmbag
	 *        students jmbag
	 * @param prezime
	 *        students lastName
	 * @param ime
	 *        students firstName
	 * @param brojBodovaMI
	 *        students pointsMI
	 * @param brojBodovaZI
	 *        students pointsZI
	 * @param brojBodovaLab
	 *        students pointsLAB
	 * @param ocjena
	 *        students grade
	 * @throws NullPointerException if any of the given arguments is {@code null}
	 */
	public StudentRecord(String jmbag, String prezime, String ime, double brojBodovaMI, double brojBodovaZI,
			double brojBodovaLab, int ocjena) {
		super();
		this.jmbag = Objects.requireNonNull(jmbag, "Given jmbag cannot be null!");
		this.prezime = Objects.requireNonNull(prezime, "Given lastName cannot be null!");
		this.ime = Objects.requireNonNull(ime, "Given fistName cannot be null!");
		this.brojBodovaMI = brojBodovaMI;
		this.brojBodovaZI = brojBodovaZI;
		this.brojBodovaLab = brojBodovaLab;
		this.ocjena = ocjena;
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
	 * Returns last name of {@code this} StudentRecord.
	 * 
	 * @return last name of {@code this} StudentRecord
	 */
	public String getPrezime() {
		return prezime;
	}

	/**
	 * Returns first name of {@code this} StudentRecord.
	 * 
	 * @return first name of {@code this} StudentRecord
	 */
	public String getIme() {
		return ime;
	}

	/**
	 * Returns pointsMI of {@code this} StudentRecord.
	 * 
	 * @return pointsMI of {@code this} StudentRecord
	 */
	public double getBrojBodovaMI() {
		return brojBodovaMI;
	}

	/**
	 * Returns pointsZI of {@code this} StudentRecord.
	 * 
	 * @return pointsZI of {@code this} StudentRecord
	 */
	public double getBrojBodovaZI() {
		return brojBodovaZI;
	}

	/**
	 * Returns pointsLAB of {@code this} StudentRecord.
	 * 
	 * @return pointsLAB of {@code this} StudentRecord
	 */
	public double getBrojBodovaLab() {
		return brojBodovaLab;
	}

	/**
	 * Returns grade of {@code this} StudentRecord.
	 * 
	 * @return grade of {@code this} StudentRecord
	 */
	public int getOcjena() {
		return ocjena;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return jmbag + "\t" + prezime + "\t" + ime + "\t" + brojBodovaMI + "\t"
				+ brojBodovaZI + "\t" + brojBodovaLab + "\t" + ocjena;
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
