package _2020.day17;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.lines(Paths.get("src/_2020/day17/input.txt")).collect(toList());

		Set<List<Integer>> coordinates = new HashSet<>();
		for (int y = 0; y < lines.size(); y++) {
			char[] characters = lines.get(y).toCharArray();
			for (int x = 0; x < characters.length; x++) {
				if(characters[x] == '#') {
					coordinates.add(List.of(x, y, 0, 0));
				}
			}

		}

		int length = lines.size();
		Set<List<Integer>> newCoordinates = new HashSet<>();
		for (int i = 0; i < 6; i++) {
			newCoordinates.clear();
			int growth = i +1;
			for(int x = -growth; x < length + growth; x ++) {
				for(int y = -growth; y < length + growth; y ++) {
					for(int z = -growth; z <= growth; z ++) {
						for(int w = -growth; w <= growth; w ++) {
							List<Integer> coordinate = List.of(x, y, z, w);
							int neighBours = findNeighBours(coordinate, coordinates);
							if (coordinates.contains(coordinate) && (neighBours == 2 || neighBours == 3)) {
								newCoordinates.add(coordinate);
							}
							if (!coordinates.contains(coordinate) && neighBours == 3) {
								newCoordinates.add(coordinate);
							}
						}
					}
				}
			}
			coordinates = new HashSet<>(newCoordinates);
		}
		System.out.println(coordinates.size());
	}

	private static int findNeighBours(List<Integer> coordinate, Set<List<Integer>> coordinates) {
		int count = 0;
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					for (int w = -1; w <= 1; w++) {
						if (x != 0 || y != 0 || z != 0 || w!=0) {
							count += coordinates.contains(List.of(coordinate.get(0) + x, coordinate.get(1) + y, coordinate.get(2) + z, coordinate.get(3) + w)) ? 1 : 0;
						}
					}
				}
			}
		}
		return count;
	}

}
