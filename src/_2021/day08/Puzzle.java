package _2021.day08;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		long total = Files.lines(Paths.get("src/_2021/day08/input.txt"))
				.mapToInt(Puzzle::calculateOutput)
				.sum();

		System.out.println(total);

	}

	private static int calculateOutput(String line) {
		String[] split = line.split(" \\| ");
		List<Set<Character>> inputs = asList(split[0].split(" ")).stream().map(s -> s.chars().mapToObj(i -> (char) i).collect(toSet())).distinct().collect(toList());
		List<Set<Character>> outputs = asList(split[1].split(" ")).stream().map(s -> s.chars().mapToObj(i -> (char) i).collect(toSet())).collect(toList());

		return calculate(inputs, outputs);
	}

	private static int calculate(List<Set<Character>> inputs, List<Set<Character>> outputs) {
		Set<Character>[] numbers = new Set[10];

		numbers[1] = inputs.stream().filter(s -> s.size() == 2).findFirst().get();
		numbers[4] = inputs.stream().filter(s -> s.size() == 4).findFirst().get();
		numbers[7] = inputs.stream().filter(s -> s.size() == 3).findFirst().get();
		numbers[8] = inputs.stream().filter(s -> s.size() == 7).findFirst().get();
		inputs.removeAll(List.of(numbers[1], numbers[4], numbers[7], numbers[8]));

		numbers[3] = inputs.stream().filter(s -> s.size() == 5).filter(digit -> digit.containsAll(numbers[1])).findFirst().get();
		inputs.remove(numbers[3]);

		numbers[6] = inputs.stream().filter(s -> s.size() == 6).filter(digit1 -> !digit1.containsAll(numbers[1])).findFirst().get();
		inputs.remove(numbers[6]);

		numbers[5] =  inputs.stream().filter(s -> s.size() == 5).filter(digit1 -> numbers[6].containsAll(digit1)).findFirst().get();
		inputs.remove(numbers[5]);

		numbers[2] = inputs.stream().filter(s -> s.size() == 5).findFirst().get();
		inputs.remove(numbers[2]);

		numbers[9] = inputs.stream().filter(s -> s.size() == 6).filter(digit -> digit.containsAll(numbers[4])).findFirst().get();
		inputs.remove(numbers[9]);

		numbers[0] = inputs.get(0);

		StringBuilder stringBuilder = new StringBuilder();
		for (Set<Character> output : outputs) {
			for (int i = 0; i < numbers.length; i++) {
				if(output.equals(numbers[i])) {
					stringBuilder.append(i);
					break;
				}
			}
		}
		return Integer.parseInt(stringBuilder.toString());

	}
}
