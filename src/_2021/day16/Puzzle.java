package _2021.day16;

import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.util.Comparator.comparing;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class Puzzle {

	private static int position = 0;
	private static String binaryString;

	public static void main(String[] args) throws IOException {

		binaryString = Files.lines(Paths.get("src/_2021/day16/input.txt"))
				.map(Puzzle::toBinaryString)
				.findFirst().get();

		System.out.println(readPackage());
	}

	private static Long readPackage() {
		int version = Integer.parseInt(binaryString.substring(position, position + 3), 2);
		int packetId = Integer.parseInt(binaryString.substring(position + 3, position + 6), 2);
		position += 6;
		if(packetId == 4) {
			return readLiteral(binaryString);
		} else {
			int lengthTypeId = Character.getNumericValue(binaryString.charAt(position));
			position += 1;
			List<Long> literals;
			if(lengthTypeId == 0) {
				int totalLength = Integer.parseInt(binaryString.substring(position, position + 15), 2);
				position += 15;
				literals = readOperatorWithLength(totalLength);

			} else {
				int numberOfPackages = Integer.parseInt(binaryString.substring(position, position + 11), 2);
				position += 11;
				literals = readOperatorWithPackages(numberOfPackages);
			}

			switch (packetId){
			case 0: return literals.stream().reduce(0L, Math::addExact);
			case 1: return literals.stream().reduce(1L, Math::multiplyExact);
			case 2: return literals.stream().min(Long::compareTo).get();
			case 3: return literals.stream().max(Long::compare).get();
			case 5: return literals.get(0) > literals.get(1) ? 1L : 0L;
			case 6: return literals.get(0) < literals.get(1) ? 1L : 0L;
			case 7: return literals.get(0).equals(literals.get(1)) ? 1L : 0L;
			default: return null;
			}

		}
	}

	private static String toBinaryString(String hexString) {
		return hexString.chars()
				.map(i -> Integer.parseInt((char) i + "", 16))
				.mapToObj(Integer::toBinaryString)
				.map(s -> format("%4s", s).replace(' ', '0'))
				.collect(Collectors.joining());
	}

	private static Long readLiteral(String binaryString) {
		boolean lastPackage = false;
		StringBuilder literalBuilder = new StringBuilder();

		do {
			if (Character.getNumericValue(binaryString.charAt(position)) == 0) {
				lastPackage = true;
			}
			literalBuilder.append(binaryString, position + 1, position + 5);
			position += 5;
		} while (!lastPackage);

		return parseLong(literalBuilder.toString(), 2);
	}

	private static List<Long> readOperatorWithLength(int totalLength) {
		int endPosition = position + totalLength;
		List<Long> literals = new ArrayList<>();
		while(position + 6 < endPosition) {
			literals.add(readPackage());
		}
		return literals;
	}

	private static List<Long> readOperatorWithPackages(int numberOfPackages) {
		List<Long> literals = new ArrayList<>();
		for (int i = 0; i < numberOfPackages; i++) {
			literals.add(readPackage());
		}
		return literals;
	}

}
