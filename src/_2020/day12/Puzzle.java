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

		int north = 0;
		int east = 0;
		int wayPointNorth = 1;
		int wayPointEast = 10;
//		List<int[]> vectors= List.of(new int[]{0, 1}, new int[]{-1, 0}, new int[]{0, -1}, new int[]{1, 0});

		int currentDirection = 0;
		for (String instruction : instructions) {
			String direction = instruction.substring(0, 1);
			int amount = Integer.parseInt(instruction.substring(1));
			if(direction.equals("N")) {
//				north += amount;
				wayPointNorth += amount;
			}
			if(direction.equals("S")) {
//				north -= amount;
				wayPointNorth -= amount;
			}
			if(direction.equals("E")) {
//				east += amount;
				wayPointEast += amount;
			}
			if(direction.equals("W")) {
//				east -= amount;
				wayPointEast -= amount;
			}
			if(direction.equals("F")) {
//				int[] vector = vectors.get(currentDirection);
				for(int i = 0; i< amount; i++) {
					north +=  wayPointNorth;
					east +=  wayPointEast;
				}
			}
			if(direction.equals("R")) {
				int turn = amount / 90;
//				currentDirection = (currentDirection + turn) % vectors.size();
				for(int i = 0; i < turn; i++) {
					int temp = wayPointNorth;
					wayPointNorth = wayPointEast * -1;
					wayPointEast = temp;
				}
			}
			if(direction.equals("L")) {
				int turn = amount / 90;
//				currentDirection = (currentDirection - turn + vectors.size()) % vectors.size();
				for(int i = 0; i < turn; i++) {
					int temp = wayPointEast;
					wayPointEast = wayPointNorth * -1;
					wayPointNorth = temp;
				}
			}
		}

		System.out.println(Math.abs(north) + Math.abs(east));

	}
}
