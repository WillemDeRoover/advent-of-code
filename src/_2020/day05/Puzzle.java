package _2020.day05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle {

	public static void main(String[] args) throws IOException {

		List<Integer> seats = Files.lines(Paths.get("src/_2020/day05/input.txt"))
				.mapToInt(Puzzle::toSeatNumber)
				.sorted()
				.boxed().collect(Collectors.toList());

		int min = seats.get(0);
		int max = seats.get(seats.size() -1);

		for(int i = min; i <= max; i++) {
			if(!seats.contains(i)) {
				System.out.println(i);
			}
		}
	}

	private static int toSeatNumber(String substring) {
		String rowString = substring
				.replace('F', '0')
				.replace('B', '1')
				.replace('L', '0')
				.replace('R', '1');
		return Integer.parseInt(rowString, 2);
	}

}
