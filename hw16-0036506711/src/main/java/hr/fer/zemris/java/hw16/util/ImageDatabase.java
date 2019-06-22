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

public class ImageDatabase {
	
	private synchronized static List<String> getLines(HttpServletRequest request) 
			throws IOException {
		return Files.readAllLines(Paths.get(
				request.getServletContext().getRealPath("/WEB-INF/opisnik.txt")));
	}

	public static String[] loadTags(HttpServletRequest request) throws IOException {
		List<String> lines = getLines(request);
		Set<String> tags = new HashSet<>();
		for (int i = 2; i < lines.size(); i += 3) {
			Arrays.stream(lines.get(i).split(",")).map(String::strip).forEach(tags::add);
		}
		return tags.toArray(String[]::new);
	}
	
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
	
	public static BufferedImage createThumbnail(BufferedImage image) {
		Image tmp = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
		return resized;
	}
	
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
	
	public synchronized static BufferedImage loadImageWithId(HttpServletRequest req, String id) 
			throws IOException {
		return ImageIO.read(Files.newInputStream(
				Paths.get(req.getServletContext().getRealPath("/WEB-INF/slike/" + id))));
	}
	
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
