package _2020.day1;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class puzzle1 {

	public static void main(String[] args) throws IOException {
		int[] numbers = Files.lines(Paths.get("src/y2020/day1/input1.txt"))
				.mapToInt(Integer::valueOf)
				.toArray();

		for (int number1 : numbers) {
			for (int number2 : numbers) {
				for (int number3 : numbers) {
					if (number1 + number2 + number3 == 2020) {
						System.out.println(number1 * number2 * number3);
					}
				}
			}
		}
	}

}
