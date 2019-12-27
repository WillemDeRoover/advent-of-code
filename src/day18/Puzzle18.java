package day18;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

public class Puzzle18 {

	private static String INPUT = "src/day18/vaultinput.txt";
	public static final List<Character> ENTRANCES = Arrays.asList('@', '$', '%', '&');

	public static void main(String[] args) throws IOException {
		Maze maze = new Maze(INPUT);
		Map<Character, List<Path>> keyGraph = createKeyGraph(maze);
		System.out.println(findShortestPathToAllKeys(keyGraph));
	}

	private static Map<Character, List<Path>> createKeyGraph(Maze maze) {
		return maze.getKeysAndEntranceLocation().entrySet().stream()
				.collect(toMap(Map.Entry::getKey, entry -> createPaths(entry.getKey(), entry.getValue(), maze)));
	}

	private static List<Path> createPaths(Character fromKey, Location location, Maze maze) { ;
		List<Path> paths = new ArrayList<>();
		Queue<Location> searchQueue = new LinkedList<>();
		Set<Location> visitedLocation = new HashSet<>();
		searchQueue.add(location);
		while(!searchQueue.isEmpty()){
			Location currentLocation = searchQueue.remove();
			visitedLocation.add(currentLocation);
			if(maze.isKey(currentLocation) && currentLocation.currentDistance != 0) {
				char toKey = maze.getTile(currentLocation);
				paths.add(new Path(fromKey, toKey, currentLocation.currentDistance, currentLocation.currentDoors, currentLocation.currentKeys));
				currentLocation.currentKeys.add(toKey);
			} else if (maze.isDoor(currentLocation)) {
				currentLocation.currentDoors.add(maze.getTile(currentLocation));
			}
			Set<Location> nextLocations = calculateNextLocations(currentLocation, maze);
			nextLocations.removeAll(visitedLocation);
			nextLocations.removeAll(searchQueue);
			searchQueue.addAll(nextLocations);
		}
		return paths;
	}

	private static Set<Location> calculateNextLocations(Location currentLocation, Maze maze) {
		return Stream.of(currentLocation.getNorth(), currentLocation.getSouth(), currentLocation.getEast(), currentLocation.getWest())
				.filter(location -> !maze.isWall(location))
				.collect(toSet());
	}

	private static int findShortestPathToAllKeys(Map<Character, List<Path>> keyGraph) {
		Set<Character> allKeys = keyGraph.keySet().stream().filter(key -> !ENTRANCES.contains(key)).collect(toSet());
		Map<State, Integer> stateMap = initStateMap(keyGraph);

		while(true) {
			Map.Entry<State, Integer> lowestDistanceEntry = stateMap.entrySet().stream().min(Comparator.comparing(Map.Entry::getValue)).get();
			State currentState = lowestDistanceEntry.getKey();
			int currentDistance = lowestDistanceEntry.getValue();
			stateMap.remove(currentState);

			if(currentState.keySet.containsAll(allKeys)) {
				return currentDistance;
			}

			for (Path availablePath : getAvailablePaths(currentState, keyGraph)) {
				State state = currentState.updateWith(availablePath);
				Integer previousDistance = stateMap.getOrDefault(state, Integer.MAX_VALUE);
				int newDistance = currentDistance + availablePath.distance;
				if(newDistance < previousDistance) {
					stateMap.put(state, newDistance);
				}
			}
		}
	}

	private static Map<State, Integer> initStateMap(Map<Character, List<Path>> keyGraph) {
		Set<Character> entrances = keyGraph.keySet().stream().filter(ENTRANCES::contains).collect(toSet());
		Map<State, Integer> stateMap = new HashMap<>();
		stateMap.put(new State(entrances, emptySet()), 0);
		return stateMap;
	}

	private static List<Path> getAvailablePaths(State currentState, Map<Character, List<Path>> keyGraph) {
		return currentState.currentPositions.stream()
				.flatMap(currentPosition -> getAvailablePaths(currentPosition, currentState.keySet, keyGraph).stream())
				.collect(toList());
	}

