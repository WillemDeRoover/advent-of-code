package _2020.day23;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Puzzle {

	private static int max;

	public static void main(String[] args) {
		List<Integer> circle = Stream.of("157623984".split("")).map(Integer::valueOf).collect(toList());
		Map<Integer, Integer> nextCupMap = createNextCupMap(circle);
		max = nextCupMap.values().stream().mapToInt(i -> i).max().getAsInt() + 1;

		int currentCup = circle.get(0);
		for(int i = 0; i < 10000000; i++) {
			List<Integer> nextThreeCups = getNextThreeCups(currentCup, nextCupMap);
			nextCupMap.put(currentCup, nextCupMap.get(nextThreeCups.get(2)));

			int destination = calculateDestination(currentCup, nextThreeCups);
			nextCupMap.put(nextThreeCups.get(2), nextCupMap.get(destination));
			nextCupMap.put(destination, nextThreeCups.get(0));

			currentCup = nextCupMap.get(currentCup);
		}

		int i1 = nextCupMap.get(1);
		int i2 = nextCupMap.get(i1);
		System.out.println((long) i1 * i2);
	}

	private static Map<Integer, Integer> createNextCupMap(List<Integer> circle) {
		Map<Integer, Integer> nextMap = new HashMap<>();
		Integer previous = null;
		for (Integer current : circle) {
			if(previous != null) {
				nextMap.put(previous, current);
			}
			previous = current;
		}
		for(int i = 10; i <= 1000000; i++) {
			nextMap.put(previous, i);
			previous = i;
		}
		nextMap.put(previous, circle.get(0));
		return nextMap;
	}

	private static List<Integer> getNextThreeCups(int current, Map<Integer, Integer> nextMap) {
		List<Integer> subList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			int next = nextMap.get(current);
			subList.add(next);
			current = next;
		}
		return subList;
	}

	private static int calculateDestination(int current, List<Integer> subList) {
		int destination = current;
		do {
			destination = ((destination - 1) + max) % max;
		} while(destination == 0 || subList.contains(destination));
		return destination;
	}
}
