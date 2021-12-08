package _2021.day02;

import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		String[][] instructions = Files.lines(Paths.get("src/_2021/day02/input.txt"))
				.map(line -> line.split(" "))
				.toArray(String[][]::new);

		calculatePosition(instructions);
		calculatePositionUsingAim(instructions);

	}

	private static void calculatePosition(String[][] instructions) {
		int position = 0;
		int depth = 0;
		for(String[] instruction: instructions) {
			int x = parseInt(instruction[1]);
			if(instruction[0].equals("up")) {
				depth -= x;
			}
			if(instruction[0].equals("down")) {
				depth += x;
			}
			if(instruction[0].equals("forward")) {
				position += x;
			}
		}
		System.out.println(position * depth);
	}

	private static void calculatePositionUsingAim(String[][] instructions) {
		int position = 0;
		int depth = 0;
		int aim = 0;

		for(String[] instruction: instructions) {
			int x = parseInt(instruction[1]);
			if(instruction[0].equals("up")) {
				aim -= x;
			}
			if(instruction[0].equals("down")) {
				aim += x;
			}
			if(instruction[0].equals("forward")) {
				position += x;
				depth += aim * x;
			}
		}

		System.out.println(position * depth);
	}

}
