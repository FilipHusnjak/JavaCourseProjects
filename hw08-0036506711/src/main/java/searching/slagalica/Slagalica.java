package searching.slagalica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import searching.algorithms.SearchUtil;
import searching.algorithms.Transition;

/**
 * Class that represents a Puzzle and provides implementations for all required
 * Interfaces so that the solution can be found with {@link SearchUtil#bfs(Supplier, Function, Predicate)}
 * or {@link SearchUtil#bfsv(Supplier, Function, Predicate)} methods.
 * 
 * @author Filip Husnjak
 */
public class Slagalica implements Supplier<KonfiguracijaSlagalice>,
		Function<KonfiguracijaSlagalice, List<Transition<KonfiguracijaSlagalice>>>, Predicate<KonfiguracijaSlagalice> {

	/**
	 * Initial configuration of a puzzle
	 */
	private final KonfiguracijaSlagalice initialConfig;
	
	/**
	 * Correct configuration of a puzzle
	 */
	private final KonfiguracijaSlagalice solvedConfig = new KonfiguracijaSlagalice(
			new int[] {1, 2, 3, 4, 5, 6, 7, 8, 0});
	
	/**
	 * Helper array to calculate successor configurations
	 */
	private int[] moves = new int[] {1, -1, 3, -3};
	
	/**
	 * Constructs new {@link Slagalica} with specified initial configuration.
	 * 
	 * @param initialConfig
	 *        initial configuration of a puzzle
	 */
	public Slagalica(KonfiguracijaSlagalice initialConfig) {
		this.initialConfig = initialConfig;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean test(KonfiguracijaSlagalice t) {
		return Arrays.equals(t.getPolje(), solvedConfig.getPolje());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Transition<KonfiguracijaSlagalice>> apply(KonfiguracijaSlagalice t) {
		List<Transition<KonfiguracijaSlagalice>> succ = new ArrayList<>();
		int spaceIndex = t.indexOfSpace();
		int row = spaceIndex / 3;
		for (int i = 0; i < moves.length; ++i) {
			int newIndex = spaceIndex + moves[i];
			if (newIndex < 0 || newIndex >= 9 || (row != newIndex / 3 && spaceIndex % 3 != newIndex % 3)) {
				continue;
			}
			int[] newArray = Arrays.copyOf(t.getPolje(), t.getPolje().length);
			newArray[spaceIndex] = newArray[newIndex];
			newArray[newIndex] = 0;
			succ.add(new Transition<KonfiguracijaSlagalice>(new KonfiguracijaSlagalice(newArray), 1));
		}
		return succ;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public KonfiguracijaSlagalice get() {
		return initialConfig;
	}
	
}
