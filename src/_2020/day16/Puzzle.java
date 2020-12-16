package _2020.day16;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Puzzle {
	private static final Pattern pattern = Pattern.compile("(\\d+)-(\\d+) or (\\d+)-(\\d+)");

	public static void main(String[] args) throws IOException {
		String input = Files.readString(Paths.get("src/_2020/day16/input.txt"));
		String[] inputs = input.split("\\n\\n");
		Map<String, Predicate<Integer>> rules = Stream.of(inputs[0].split("\\n"))
				.map(s -> s.split(": ")).collect(toMap(sa -> sa[0], sa -> toPredicate(sa[1])));

		List<List<Integer>> tickets = getValidTickets(inputs[2], rules);
		Map<String, List<Integer>> possibleFieldPositions = getPossibleFieldPositions(rules, tickets);

		List<String> sortedFields = possibleFieldPositions.entrySet().stream()
				.sorted(comparing(e -> e.getValue().size()))
				.map(Map.Entry::getKey)
				.collect(toList());

		for(int i = sortedFields.size() - 2; i >= 0; i--) {
			List<Integer> smallerList = possibleFieldPositions.get(sortedFields.get(i));
			List<Integer> largerList = possibleFieldPositions.get(sortedFields.get(i+1));
			largerList.removeAll(smallerList);
		}

		List<Integer> myTicket = getValidTickets(inputs[1], rules).get(0);
		long result = possibleFieldPositions.keySet().stream().filter(s -> s.startsWith("departure"))
				.map(possibleFieldPositions::get)
				.map(list -> list.get(0))
				.mapToLong(myTicket::get)
				.reduce(Math::multiplyExact).getAsLong();

		System.out.println(result);

	}

	private static Predicate<Integer> toPredicate(String s) {
		Matcher matcher = pattern.matcher(s);
		matcher.find();
		int min1 = Integer.parseInt(matcher.group(1));
		int max1 = Integer.parseInt(matcher.group(2));
		int min2 = Integer.parseInt(matcher.group(3));
		int max2 = Integer.parseInt(matcher.group(4));
		return i -> (i >= min1 && i <= max1) || (i >= min2 && i <= max2);
	}

	private static List<List<Integer>> getValidTickets(String input1, Map<String, Predicate<Integer>> rules) {
		return Stream.of(input1.split("\\n"))
				.skip(1)
				.map(s -> Stream.of(s.split(",")).map(Integer::valueOf).collect(toList()))
				.filter(ticket -> hasValidFields(ticket, rules))
				.collect(toList());
	}

	private static Map<String, List<Integer>> getPossibleFieldPositions(Map<String, Predicate<Integer>> rules, List<List<Integer>> tickets) {
		Map<String, List<Integer>> positionsMap = new HashMap<>();
		for (int i = 0; i < tickets.get(0).size(); i++) {
			for (Map.Entry<String, Predicate<Integer>> rule: rules.entrySet()) {
				boolean matches = true;
				for (List<Integer> ticket : tickets) {
					matches &= rule.getValue().test(ticket.get(i));
				}
				if(matches) {
					List<Integer> positions = positionsMap.getOrDefault(rule.getKey(), new ArrayList<>());
					positions.add(i);
					positionsMap.put(rule.getKey(), positions);
				}
			}
		}
		return positionsMap;
	}

	private static boolean hasValidFields(List<Integer> ticket, Map<String, Predicate<Integer>> rules) {
		for (Integer field : ticket) {
			if(rules.values().stream().noneMatch(rule -> rule.test(field))) {
				return false;
			}
		}
		return true;
	}

}
