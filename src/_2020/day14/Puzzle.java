package _2020.day14;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

	private static Pattern memoryPattern = Pattern.compile("mem\\[(\\d+)]\\s=\\s(\\d+)");

	public static void main(String[] args) throws IOException {
		String bitmask = null;
		Map<Long, Long> memoryPart1 = new HashMap<>();
		Map<Long, Long> memoryPart2 = new HashMap<>();

		List<String> instructions = Files.lines(Paths.get("src/_2020/day14/input.txt"))
				.collect(toList());

		for (String s : instructions) {
			if(s.startsWith("mask")) {
				bitmask = s.replace("mask = ", "");
			}
			else {
				Matcher matcher = memoryPattern.matcher(s);
				matcher.find();
				long address = Long.parseLong(matcher.group(1));
				long content = Long.parseLong(matcher.group(2));

				memoryPart1.put(address, applyBitMaskToContent(toBinary(content), bitmask));
				applyBitMaskToAddress(toBinary(address), bitmask).forEach(anAddress -> memoryPart2.put(anAddress, content));
			}
		}

		System.out.println(memoryPart1.values().stream().mapToLong(Long::longValue).sum());
		System.out.println(memoryPart2.values().stream().mapToLong(Long::longValue).sum());

	}

	private static String toBinary(long content) {
		return String.format("%36s", Long.toBinaryString(content)).replace(' ', '0');
	}

	private static Long applyBitMaskToContent(String content, String bitmask) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < bitmask.length(); i++) {
			char bitMaskChar = bitmask.charAt(i);
			if(bitMaskChar == 'X') {
				builder.append(content.charAt(i));
			} else {
				builder.append(bitMaskChar);
			}
		}
		return Long.parseLong(builder.toString(), 2);
	}

	private static List<Long> applyBitMaskToAddress(String address, String bitmask) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < bitmask.length(); i++) {
			char bitMaskChar = bitmask.charAt(i);
			if(bitMaskChar == '0') {
				builder.append(address.charAt(i));
			} else {
				builder.append(bitMaskChar);
			}
		}
		return createAddresses(builder.toString());
	}

	private static List<Long> createAddresses(String toString) {
		if(!toString.contains("X")) {
			return List.of(Long.parseLong(toString, 2));
		} else {
			List<Long> addresses = new ArrayList<>();
			addresses.addAll(createAddresses(toString.replaceFirst("X", "0")));
			addresses.addAll(createAddresses(toString.replaceFirst("X", "1")));
			return addresses;
		}
	}

}
