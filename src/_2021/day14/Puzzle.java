package _2021.day14;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Puzzle {

	public static void main(String[] args) throws IOException {

		String template = Files.lines(Paths.get("src/_2021/day14/input.txt"))
				.limit(1)
				.collect(toList()).get(0);

		Map<String, String> sequenceMap = Files.lines(Paths.get("src/_2021/day14/input.txt"))
				.skip(2)
				.map(s -> s.split(" -> "))
				.collect(toMap(a -> a[0], a -> a[1]));

		Map<String, Long> charCount = range(0, template.length())
				.map(template::charAt)
				.mapToObj(c -> (char) c + "")
				.collect(groupingBy(Function.identity(), counting()));


		Map<String, Long> sequenceCount = range(0, template.length() - 1 )
				.mapToObj(i -> template.substring(i, i + 2))
				.collect(groupingBy(Function.identity(), counting()));

		for (int i = 0; i < 40; i++) {
			Map<String, Long> newSequenceCount = new HashMap<>();
			for (Map.Entry<String, Long> entry : sequenceCount.entrySet()) {
				String sequence = entry.getKey();
				String insert = sequenceMap.get(sequence);
				String pre = sequence.charAt(0) + insert;
				String post = insert + sequence.charAt(1);
				newSequenceCount.put(pre, newSequenceCount.getOrDefault(pre,0L) + entry.getValue());
				newSequenceCount.put(post, newSequenceCount.getOrDefault(post, 0L) + entry.getValue());
				charCount.put(insert, charCount.getOrDefault(insert, 0L) + entry.getValue());
			}
			sequenceCount = newSequenceCount;
		}

		List<Long> sortedCount = charCount.values().stream().sorted().collect(toList());
		System.out.println(sortedCount.get(sortedCount.size()-1) - sortedCount.get(0));

	}

}
