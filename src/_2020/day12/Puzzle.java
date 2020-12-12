package _2020.day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		List<String> instructions = Files.lines(Paths.get("src/_2020/day12/input.txt"))
				.collect(Collectors.toList());

		int[] northEast = {0, 0};
		int[] wpNorthEast = {1, 10};

//		List<int[]> vectors= List.of(new int[]{0, 1}, new int[]{-1, 0}, new int[]{0, -1}, new int[]{1, 0});
//		int currentDirection = 0;

		for (String instruction : instructions) {
			String direction = instruction.substring(0, 1);
			int amount = Integer.parseInt(instruction.substring(1));
			if(direction.equals("N")) {
//				northEast[0] += amount;
				wpNorthEast[0] += amount;
			}
			if(direction.equals("S")) {
//				northEast[0] -= amount;
				wpNorthEast[0] -= amount;
			}
			if(direction.equals("E")) {
//				northEast[1] += amount;
				wpNorthEast[1] += amount;
			}
			if(direction.equals("W")) {
//				northEast[1] -= amount;
				wpNorthEast[1] -= amount;
			}
			if(direction.equals("F")) {
//				int[] vector = vectors.get(currentDirection);
//				northEast[0] += amount * vector[0];
//				northEast[1] += amount * vector[1];
				northEast[0] += amount * wpNorthEast[0];
				northEast[1] += amount * wpNorthEast[1];
			}
			if(direction.equals("R")) {
				int turn = amount / 90;
//				currentDirection = (currentDirection + turn) % vectors.size();
				for(int i = 0; i < turn; i++) {
					wpNorthEast = new int[]{wpNorthEast[1] * -1, wpNorthEast[0]};
				}
			}
			if(direction.equals("L")) {
				int turn = amount / 90;
//				currentDirection = (currentDirection - turn + vectors.size()) % vectors.size();
				for(int i = 0; i < turn; i++) {
					wpNorthEast = new int[] {wpNorthEast[1], wpNorthEast[0] * -1};
				}
			}
		}

		System.out.println(Math.abs(northEast[0]) + Math.abs(northEast[1]));
	}
}
