package _2021.day19;

import static java.lang.Integer.parseInt;
import static java.nio.file.Files.readString;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Puzzle {

	private static final int x = 0;
	private static final int y = 1;
	private static final int z = 2;

	private static final List<Function<List<Integer>, List<Integer>>> ROTATIONS = List.of(
				list -> List.of(list.get(x), list.get(y), list.get(z)),
				list -> List.of(list.get(z), list.get(x), list.get(y)),
				list -> List.of(list.get(y), list.get(z), list.get(x)),

				list -> List.of(-list.get(z), list.get(y), list.get(x)),
				list -> List.of(list.get(x), -list.get(z), list.get(y)),
				list -> List.of(list.get(y), list.get(x), -list.get(z)),

				list -> List.of(-list.get(x), list.get(y), -list.get(z)),
				list -> List.of(-list.get(z), -list.get(x), list.get(y)),
				list -> List.of( list.get(y), -list.get(z), -list.get(x)),

				list -> List.of(list.get(z), list.get(y), -list.get(x)),
				list -> List.of(-list.get(x), list.get(z), list.get(y)),
				list -> List.of(list.get(y), -list.get(x), list.get(z)),

				list -> List.of(list.get(x), list.get(z), -list.get(y)),
				list -> List.of(-list.get(y), list.get(x), list.get(z)),
				list -> List.of(list.get(z), -list.get(y), list.get(x)),

				list -> List.of(list.get(z), -list.get(x), -list.get(y)),
				list -> List.of(-list.get(y), list.get(z), -list.get(x)),
				list -> List.of(-list.get(x), -list.get(y), list.get(z)),

				list -> List.of(-list.get(x), -list.get(z), -list.get(y)),
				list -> List.of(-list.get(y), -list.get(x), -list.get(z)),
				list -> List.of(-list.get(z), -list.get(y), -list.get(x)),

				list -> List.of(-list.get(z), list.get(x), -list.get(y)),
				list -> List.of(-list.get(y), -list.get(z), list.get(x)),
				list -> List.of(list.get(x), -list.get(y), -list.get(z))
	);

	public static void main(String[] args) throws IOException {
		Queue<Scanner> scanners = createScannerMap(readString(Paths.get("src/_2021/day19/input.txt")));
		Set<List<Integer>> knownBeacons = scanners.poll().beacons;
		Set<List<Integer>> offsets = new HashSet<>();
		outer: while(!scanners.isEmpty()) {
			Scanner currentScanner = scanners.poll();
			Set<List<Integer>> beacons = currentScanner.beacons;
				for (Function<List<Integer>, List<Integer>> rotation : ROTATIONS) {
					Set<List<Integer>> rotatedBeacons = beacons.stream().map(rotation).collect(toSet());
					Map<List<Integer>, Long> offsetMap = rotatedBeacons.stream()
							.flatMap(newCoordinate -> calculateDistance(knownBeacons, newCoordinate).stream())
							.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

					Optional<List<Integer>> optionalOffset = offsetMap.entrySet().stream()
							.filter(e -> e.getValue() >= 12)
							.map(Map.Entry::getKey).findFirst();

					if (optionalOffset.isPresent()) {
						List<Integer> offset = optionalOffset.get();
						Set<List<Integer>> offsettedBeacons = recalculateBeacons(rotatedBeacons, offset);
						knownBeacons.addAll(offsettedBeacons);
						offsets.add(offset);
						continue outer;
					}
				}
				scanners.add(currentScanner);
			}
		System.out.println(knownBeacons.size());
		System.out.println(calculateManhattanDistance(offsets));
	}

	private static int calculateManhattanDistance(Set<List<Integer>> knownBeacons) {
		int maxDistance = Integer.MIN_VALUE;

		for (List<Integer> outer : knownBeacons) {
			for (List<Integer> inner : knownBeacons) {
				int distance = outer.get(x) - inner.get(x) + outer.get(y) - inner.get(y) + outer.get(z) - inner.get(z);
				if(distance > maxDistance) {
					maxDistance = distance;
				}
			}
		}
		return maxDistance;
	}

	private static Set<List<Integer>> recalculateBeacons(Set<List<Integer>> beacons, List<Integer> offset) {
		return beacons.stream()
				.map(beacon -> List.of(beacon.get(x) - offset.get(x), beacon.get(y) - offset.get(y), beacon.get(z) - offset.get(z)))
				.collect(toSet());
	}

	private static Set<List<Integer>> calculateDistance(Set<List<Integer>> knowCoordinates, List<Integer> coordinate) {
		return knowCoordinates.stream()
				.map(knownCoordinate -> List.of(coordinate.get(x) - knownCoordinate.get(x), coordinate.get(y) - knownCoordinate.get(y), coordinate.get(z) - knownCoordinate.get(z)))
				.collect(toSet());
	}

	private static Queue<Scanner> createScannerMap(String input) {
		Queue<Scanner> scanners = new LinkedList<>();
		String[] scannerBlocks = input.split("\n\n");
		for (String scannerBlock : scannerBlocks) {
			String[] beaconStrings = scannerBlock.split("\n");
			int scannerId = parseInt(beaconStrings[0].replaceAll("\\D", ""));
			Set<List<Integer>> beacons = new HashSet<>();
			for (int i = 1; i < beaconStrings.length; i++) {
				beacons.add(Arrays.stream(beaconStrings[i].split(",")).map(Integer::parseInt).collect(Collectors.collectingAndThen(toList(), List::copyOf)));
			}
			scanners.add(new Scanner(scannerId, beacons));
		}
		return scanners;
	}

	private static class Scanner {
		public Scanner(int id, Set<List<Integer>> beacons) {
			this.id = id;
			this.beacons = beacons;
		}

		int id;
		Set<List<Integer>> beacons;
	}

}
