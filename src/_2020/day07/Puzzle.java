package _2020.day07;

import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

	public class Puzzle {

		private static Map<String, String> bagMap;

		public static void main(String[] args) throws IOException {
			bagMap = Files.lines(Paths.get("src/_2020/day07/input.txt"))
					.collect(toMap(Puzzle::parentBag, Function.identity()));

			System.out.println(containsShinyGold());
			System.out.println(countChildBags("shiny gold"));
		}

		private static String parentBag(String s) {
			Matcher matcher = Pattern.compile("^(\\w+\\s\\w+)").matcher(s);
			matcher.find();
			return matcher.group(0);
		}

		private static long containsShinyGold() {
			return bagMap.keySet()
					.stream()
					.filter(Puzzle::containsShinyGold)
					.count();
		}

		private static boolean containsShinyGold(String s) {
			String content = bagMap.get(s);
			Matcher matcher = Pattern.compile("(\\d)\\s(\\w+\\s\\w+)").matcher(content);
			List<String> childBags = new ArrayList<>();
			while(matcher.find()) {
				if (matcher.group(2).equals("shiny gold")) {
					return true;
				}
				childBags.add(matcher.group(2));
			}
			return childBags.stream().anyMatch(Puzzle::containsShinyGold);
		}

		private static int countChildBags(String startBag) {
			String content = bagMap.get(startBag);
			Matcher matcher = Pattern.compile("(\\d)\\s(\\w+\\s\\w+)").matcher(content);
			int count = 0;
			while (matcher.find()) {
				int amount =  Integer.parseInt(matcher.group(1));
				String bagName = matcher.group(2);
				count += amount + (amount * countChildBags(bagName));
			}
			return count;
		}

	}
