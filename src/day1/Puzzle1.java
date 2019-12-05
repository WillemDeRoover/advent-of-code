package day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Puzzle1 {

	public static void main(String[] args) throws IOException {
		double fuelCalculation = Files.lines(Paths.get("day1input.txt"))
				.map(Double::valueOf)
				.mapToDouble(Puzzle1::calculateFuelRec)
				.sum();
		System.out.println("result:" + fuelCalculation);
	}

	private static double calculateFuel(double mass) {
		return Math.floor(mass / 3) - 2;
	}

	private static double calculateFuelRec(double mass) {
		if(mass > 0) {
			double fuel = calculateFuel(mass);
			return fuel + calculateFuelRec(fuel);
		}
		return 0;
	}


}