	private static List<Path> getAvailablePaths(char currentKey, Set<Character> currentKeySet, Map<Character, List<Path>> keyGraph) {
		return keyGraph.get(currentKey).stream()
						.filter(path -> !currentKeySet.contains(path.toKey)) //we don't have the destination key yet
						.filter(path -> path.canReachKey(currentKeySet)) //we have all the keys to reach the destination key
						.filter(path -> currentKeySet.containsAll((path.keys)))//we have all already collected all keys on the path to the destination key
						.collect(toList());
	}

	static class State {
		Set<Character> currentPositions;
		Set<Character> keySet;

		State(Set<Character> currentPositions, Set<Character> keySet) {
			this.currentPositions = currentPositions;
			this.keySet = keySet;
		}

		State updateWith(Path path) {
			State updatedState = new State(new HashSet<>(currentPositions), new HashSet<>(keySet));
			updatedState.currentPositions.remove(path.fromKey);
			updatedState.currentPositions.add(path.toKey);
			updatedState.keySet.add(path.toKey);
			return updatedState;
		}

		@Override
		public boolean equals(Object o) {
			State state = (State) o;
			return Objects.equals(currentPositions, state.currentPositions) &&
					Objects.equals(keySet, state.keySet);
		}

		@Override
		public int hashCode() {
			return Objects.hash(currentPositions, keySet);
		}
	}

	static class Path {
		char fromKey;
		char toKey;
		int distance;
		Set<Character> doors;
		Set<Character> keys; // keys that are on the path between fromKey and toKey

		Path(char fromKey, char toKey, int distance, Set<Character> doors, Set<Character> keys) {
			this.fromKey = fromKey;
			this.toKey = toKey;
			this.distance = distance;
			this.doors = doors.stream().map(Character::toLowerCase).collect(toSet());
			this.keys = new HashSet<>(keys);
		}

		boolean canReachKey(Set<Character> keys) {
			return keys.containsAll(doors);
		}
	}

	static class Location {
		int x;
		int y;
		int currentDistance;
		Set<Character> currentKeys;
		Set<Character> currentDoors;

		Location(int x, int y) {
			this.x = x;
			this.y = y;
			this.currentDistance = 0;
			this.currentKeys = new HashSet<>();
			this.currentDoors = new HashSet<>();
		}

		private Location(int x, int y, int currentDistance, Set<Character> currentDoors, Set<Character> currentKeys) {
			this.x = x;
			this.y = y;
			this.currentDistance = currentDistance;
			this.currentDoors = new HashSet<>(currentDoors);
			this.currentKeys = new HashSet<>(currentKeys);
		}

		Location getNorth() {
			return new Location(x, y + 1, currentDistance + 1, currentDoors, currentKeys);
		}

		Location getSouth() {
			return new Location(x, y - 1, currentDistance + 1, currentDoors, currentKeys);
		}

		Location getEast() {
			return new Location(x + 1, y, currentDistance + 1, currentDoors, currentKeys);
		}

		Location getWest() {
			return new Location(x - 1, y, currentDistance + 1, currentDoors, currentKeys);
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

		Maze(String url) throws IOException {
			tiles = Files.lines(Paths.get(url)).collect(toList());
		}

		char getTile(Location location) {
			return tiles.get(location.y).charAt(location.x);
		}

		Map<Character, Location> getKeysAndEntranceLocation() {
			int entranceIndex = 0;
			Map<Character, Location> keyLocationMap = new HashMap<>();
			for (int y = 0; y < tiles.size(); y++) {
				for(int x = 0; x < tiles.get(y).length(); x++) {
					char tile = tiles.get(y).charAt(x);
					if(isKey(tile)) {
						keyLocationMap.put(tile, new Location(x, y));
					}
					if(isEntrance(tile)) {
						keyLocationMap.put(ENTRANCES.get(entranceIndex++), new Location(x, y));
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
