package hr.fer.zemris.java.blog.dao;

import java.util.List;

import hr.fer.zemris.java.blog.model.BlogComment;
import hr.fer.zemris.java.blog.model.BlogEntry;
import hr.fer.zemris.java.blog.model.BlogUser;

public interface DAO {

	/**
	 * Returns blog entry with the specified ID.
	 * 
	 * @param id
	 *        id of the blog entry to be returned
	 * @return {@link BlogEntry} with specified ID or {@code null} if there are
	 * 		   no blogs with specified id
	 * @throws DAOException if there was an error when reaching database
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;
	
	/**
	 * Returns {@link BlogUser} with the specified nickname.
	 * 
	 * @param nickname
	 *        nickname of the {@link BlogUser}
	 * @return {@link BlogUser} with given nickname or {@code null} if the given
	 *         nickname does not exist
	 * @throws DAOException if there was an error when reaching database
	 */
	public BlogUser getBlogUser(String nickname) throws DAOException;
	
	/**
	 * Returns list of {@link BlogUser} objects stored in database.
	 * 
	 * @return list of {@link BlogUser} objects stored in database
	 * @throws DAOException if there was an error when reaching database
	 */
	public List<BlogUser> getBlogUsers() throws DAOException;
	
	/**
	 * Stores the given {@link BlogUser} object in database.
	 * 
	 * @param user
	 *        user to be stored in database
	 * @throws DAOException if there was an error when reaching database
	 */
	public void saveBlogUser(BlogUser user) throws DAOException;
	
	/**
	 * Stores the given {@link BlogEntry} object in database.
	 * 
	 * @param entry
	 *        {@link BlogEntry} to be stored in database
	 * @throws DAOException if there was an error when reaching database
	 */
	public void saveBlogEntry(BlogEntry entry) throws DAOException;
		
	/**
	 * Stores the given {@link BlogComment} object in database.
	 * 
	 * @param comment
	 *        {@link BlogComment} to be stored in database
	 * @throws DAOException if there was an error when reaching database
	 */
	public void saveComment(BlogComment comment) throws DAOException;
}