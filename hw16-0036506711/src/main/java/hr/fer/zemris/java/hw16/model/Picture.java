package hr.fer.zemris.java.hw16.model;

import java.util.Set;

/**
 * Class which holds image information when loaded.
 * 
 * @author Filip Husnjak
 */
public class Picture {
	
	/** Picture ID */
	private String id;

	/** Picture description */
	private String description;
	
	/** Picture tags */
	private String[] tags;

	/**
	 * Constructs new {@link Picture} class with given parameters.
	 * 
	 * @param id
	 *        id of the picture
	 * @param description
	 *        description of the picture
	 * @param tags
	 *        tags that belong to the picture
	 */
	public Picture(String id, String description, Set<String> tags) {
		this.id = id;
		this.description = description;
		this.tags = tags.toArray(String[]::new);
	}

	/**
	 * Returns ID of this picture.
	 * 
	 * @return ID of this picture
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns description of this picture.
	 * 
	 * @return description of this picture
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns tags of this picture.
	 * 
	 * @return tags of this picture
	 */
	public String[] getTags() {
		return tags;
	}
	
}
