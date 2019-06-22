package hr.fer.zemris.java.blog.servleti;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogComment;
import hr.fer.zemris.java.blog.model.BlogCommentForm;
import hr.fer.zemris.java.blog.model.BlogEntry;
import hr.fer.zemris.java.blog.model.BlogUser;
import hr.fer.zemris.java.util.Util;
import hr.fer.zemris.java.blog.model.BlogEntryForm;

/**
 * Represents servlet which performs operations triggered by /servleti/author/*
 * path, both GET and POST requests.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/servleti/author/*")
public class AuthorServlet extends HttpServlet {

	private static final long serialVersionUID = 2284001583041160634L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		processGetRequest(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		processPostRequest(req, resp);
	}

	/**
	 * Processes GET request. GET request can be triggered in 4 cases:
	 * <ul>
	 * 	<li> User wants to see list of blogs for specified user
	 *  <li> User wants to add new Blog
	 *  <li> User wants to read someone's blog
	 *  <li> User wants to edit its blog
	 * </ul>
	 * 
	 * @param req
	 *        request object used for processing
	 * @param resp
	 *        request object used for processing
	 * @throws ServletException if there was an error executing this servlet
	 * @throws IOException if there was an error communicating with database
	 */
	private void processGetRequest(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String info = req.getPathInfo();
		if (info == null) {
			Util.sendError(req, resp, 404);
			return;
		}
		// Get list of path parts
		List<String> path = Util.getPathList(info);
		if (path.size() > 3 || path.size() < 1) {
			Util.sendError(req, resp, 404);
			return;
		}
		BlogUser requestUser = DAOProvider.getDAO().getBlogUser(path.get(0));
		if (requestUser == null) {
			Util.sendError(req, resp, 404);
			return;
		}
		// Check if user provided only a nickname
		if (path.size() == 1) {
			dispatchToListEntries(req, resp, requestUser);
		} else if (path.size() == 2) {
			// Check if user wants to add new entry, if not its assumed that the user
			// wants to read blog content and that he provided proper blog ID
			if (path.get(1).equals("new")) {
				addNewEntry(req, resp, requestUser);
			} else {
				try {
					// Displays blog with provided ID
					readEntryContent(req, resp, requestUser, Long.valueOf(path.get(1)));
				} catch (NumberFormatException e) {
					Util.sendError(req, resp, 404);
				}
			}
		// If user wants to edit blog, it is expected that proper ID is provided
		} else if (path.get(1).equals("edit")) {
			try {
				editBlogEntry(req, resp, requestUser, Long.valueOf(path.get(2)));
			} catch (NumberFormatException e) {
				Util.sendError(req, resp, 404);
			}
		} else {
			Util.sendError(req, resp, 404);
		}
	}

	/**
	 * Processes the POST request. It can be triggered in 3 occasions:
	 * <ul>
	 * 	<li> User wants to save new blog or cancel addition of the new blog
	 *  <li> User wants to save edited blog or cancel editing
	 *  <li> User wants to add a comment
	 * </ul>
	 * 
	 * @param req
	 *        request object used for processing
	 * @param resp
	 *        request object used for processing
	 * @throws ServletException if there was an error executing this servlet
	 * @throws IOException if there was an error communicating with database
	 */
	private void processPostRequest(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String method = req.getParameter("method");
		// Check whether method is 'Save', if not just redirect user to main page
		if(!"Save".equals(method)) {
			redirectToMainPage(req, resp);
			return;
		}
		String info = req.getPathInfo();
		if (info == null) {
			Util.sendError(req, resp, 404);
			return;
		}
		List<String> path = Util.getPathList(info);
		if (path.size() < 2) {
			Util.sendError(req, resp, 400);
			return;
		}
		// If user requested adding or editing a blog process it, otherwise its assumed
		// user wants to add new comment and proper id is provided.
		if (path.get(1).equals("new") || path.get(1).equals("edit")) {
			processSaveBlog(req, resp);
		} else {
			try {
				processAddComment(req, resp, Long.valueOf(path.get(1)));
			} catch (NumberFormatException e) {
				Util.sendError(req, resp, 400);
				return;
			}
		}
	}
	
	/**
	 * Adds comment to the blog with specified ID. If there was an error adding
	 * the comment error page is requested.
	 * 
	 * @param req
	 *        request object used for processing
	 * @param resp
	 *        request object used for processing
	 * @param id
	 *        ID of the blog whose comment is to be added
	 * @throws ServletException if there was an error executing this servlet
	 * @throws IOException if there was an error communicating with database
	 */
	private void processAddComment(HttpServletRequest req, HttpServletResponse resp,
			Long id) throws ServletException, IOException {
		BlogEntry entry = getBlogEntry(getAuthor(req), id);
		if (entry == null) {
			Util.sendError(req, resp, 400);
			return;
		}
		BlogCommentForm form = new BlogCommentForm();
		Util.checkForm(req, form);
		if (form.hasErrors()) {
			dispatchToReadEntry(req, resp, entry, null, form);
			return;
		}
		BlogComment comment = new BlogComment();
		form.fillRecord(comment);
		comment.setBlogEntry(entry);
		comment.setPostedOn(new Date());
		DAOProvider.getDAO().saveComment(comment);
		redirectToBlogEntry(req, resp, getAuthor(req), entry);
	}

	/**
	 * Checks the user input and if everything is ok and user has rights the new
	 * blog is saved.
	 * 
	 * @param req
	 *        request object used for processing
	 * @param resp
	 *        request object used for processing
	 * @throws ServletException if there was an error executing this servlet
	 * @throws IOException if there was an error communicating with database
	 */
	private void processSaveBlog(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		BlogEntryForm form = new BlogEntryForm();
		Util.checkForm(req, form);
		if (form.hasErrors()) {
			dispatchToNewEntry(req, resp, form);
			return;
		}
		saveBlogEntry(req, resp, form);
	}

	/**
	 * Redirects user to the main page.
	 * 
	 * @param req
	 *        request object used for processing
	 * @param resp
	 *        request object used for processing
	 * @throws ServletException if there was an error executing this servlet
	 * @throws IOException if there was an error communicating with database
	 */
	private void redirectToMainPage(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		BlogUser author = getAuthor(req);
		if (author == null) {
			Util.sendError(req, resp, 400);
			return;
		}
		resp.sendRedirect(req.getServletContext().getContextPath() +
				req.getServletPath() + "/" + author.getNick());
	}
	
	/**
	 * If URL is valid and current user has proper rights its determined whether
	 * existing blog should be updated or created and then proper method is
	 * executed.
	 * 
	 * @param req
	 *        request object used for processing
	 * @param resp
	 *        request object used for processing
	 * @throws ServletException if there was an error executing this servlet
	 * @throws IOException if there was an error communicating with database
	 */
	private void saveBlogEntry(HttpServletRequest req, HttpServletResponse resp, 
			BlogEntryForm form) throws ServletException, IOException {
		BlogUser author = getAuthor(req);
		if (author == null || !Util.checkSession(req, author)) {
			Util.sendError(req, resp, 400);
			return;
		}
		Long id;
		try {
			id = getBlogId(req.getPathInfo());
		} catch (Exception e) {
			Util.sendError(req, resp, 400);
			return;
		}
		BlogEntry entry;
		if (id == null) {
			entry = newBlogEntry(author, form);
		} else {
			entry = updateBlogEntry(author, id, form);
		}
		if (entry == null) {
			Util.sendError(req, resp, 400);
			return;
		}
		redirectToBlogEntry(req, resp, author, entry);
	}
	
	/**
	 * Redirects user to proper blog entry.
	 * 
	 * @param req
	 *        request object used for processing
	 * @param resp
	 *        request object used for processing
	 * @param author
	 *        author of the blog
	 * @param entry
	 *        blog which should be opened
	 * @throws IOException if there was an error communicating with database
	 */
	private void redirectToBlogEntry(HttpServletRequest req, HttpServletResponse resp,
			BlogUser author, BlogEntry entry) throws IOException {
		resp.sendRedirect(req.getServletContext().getContextPath() + 
				req.getServletPath() + "/" + author.getNick() + "/" + entry.getId());
	}

	/**
	 * Returns author from the request URL.

	 * @return author from the request URL
	 */
	private BlogUser getAuthor(HttpServletRequest req) {
		String info = req.getPathInfo();
		if (info == null) return null;
		List<String> path = Util.getPathList(info);
		if (path.size() < 1) return null;
		return DAOProvider.getDAO().getBlogUser(path.get(0));
	}

	/**
	 * Updates blog entry with the given id using the given form.
	 * 
	 * @param author
	 *        author of the blog
	 * @param id
	 *        id of blog to be updated
	 * @param form
	 *        form used to fetch data from the user
	 * @return updated entry or {@code null} if blog could not be found
	 */
	private BlogEntry updateBlogEntry(BlogUser author, Long id, BlogEntryForm form) {
		BlogEntry entry = getBlogEntry(author, id);
		if (entry == null) {
			return null;
		}
		form.fillRecord(entry);
		entry.setLastModifiedAt(new Date());
		return entry;
	}

	/**
	 * Creates new blog for the specified author using given form.
	 * 
	 * @param author
	 *        author of the new blog
	 * @param form
	 *        form used to fetch data from the user
	 * @return new blog for the specified author
	 */
	private BlogEntry newBlogEntry(BlogUser author, BlogEntryForm form) {
		BlogEntry entry = new BlogEntry();
		form.fillRecord(entry);
		entry.setCreatedAt(new Date());
		entry.setCreator(author);
		DAOProvider.getDAO().saveBlogEntry(entry);
		return entry;
	}

	/**
	 * Returns blog id from the given info string.
	 * 
	 * @param info
	 *        string from which info ID is extracted
	 * @return blog id extracted from the given info string
	 */
	private Long getBlogId(String info) {
		if (info == null) throw new IllegalArgumentException();
		List<String> path = Util.getPathList(info);
		if (path.size() < 2) throw  new IllegalArgumentException();
		if (path.size() == 2) return null;
		return Long.valueOf(path.get(2));
	}
	
	/**
	 * Reads blog and shows proper page to the user.
	 * 
	 * @param user
	 *        user whose blog should be read
	 * @param id
	 *        id of the blog
	 * @throws ServletException if there was an error executing this servlet
	 * @throws IOException if there was an error communicating with database
	 */
	private void readEntryContent(HttpServletRequest req, HttpServletResponse resp, 
			BlogUser user, Long id) throws ServletException, IOException {
		BlogEntry entry = getBlogEntry(user, id);
		if (entry == null) {
			Util.sendError(req, resp, 404);
			return;
		}
		dispatchToReadEntry(req, resp, entry, user, new BlogCommentForm());
	}
	
	/**
	 * Dispatches the request to the given blog page.
	 *
	 * @param entry
	 *        blog to be opened
	 * @param user
	 *        user used for data
	 * @param form
	 *        form used to display comments form
	 * @throws ServletException if there was an error executing this servlet
	 * @throws IOException if there was an error communicating with database
	 */
	private void dispatchToReadEntry(HttpServletRequest req, HttpServletResponse resp, 
			BlogEntry entry, BlogUser user, BlogCommentForm form) throws ServletException, IOException {
		Util.provideLoggedUserInfo(req);
		req.setAttribute("formular", form);
		req.setAttribute("entry", entry);
		req.setAttribute("comments", entry.getComments());
		req.setAttribute("logged", user == null ? false : Util.checkSession(req, user));
		req.getRequestDispatcher("/WEB-INF/pages/readEntry.jsp").forward(req, resp);
	}

	/**
	 * Fills and opens the form for editing blog.
	 *
	 * @param user
	 *        user which requested editing blog
	 * @param id
	 *        id of the blog to be edited
	 * @throws ServletException if there was an error executing this servlet
	 * @throws IOException if there was an error communicating with database
	 */
	private void editBlogEntry(HttpServletRequest req, HttpServletResponse resp, 
			BlogUser user, Long id) throws ServletException, IOException {
		if (!Util.checkSession(req, user)) {
			Util.sendError(req, resp, 404);
			return;
		}
		fillAndOpenForm(req, resp, getBlogEntry(user, id));
	}
	
	/**
	 * Returns blog with the specified ID and created by the given user. If the
	 * blog does not exist {@code null} is returned.
	 * 
	 * @param user
	 *        user whose blog is to be returned
	 * @param id
	 *        id of the blog to be returned
	 * @return blog with the specified ID and created by the given user. If the
	 *         blog does not exist {@code null} is returned
	 */
	private BlogEntry getBlogEntry(BlogUser user, Long id) {
		for (BlogEntry entry: user.getBlogEntries()) {
			if (entry.getId().equals(id)) {
				return entry;
			}
		}
		return null;
	}

	/**
	 * Opens form for adding new blog which is being created by the given user.
	 * 
	 * @param user
	 *        user which requested creation of the new blog
	 * @throws ServletException if there was an error executing this servlet
	 * @throws IOException if there was an error communicating with database
	 */
	private void addNewEntry(HttpServletRequest req, HttpServletResponse resp, BlogUser user) 
			throws ServletException, IOException {
		if (!Util.checkSession(req, user)) {
			Util.sendError(req, resp, 404);
			return;
		}
		fillAndOpenForm(req, resp, new BlogEntry());
	}
	
	/**
	 * Dispatches the request to the page containing form for creating new blog.
	 * 
	 * @param form
	 *        form used to fill page
	 * @throws ServletException if there was an error executing this servlet
	 * @throws IOException if there was an error communicating with database
	 */
	private void dispatchToNewEntry(HttpServletRequest req, HttpServletResponse resp, 
			BlogEntryForm form) throws ServletException, IOException {
		Util.provideLoggedUserInfo(req);
		req.setAttribute("formular", form);
		req.getRequestDispatcher("/WEB-INF/pages/newEntry.jsp").forward(req, resp);
	}

	/**
	 * Dispatches the request to the page which lists all the blogs of specified user.
	 * 
	 * @param user
	 *        user whose blogs are to be listed
	 * @throws ServletException if there was an error executing this servlet
	 * @throws IOException if there was an error communicating with database
	 */
	private void dispatchToListEntries(HttpServletRequest req, HttpServletResponse resp, BlogUser user) 
			throws ServletException, IOException {
		Util.provideLoggedUserInfo(req);
		req.setAttribute("user", user);
		req.setAttribute("logged", Util.checkSession(req, user));
		req.setAttribute("entries", user.getBlogEntries());
		req.getRequestDispatcher("/WEB-INF/pages/listEntries.jsp").forward(req, resp);
	}
	
	/**
	 * Fills and opens form for adding new blog or editing existing one.
	 * 
	 * @param entry
	 *        blog used to fill page 
	 * @throws ServletException if there was an error executing this servlet
	 * @throws IOException if there was an error communicating with database
	 */
	private void fillAndOpenForm(HttpServletRequest req, HttpServletResponse resp, BlogEntry entry) 
			throws ServletException, IOException {
		if (entry == null) {
			Util.sendError(req, resp, 404);
			return;
		}
		BlogEntryForm form = new BlogEntryForm();
		form.fillFromRecord(entry);
		dispatchToNewEntry(req, resp, form);
	}
	
}
