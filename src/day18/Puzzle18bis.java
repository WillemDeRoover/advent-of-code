package day18;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

public class Puzzle18bis {

	private static String INPUT = "src/day18/input.txt";

	public static void main(String[] args) throws IOException {
		Maze maze = new Maze(INPUT);
		Map<Character, Map<Character, Path>> keyGraph = createKeyGraph(maze);
		findShortestPathToAllKeys(keyGraph);
	}

	private static void findShortestPathToAllKeys(Map<Character, Map<Character, Path>> keyGraph) {

	}

	private static Map<Character, Map<Character, Path>> createKeyGraph(Maze maze) {
		return maze.getAllKeys().entrySet().stream()
				.collect(toMap(Map.Entry::getKey, entry -> createPaths(entry.getValue(), maze)));
	}


	private static Map<Character, Path> createPaths(Location location, Maze maze) {
		Map<Character, Path> paths = new HashMap<>();
		Queue<Location> searchQueue = new LinkedList<>();
		Set<Location> visitedLocation = new HashSet<>();
		searchQueue.add(location);
		while(!searchQueue.isEmpty()){
			Location currentLocation = searchQueue.remove();
			visitedLocation.add(currentLocation);
			if(maze.isKey(currentLocation) && currentLocation.currentDistance != 0) {
				char toKey = maze.getTile(currentLocation);
				paths.put(toKey, new Path(toKey, currentLocation.currentDistance, currentLocation.currentDoors));
			} else if (maze.isDoor(currentLocation)) {
				currentLocation.currentDoors.add(maze.getTile(currentLocation));
			}
			Set<Location> nextLocations = calculateNextLocations(currentLocation, maze);
			nextLocations.removeAll(visitedLocation);
			searchQueue.addAll(nextLocations);
		}
		return paths;
	}

	private static Set<Location> calculateNextLocations(Location currentLocation, Maze maze) {
		return Stream.of(currentLocation.getNorth(), currentLocation.getSouth(), currentLocation.getEast(), currentLocation.getWest())
				.filter(location -> !maze.isWall(location))
				.collect(toSet());
	}

	static class Path {
		char toKey;
		int distance;
		Set<Character> doors;

		public Path(char toKey, int distance, Set<Character> doors) {
			this.toKey = toKey;
			this.distance = distance;
			this.doors = new HashSet<>(doors);
		}
	}

	static class Location {
		int x;
		int y;
		int currentDistance;
		Set<Character> currentDoors;

		public Location(int x, int y) {
			this.x = x;
			this.y = y;
			this.currentDistance = 0;
			this.currentDoors = new HashSet<>();
		}

		public Location(int x, int y, int currentDistance, Set<Character> currentDoors) {
			this.x = x;
			this.y = y;
			this.currentDistance = currentDistance;
			this.currentDoors = new HashSet<>(currentDoors);
		}

		Location getNorth() {
			return new Location(x, y + 1, currentDistance + 1, currentDoors);
		}

		Location getSouth() {
			return new Location(x, y - 1, currentDistance + 1, currentDoors);
		}

		Location getEast() {
			return new Location(x + 1, y, currentDistance + 1, currentDoors);
		}

		Location getWest() {
			return new Location(x - 1, y, currentDistance + 1, currentDoors);
		}

		@Override
		public boolean equals(Object o) {
			Location location = (Location) o;
			return x == location.x &&
					y == location.y;
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
	}

	static class Maze {
		List<String> tiles;

		public Maze(String url) throws IOException {
			tiles = Files.lines(Paths.get(url)).collect(toList());
		}

		char getTile(Location location) {
			return tiles.get(location.y).charAt(location.x);
		}

		char getTile(int x, int y) {
			return tiles.get(y).charAt(x);
		}

		Map<Character, Location> getAllKeys() {
			Map<Character, Location> keyLocationMap = new HashMap<>();
			for (int y = 0; y < tiles.size(); y++) {
				for(int x = 0; x < tiles.get(y).length(); x++) {
					char tile = tiles.get(y).charAt(x);
					if(isKey(tile) || isEntrance(tile)) {
						keyLocationMap.put(tile, new Location(x, y));
					}
				}
			}
			return keyLocationMap;
		}

		boolean isKey(Location location) {
			return isKey(tiles.get(location.y).charAt(location.x));
		}

		boolean isKey(char tile) {
			return Character.isLetter(tile) && Character.isLowerCase(tile);
		}

		boolean isEntrance(char tile) {
			return tile == '@';
		}

		boolean isDoor(Location location) {
			return isDoor(tiles.get(location.y).charAt(location.x));
		}

		boolean isDoor(char tile) {
			return Character.isLetter(tile) && Character.isUpperCase(tile);
		}

		boolean isWall(Location location) {
			return isWall(tiles.get(location.y).charAt(location.x));
		}

		boolean isWall(char tile) {
			return tile == '#';
		}

	}

}
