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

	private static int getInvalidPosition(List<Long> digits) {
		for(int i = 25; i < digits.size(); i++) {
			boolean found  = false;
			for(int j = i - 25; j < i; j++) {
				int first = j, current = i;
				found = found || IntStream.range(j+1, i).anyMatch(second -> digits.get(first)+ digits.get(second) == digits.get(current));
			}
			if(!found) {
				return i;
			}
		}
		return 0;
	}

	private static long getWeakness(List<Long> digits, int invalidPosition) {
		long invalidNumber = digits.get(invalidPosition);
		for(int i = 0; i < invalidPosition; i++) {
			long total = 0;
			for(int j = i; total < invalidNumber; j++) {
				total += digits.get(j);
				if(total == invalidNumber) {
					List<Long> range = digits.subList(i, j + 1);
					return range.stream().min(Long::compare).get() + range.stream().max(Long::compare).get();
				}
			}
		}
		return 0;
	}

}
