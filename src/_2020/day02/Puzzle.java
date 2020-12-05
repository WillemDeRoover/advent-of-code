package _2020.day02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {
	private static Pattern pattern = Pattern.compile("(\\d+)-(\\d+)\\s(\\w):\\s(\\w+)");

	public static void main(String[] args) throws IOException {
		long correctPasswordTotal1 = Files.lines(Paths.get("src/_2020/day02/input.txt"))
				.filter(Puzzle::isCorrectPassword)
				.count();
		System.out.println(correctPasswordTotal1);

		long correctPasswordTotal2 = Files.lines(Paths.get("src/_2020/day02/input.txt"))
				.filter(Puzzle::isCorrectPassword2)
				.count();
		System.out.println(correctPasswordTotal2);
	}

	public static boolean isCorrectPassword(String passwordString) {
		Matcher matcher = pattern.matcher(passwordString);
		matcher.matches();
		int min = Integer.parseInt(matcher.group(1));
		int max = Integer.parseInt(matcher.group(2));
		char character = matcher.group(3).charAt(0);
		String password = matcher.group(4);
		long total = password.chars().filter(ch -> ch == character).count();
		return total >= min && total <= max;
	}

	public static boolean isCorrectPassword2(String passwordString) {
		Matcher matcher = pattern.matcher(passwordString);
		matcher.matches();
		int first = Integer.parseInt(matcher.group(1));
		int second = Integer.parseInt(matcher.group(2));
		char character = matcher.group(3).charAt(0);
		String password = matcher.group(4);
		return password.charAt(first - 1) == character ^ password.charAt(second- 1) == character;
	}
}
