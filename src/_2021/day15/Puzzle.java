package _2021.day15;

import static java.util.Comparator.comparing;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Puzzle {

	private static int maxWidth;
	private static int maxDepth;

	public static void main(String[] args) throws IOException {

		int[][] cave = Files.lines(Paths.get("src/_2021/day15/input.txt"))
				.map(s -> s.chars().map(Character::getNumericValue).toArray())
				.toArray(int[][]::new);

		maxWidth = cave[0].length * 5;
		maxDepth = cave.length * 5;

		int caveDepth = cave.length;
		int caveWidth = cave[0].length;

		int[][] newCave = new int[maxDepth][maxWidth];

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				for (int k = 0; k < caveDepth; k++) {
					for (int l = 0; l < caveWidth; l++) {
						int newValue = cave[k][l] + i + j;
						if(newValue > 9) {
							newValue %= 9;
						}
						newCave[(i * caveDepth) + k][(j * caveWidth) + l] = newValue;
					}
				}
			}
		}

		Set<List<Integer>> visited = new HashSet<>();
		Map<List<Integer>, Integer> riskRating = new HashMap<>();
		for (int i = 0; i < maxDepth; i++) {
			for (int j = 0; j < maxWidth; j++) {
				riskRating.put(List.of(i, j), Integer.MAX_VALUE);
			}
		}
		riskRating.put(List.of(0, 0), 0);
		PriorityQueue<List<Integer>> visitNext = new PriorityQueue<>(comparing(riskRating::get));
		visitNext.add(List.of(0, 0));

		while (!visited.contains(List.of(maxDepth - 1, maxWidth - 1))) {
			List<Integer> currentCoordinate = visitNext.poll();
			Integer currentRisk = riskRating.get(currentCoordinate);
			List<List<Integer>> neighBours = getNeighBours(currentCoordinate).stream().filter(not(visited::contains)).collect(toList());

			for (List<Integer> neighBour : neighBours) {
				Integer currentNeighbourRisk = riskRating.get(neighBour);
				int newNeighBourRisk = currentRisk + newCave[neighBour.get(0)][neighBour.get(1)];
				if(newNeighBourRisk < currentNeighbourRisk) {
					riskRating.put(neighBour, newNeighBourRisk);
					visitNext.add(neighBour);
				}
			}
			visited.add(currentCoordinate);
		}

		System.out.println(riskRating.get(List.of(maxDepth - 1, maxWidth - 1)));

	}

	public static List<List<Integer>> getNeighBours(List<Integer> coordinate) {
		ArrayList<List<Integer>> neighBours = new ArrayList<>();
		int i = coordinate.get(0);
		int j = coordinate.get(1);
		if (i - 1 >= 0) {
			neighBours.add(List.of(i - 1, j));
		}

		if (i + 1 < maxDepth) {
			neighBours.add(List.of(i + 1, j));
		}

		if (j - 1 >= 0) {
			neighBours.add(List.of(i, j - 1));
		}

		if (j + 1 < maxWidth) {
			neighBours.add(List.of(i, j + 1));
		}

		return neighBours;
	}

}
