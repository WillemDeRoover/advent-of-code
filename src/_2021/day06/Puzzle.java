package _2021.day06;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		int[] fish = Arrays.stream(Files.lines(Paths.get("src/_2021/day06/input.txt")).collect(toList()).get(0).split(","))
				.mapToInt(Integer::parseInt)
				.toArray();

		Map<Integer, Long> fishMap = new HashMap<>(Map.of(0, 0L, 1, 0L, 2, 0L, 3, 0L, 4, 0L, 5, 0L, 6, 0L, 7, 0L, 8, 0L));
		for (int aFish : fish) {
			fishMap.compute(aFish, (k, v) -> v+1);
		}

		for(int i = 0; i < 256; i++) {
			Map<Integer, Long> nextFishMap = new HashMap<>();
			for(int j = 0; j < 9; j++) {
				if(j != 0) {
					Long amount = fishMap.get(j);
					nextFishMap.put(j - 1, nextFishMap.getOrDefault(j - 1, 0L) + amount);
				} else {
					nextFishMap.put(6, fishMap.get(j));
					nextFishMap.put(8, fishMap.get(j));
				}

			}
			fishMap = nextFishMap;
		}

		fishMap.entrySet().stream().map(Map.Entry::getValue).reduce(Long::sum).ifPresent(System.out::println);
	}
}
