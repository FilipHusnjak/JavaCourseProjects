package hr.fer.zemris.java.blog.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents comment of the specified {@link BlogEntry}.
 * 
 * @author Filip Husnjak
 */
@Entity
@Table(name="blog_comments")
public class BlogComment {

	/** ID of the comment */
	@Id @GeneratedValue
	private Long id;
	
	/** {@link BlogEntry} object which this comment belongs to */
	@ManyToOne
	@JoinColumn(nullable=false)
	private BlogEntry blogEntry;
	
	/** Email of the user that wrote the comment */
	@Column(length=100,nullable=false)
	private String usersEmail;
	
	/** Message of the comment */
	@Column(length=4096,nullable=false)
	private String message;
	
	/** Date when the comment was posted */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date postedOn;

	/**
	 * Returns ID of this comment.
	 * 
	 * @return ID of this comment
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets ID of this comment to the given one.
	 * 
	 * @param id
	 *        id of the comment
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Returns {@link BlogEntry} which represents the holder of this comment.
	 * 
	 * @return {@link BlogEntry} which represents the holder of this comment
	 */
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}
	
	/**
	 * Sets the holder of this comment to the specified one.
	 * 
	 * @param blogEntry
	 *        new holder of this comment
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Returns email of the user who wrote this comment.
	 * 
	 * @return email of the user who wrote this comment
	 */
	public String getUsersEmail() {
		return usersEmail;
	}

	/**
	 * Sets email of the user to the given one.
	 * 
	 * @param usersEMail
	 *        email to be set
	 */
	public void setUsersEmail(String usersEMail) {
		this.usersEmail = usersEMail;
	}
	
	/**
	 * Returns message of this comment.
	 * 
	 * @return message of this comment
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the message of this comment to the given one.
	 * 
	 * @param message
	 *        message of the comment
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Returns the date when the comment was posted.
	 * 
	 * @return the date when the comment was posted
	 */
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Sets the date of posting this comment to the specified one.
	 * 
	 * @param postedOn
	 *        date of posting this comment
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}