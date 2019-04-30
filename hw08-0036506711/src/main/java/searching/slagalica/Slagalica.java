package searching.slagalica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import searching.algorithms.Transition;

public class Slagalica implements Supplier<KonfiguracijaSlagalice>,
		Function<KonfiguracijaSlagalice, List<Transition<KonfiguracijaSlagalice>>>, Predicate<KonfiguracijaSlagalice> {

	private final KonfiguracijaSlagalice initialConfig;
	
	private final KonfiguracijaSlagalice solvedConfig = new KonfiguracijaSlagalice(
			new int[] {1, 2, 3, 4, 5, 6, 7, 8, 0});
	
	private int[] moves = new int[] {1, -1, 3, -3};
	
	public Slagalica(KonfiguracijaSlagalice initialConfig) {
		this.initialConfig = initialConfig;
	}

	@Override
	public boolean test(KonfiguracijaSlagalice t) {
		return Arrays.equals(t.getPolje(), solvedConfig.getPolje());
	}

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

	@Override
	public KonfiguracijaSlagalice get() {
		return initialConfig;
	}
	
}
