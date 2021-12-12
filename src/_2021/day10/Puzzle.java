package _2021.day10;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Puzzle {

	private static Set<Character> open = Set.of('(', '[', '{' , '<');
	private static Map<Character, Character> outputMap = Map.of('(', ')', '[', ']', '{', '}', '<', '>');
	private static Map<Character, Integer> errorScoreMap = Map.of(')',3, ']', 57, '}', 1197, '>', 25137);
	private static Map<Character, Integer> completeScoreMap = Map.of('(', 1, '[', 2, '{', 3, '<', 4);

	public static void main(String[] args) throws IOException {
		List<char[]> input = Files.lines(Paths.get("src/_2021/day10/input.txt")).map(String::toCharArray).collect(toList());

		int totalErrorScore = input.stream().mapToInt(Puzzle::calculateErrorScore).sum();
		System.out.println(totalErrorScore);

		List<Long> completeScore = input.stream().filter(c -> calculateErrorScore(c) == 0).map(Puzzle::calculateCompleteScore).sorted().collect(toList());
		System.out.println(completeScore.get(completeScore.size() / 2));
	}

	private static int calculateErrorScore(char[] characters) {
		Stack<Character> stack = new Stack();
		for (char character : characters) {
			if(open.contains(character)) {
				stack.push(character);
			} else {
				Character start = stack.pop();
				if(outputMap.get(start) != character) {
					return errorScoreMap.get(character);
				}
			}
		}
		return 0;
	}

	private static long calculateCompleteScore(char[] characters) {
		Stack<Character> stack = new Stack();
		for (char character : characters) {
			if(open.contains(character)) {
				stack.push(character);
			} else {
				stack.pop();
			}
		}
		long completeScore = 0;
		while (!stack.isEmpty()) {
			Character end = stack.pop();
			completeScore  = completeScore * 5 + completeScoreMap.get(end);

		}
		System.out.println(completeScore);
		return completeScore;
	}

}
