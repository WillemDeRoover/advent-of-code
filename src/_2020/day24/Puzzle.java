package _2020.day24;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		List<String> tileReferences = Files.lines(Paths.get("src/_2020/day24/input.txt")).collect(toList());

		Set<List<Integer>> blackTiles = calculateBlackTiles(tileReferences);
		System.out.println(blackTiles.size());

		blackTiles = evolve(blackTiles, 100);
		System.out.println(blackTiles.size());
	}

	private static Set<List<Integer>> calculateBlackTiles(List<String> tileReferences) {
		Set<List<Integer>> blackTiles= new HashSet<>();

		for (String tileReference : tileReferences) {
			String tilePath = tileReference;
			List<Integer> currentTile = new ArrayList<>(Arrays.asList(0, 0));
			while(!tilePath.isEmpty()) {
				for (Direction direction : Direction.values()) {
					if (tilePath.startsWith(direction.value)) {
						currentTile.set(0, currentTile.get(0) +  direction.vector.get(0));
						currentTile.set(1, currentTile.get(1) +  direction.vector.get(1));
						tilePath = tilePath.replaceFirst(direction.value, "");
					}
				}
			}

			if(!blackTiles.add(currentTile)) {
				blackTiles.remove(currentTile);
			}
		}
		return blackTiles;
	}

	private static Set<List<Integer>> evolve(Set<List<Integer>> blackTiles, int iterations) {
		for (int i = 0; i < iterations; i++) {
			List<List<Integer>> allNeighbours = new ArrayList<>();
			Set<List<Integer>> nextConfiguration = new HashSet<>();
			for (List<Integer> blackTile : blackTiles) {
				int count = 0;
				List<List<Integer>> blackTileNeighbours = getNeighbours(blackTile);
				allNeighbours.addAll(blackTileNeighbours);
				for (List<Integer> neighbour : blackTileNeighbours) {
					if(blackTiles.contains(neighbour)) {
						count++;
					}
				}
				if(count == 1 || count == 2) {
					nextConfiguration.add(blackTile);
				}
			}

			Map<List<Integer>, Long> neighboursMap = allNeighbours.stream()
					.filter(not(blackTiles::contains))
					.filter(not(nextConfiguration::contains))
					.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
			neighboursMap.entrySet().stream()
					.filter(e -> e.getValue() == 2)
					.forEach(e -> nextConfiguration.add(e.getKey()));

			blackTiles = new HashSet<>(nextConfiguration);
		}
		return blackTiles;
	}

	private static List<List<Integer>> getNeighbours(List<Integer> blackTile) {
		List<List<Integer>> neighbours = new ArrayList<>();
		for (Direction direction : Direction.values()) {
			neighbours.add(List.of(blackTile.get(0) + direction.vector.get(0), blackTile.get(1) + direction.vector.get(1)));
		}
		return neighbours;
	}

	public enum Direction {
		EAST("e", List.of(1, 0)),
		SOUTHEAST("se", List.of(1, -1)),
		SOUTHWEST("sw", List.of(0, -1)),
		WEST("w", List.of(-1, 0)),
		NORTHWEST("nw", List.of(-1, 1)),
		NORTHEAST("ne", List.of(0,1));

		final String value;
		final List<Integer> vector;

		Direction(String value, List<Integer> vector) {
			this.value = value;
			this.vector = vector;
		}
	}
}
