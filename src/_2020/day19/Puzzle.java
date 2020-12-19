package _2020.day19;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TODO try this with regexes
public class Puzzle {
	private static Map<Integer, List<List<String>>> parserMap;

	public static void main(String[] args) throws IOException {
		parserMap = Files.lines(Paths.get("src/_2020/day19/input.txt"))
				.takeWhile(s -> s.matches("\\d+:.+"))
				.map(s -> s.replace("\"", ""))
				.collect(Collectors.toMap(Puzzle::getIndex, Puzzle::getParser));

		long count = Files.lines(Paths.get("src/_2020/day19/input.txt"))
				.filter(s -> s.matches("^\\w+$"))
				.map(expression -> evaluate(expression, 0))
				.filter(list -> !list.isEmpty())
				.filter(list -> list.stream().anyMatch(String::isEmpty))
				.count();

		System.out.println(count);
	}

	private static List<String> evaluate(String expression, int ruleNumber) {
		List<List<String>> rules = parserMap.get(ruleNumber);
		List<String> result = new ArrayList<>();
		for (List<String> rule : rules) {
			if(rule.get(0).equals("a") || rule.get(0).equals("b")) {
				return expression.startsWith(rule.get(0)) ? List.of(expression.substring(1)) : emptyList();
			}
			List<String> expressions = List.of(expression);
			for (String element : rule) {
				expressions = expressions.stream().map(anExpression -> evaluate(anExpression, Integer.parseInt(element))).flatMap(Collection::stream).collect(toList());
				if(expressions.isEmpty()) {
					break;
				}
			}
			if(!expressions.isEmpty()) {
				result.addAll(expressions);
			}
		}
		return result;
	}

	private static List<List<String>> getParser(String s) {
		String computations = s.split(": ")[1];
		return Stream.of(computations.split(" \\| ")).map(subRule -> subRule.split(" ")).map(Arrays::asList).collect(toList());
	}

	private static Integer getIndex(String s) {
		return Integer.valueOf(s.substring(0, s.indexOf(":")));
	}

}
