package _2021.day13;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		Set<List<Integer>> dots = Files.lines(Paths.get("src/_2021/day13/input.txt"))
				.takeWhile(not(String::isEmpty))
				.map(s -> s.split(","))
				.map(a -> asList(parseInt(a[0]), parseInt(a[1])))
				.collect(toSet());

		List<int[]> foldingInstructions = Files.lines(Paths.get("src/_2021/day13/input.txt"))
				.dropWhile(not(String::isEmpty))
				.filter(not(String::isEmpty))
				.map(s -> s.split("="))
				.map(a -> new int[] { a[0].charAt(a[0].length() - 1) == 'x' ? 0 : 1, parseInt(a[1]) })
				.collect(toList());


		for (int[] foldingInstruction : foldingInstructions) {
			dots = dots.stream()
					.filter(a -> a.get(foldingInstruction[0]) != foldingInstruction[1])
					.map(a -> fold(a, foldingInstruction))
					.collect(toSet());
		}

		int[][] code = new int[10][100];
		for (List<Integer> dot : dots) {
			code[dot.get(1)][dot.get(0)] = 1;
		}

		for (int i = 0; i < code.length; i++) {
			for (int j = 0; j < code[i].length; j++) {
				System.out.print(code[i][j]);
			}
			System.out.println();
		}

	}

	private static List<Integer> fold(List<Integer> dot, int[] foldingInstruction) {
		Integer number = dot.get(foldingInstruction[0]);
		int foldLine = foldingInstruction[1];
		if (number < foldLine) {
			return dot;
		}

		int difference = number - foldLine;
		dot.set(foldingInstruction[0], foldLine - difference);
		return dot;
	}


}
