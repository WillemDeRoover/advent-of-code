package _2020.day09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		List<Long> digits = Files.lines(Paths.get("src/_2020/day09/input.txt"))
				.map(Long::valueOf)
				.collect(Collectors.toList());

		int invalidPosition = getInvalidPosition(digits);
		System.out.println("invalid number: " + digits.get(invalidPosition));
		System.out.println("weakness: " + getWeakness(digits, invalidPosition));
	}

	private static long getWeakness(List<Long> digits, int invalidPosition) {
		long invalidNumber = digits.get(invalidPosition);
		for(int i = 0; i < invalidPosition; i++) {
			long total = 0;
			for(int j = i; total < invalidNumber; j++) {
				total += digits.get(j);
				if(total == invalidNumber) {
					long min = IntStream.range(i, j+1).mapToLong(digits::get).min().getAsLong();
					long max = IntStream.range(i, j+1).mapToLong(digits::get).max().getAsLong();
					return min + max;

				}
			}
		}
		return 0;
	}

	private static int getInvalidPosition(List<Long> digits) {
		for(int i = 25; i < digits.size(); i++) {
			int max = i-1;
			int min = i - 25;
			boolean found  = false;
			for(int j = min; j <= max; j++) {
				for(int k = j + 1; k <= max; k++) {
					if(digits.get(j)+ digits.get(k) == digits.get(i)) {
						found = true;
					}
				}
			}
			if(!found) {
				return i;
			}
		}
		return 0;
	}

}
