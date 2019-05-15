package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerAnimator;
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
 * reflections of light rays on the spheres from multiple light sources. It uses
 * all available cores on this system for better performance.
 * 
 * @author Filip Husnjak
 */
public class RayCasterParallel2 {

	/**
	 * Precision when comparing two intersection points
	 */
	private static double PRECISION = Math.pow(10, -6);

	/**
	 * Program starts here, takes no arguments and shows the raytracing simulation
	 * with parallelization and rotation. Rotation is simulated with changable
	 * eye vector but constant viewUp and view vectors.
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), getIRayTracerAnimator(), 30, 30);
	}

	/**
	 * Returns implementation of {@link IRayTracerAnimator}.
	 * 
	 * @return implementation of {@link IRayTracerAnimator}
	 */
	private static IRayTracerAnimator getIRayTracerAnimator() {
		return new IRayTracerAnimator() {
			long time;

			@Override
			public void update(long deltaTime) {
				time += deltaTime;
			}

			@Override
			public Point3D getViewUp() { // fixed in time
				return new Point3D(0, 0, 10);
			}

			@Override
			public Point3D getView() { // fixed in time
				return new Point3D(-2, 0, -0.5);
			}

			@Override
			public long getTargetTimeFrameDuration() {
				return 100; // redraw scene each 150 milliseconds
			}

			@Override
			public Point3D getEye() { // changes in time
				double t = (double) time / 10000 * 2 * Math.PI;
				double t2 = (double) time / 5000 * 2 * Math.PI;
				double x = 50 * Math.cos(t);
				double y = 50 * Math.sin(t);
				double z = 30 * Math.sin(t2);
				return new Point3D(x, y, z);
			}
		};
	}

	/**
	 * Returns implementation of {@link IRayTracerProducer}.
	 * 
	 * @return implementation of {@link IRayTracerProducer}
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			
			private ForkJoinPool pool = new ForkJoinPool();
			
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
				Scene scene = RayTracerViewer.createPredefinedScene2();

				class Job extends RecursiveAction {

					private static final long serialVersionUID = 1L;

					private int yStart;

					private int yEnd;

					private int minHeight = height / 16;

					public Job(int yStart, int yEnd) {
						this.yEnd = yEnd;
						this.yStart = yStart;
					}

					@Override
					protected void compute() {
						if (cancel.get()) return;
						int h = yEnd - yStart;
						if (h <= minHeight) {
							computeDirect();
							return;
						}
						invokeAll(new Job(yStart, yStart + h / 2), new Job(yEnd - h + h / 2, yEnd));
					}

					private void computeDirect() {
						int offset = yStart * width;
						short[] rgb = new short[3];
						for (int y = yStart; y < yEnd; y++) {
							for (int x = 0; x < width; x++) {
								if (cancel.get()) return;
								Point3D screenPoint = screenCorner
										.add(xAxis.scalarMultiply(x * horizontal / (width - 1))
												.modifySub(yAxis.scalarMultiply(y * vertical / (height - 1))));
								Ray ray = Ray.fromPoints(eye, screenPoint);
								tracer(scene, ray, rgb);
								red[offset] = rgb[0] > 255 ? 255 : rgb[0];
								green[offset] = rgb[1] > 255 ? 255 : rgb[1];
								blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
								offset++;
							}
						}
					}

				}

				pool.invoke(new Job(0, height));
				if (cancel.get()) return;
				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
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
		RayIntersection closest = null;
		
		for (GraphicalObject object : scene.getObjects()) {
			RayIntersection intersection = object.findClosestRayIntersection(ray);
			if (intersection == null || !intersection.isOuter()) continue;
			
			if (closest == null || intersection.getDistance() < closest.getDistance()) {
				closest = intersection;
			}
		}
		
		return closest;
	}

}
