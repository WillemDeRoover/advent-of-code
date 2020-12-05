package _2020.day03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		char[][] geology = Files.lines(Paths.get("src/_2020/day03/input.txt"))
				.map(String::toCharArray)
				.toArray(char[][]::new);

		long treeCount = calculateTreeCount(geology, 1, 1)
				* calculateTreeCount(geology, 3, 1)
				* calculateTreeCount(geology, 5, 1)
				* calculateTreeCount(geology, 7, 1)
				* calculateTreeCount(geology, 1, 2);
		System.out.println(treeCount);
	}

	private static long calculateTreeCount(char[][] geology, int width, int height) {
		int x = 0, y = 0, count = 0;
		int maxWidth = geology[0].length;
		while(y < geology.length) {
			if(geology[y][x] == '#') {
				count++;
			}
			x = (x + width) % maxWidth;
			y = y + height;
		}
		return count;
	}

}
