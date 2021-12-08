package _2021.day05;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

	private static final Pattern pattern = Pattern.compile("(\\d+),(\\d+)\\D*(\\d+),(\\d+)");

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.lines(Paths.get("src/_2021/day05/input.txt")).collect(toList());
		int[][] grid = new int[1000][1000];

		for (String line : lines) {
			Matcher matcher = pattern.matcher(line);
			matcher.find();
			int x1 = parseInt(matcher.group(1));
			int y1 = parseInt(matcher.group(2));
			int x2 = parseInt(matcher.group(3));
			int y2 = parseInt(matcher.group(4));

			Function<Integer, Integer> operationX = null;
			Function<Integer, Integer> operationY = null;
			
			if (x1 != x2 && y1 != y2) {
				if (x1 < x2 && y1 < y2) {
					operationX = x -> x++;
					operationY = y -> y++;
				} else if (x1 < x2 && y1 > y2) {
					operationX = x -> x++;
					operationY = y -> y--;
				} else if (x1 > x2 && y1 < y2) {
					operationX = x -> x--;
					operationY = y -> y++;
				} else {
					operationX = x -> x--;
					operationY = y -> y--;
				}
				for (int x = x1, y = y1; x != x2 && y != y2; x = operationX.apply(x), y = operationY.apply(y)) {
					grid[y][x]++;
				}
				grid[y2][x2]++;
				continue;
			}

			if (x1 != x2) {
				if (x1 < x2) {
					operationX = x -> x++;
				} else {
					operationX = x -> x--;
				}
				for (int i = x1; i != x2; i = operationX.apply(i)) {
					grid[y1][i]++;
				}
				grid[y1][x2]++;
			}

			if (y1 != y2) {
				if (y1 < y2) {
					operationY = y -> y++;
				} else {
					operationY = y -> y--;
				}
				for (int i = y1; i != y2; i = operationY.apply(i)) {
					grid[i][x1]++;
				}
				grid[y2][x1]++;
			}
		}

		int total = 0;
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[0].length; x++) {
				if (grid[y][x] >= 2) {
					total++;
				}
			}
		}
		System.out.println(total);
	}
}
