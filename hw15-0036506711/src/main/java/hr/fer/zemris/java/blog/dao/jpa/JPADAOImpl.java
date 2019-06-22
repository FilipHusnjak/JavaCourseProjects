package hr.fer.zemris.java.blog.dao.jpa;

import java.util.List;

import hr.fer.zemris.java.blog.dao.DAO;
import hr.fer.zemris.java.blog.dao.DAOException;
import hr.fer.zemris.java.blog.model.BlogComment;
import hr.fer.zemris.java.blog.model.BlogEntry;
import hr.fer.zemris.java.blog.model.BlogUser;

public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
	}

	@Override
	public BlogUser getBlogUser(String nickname) throws DAOException {
		List<BlogUser> users = JPAEMProvider.getEntityManager()
				.createNamedQuery("BlogUser.login", BlogUser.class)
					.setParameter("nick", nickname).getResultList();
		return users.size() > 0 ? users.get(0) : null;
	}

	@Override
	public void saveBlogUser(BlogUser user) throws DAOException {
		JPAEMProvider.getEntityManager().persist(user);
	}

	@Override
	public void saveBlogEntry(BlogEntry entry) throws DAOException {
		JPAEMProvider.getEntityManager().persist(entry);
	}

	@Override
	public void saveComment(BlogComment comment) throws DAOException {
		JPAEMProvider.getEntityManager().persist(comment);
	}

	@Override
	public List<BlogUser> getBlogUsers() throws DAOException {
		return JPAEMProvider.getEntityManager()
				.createNamedQuery("BlogUser.list", BlogUser.class).getResultList();
	}

}