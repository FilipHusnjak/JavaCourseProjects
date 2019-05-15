package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Program that calculates and draws Newton fractals. Program expects at least 2 arguments
 * through the console, which represent roots of complex polynomial. Legal complex
 * numbers are as followed: 1, -1 + i0, i, 0 - i1 etc. Complex part has form
 * 'ib' and b is positive real number. If no b is given its assumed that b = 1.
 * In order to trigger drawing you have to write 'done' in the console.
 * 
 * @author Filip Husnjak
 */
public class Newton {

	/**
	 * Program starts here and expects at least 2 complex numbers to be given
	 * through the console. After 'done' is written program will start calculating
	 * and drawing.
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.print("Please enter at least two roots, one root per line. Enter 'done' when done.\nRoot 1> ");
		List<Complex> roots = new ArrayList<>();
		try (Scanner sc = new Scanner(System.in)) {
			int counter = 1;
			while (sc.hasNext()) {
				String line = sc.nextLine();
				if (line.equals("done")) {
					if (counter <= 2) {
						throw new IllegalArgumentException("You have to provide at least 2 roots!");
					}
					break;
				}
				try {
					roots.add(Complex.parse(line));
					counter++;
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}
				System.out.print("Root " + counter + "> ");
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			return;
		}
		FractalViewer.show(new FractalProducer(new ComplexRootedPolynomial(Complex.ONE,
				roots.toArray(new Complex[0]))));
	}
	
	/**
	 * Implementation of {@link #IFractalProducer} with proper implementation of
	 * produce method. Produce method has parallel implementation and it splits
	 * the job into {@code 8 * availableProcessors()} parts that are then added
	 * to the ThreadPool which is created using ExecutorService class.
	 * 
	 * @author Filip Husnjak
	 */
	private static class FractalProducer implements IFractalProducer {
		
		/**
		 * Minimum distance between old and newly created complex number using Newtons formula
		 */
		private static final double convergenceTreshold = 0.001;
		
		/**
		 * Minimum distance between closes root and given complex number
		 */
		private static final double rootTreshold = 0.002;
		
		/**
		 * Maximum number of iterations when trying to converge
		 */
		private static final int maxIter = 16 * 16 * 16;
		
		/**
		 * Rooted polynomial given through constructor
		 */
		private final ComplexRootedPolynomial rootedPolynomial;
		
