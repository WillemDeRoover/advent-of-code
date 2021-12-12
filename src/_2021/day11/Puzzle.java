package _2021.day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;

public class Puzzle {
	

	public static void main(String[] args) throws IOException {
		int[][] input = Files.lines(Paths.get("src/_2021/day11/input.txt"))
				.map(s -> s.chars().map(Character::getNumericValue).toArray())
				.toArray(int[][]::new);

		int flashCount = 0;
		int stepCount = 0;
		for (int i = 0; i < 100; i++) {
			flashCount += step(input);
			stepCount++;
		}

		System.out.println(flashCount);

		while(flashCount != 100) {
			stepCount++;
			flashCount = step(input);
		}

		System.out.println(stepCount);

	}

	private static int step(int[][] input) {
		int flashCount = 0;
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[i].length; j++) {
				input[i][j]++;
			}
		}

		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[i].length; j++) {
				if(input[i][j] > 9) {
					flashCount+= flash(i, j, input);
				};
			}
		}

		return flashCount;
	}

	private static int flash(int i, int j, int[][] input) {
		int flashCount = 1;
		input[i][j] = 0;
		for (int k = i -1; k < i +2; k++) {
			if(k >= 0 && k < 10) {
				for (int l = j -1; l < j + 2; l++) {
					if(l >= 0 && l < 10) {
						if(input[k][l] != 0) {
							int level  = ++input[k][l];
							if(level > 9) {
								flashCount+= flash(k, l, input);
							}
						}
					}
				}
			}
		}
		return flashCount;
	}


}
