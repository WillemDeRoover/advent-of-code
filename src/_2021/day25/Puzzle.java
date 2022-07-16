package _2021.day25;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		char[][] next = Files.lines(Paths.get("src/_2021/day25/input.txt"))
				.map(String::toCharArray)
				.toArray(char[][]::new);

		char[][] current = new char[next.length][next[0].length];
		int count = 0;

		while (!Arrays.equals(current, next)) {
			current = next;
			next = new char[current.length][current[0].length];

			for (int i = 0; i < current.length; i++) {
				for (int j = 0; j < current[i].length; j++) {
					int nextHorizontalPosition = (j + 1) % current[i].length;
					if(current[i][j] == '>' && current[i][nextHorizontalPosition] == '.') {
						next[i][nextHorizontalPosition] = '>';
						next[i][j++] = '.';
					} else {
						next[i][j] = current[i][j];
					}

				}
			}

			char[][] intermediate = next;
			next = new char[intermediate.length][intermediate[0].length];

			for (int j = 0; j < intermediate[0].length; j++) {
				for (int i = 0; i < intermediate.length; i++) {
					int nextVerticalPosition = (i + 1) % intermediate.length;
					if(intermediate[i][j] == 'v' && intermediate[nextVerticalPosition][j] == '.') {
						next[nextVerticalPosition][j] = 'v';
						next[i++][j] = '.';
					} else {
						next[i][j] = intermediate[i][j];
					}

				}
			}

			count++;
		}

		System.out.println(count);
	}

	private static boolean equals(char[][] a, char[][] b) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				if (a[i][j] != b[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

}
