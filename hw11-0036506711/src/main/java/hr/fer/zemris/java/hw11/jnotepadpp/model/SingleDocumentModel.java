package hr.fer.zemris.java.hw11.jnotepadpp.model;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Represents a model of a single document. Provides useful methods for retrieving
 * information about the current state of a document.
 * 
 * @author Filip Husnjak
 */
public interface SingleDocumentModel {
	
	/**
	 * Returns {@link JTextArea} component used by {@code this} document.
	 * 
	 * @return {@link JTextArea} component used by {@code this} document
	 */
	JTextArea getTextComponent();

	/**
	 * Returns path of this document. If document was not saved yet returned
	 * path is {@code null}.
	 * 
	 * @return path of this document. If document was not saved yet returned
	 * 		   path is {@code null}
	 */
	Path getFilePath();

	/**
	 * Sets the path of {@code this} document to the given one. Given path
	 * cannot be {@code null}.
	 * 
	 * @param path
	 *        new path of {@code this} document
	 * @throws NullPointerException if the given path is {@code null}
	 */
	void setFilePath(Path path);

	/**
	 * Returns {@code true} is this document was modified after last saving.
	 * 
	 * @return {@code true} is this document was modified after last saving
	 */
	boolean isModified();

	/**
	 * Sets modification status of this document to the given one.
	 * 
	 * @param modified
	 *        new modification status of {@code this} document
	 */
	void setModified(boolean modified);

	/**
	 * Adds listener to {@code this} document intern collection.
	 * 
	 * @param l
	 *        listener to be added
	 * @throws NullPointerException if the given listener is {@code null}
	 */
	void addSingleDocumentListener(SingleDocumentListener l);

	/**
	 * Removes given listener from {@code this} document.
	 * 
	 * @param l
	 *        listener to be removed
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
	
}
