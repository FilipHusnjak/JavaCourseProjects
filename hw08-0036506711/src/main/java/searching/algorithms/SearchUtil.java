package searching.algorithms;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SearchUtil {
	public static <S> Node<S> bfs(Supplier<S> s0, Function<S, List<Transition<S>>> succ, Predicate<S> goal) {
		List<Node<S>> toExplore = new LinkedList<>(Arrays.asList(new Node<S>(null, s0.get(), 0)));
		while (!toExplore.isEmpty()) {
			Node<S> ni = toExplore.remove(0);
			if (goal.test(ni.getState())) {
				return ni;
			}
			toExplore.addAll(succ.apply(ni.getState()).stream()
					.map(t -> new Node<>(ni, t.getState(), ni.getCost() + t.getCost())).collect(Collectors.toList()));
		}
		return null;
	}

	public static <S> Node<S> bfsv(Supplier<S> s0, Function<S, List<Transition<S>>> succ, Predicate<S> goal) {
		List<Node<S>> toExplore = new LinkedList<>(Arrays.asList(new Node<S>(null, s0.get(), 0)));
		Set<S> visited = new HashSet<>(Arrays.asList(s0.get()));
		while (!toExplore.isEmpty()) {
			Node<S> ni = toExplore.remove(0);
			if (goal.test(ni.getState())) {
				return ni;
			}
			List<Node<S>> children = succ.apply(ni.getState()).stream()
					.map(t -> new Node<>(ni, t.getState(), ni.getCost() + t.getCost())).collect(Collectors.toList());
			children.removeIf(c -> visited.contains(c.getState()));
			visited.addAll(children.stream().map(Node::getState).collect(Collectors.toList()));
			toExplore.addAll(children);
		}
		return null;
	}
}
