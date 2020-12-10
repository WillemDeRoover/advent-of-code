package _2020.day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		TreeSet<Integer> sortedJolts = Files.lines(Paths.get("src/_2020/day10/input.txt"))
				.map(Integer::parseInt)
				.collect(Collectors.toCollection(TreeSet::new));
		sortedJolts.add(sortedJolts.last() + 3);

		useAllAdapters(sortedJolts);
		findAllCombinations(sortedJolts);
	}

	private static void useAllAdapters(TreeSet<Integer> sortedJolts) {
		int currentJolt = 0;
		int ones = 0;
		int threes = 0;
		for (Integer nextJolt : sortedJolts) {
			int difference = nextJolt - currentJolt;
			if(difference == 1) {
				ones++;
			} else if(difference ==3){
				threes ++;
			}
			currentJolt = nextJolt;
		}
		System.out.println(ones * threes);
	}

	private static void findAllCombinations(TreeSet<Integer> sortedJolts) {
		Map<Integer, Long> combinations = new HashMap<>();
		combinations.put(0, 1L);
		for(Integer i : sortedJolts) {
			long total = 0;
			for(int diff = 1; diff <= 3; diff++) {
				int previousPos = i - diff;
				if(combinations.containsKey(previousPos)) {
					total += combinations.get(previousPos);
				}
			}
			combinations.put(i, total);
		}
		System.out.println(combinations.get(sortedJolts.last()));
	}

}
