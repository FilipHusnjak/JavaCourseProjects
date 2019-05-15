package hr.fer.zemris.java.raytracer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Program that uses raycasting algorithm and Phong reflection model to simulate
 * reflections of light rays on the spheres from multiple light sources.
 * 
 * @author Filip Husnjak
 */
public class RayCaster {

	/**
	 * Precision when comparing two intersection points
	 */
	private static double PRECISION = Math.pow(10, -6);

	/**
	 * Program starts here, takes no arguments and shows the raytracing simulation
	 * without parallelization.
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Returns implementation of {@link IRayTracerProducer}.
	 * 
	 * @return implementation of {@link IRayTracerProducer}
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer, AtomicBoolean cancel) {
				System.out.println("Započinjem izračune...");
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];
				Point3D zAxis = eye.sub(view).modifyNormalize();
				Point3D xAxis = viewUp.vectorProduct(zAxis).modifyNormalize();
				Point3D yAxis = zAxis.vectorProduct(xAxis);
				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2))
						.modifyAdd(yAxis.scalarMultiply(vertical / 2));
				Scene scene = RayTracerViewer.createPredefinedScene();
				short[] rgb = new short[3];
				int offset = 0;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						if (cancel.get()) return;
						Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply(x * horizontal / (width - 1))
								.modifySub(yAxis.scalarMultiply(y * vertical / (height - 1))));
						Ray ray = Ray.fromPoints(eye, screenPoint);
						tracer(scene, ray, rgb);
						if (check(x) && check(y)) {
							System.out.println("Informacije za točku x=" + x + ", y=" + y);
							System.out.println("Screen-point: " + pointToString(screenPoint));
							System.out.println("Ray: start=" + pointToString(ray.start) + ", direction="
									+ pointToString(ray.direction));
							System.out.println("RGB = " + Arrays.toString(rgb));
							System.out.println();
						}
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						offset++;
					}
				}
				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}

	/**
	 * Returns point parameters formatted to string. Helper method for writing
	 * information to the console.
	 * 
	 * @param p
	 *        point to be formatted
	 * @return point parameters formatted to string
	 */
	private static String pointToString(Point3D p) {
		return "(" + p.x + ", " + p.y + ", " + p.z + ")";
	}

	/**
	 * Returns {@code true} if the given value is 0, 166, 250, 333 or 499.
	 * 
	 * @param v
	 *        value to be checked
	 * @return {@code true} if the given value is 0, 166, 250, 333 or 499
	 */
	private static boolean check(int v) {
		return v == 0 || v == 166 || v == 250 || v == 333 || v == 499;
	}

	/**
	 * Determines the closest intersection of a given ray with sphere.
	 * Then for each light source in the scene this method determines if that
	 * light source has any impact on the reflection and if it does rgb array is
	 * filled with proper values based on Phond reflection model.
	 * 
	 * @param scene
	 *        scene of the program
	 * @param ray
	 *        ray from the observer thought the pixel on the screen
	 * @param rgb
	 *        array to be filled with proper values that represent intensity
	 *        of each color
	 */
	protected static void tracer(Scene scene, Ray ray, short[] rgb) {
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;
		RayIntersection closest = findClosestIntersection(scene, ray);
		if (closest == null) {
			return;
		}
		rgb[0] = 15;
		rgb[1] = 15;
		rgb[2] = 15;
		for (LightSource source : scene.getLights()) {
			Ray sourceRay = Ray.fromPoints(source.getPoint(), closest.getPoint());
			RayIntersection intersection = findClosestIntersection(scene, sourceRay);
			if (intersection != null
					&& source.getPoint().sub(closest.getPoint()).norm() > PRECISION + intersection.getDistance()) {
				continue;
			}
			if (intersection == null) intersection = closest;
			Point3D normal = intersection.getNormal();
			double scalarProductDiffuse = -sourceRay.direction.scalarProduct(normal);
			rgb[0] += source.getR() * intersection.getKdr() * scalarProductDiffuse;
			rgb[1] += source.getG() * intersection.getKdg() * scalarProductDiffuse;
			rgb[2] += source.getB() * intersection.getKdb() * scalarProductDiffuse;

			Point3D rm = normal.scalarMultiply(-2 * sourceRay.direction.scalarProduct(normal))
					.modifyAdd(sourceRay.direction).modifyNormalize();
			double cos = rm.scalarProduct(ray.start.sub(intersection.getPoint()).modifyNormalize());
			if (cos <= 0) continue;
			double scalarProductReflect = Math.pow(cos, intersection.getKrn());
			rgb[0] += source.getR() * intersection.getKrr() * scalarProductReflect;
			rgb[1] += source.getG() * intersection.getKrg() * scalarProductReflect;
			rgb[2] += source.getB() * intersection.getKrb() * scalarProductReflect;
		}
	}

	/**
	 * Finds and returns the closest intersection of a ray with any object in the scene.
	 * If there are no intersections {@code null} is returned.
	 * 
	 * @param scene
	 *        scene of the program
	 * @param ray
	 *        ray whose intersections are to be found
	 * @return returns the closest intersection of a ray with any object in the scene
	 */
	private static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
		RayIntersection intersection = null;
		for (GraphicalObject object : scene.getObjects()) {
			if (intersection == null) {
				intersection = object.findClosestRayIntersection(ray);
			} else {
				RayIntersection tmp = object.findClosestRayIntersection(ray);
				intersection = tmp != null && tmp.getDistance() < intersection.getDistance() ? tmp : intersection;
			}
		}
		return intersection;
	}

}
