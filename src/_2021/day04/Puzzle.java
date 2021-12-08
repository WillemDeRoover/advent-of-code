package _2021.day04;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

	private static final Pattern pattern = Pattern.compile("\\d+");

	public static void main(String[] args) throws IOException {
		part1();
		part2();

	}

	private static void part1() throws IOException {
		List<Integer> numbers = getNumbers();
		List<Board> boards = getBoards();

		out: for (Integer number : numbers) {
			for (Board board : boards) {
				for (Set<Integer> horizontalRow : board.horizontalRows) {
					horizontalRow.remove(number);
					if(horizontalRow.isEmpty()) {
						System.out.println(board.countBoard() * number);
						break out;
					}
				}
				for (Set<Integer> verticalRow : board.verticalRows) {
					verticalRow.remove(number);
					if(verticalRow.isEmpty()) {
						System.out.println(board.countBoard() * number);
						break out;
					}
				}
			}
		}
	}

	private static void part2() throws IOException {
		List<Integer> numbers = getNumbers();
		List<Board> boards = getBoards();

		for (Integer number : numbers) {
			for (Board board : boards) {
				for (Set<Integer> horizontalRow : board.horizontalRows) {
					horizontalRow.remove(number);
				}
				for (Set<Integer> verticalRow : board.verticalRows) {
					verticalRow.remove(number);
				}
			}

			if(boards.size() == 1 && boards.get(0).hasWon()) {
				System.out.println(boards.get(0).countBoard() * number);
				break;
			}
			boards = boards.stream().filter(not(Board::hasWon)).collect(toList());

		}
	}


	private static List<Integer> getNumbers() throws IOException {
		return Files.lines(Paths.get("src/_2021/day04/input.txt"))
				.limit(1)
				.map(s -> s.split(","))
				.map(Arrays::asList).flatMap(List::stream)
				.map(Integer::parseInt)
				.collect(toList());
	}

	private static List<Board> getBoards() throws IOException {
		List<String> lines = Files.lines(Paths.get("src/_2021/day04/input.txt")).skip(1).filter(not(String::isEmpty)).collect(toList());
		List<Board> boards = new ArrayList<>();
		for (int i = 0; i < lines.size(); i = i + 5) {
			List<Set<Integer>> horizontalRows = new ArrayList<>();
			List<Set<Integer>> verticalRows = List.of(new HashSet<>(),new HashSet<>(),new HashSet<>(),new HashSet<>(),new HashSet<>());
			for (int j = 0; j < 5; j++) {
				String line = lines.get(i+j);
				Matcher matcher = pattern.matcher(line);
				List<Integer> collect = matcher.results().map(MatchResult::group).map(Integer::parseInt).collect(toList());
				horizontalRows.add(new HashSet<>(collect));
				for (int k = 0; k < 5; k++) {
					verticalRows.get(k).add(collect.get(k));
				}
			}

			boards.add(new Board(horizontalRows, verticalRows));

		}

		return boards;
	}

	static class Board {
		List<Set<Integer>> horizontalRows;
		List<Set<Integer>> verticalRows;

		public Board(List<Set<Integer>> horizontalRows, List<Set<Integer>> verticalRows) {
			this.horizontalRows = horizontalRows;
			this.verticalRows = verticalRows;
		}

		boolean hasWon() {
			return horizontalRows.stream().anyMatch(Set::isEmpty) || verticalRows.stream().anyMatch(Set::isEmpty);
		}

		int countBoard() {
			return horizontalRows.stream()
					.map(row -> row.stream().reduce(Integer::sum))
					.map(o -> o.orElse(0))
					.reduce(Integer::sum)
					.orElse(0);
		}

	}
}
