package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.ScaleCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;
import hr.fer.zemris.math.Vector2D;

/**
 * Implementation of {@code LSystemBuilder} interface. It has 5 important parameters
 * which define how Lindermay system will be drawn:
 * <ul>
 *  <li>
 *  unitLegnth - Unit length of the turtle translation
 *  </li>
 *  <li>
 *  unitLengthDegreeScaler - Unit length degree scaler used to scale {@link #unitLength}
 *  </li>
 *  <li>
 *  origin - Initial position of a turtle
 *  </li>
 *  <li>
 *  angle - Initial angle the turtle is looking from
 *  </li>
 *  <li>
 *  axiom - Initial array of symbols from which the system will be developed
 *  </li>
 * </ul>
 * 
 * @author Filip Husnjak
 *
 */
public class LSystemBuilderImpl implements LSystemBuilder {
	
	/**
	 * Dictionary that maps character with its command
	 */
	private Dictionary<Character, Command> regCommands = new Dictionary<>();
	
	/**
	 * Dictionary that maps character with its production
	 */
	private Dictionary<Character, String> regProductions = new Dictionary<>();
	
	/**
	 * Unit length of the turtle translation
	 */
	private double unitLength = 0.1;
	
	/**
	 * Unit length degree scaler used to scale {@link #unitLength} 
	 */
	private double unitLengthDegreeScaler = 1;
	
	/**
	 * Initial position of a turtle
	 */
	private Vector2D origin = new Vector2D(0, 0);
	
	/**
	 * Initial angle the turtle is looking from, 0 means its looking in the positive x direction
	 */
	private double angle = 0;
	
	/**
	 * Initial array of symbols from which the system will be developed
	 */
	private String axiom = "";
	
	/**
	 * Maps the right setup functionalities to keywords
	 */
	private Map<String, ISetup> setupMap =  new HashMap<String, ISetup>();

	/**
	 * Constructs {@code LSystemBuilderImpl} object and fills the {@link #setupMap} with
	 * proper setup implementations for each existing keyword.
	 */
	public LSystemBuilderImpl() {
		setupMap.put("origin", parts -> {
			if (parts.length != 3) throw new IllegalArgumentException("The origin vector has to have 3 parts!");
			try {
				setOrigin(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Numbers not given as second and third parameter!");
			}
		});
		
		setupMap.put("angle", parts -> {
			if (parts.length != 2) throw new IllegalArgumentException("Missing specified angle value!");
			try {
				setAngle(Double.parseDouble(parts[1]));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Specified angle has to be real number!");
			}
		});
		
		setupMap.put("unitLength", parts -> {
			if (parts.length != 2) throw new IllegalArgumentException("Missing specified unitLength value!");
			try {
				setUnitLength(Double.parseDouble(parts[1]));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Specified unitLength has to be real number!");
			}
		});
		
		setupMap.put("command", parts -> {
			if (parts.length < 3) throw new IllegalArgumentException("Command line has to have at least 3 parts!");
			registerCommand(parts[1].charAt(0), String.join(" ", Arrays.copyOfRange(parts, 2, parts.length)));
		});
		
		setupMap.put("axiom", parts -> {
			if (parts.length != 2) throw new IllegalArgumentException("Axiom line has to have 2 parts!");
			setAxiom(parts[1]);
		});
		
		setupMap.put("production", parts -> {
			if (parts.length != 3) throw new IllegalArgumentException("Production line has to have 3 parts!");
			registerProduction(parts[1].charAt(0), parts[2]);
		});
		
		setupMap.put("unitLengthDegreeScaler", parts -> {
			if (parts.length < 2) throw new IllegalArgumentException("Missing specified unitLengthDegreeScaler value!");
			try {
				String[] fraction = String.join("", Arrays.copyOfRange(parts, 1, parts.length)).split("/");
				if (fraction.length != 2 && fraction.length != 1) throw new IllegalArgumentException("Fraction or decimal number not given!");
				setUnitLengthDegreeScaler(Double.parseDouble(fraction[0]) / (fraction.length == 2 ? Double.parseDouble(fraction[1]) : 1.0d));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Fraction does not contain real numbers!");
			}
		});
	}
	
	/**
	 * Returns new {@code LSystem} object.
	 * 
	 * @return new {@code LSystem} object
	 */
	@Override
	public LSystem build() {
		return new LSystemImpl();
	}

	/**
	 * Parses the given lines and sets the proper initial variables and returns
	 * {@code this} instance of {@code LSystemBuilder} class.
	 * 
	 * @return {@code this} instance of {@code LSystemBuilder} class
	 * @throws IllegalArgumentException if given lines cannot be parsed
	 * @throws NullPointerException if the given array is {@code null}
	 */
	@Override
	public LSystemBuilder configureFromText(String[] lines) {
		Objects.requireNonNull(lines, "Given array cannot be null!");
		for (int i = 0; i < lines.length; ++i) {
			String[] parts = lines[i].trim().split("\\s+");
			if (parts.length <= 0 || parts[0].isEmpty()) continue;
			setupMap.getOrDefault(parts[0], p -> { throw new IllegalArgumentException("Unrecognized parameter: " + parts[0]); }).setup(parts);
		}
		return this;
	}

	/**
	 * Parses the given {@link #action} and creates right {@code Command} instance
	 * which is added to command list.
	 * 
	 * @return {@code this} instance of {@code LSystemBuilder} class
	 * @throws IllegalArgumentException if the given action cannot be parsed or if the
	 *         command for the given symbol is already defined
	 */
	@Override
	public LSystemBuilder registerCommand(char symbol, String action) {
		if (regCommands.get(symbol) != null) {
			throw new IllegalArgumentException("Multiple commands for the same symbol defined!");
		}
		String[] parts = Objects.requireNonNull(action, "Given action cannot be null!").split("\\s+");
		try {
			switch (parts[0]) {
			case "draw":
				regCommands.put(symbol, new DrawCommand(Double.parseDouble(parts[1])));
				break;
			case "rotate":
				regCommands.put(symbol, new RotateCommand(Double.parseDouble(parts[1]) / 180 * Math.PI));
				break;
			case "skip":
				regCommands.put(symbol, new SkipCommand(Double.parseDouble(parts[1])));
				break;
			case "scale":
				regCommands.put(symbol, new ScaleCommand(Double.parseDouble(parts[1])));
				break;
			case "push":
				regCommands.put(symbol, new PushCommand());
				break;
			case "pop":
				regCommands.put(symbol, new PopCommand());
				break;
			case "color":
				regCommands.put(symbol, new ColorCommand(new Color(Integer.parseInt(parts[1], 16))));
				break;
			default:
				throw new IllegalArgumentException("Unrecognized command: " + parts[0]);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Number expected but not given: " + parts[1]);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Missing arguments for the " + parts[0] + " command!");
		}
		return this;
	}

	/**
	 * Adds new production to {@link #regProductions} defined with given symbol and
	 * String.
	 * 
	 * @param symbol
	 *        symbol whose production is to be registered
	 * @param production
	 *        specified production of a symbol
	 * @return {@code this} LSystemBuilder instance
	 * @throws NullPointerException if the given production is {@code null}
	 * @throws IllegalArgumentException if the production for the symbol already exists
	 */
	@Override
	public LSystemBuilder registerProduction(char symbol, String production) {
		if (regProductions.get(symbol) != null) {
			throw new IllegalArgumentException("Multiple productions for the same symbol defined!");
		}
		regProductions.put(symbol, Objects.requireNonNull(production, "Given production cannot be null!"));
		return this;
	}

	/**
	 * Sets the initial angle to the given one.
	 * 
	 * @param angle
	 *        initial angle
	 * @return {@code this} LSystemBuilder instance
	 */
	@Override
	public LSystemBuilder setAngle(double angle) {
		this.angle = angle;
		return this;
	}

	/**
	 * Sets the initial sequence of characters to the given one.
	 * 
	 * @param axiom
	 *        initial sequence of characters
	 * @return {@code this} LSystemBuilder instance
	 * @throws NullPointerException if the given axiom is {@code null}
	 */
	@Override
	public LSystemBuilder setAxiom(String axiom) {
		this.axiom = Objects.requireNonNull(axiom, "Given axiom cannot be null!");
		return this;
	}

	/**
	 * Sets the origin to the given one.
	 * 
	 * @param x
	 *        x value of the origin vector
	 * @param y
	 *        y value of the origin vector
	 * @return {@code this} LSystemBuilder instance
	 */
	@Override
	public LSystemBuilder setOrigin(double x, double y) {
		origin = new Vector2D(x, y);
		return this;
	}

	/**
	 * Sets the unitLength to the given one.
	 * 
	 * @param unitLength
	 *        unitLength of the translation
	 * @return {@code this} LSystemBuilder instance
	 */
	@Override
	public LSystemBuilder setUnitLength(double unitLength) {
		this.unitLength = unitLength;
		return this;
	}

	/**
	 * Sets the unitLengthDegreeScaler to the given one.
	 * 
	 * @param unitLengthDegreeScaler
	 *        unitLengthDegreeScaler used when drawing different levels
	 * @return {@code this} LSystemBuilder instance
	 */
	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
		this.unitLengthDegreeScaler = unitLengthDegreeScaler;
		return this;
	}
	
