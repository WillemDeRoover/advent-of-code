package _2020.day22;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toCollection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		String[] players = Files.readString(Paths.get("src/_2020/day22/input.txt")).split("\\n\\n");
		List<Queue<Integer>> resultDecks = recursiveCombat(getDeck(players[0]), getDeck(players[1]));
		resultDecks.stream()
				.filter(not(Collection::isEmpty))
				.map(Puzzle::calculate)
				.forEach(System.out::println);
	}

	private static Queue<Integer> getDeck(String player) {
		return Stream.of(player.split("\\n"))
				.skip(1)
				.map(Integer::valueOf)
				.collect(toCollection(LinkedList::new));
	}

	private static List<Queue<Integer>> recursiveCombat(Queue<Integer> deck1, Queue<Integer> deck2) {
		Set<Integer> previousDecks = new HashSet<>();

		while (!deck1.isEmpty() && !deck2.isEmpty()) {
			if(!previousDecks.add(deck1.hashCode() * 31 + deck2.hashCode())) {
				return List.of(deck1, new LinkedList<>());
			}

			int player1Card = deck1.remove();
			int player2Card = deck2.remove();
			if(player1Card <= deck1.size() && player2Card <= deck2.size()) {
				List<Queue<Integer>> resultDecks = recursiveCombat(createRecursiveDeck(deck1, player1Card), createRecursiveDeck(deck2, player2Card));
				if(!resultDecks.get(0).isEmpty()) {
					deck1.add(player1Card);
					deck1.add(player2Card);
				} else {
					deck2.add(player2Card);
					deck2.add(player1Card);
				}
			} else {
				if(player1Card > player2Card) {
					deck1.add(player1Card);
					deck1.add(player2Card);
				} else {
					deck2.add(player2Card);
					deck2.add(player1Card);
				}
			}
		}
		return List.of(deck1, deck2);
	}

	private static LinkedList<Integer> createRecursiveDeck(Queue<Integer> deck1, int depth) {
		return new LinkedList<>(deck1).stream()
				.limit(depth)
				.collect(toCollection(LinkedList::new));
	}

	private static long calculate(Queue<Integer> deck) {
		int size = deck.size();
		long total = 0;
		for (int value : deck) {
			total += value * size--;
		}
		return total;
	}
}
