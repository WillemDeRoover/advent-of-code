package _2021.day20;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		String algorithm = Files.lines(Paths.get("src/_2021/day20/input.txt")).findFirst().get();
		List<String> lines = Files.lines(Paths.get("src/_2021/day20/input.txt")).skip(2).collect(toList());
		HashSet<List<Integer>> lightPixels = new HashSet<>();
		for (int y = 0; y < lines.size(); y++) {
			for (int x = 0; x < lines.get(y).length(); x++) {
				if(lines.get(y).charAt(x) == '#') {
					lightPixels.add(List.of(y, x));
				}
			}
		}

		int minX = 0;
		int minY = 0;
		int maxY = lines.size();
		int maxX = lines.get(0).length();
		boolean pixelSwitch = false;
		for (int i = 0; i < 50; i++) {

			HashSet<List<Integer>> nextLightPixels = new HashSet<>();
			for (int y = minY - 1; y <= maxY + 1; y++) {
				for (int x = minX - 1; x <= maxX + 1; x++) {

					StringBuilder binaryString = new StringBuilder();
					for (int i1 = y - 1; i1 <= y + 1; i1++) {
						for (int j = x - 1; j <= x + 1; j++) {
							if(lightPixels.contains(List.of(i1, j)) || (pixelSwitch && (i1 > maxY || i1 < minY || j > maxX || j < minX)))  {
								binaryString.append(1);
							} else {
								binaryString.append(0);
							}
						}
					}

					int position = parseInt(binaryString.toString(), 2);
					if(algorithm.charAt(position) == '#') {
							nextLightPixels.add(List.of(y, x));
						}
				}
			}
			lightPixels = nextLightPixels;
			if(algorithm.charAt(0) == ('#')) {
				pixelSwitch = !pixelSwitch;
			}
			maxY++; maxX++; minX--;minY--;
		}

		System.out.println(lightPixels.size());
	}
}
