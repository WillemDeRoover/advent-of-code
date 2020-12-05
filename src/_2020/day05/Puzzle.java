package _2020.day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Puzzle {

	private static final Pattern keyValuePairPattern = Pattern.compile("(\\S+):(\\S+)");
	private static final Pattern hairColorPattern = Pattern.compile("^#[\\w\\d]{6}$");
	private static final Pattern pidPattern = Pattern.compile("^\\d{9}$");

	private static final Set<String> eyeColors = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

	private static final Map<String, List<Predicate<String>>> passwordFieldMap = Map.of(
			"byr", List.of(s -> between(s, 1920, 2002)),
			"iyr", List.of(s -> between(s, 2010, 2020)),
			"eyr", List.of(s -> between(s, 2020, 2030)),
			"hgt", List.of(s -> height(s, "cm", 150, 193), s -> height(s, "in", 59, 76)),
			"hcl", List.of(s -> hairColorPattern.matcher(s).matches()),
			"ecl", List.of(eyeColors::contains),
			"pid", List.of(s -> pidPattern.matcher(s).matches()),
			"cid", List.of(s -> true)
			);


	public static void main(String[] args) throws IOException {
		String file = Files.readString(Paths.get("src/_2020/day04/input1.txt"));
		long count = Stream.of(file.split("\\n\\n"))
				.filter(Puzzle::containsRequiredFields)
				.count();

		System.out.println(count);
	}

	private static boolean containsRequiredFields(String s) {
		Matcher matcher = keyValuePairPattern.matcher(s);
		Set<String> presentAndValidFields = new HashSet<>();
		while(matcher.find()) {
			String field = matcher.group(1);
			passwordFieldMap.get(field).stream()
					.filter(p -> p.test(matcher.group(2)))
					.findFirst()
					.ifPresent(p -> presentAndValidFields.add(field));
		}

		Set<String> requiredFields = new HashSet<>(passwordFieldMap.keySet());
		requiredFields.removeAll(presentAndValidFields);
		return requiredFields.isEmpty() || (requiredFields.size() == 1 && requiredFields.contains("cid"));
	}

	private static boolean between(String s, int lowerbound, int higherBound) {
		int i = Integer.parseInt(s);
		return i >= lowerbound && i <= higherBound;
	}

	private static boolean height(String s, String measure, int lowerBound, int upperBound) {
		if(!s.endsWith(measure)) {
			return false;
		}
		return between(s.replace(measure, ""), lowerBound, upperBound);
	}

}
