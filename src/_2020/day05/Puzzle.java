package _2020.day05;

import static java.util.stream.Collectors.toCollection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeSet;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		TreeSet<Integer> seats = Files.lines(Paths.get("src/_2020/day05/input.txt"))
				.map(Puzzle::toSeatNumber)
				.collect(toCollection(TreeSet::new));

		for(int i = seats.first(); i <= seats.last(); i++) {
			if(!seats.contains(i)) {
				System.out.println(i);
			}
		}
	}

	private static int toSeatNumber(String substring) {
		String rowString = substring
				.replaceAll("[FL]", "0")
				.replaceAll("[BR]", "1");
		return Integer.parseInt(rowString, 2);
	}

}
