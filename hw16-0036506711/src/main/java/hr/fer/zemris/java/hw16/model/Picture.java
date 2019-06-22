package hr.fer.zemris.java.hw16.model;

import java.util.Set;

public class Picture {
	
	private String id;

	private String description;
	
	private String[] tags;

	public Picture(String id, String description, Set<String> tags) {
		this.id = id;
		this.description = description;
		this.tags = tags.toArray(String[]::new);
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String[] getTags() {
		return tags;
	}
	
}
