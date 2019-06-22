package hr.fer.zemris.java.blog.model;

import javax.servlet.http.HttpServletRequest;

/**
 * Represents formular for blogs.
 * 
 * @author Filip Husnjak
 */
public class BlogEntryForm extends Formular<BlogEntry> {
	
	/** Title of the blog */
	private String title;
	
	/** Text of the blog */
	private String text;
	
	@Override
	public void fillRecord(BlogEntry entry) {
		entry.setTitle(title);
		entry.setText(text);
	}
	
	@Override
	public void fillFromRequest(HttpServletRequest req) {
		title = set(req.getParameter("title"));
		text = set(req.getParameter("text"));
	}
	
	@Override
	public void fillFromRecord(BlogEntry entry) {
		title = set(entry.getTitle());
		text = set(entry.getText());
	}
	
	@Override
	public void validate() {
		errors.clear();
		if (title.isEmpty()) {
			errors.put("title", "Title name is obligatory!");
		}
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}
	
	
	
}