	/**
	 * Implementation of {@code LSystem} interface.
	 * 
	 * @author Filip Husnjak
	 *
	 */
	private class LSystemImpl implements LSystem {

		/**
		 * Draws the Lindermay system at specified level on specified {@code Painter} object.
		 * 
		 * @param level 
		 *        the level of the Lindermay system
		 * @param painter
		 *        Painter object on which the Lindermay system will be drawn
		 */
		@Override
		public void draw(int level, Painter painter) {
			Context context = new Context();
			context.pushState(new TurtleState(origin.copy(), Vector2D.UNIT_VEC.rotated(angle / 180 * Math.PI), Color.BLACK, unitLength));
			new ScaleCommand(Math.pow(unitLengthDegreeScaler, level)).execute(context, painter);
			String sequence = generate(level);
			for (int i = 0; i < sequence.length(); ++i) {
				Command command = regCommands.get(sequence.charAt(i));
				if (command == null) continue;
				command.execute(context, painter);
			}
		}

		/**
		 * Generates and returns sequence of characters at given level as {@code String} object.
		 * 
		 * @return sequence of characters at given level as {@code String} object
		 */
		@Override
		public String generate(int level) {
			String sequence = axiom;
			for (int i = 0; i < level; ++i) {
				StringBuilder tmp = new StringBuilder();
				for (int j = 0; j < sequence.length(); ++j) {
					String newSequence = regProductions.get(sequence.charAt(j));
					tmp.append(newSequence == null ? sequence.charAt(j) : newSequence);
				}
				sequence = tmp.toString();
			}
			return sequence;
		}
		
	}
	
	/**
	 * Functional interface whose method is used to setup parameters of {@code LSystemBuilderImpl}
	 * class.
	 * 
	 * @author Filip Husnjak
	 *
	 */
	@FunctionalInterface
	private static interface ISetup {
		
		/**
		 * Sets the right parameter from given parts.
		 * 
		 * @param parts
		 *        given parts from which the parameter is to be set
		 * @throws IllegalArgumentException if the given parts does not meet the requirements when setting parameter
		 */
		void setup(String[] parts);
	}

}
