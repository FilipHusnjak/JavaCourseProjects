package hr.fer.zemris.java.hw11.jnotepadpp.model;

import java.nio.file.Path;

/**
 * Represents model that holds multiple instances of {@link SingleDocumentModel}
 * in its internal collection. It also provides useful methods for manipulating
 * with those documents.
 * 
 * @author Filip Husnjak
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	
	/**
	 * Creates new document, creates tab, switches to it and adds new
	 * instance of {@link SingleDocumentModel} to its internal collection.
	 * Returns the created document.
	 * 
	 * @return the created instance of {@link SingleDocumentModel}
	 */
	SingleDocumentModel createNewDocument();

	/**
	 * Returns the currently active document. If there is no currently active
	 * document this method returns {@code null}.
	 * 
	 * @return the currently active document
	 */
	SingleDocumentModel getCurrentDocument();

	/**
	 * Loads file with a given path from disk, switches tab and creates new
	 * instance of {@link SingleDocumentModel} that it then returned.
	 * 
	 * @param path
	 *        path of a file to load
	 * @return new instance of {@link SingleDocumentModel}
	 * @throws NullPointerException if the given path is {@code null}
	 * @throws IllegalArgumentException if the given file does not exist or cannot
	 *         be opened
	 */
	SingleDocumentModel loadDocument(Path path);

	/**
	 * Saves the given {@link SingleDocumentModel} using newPath if not {@code null}
	 * otherwise document is saved using path associated from document.
	 * 
	 * @param model
	 *        document to be saved
	 * @param newPath
	 *        path of the file
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);

	/**
	 * Removes the tab representing the given document and removes the given
	 * document from its internal collection. The method does not check
	 * whether the document is modified or not.
	 * 
	 * @param model
	 *        document to be closed
	 */
	void closeDocument(SingleDocumentModel model);

	/**
	 * Adds the given listener to the internal collection of {@code this}
	 * model.
	 * 
	 * @param l
	 *        listener to be added
	 * @throws NullPointerException if the given listener is {@code null}
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Removes the given listener from this {@link MultipleDocumentModel}.
	 * 
	 * @param l
	 *        listener to be removed
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Returns number of documents in this {@link MultipleDocumentModel}.
	 * 
	 * @return number of documents in this {@link MultipleDocumentModel}
	 */
	int getNumberOfDocuments();
	
	/**
	 * Switches tab to the given document.
	 * 
	 * @param model
	 *        document to be activated
	 * @throws IllegalArgumentException if the given document does not exist
	 */
	void activateDocument(SingleDocumentModel model);
	
	/**
	 * Returns document at the specified index.
	 * 
	 * @param index
	 *        index of the document to be returned
	 * @return document at the specified index
	 * @throws IndexOutOfBoundsException if the given index is less than 0 or
	 * 		   greater than number of documents - 1.
	 */
	SingleDocumentModel getDocument(int index);
	
}
