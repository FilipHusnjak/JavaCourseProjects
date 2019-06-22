package hr.fer.zemris.java.blog.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Represents user of the blog. Each user can create and edit its own blogs and
 * also comment own and other blogs.
 * 
 * @author Filip Husnjak
 */
@NamedQueries({
	@NamedQuery(name="BlogUser.login", 
			query="select b from BlogUser as b where b.nick=:nick"),
	@NamedQuery(name="BlogUser.list", 
			query="select b from BlogUser as b")
})
@Entity
@Table(name="blog_users")
@Cacheable(true)
public class BlogUser {
	
	/** ID of the user */
	@Id @GeneratedValue
	private Long id;
	
	/** First name of the user */
	@Column(length=20, nullable=false)
	private String firstName;
	
	/** Last name of the user */
	@Column(length=20, nullable=false)
	private String lastName;
	
	/** Nickname of the user */
	@Column(length=20, nullable=false, unique=true)
	private String nick;
	
	/** Email of the user */
	@Column(length=50, nullable=false)
	private String email;
	
	/** Password hash of the user */
	@Column(length=64, nullable=false)
	private String passwordHash;
	
	/** List of blogs created by this user */
	@OneToMany(mappedBy="creator", fetch=FetchType.LAZY, 
			cascade=CascadeType.PERSIST, orphanRemoval=true)
	private List<BlogEntry> blogEntries;
	
	/**
	 * Returns ID of this user.
	 * 
	 * @return ID of this user
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets ID of this user to the specified one.
	 * 
	 * @param id
	 *        new id of the user
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Returns first name of the user.
	 * 
	 * @return first name of the user
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets first name of the user to the specified one.
	 * 
	 * @param firstName
	 *        first name of the user
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns last name of the user
	 * 
	 * @return last name of the user
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets last name of the user to the specified one.
	 * 
	 * @param lastName
	 *        new last name of this user
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns nickname of this user.
	 * 
	 * @return nickname of this user
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Sets nickname of this user to the specified one.
	 * 
	 * @param nick
	 *        new nick of this user
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Returns email of this user.
	 * 
	 * @return email of this user
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets email of this user to the specified one.
	 * 
	 * @param email
	 *        new email of this user
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns password hash of this user.
	 * 
	 * @return password hash of this user
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Sets password hash of this user to the specified one.
	 * 
	 * @param passwordHash
	 *        new password hash of this user
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	/**
	 * Returns list of blogs created by this user.
	 * 
	 * @return list of blogs created by this user
	 */
	public List<BlogEntry> getBlogEntries() {
		return blogEntries;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BlogUser))
			return false;
		BlogUser other = (BlogUser) obj;
		return Objects.equals(id, other.id);
	}
	
}