		/**
		 * Thread pool whose threads are daemon
		 */
		private ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), 
				(r) -> {
					Thread t = new Thread(r);
					t.setDaemon(true);
					return t;
				});
		
		/**
		 * Constructs new {@code FractalProducer} with specified rootedPolynomial.
		 * 
		 * @param rootedPolynomial
		 *        rooted version of a polynomial whose closest roots are to be calculated
		 * @throws NullPointerException if the given polynomial is {@code null}
		 */
		public FractalProducer(ComplexRootedPolynomial rootedPolynomial) {
			this.rootedPolynomial = Objects.requireNonNull(rootedPolynomial, "Given polynomial cannot be null!");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			ComplexPolynomial normalPolynomial = rootedPolynomial.toComplexPolynom();
			ComplexPolynomial derivedPolynomial = normalPolynomial.derive();
			short[] data = new short[width * height];
			int yStart = 0, n = 8 * Runtime.getRuntime().availableProcessors();
			int howMany = height / n;
			int left = height % n;
			List<Future<?>> results = new ArrayList<Future<?>>();
			for (int i = 0; i < n - 1; ++i) {
				results.add(pool.submit(new Job(yStart, yStart + howMany, width, height, reMin, reMax, imMin,
						imMax, normalPolynomial, derivedPolynomial, data)));
				yStart += howMany;
			}
			results.add(pool.submit(new Job(yStart, yStart + howMany + left, width, height, reMin, reMax, imMin,
					imMax, normalPolynomial, derivedPolynomial, data)));
			for (Future<?> result: results) {
				while (true) {
					try {
						result.get();
						break;
					} catch (InterruptedException | ExecutionException e) {
					}
				}
			}
			observer.acceptResult(data, (short)(normalPolynomial.order() + 1), requestNo);
		}
		
		/**
		 * Represents a Job that is executed by workers in created Thread pool.
		 * It fills the {@link #data} array on proper indexes with proper values.
		 * It has defined yFrom and yTo that represent which part of window height
		 * this Job needs to process. So in total this job processes 
		 * {@code (yTo - yFrom) * width} pixels.
		 * 
		 * @author Filip Husnjak
		 */
		private class Job implements Runnable {

			/**
			 * Start y value
			 */
			private int yFrom;
			
			/**
			 * Last y value to be processed is yTo - 1
			 */
			private int yTo;
			
			/**
			 * Number of pixels in a row
			 */
			private int width;
			
			/**
			 * Number of pixels in each column
			 */
			private int height;
			
			/**
			 * Minimum real part of complex number in observed complex plane
			 */
			private double reMin;
			
			/**
			 * Maximum real part of complex number in observed complex plane
			 */
			private double reMax;
			
			/**
			 * Minimum imaginary part of complex number in observed complex plane
			 */
			private double imMin;
			
			/**
			 * Maximum imaginary part of complex number in observed complex plane
			 */
			private double imMax;
			
			/**
			 * Polynomial used for calculations in its normal form
			 */
			private ComplexPolynomial normalPolynomial;
			
			/**
			 * Derivation of a polynomial used for calculations
			 */
			private ComplexPolynomial derivedPolynomial;
			
			/**
			 * Array to be filled with proper values
			 */
			private short[] data;
			
			/**
			 * Constructs new Job with specified parameters.
			 * 
			 * @param yFrom
			 *        start y value
			 * @param yTo
			 *        last y value to be processed is yTo - 1
			 * @param width
			 *        number of pixels in a row
			 * @param height
			 *        number of pixels in each column
			 * @param reMin
			 *        minimum real part of complex number in observed complex plane
			 * @param reMax
			 *        maximum real part of complex number in observed complex plane
			 * @param imMin
			 *        minimum imaginary part of complex number in observed complex plane
			 * @param imMax
			 *        maximum imaginary part of complex number in observed complex plane
			 * @param normalPolynomial
			 *        polynomial used for calculations in its normal form
			 * @param derivedPolynomial
			 *        derivation of a polynomial used for calculations
			 * @param data
			 *        array to be filled with proper values
			 */
			public Job(int yFrom, int yTo, int width, int height, double reMin, double reMax, double imMin,
					double imMax, ComplexPolynomial normalPolynomial, ComplexPolynomial derivedPolynomial, short[] data) {
				super();
				this.yFrom = yFrom;
				this.yTo = yTo;
				this.width = width;
				this.height = height;
				this.reMin = reMin;
				this.reMax = reMax;
				this.imMin = imMin;
				this.imMax = imMax;
				this.normalPolynomial = normalPolynomial;
				this.derivedPolynomial = derivedPolynomial;
				this.data = data;
			}
			
			/**
			 * Calculates and returns complex number in complex plane based on 
			 * x, y, reMax, reMin, imMax, imMin, width and height values.
			 * 
			 * @param x
			 *        x position of a pixel
			 * @param y
			 *        y position of a pixel
			 * @return complex number based on x, y, reMax, reMin,
			 *         imMax, imMin, width and height values
			 */
			private Complex calculateComplex(int x, int y) {
				return new Complex((double) x / (width - 1) * (reMax - reMin) + reMin,
						(double) (height - 1 - y) / (height - 1) * (imMax - imMin) + imMin);
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void run() {
				for (int y = yFrom; y < yTo; ++y) {
					for (int x = 0; x < width; ++x) {
						Complex zn = calculateComplex(x, y), znold;
						int iter = 0;
						do {
							znold = zn;
							zn = zn.sub(normalPolynomial.apply(zn).div(derivedPolynomial.apply(zn)));
						} while (++iter < maxIter && zn.sub(znold).getMagnitude() > convergenceTreshold);
						data[y * width + x] = (short) (rootedPolynomial.indexOfClosestRootFor(zn, rootTreshold) + 1);
					}
				}
			}
			
		}
		
	}

}
