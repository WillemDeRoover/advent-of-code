package day10;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Puzzle10 {

	private static List<Point> asteroids;

	public static void main(String[] args) throws IOException {
		initAsteroidMap("asteroidmaptest.txt");

		Map.Entry<Point, Integer> monitoringStation = calculateLocationForMonitoringStation();
		System.out.println("The asteroid at location x: " + monitoringStation.getKey().x + " y: " + monitoringStation.getKey().y + " has a detection rate of " + monitoringStation.getValue());

		vaporizeAsteroids(monitoringStation.getKey());
	}

	private static Map.Entry<Point, Integer> calculateLocationForMonitoringStation() {
		Map<Point, Integer> asteroidDetectionMap = asteroids.stream().collect(toMap(Function.identity(), Puzzle10::calculateDetectionRate));
		return asteroidDetectionMap.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get();
	}

	private static int calculateDetectionRate(Point asteroid) {
		Map<Double, List<Point>> sortedSlopeMap = createdSortedSlopeMap(asteroid);
		return calculateDetectionRate(asteroid, sortedSlopeMap.values());
	}

	private static int calculateDetectionRate(Point asteroid, Collection<List<Point>> slopeLists) {
		int detectionRate = 0;
		for (List<Point> slopList : slopeLists) {
			int index = slopList.indexOf(asteroid);
			if (index == 0 || index == slopList.size() - 1) {
				detectionRate += 1;
			} else {
				detectionRate += 2;
			}
		}
		return detectionRate;
	}

	private static void vaporizeAsteroids(Point monitoringStation) {
		List<Map.Entry<Double, List<Point>>> sortedSlopeListOfMonitoringStation =
				createdSortedSlopeMap(monitoringStation).entrySet().stream()
						.sorted(Comparator.comparing(Map.Entry::getKey))
						.collect(toList());

		int numberOfDestroyedPlanets = 0;
		for (Map.Entry<Double, List<Point>> doubleListEntry : sortedSlopeListOfMonitoringStation) {
			List<Point> asteroidLine = doubleListEntry.getValue();
			int index = asteroidLine.indexOf(monitoringStation);
			if (index < asteroidLine.size() - 1) {
				Point point = asteroidLine.get(index + 1);
				asteroidLine.remove(index + 1);
				System.out.println("destroyed asteroid at x: " + point.x + " y: " + point.y + "(" + ++numberOfDestroyedPlanets + ")");
			}
		}

		for (Map.Entry<Double, List<Point>> doubleListEntry : sortedSlopeListOfMonitoringStation) {
			List<Point> asteroidLine = doubleListEntry.getValue();
			int index = asteroidLine.indexOf(monitoringStation);
			if (index > 0) {
				Point point = asteroidLine.get(index - 1);
				asteroidLine.remove(index - 1);
				System.out.println("destroyed asteroid at x: " + point.x + " y: " + point.y + "(" + ++numberOfDestroyedPlanets + ")");
			}
		}

	}

	private static Map<Double, List<Point>> createdSortedSlopeMap(Point asteroid) {
		Map<Double, List<Point>> slopeMap = calculateSlopeMap(asteroid);
		slopeMap.values().forEach(slopeList -> slopeList.add(asteroid));
		return slopeMap.entrySet().stream().collect(toMap(Map.Entry::getKey, entry -> sortAsteroidLine(entry.getValue())));
	}

	private static Map<Double, List<Point>> calculateSlopeMap(Point asteroid) {
		return asteroids.stream()
				.filter(otherAsteroid -> !otherAsteroid.equals(asteroid))
				.collect(groupingBy(otherAsteroid -> slope(asteroid, otherAsteroid)));
	}

	private static double slope(Point asteroid, Point otherAsteroid) {
		return (double) (otherAsteroid.y - asteroid.y) / (double) (otherAsteroid.x - asteroid.x);
	}

	private static List<Point> sortAsteroidLine(List<Point> asteroidLine) {
		return asteroidLine.stream()
				.sorted(Comparator.comparing(Point::getX).thenComparing(Point::getY))
				.collect(toList());
	}

	private static void initAsteroidMap(String asteroidFile) throws IOException {
		char[][] asteroidMap = Files.lines(Paths.get(asteroidFile))
				.map(String::toCharArray)
				.toArray(char[][]::new);

		asteroids = new ArrayList<>();
		for (int y = 0; y < asteroidMap.length; y++) {
			for (int x = 0; x < asteroidMap[y].length; x++) {
				if (asteroidMap[y][x] == '#') {
					asteroids.add(new Point(x, y));
				}
			}
		}

	}

}

