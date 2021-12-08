package _2021.day07;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		int[] crabs = Arrays.stream(Files.lines(Paths.get("src/_2021/day07/input.txt")).collect(toList()).get(0).split(","))
				.mapToInt(Integer::parseInt)
				.toArray();

		long least = Long.MAX_VALUE;
		for (int i = 0; i < 1884; i++) {
			long sum = 0;
			for (int crab : crabs) {
				long distance;
				if(crab >= i) {
					distance = crab - i;
				} else {
					distance = i - crab;
				}
				long consumption = (distance * (distance + 1))/2;
				sum += consumption;
			}
			if(sum < least) {
				least = sum;
			}
		}

		System.out.println(least);

	}
}
