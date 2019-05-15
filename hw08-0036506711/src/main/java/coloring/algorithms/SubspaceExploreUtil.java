package coloring.algorithms;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Class that contains static methods for visiting all elements in some space
 * and executing provided action upon each one. Used algorithms are bfs and dfs.
 * 
 * @author Filip Husnjak
 */
public class SubspaceExploreUtil {
	
	/**
	 * Visits all elements in some space and executes the given action upon each one.
	 * Uses Breadth First Search algorithm.
	 * 
	 * @param s0
	 *        Supplier that returns the initial element to be explored
	 * @param process
	 *        action to be executed upon each element
	 * @param succ
	 *        Function that returns all successors of the current element
	 * @param acceptable
	 *        Predicate which tests if the current element satisfies certain conditions
	 *        and if thats the case the element is processed
	 */
	public static <S> void bfs(Supplier<S> s0, Consumer<S> process, Function<S, List<S>> succ,
			Predicate<S> acceptable) {
		List<S> toExplore = new LinkedList<>(Arrays.asList(s0.get()));
		while (!toExplore.isEmpty()) {
			S si = toExplore.remove(0);
			if (!acceptable.test(si))
				continue;
			process.accept(si);
			toExplore.addAll(succ.apply(si));
		}
	}

	/**
	 * Visits all elements in some space and executes the given action upon each one.
	 * Uses Depth First Search algorithm.
	 * 
	 * @param s0
	 *        Supplier that returns the initial element to be explored
	 * @param process
	 *        action to be executed upon each element
	 * @param succ
	 *        Function that returns all successors of the current element
	 * @param acceptable
	 *        Predicate which tests if the current element satisfies certain conditions
	 *        and if thats the case the element is processed
	 */
	public static <S> void dfs(Supplier<S> s0, Consumer<S> process, Function<S, List<S>> succ,
			Predicate<S> acceptable) {
		List<S> toExplore = new LinkedList<>(Arrays.asList(s0.get()));
		while (!toExplore.isEmpty()) {
			S si = toExplore.remove(0);
			if (!acceptable.test(si))
				continue;
			process.accept(si);
			toExplore.addAll(0, succ.apply(si));
		}
	}

	/**
	 * Visits all elements in some space and executes the given action upon each one.
	 * Uses Breadth First Search algorithm with additional Collection that saves 
	 * visited elements so they are not visited twice.
	 * 
	 * @param s0
	 *        Supplier that returns the initial element to be explored
	 * @param process
	 *        action to be executed upon each element
	 * @param succ
	 *        Function that returns all successors of the current element
	 * @param acceptable
	 *        Predicate which tests if the current element satisfies certain conditions
	 *        and if thats the case the element is processed
	 */
	public static <S> void bfsv(Supplier<S> s0, Consumer<S> process, Function<S, List<S>> succ,
			Predicate<S> acceptable) {
		List<S> toExplore = new LinkedList<>(Arrays.asList(s0.get()));
		Set<S> visited = new HashSet<>(Arrays.asList(s0.get()));
		while (!toExplore.isEmpty()) {
			S si = toExplore.remove(0);
			if (!acceptable.test(si))
				continue;
			process.accept(si);
			List<S> children = (succ.apply(si));
			children.removeIf(visited::contains);
			toExplore.addAll(children);
			visited.addAll(children);
		}
	}
}
