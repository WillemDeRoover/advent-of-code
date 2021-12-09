package _2021.day09;

import static java.util.Comparator.reverseOrder;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.IntStream;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		List<List<Integer>> cave = createCave();

		List<Integer> basinSizes = new ArrayList<>();

		int totalRisk = 0;
		for (int i = 1; i <= cave.size() - 1 ; i++) {
			for (int j = 1; j < cave.get(i).size() -1; j++) {
				int height = cave.get(i).get(j);
				if(height < cave.get(i-1).get(j) && height < cave.get(i+1).get(j) && height < cave.get(i).get(j-1) && height < cave.get(i).get(j+1)) {
					totalRisk += (height + 1);
					basinSizes.add(calculateBasin(new Tupple(i, j), cave));
				}
			}
		}

		System.out.println(totalRisk);
		basinSizes.stream().sorted(reverseOrder()).limit(3).reduce((i, j) -> i * j).ifPresent(System.out::println);
	}

	private static List<List<Integer>> createCave() throws IOException {
		List<List<Integer>> cave = Files.lines(Paths.get("src/_2021/day09/input.txt"))
				.map(s -> s.chars().map(Character::getNumericValue).boxed().collect(toList()))
				.collect(toList());

		List<Integer> topBottomSide = IntStream.range(0, cave.get(0).size()).mapToObj(i -> 9).collect(toList());
		cave.add(0, new ArrayList<>(topBottomSide));
		cave.add(new ArrayList<>(topBottomSide));

		for (List<Integer> line : cave) {
			line.add(0, 9);
			line.add(9);
		}

		return cave;
	}

	private static int calculateBasin(Tupple root, List<List<Integer>> cave) {
		Set<Tupple> basin = new HashSet<>();

		Queue<Tupple> toBeProcessed = new LinkedList<>();
		toBeProcessed.add(root);

		while(!toBeProcessed.isEmpty()) {
			Tupple tupple = toBeProcessed.remove();
			if(cave.get(tupple.i).get(tupple.j) != 9) {
				basin.add(tupple);
				List<Tupple> toCheck = tupple.getSides().stream().filter(not(basin::contains)).collect(toList());
				toBeProcessed.addAll(toCheck);
			}
		}

		return basin.size();
	}


	private static class Tupple {
		int i;
		int j;

		public Tupple(int i, int j) {
			this.i = i;
			this.j = j;
		}

		List<Tupple> getSides() {
			return List.of(new Tupple(i - 1, j), new Tupple(i + 1, j), new Tupple(i, j - 1), new Tupple(i, j + 1));
		}

		@Override
		public int hashCode() {
			return Objects.hash(i, j);
		}
	}

}
