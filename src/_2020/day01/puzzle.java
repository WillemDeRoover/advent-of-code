package _2020.day01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class puzzle {

	public static void main(String[] args) throws IOException {
		int[] numbers = Files.lines(Paths.get("src/_2020/day01/input.txt"))
				.mapToInt(Integer::valueOf)
				.toArray();

		calculateTwoNumbers(numbers);
		calculateThreeNumbers(numbers);
	}

	private static void calculateTwoNumbers(int[] numbers) {
		for (int number1 : numbers) {
			for (int number2 : numbers) {
					if (number1 + number2  == 2020) {
						System.out.println("2 number calculation = " + number1 * number2);
						return;
					}
				}
			}
	}

	private static void calculateThreeNumbers(int[] numbers) {
		for (int number1 : numbers) {
			for (int number2 : numbers) {
				for (int number3 : numbers) {
					if (number1 + number2 + number3 == 2020) {
						System.out.println("3 number calculation = " + number1 * number2 * number3);
						return;
					}
				}
			}
		}
	}

}
