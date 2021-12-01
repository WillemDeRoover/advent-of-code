package _2021.day01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class puzzle {

	public static void main(String[] args) throws IOException {
		int[] numbers = Files.lines(Paths.get("src/_2021/day01/input.txt"))
				.mapToInt(Integer::valueOf)
				.toArray();

		System.out.println("single measurement:" + countIncreasingMeasurments(numbers, 1));
		System.out.println("triple measurement:" + countIncreasingMeasurments(numbers, 3));
	}

	private static int countIncreasingMeasurments(int[] numbers, int groupSize) {
		int count = 0;
		for(int i = groupSize; i < numbers.length; i++) {
			if(numbers[i-groupSize] < numbers[i]) {
				count++;
			}
		}
		return count;
	}
}
