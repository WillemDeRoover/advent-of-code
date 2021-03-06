package _2020.day06;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		String[] allAnswers = Files.readString(Paths.get("src/_2020/day06/input.txt")).split("\\n\\n");
		calculateAnyone(allAnswers);
		calculateEveryone(allAnswers);
	}

	private static void calculateAnyone(String[] allAnswers) throws IOException {
		long total = Stream.of(allAnswers)
				.map(s -> s.replaceAll("\\n", ""))
				.mapToLong(s -> s.chars().distinct().count())
				.sum();

		System.out.println(total);
	}

	private static void calculateEveryone(String[] allAnswers) throws IOException {
		long total = Stream.of(allAnswers)
				.mapToLong(Puzzle::countAnswerByGroup)
				.sum();

		System.out.println(total);
	}

	private static long countAnswerByGroup(String s) {
		List<String> groupAnswers = List.of(s.split("\\n"));
		Set<Integer> answeredByEveryone = groupAnswers.get(0).chars().boxed().collect(toSet());
		groupAnswers.stream()
				.map(individualAnswer -> individualAnswer.chars().boxed().collect(toSet()))
				.forEach(answeredByEveryone::retainAll);
		return answeredByEveryone.size();
	}

}
