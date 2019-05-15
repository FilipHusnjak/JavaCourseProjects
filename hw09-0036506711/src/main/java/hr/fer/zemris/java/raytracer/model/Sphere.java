package hr.fer.zemris.java.raytracer.model;

/**
 * Representation of a sphere in 3D coordinate system. It has defined radius,
 * center and proper constants to determine intensity of light reflection and
 * diffusion.
 * 
 * @author Filip Husnjak
 */
public class Sphere extends GraphicalObject {
	
	/**
	 * Position of a center
	 */
	private Point3D center;
	
	/**
	 * Radius of the sphere
	 */
	private double radius;
	
	/**
	 * Diffusion constant for red color
	 */
	private double kdr;
	
	/**
	 * Diffusion constant for green color
	 */
	private double kdg;
	
	/**
	 * Diffusion constant for blue color
	 */
	private double kdb;
	
	/**
	 * Reflection constant for red color
	 */
	private double krr;
	
	/**
	 * Reflection constant for green color
	 */
	private double krg;
	
	/**
	 * Reflection constant for blue color
	 */
	private double krb;
	
	/**
	 * Describes how reflective sphere is. Bigger numbers represent better reflection
	 */
	private double krn;

	/**
	 * Constructs new {@code Sphere} with specified parameters.
	 * 
	 * @param center
	 *        position of center
	 * @param radius
	 *        radius of the sphere
	 * @param kdr
	 *        diffusion constant for red color
	 * @param kdg
	 *        diffusion constant for green color   
	 * @param kdb
	 *        diffusion constant for blue color
	 * @param krr
	 *        reflection constant for red color
	 * @param krg
	 *        reflection constant for green color
	 * @param krb
	 *        reflection constant for blue color
	 * @param krn
	 *        describes how reflective sphere is,
	 *        bigger numbers represent better reflection
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		super();
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	/**
	 * Returns position of center of this Sphere
	 * 
	 * @return position of center of this Sphere
	 */
	public Point3D getCenter() {
		return center;
	}

	/**
	 * Returns the radius of this Sphere.
	 * 
	 * @return the radius of this Sphere
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Point3D startCenter = ray.start.sub(center);
		double v1 = ray.direction.scalarProduct(startCenter);
		double discriminant = v1 * v1 - Math.pow(startCenter.norm(), 2) + radius * radius;
		if (discriminant < 0) return null;
		double distance = -v1 - Math.sqrt(discriminant);
		return new RayIntersectionImpl(ray.start.add(ray.direction.scalarMultiply(distance)),
				distance, true);
	}
	
	/**
	 * This class models intersection of a ray with sphere. Intersection is represented
	 * with the point in 3D coordinate system, distance from the start of the ray
	 * and flag which tells whether the intersection is outer or inner.
	 * 
	 * @author Filip Husnjak
	 */
	private class RayIntersectionImpl extends RayIntersection {

		/**
		 * Constructs {@link RayIntersection} object with specified parameters.
		 * 
		 * @param point
		 *        point of intersection
		 * @param distance
		 *        distance from the start of the ray to the point of intersection
		 * @param outer
		 *        flag that is set to {@code true} if the intersection was outer
		 */
		protected RayIntersectionImpl(Point3D point, double distance, boolean outer) {
			super(point, distance, outer);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getKdr() {
			return kdr;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getKdg() {
			return kdg;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getKdb() {
			return kdb;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getKrr() {
			return krr;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getKrg() {
			return krg;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getKrb() {
			return krb;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getKrn() {
			return krn;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Point3D getNormal() {
			return getPoint().sub(center).modifyNormalize();
		}

	}

}
