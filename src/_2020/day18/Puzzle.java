package _2020.day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

//TODO try this with regexes
public class Puzzle {

	public static void main(String[] args) throws IOException {
		List<String> computations = Files.lines(Paths.get("src/_2020/day18/input.txt"))
				.map(line -> line.replace(" ", ""))
				.collect(Collectors.toList());

		long leftToRight = computations.stream()
				.mapToLong(Puzzle::computeLeftToRight)
				.sum();
		System.out.println(leftToRight);

		long addBeforeMult = computations.stream()
				.mapToLong(Puzzle::addBeforeMult)
				.sum();
		System.out.println(addBeforeMult);

	}

	private static long addBeforeMult(String computation) {
		boolean foundAddition = false;
		Stack<Long> numbers = new Stack<>();
		int pos = 0;
		while (pos < computation.length()) {
			char currentChar = computation.charAt(pos);
			if (Character.isDigit(currentChar)) {
				numbers.push((long) (currentChar - '0'));
			} else if (currentChar == '(') {
				int endBracket = findMatchingEndBracket(pos, computation);
				long result = addBeforeMult(computation.substring(pos + 1, endBracket));
				numbers.push(result);
				pos = endBracket;
			}

			if(foundAddition) {
				Long e1 = numbers.pop();
				Long e2 = numbers.pop();
				long result = Math.addExact(e1, e2);
				numbers.push(result);
				foundAddition = false;
			}

			if (currentChar == '+') {
				foundAddition = true;
			}
			pos++;
		}
		return numbers.stream().mapToLong(Long::longValue).reduce((e1, e2) -> e1 * e2).getAsLong();
	}

	private static long computeLeftToRight(String computation) {
		BiFunction<Long, Long, Long> currentComputation = null;
		List<Long> numbers = new ArrayList<>();
		int pos = 0;
		while(pos < computation.length()) {
			char currentChar = computation.charAt(pos);
			if(Character.isDigit(currentChar)) {
				numbers.add((long) (currentChar - '0'));
			} else if(currentChar == '+') {
				currentComputation = Math::addExact;
			} else if(currentChar == '*'){
				currentComputation = Math::multiplyExact;
			} else {
				int endBracket = findMatchingEndBracket(pos, computation);
				long result = computeLeftToRight(computation.substring(pos + 1, endBracket));
				numbers.add(result);
				pos = endBracket;
			}
			if(numbers.size() == 2) {
				Long result = currentComputation.apply(numbers.get(0), numbers.get(1));
				numbers.clear();
				numbers.add(result);
			}
			pos++;
		}
		return numbers.get(0);
	}

	private static int findMatchingEndBracket(int pos, String computation) {
		int count = 1;
		while(count != 0) {
			pos++;
			if(computation.charAt(pos) == '(') {
				count++;
			}
			if(computation.charAt(pos) == ')') {
				count--;
			}
		}
		return pos;

	}

}
