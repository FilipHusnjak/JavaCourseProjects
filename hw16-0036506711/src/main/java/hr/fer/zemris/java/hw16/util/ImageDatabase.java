package hr.fer.zemris.java.hw16.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw16.model.Picture;

/**
 * Represents database which loads images and their information. Also is able
 * to return images for more specified request like images with some id/name or
 * images with given tag.
 * 
 * @author Filip Husnjak
 */
public class ImageDatabase {
	
	/**
	 * Loads lines from txt file that represent description of each image.
	 * 
	 * @param request
	 *        request object used to get real path on disk to the proper file
	 * @return List of lines read from the file
	 * @throws IOException if an I/O error occurs.
	 */
	private synchronized static List<String> getLines(HttpServletRequest request) 
			throws IOException {
		return Files.readAllLines(Paths.get(
				request.getServletContext().getRealPath("/WEB-INF/opisnik.txt")));
	}

	/**
	 * Loads all tags that images belong to. There are no duplicates in result array.
	 * 
	 * @param request
	 *        request object used to get real path on disk to the proper file
	 * @return array of tags
	 * @throws IOException if an I/O error occurs.
	 */
	public static String[] loadTags(HttpServletRequest request) throws IOException {
		List<String> lines = getLines(request);
		Set<String> tags = new HashSet<>();
		for (int i = 2; i < lines.size(); i += 3) {
			Arrays.stream(lines.get(i).split(",")).map(String::strip).forEach(tags::add);
		}
		return tags.toArray(String[]::new);
	}
	
	/**
	 * Returns array of image ids that belong to the given tag.
	 * 
	 * @param request
	 *        request object used to get real path on disk to the proper file
	 * @param tag
	 *        tag used for search
	 * @return array of image ids that belong to the given tag
	 * @throws IOException if an I/O error occurs.
	 */
	public static String[] imagesForTag(HttpServletRequest request, String tag) 
			throws IOException {
		List<String> lines = getLines(request);
		Set<String> images = new HashSet<>();
		for (int i = 0; i < lines.size(); i += 3) {
			if (lines.get(i + 2).contains(tag)) {
				images.add(lines.get(i).strip());
			}
		}
		return images.toArray(String[]::new);
	}
	
	/**
	 * Creates smaller image from the given one. New images has size of 150x150.
	 * 
	 * @param image
	 *        images whose thumbnail is to be created
	 * @return thumbnail of the given image
	 */
	private static BufferedImage createThumbnail(BufferedImage image) {
		Image tmp = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
		return resized;
	}
	
	/**
	 * Returns {@link Picture} object with specified ID.
	 * 
	 * @param request
	 *        request object used to get real path on disk to the proper file
	 * @param id
	 *        id of the object to be returned
	 * @return {@link Picture} object with specified ID
	 * @throws IOException if an I/O error occurs.
	 */
	public static Picture loadPictureForId(HttpServletRequest request, String id) 
			throws IOException {
		List<String> lines = getLines(request);
		Set<String> tags = new HashSet<>();
		for (int i = 0; i < lines.size(); i += 3) {
			if (lines.get(i).strip().equals(id)) {
				Arrays.stream(lines.get(i + 2).split(",")).map(String::strip).forEach(tags::add);
				return new Picture(id, lines.get(i + 1), tags);
			}
		}
		return null;
	}
	
	/**
	 * Loads image with the given ID.
	 * 
	 * @param request
	 *        request object used to get real path on disk to the proper file
	 * @param id
	 *        id of the image to be returned
	 * @return image with specified ID
	 * @throws IOException if an I/O error occurs.
	 */
	public synchronized static BufferedImage loadImageWithId(HttpServletRequest req, String id) 
			throws IOException {
		return ImageIO.read(Files.newInputStream(
				Paths.get(req.getServletContext().getRealPath("/WEB-INF/slike/" + id))));
	}
	
	/**
	 * Loads thumbnail with specified ID. If thumbnail with specified ID does not
	 * exist it is created first time its ID is requested.
	 * 
	 * @param req
	 *        request object used to get real path on disk to the proper file
	 * @param id
	 *        id of the thumbnail to be returned
	 * @return thumbnail with specified ID
	 * @throws IOException if an I/O error occurs.
	 */
	public synchronized static BufferedImage loadThumbnailWithId(HttpServletRequest req, String id) 
			throws IOException {
		Path thumbnails = Paths.get(req.getServletContext().getRealPath("/WEB-INF/thumbnails"));
		if (!Files.exists(thumbnails)) {
			Files.createDirectory(thumbnails);
		}
		Path thumbnailPath = thumbnails.resolve(id);
		BufferedImage thumbnail;
		if (!Files.isRegularFile(thumbnailPath)) {
			BufferedImage image = ImageIO.read(Files.newInputStream(
					Paths.get(req.getServletContext().getRealPath("/WEB-INF/slike/" + id))));
			thumbnail = ImageDatabase.createThumbnail(image);
			ImageIO.write(thumbnail, "png", Files.newOutputStream(thumbnailPath));
		} else {
			thumbnail = ImageIO.read(Files.newInputStream(thumbnailPath));
		}
		return thumbnail;
	}
	
}
