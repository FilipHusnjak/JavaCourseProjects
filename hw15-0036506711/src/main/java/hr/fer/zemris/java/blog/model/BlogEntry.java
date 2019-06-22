package hr.fer.zemris.java.blog.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents Blog of the existing user. Each blog can have multiple comments.
 * 
 * @author Filip Husnjak
 */
@NamedQueries({
	@NamedQuery(name="BlogEntry.upit1",query="select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when")
})
@Entity
@Table(name="blog_entries")
@Cacheable(true)
public class BlogEntry {

	/** ID of the blog */
	@Id @GeneratedValue
	private Long id;
	
	/** Creator of the blog */
	@ManyToOne
	@JoinColumn(nullable=false)
	private BlogUser creator;
	
	/** List of comments that belong to this blog */
	@OneToMany(mappedBy="blogEntry", fetch=FetchType.LAZY, 
			cascade=CascadeType.PERSIST, orphanRemoval=true)
	@OrderBy("postedOn")
	private List<BlogComment> comments = new ArrayList<>();
	
	/** Creation date of this blog */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date createdAt;
	
	/** Date of the last modification */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	private Date lastModifiedAt;
	
	/** Title of this blog */
	@Column(length=200,nullable=false)
	private String title;
	
	/** Text of this blog */
	@Column(length=4096,nullable=false)
	private String text;
	
	/**
	 * Returns ID of this blog.
	 * 
	 * @return ID of this blog
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets ID of this blog.
	 * 
	 * @param id
	 *        id of the blog
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Returns list of comments that belong to this blog.
	 * 
	 * @return list of comments that belong to this blog
	 */
	public List<BlogComment> getComments() {
		return comments;
	}
	
	/**
	 * Sets list of comments to the specified one.
	 * 
	 * @param comments
	 *        new list of comments
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * Returns creation date of this blog.
	 * 
	 * @return creation date of this blog
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets creation date to the specified one.
	 * 
	 * @param createdAt
	 *        creation date of the blog
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Returns the date of the last modification of this blog.
	 * 
	 * @return the date of the last modification of this blog
	 */
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Sets last modification date to the specified one.
	 * 
	 * @param lastModifiedAt
	 *        last modification date
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * Returns title of this blog.
	 * 
	 * @return title of this blog
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets title of this blog to the specified one.
	 * 
	 * @param title
	 *        title of the blog
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns text of this blog.
	 * 
	 * @return text of this blog
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets text of this blog to the specified one.
	 * 
	 * @param text
	 *        text of the blog
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Returns creator of this blog.
	 * 
	 * @return creator of this blog
	 */
	public BlogUser getCreator() {
		return creator;
	}
	
	/**
	 * Sets creator of this blog to the specified one.
	 * 
	 * @param creator
	 *        creator of the blog
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}