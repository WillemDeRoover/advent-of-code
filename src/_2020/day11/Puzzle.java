package _2020.day11;

import static java.util.Arrays.deepEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Puzzle {

	public static final char OCCUPIED = '#';
	public static final char FLOOR = '.';
	public static final char NOT_OCCUPIED = 'L';
	private static char[][] seatRows;

	public static void main(String[] args) throws IOException {
		seatRows = Files.lines(Paths.get("src/_2020/day11/input.txt"))
				.map(String::toCharArray)
				.toArray(char[][]::new);

		do {
			char[][] nextSeatRows = tick();
			if(deepEquals(seatRows, nextSeatRows)) {
				count(seatRows);
				break;
			}
			seatRows = nextSeatRows;
		} while(true);
	}

	private static char[][] tick() {
		char[][] nextSeatRows = Arrays.stream(seatRows).map(char[]::clone).toArray(char[][]::new);
		for (int y = 0; y < seatRows.length ; y++) {
			for (int x = 0; x < seatRows[0].length; x++) {
				char currentPosition = seatRows[y][x];
				if (currentPosition == NOT_OCCUPIED) {
					if (canBeOccupied(y, x)) {
						nextSeatRows[y][x] = OCCUPIED;
					}
				} else if (currentPosition == OCCUPIED) {
					if (mustBeEmptied(y, x)) {
						nextSeatRows[y][x] =  NOT_OCCUPIED;
					}
				}
			}
		}
		return nextSeatRows;
	}

	private static boolean canBeOccupied(int y, int x) {
		for(int k = -1; k <= 1; k++) {
			for(int l = -1; l <= 1; l ++) {
				char direction = sees(y, x,k, l);
				if(direction == OCCUPIED) {
					return false;
				}
			}
		}
		return true;
	}

	private static char sees(int y, int x, int vectory, int vectorx) {
		if(vectorx == 0 && vectory == 0) {
			return FLOOR;
		}
		while(true) {
			y += vectory;
			x += vectorx;
			if(y < 0 || y >= seatRows.length || x < 0 || x >= seatRows[0].length) {
				return FLOOR;
			}
			if(seatRows[y][x] != FLOOR) {
				return seatRows[y][x];
			}
		}
	}

	private static boolean mustBeEmptied(int y, int x) {
		int total = 0;
		for(int k = -1; k <= 1; k++) {
			for(int l = -1; l <= 1; l ++) {
				char direction = sees(y, x,k, l);
				if(direction == OCCUPIED) {
					total++;
				}
			}
		}
		return total >=5;
	}

	private static void count(char[][] seatRows) {
		int total = 0;
		for (char[] row : seatRows) {
			for(char seat: row) {
				if(seat == OCCUPIED) {
					total++;
				}
			}
		}
		System.out.println(total);
	}

}
