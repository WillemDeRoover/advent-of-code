package _2021.day17;

import static java.lang.Long.parseLong;
import static java.lang.String.format;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class Puzzle {

	private static final int targetX1 = 119;
	private static final int targetX2 = 176;
	private static final int targetY1 = -84;
	private static final int targetY2 = -141;

	public static void main(String[] args) {

		int highest = 0;
		int count = 0;
		for (int startI = 0; startI < 200; startI++) {
			for (int startJ = -141; startJ < 141; startJ++) {
				int currentHighest = findTrajectory(startI, startJ);
				if (currentHighest > highest) {
					highest = currentHighest;
				}
				if (currentHighest >= 0) {
					count++;
				}
			}
		}
		System.out.println(highest);
		System.out.println(count);

	}

	private static int findTrajectory(int startI, int startJ) {
		List<Integer> currentPosition = List.of(0, 0);
		int i = startI, j = startJ;
		int highest = 0;
		do {
			currentPosition = List.of(currentPosition.get(0) + i, currentPosition.get(1) + j);
			if (currentPosition.get(1) > highest) {
				highest = currentPosition.get(1);
			}
			if (i > 0) {
				i--;
			}
			j--;
			if (currentPosition.get(0) >= targetX1 && currentPosition.get(0) <= targetX2 && currentPosition.get(1) <= targetY1 && currentPosition.get(1) >= targetY2) {
				return highest;
			}
		} while (currentPosition.get(0) < targetX2 && currentPosition.get(1) > targetY2);
		return -1;
	}

}
