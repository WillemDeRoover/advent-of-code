package day18;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

//TODO clean up and optimize
public class Puzzle18 {
	private static char WALL = '#';

	private static String INPUT = "src/day18/input.txt";
	private static String VAULT_INPUT = "src/day18/vaultInput.txt";

	private static List<String> createMaze(String url) throws IOException {
		return Files.lines(Paths.get(url)).collect(toList());
	}
	
	private static char ENTRANCE = '@';

	private static List<String> maze;
	private static Queue<Location> searchLocations = new LinkedList<>();
	private static Set<Location> visitedLocations = new HashSet<>();
	private static int minDistance = Integer.MAX_VALUE;
	private static Map<Node, Integer> nodeToDistanceMap = new HashMap<>();
	private static Set<Character> allKeys;

	public static void main(String[] args) throws IOException {
		maze = createMaze(VAULT_INPUT);
		allKeys = getAllKeysInVault();
		determineRoutesBetweenKeys();
		nodeToDistanceMap.put(createEntranceNode(), 0);

		while(minDistance == Integer.MAX_VALUE) {
			searchKeys();
		}

		System.out.println(minDistance);

	}

	private static void determineRoutesBetweenKeys() {
	}

	private static Node createEntranceNode() {
		Set<Location> locations = new HashSet<>();
		for(int y = 0; y < maze.size(); y++) {
			for (int x = 0; x < maze.get(y).length(); x++) {
				char tile = maze.get(y).charAt(x);
				if(tile == ENTRANCE) {
					locations.add(new Location(x, y, 0));
				}
			}
		}

		return new Node(emptySet(), locations);
	}


	private static void searchKeys() {
		Map.Entry<Node, Integer> entry = nodeToDistanceMap.entrySet().stream().min(Comparator.comparing(Map.Entry::getValue)).get();
		Node currentNode = entry.getKey();
		Integer currentDistance = entry.getValue();
		nodeToDistanceMap.remove(currentNode);
		if(currentNode.getAvailableKeySet().containsAll(allKeys)) {
			minDistance = currentDistance;
		}

		Set<Location> reachableKeyLocations = getAllReachableKeys(currentNode);
		for (Location reachableKeyLocation : reachableKeyLocations) {

			char key = reachableKeyLocation.getTile();

			Set<Character> keySet = new HashSet<>(currentNode.getAvailableKeySet());
			keySet.add(key);

			Set<Location> newLocations = new HashSet<>(currentNode.getLocations());
			newLocations.remove(reachableKeyLocation.originalLocation);
			newLocations.add(reachableKeyLocation);
			Node newNode = new Node(keySet, newLocations);

			int newDistance = currentDistance + reachableKeyLocation.distance;
			int existingDistance = nodeToDistanceMap.getOrDefault(newNode, Integer.MAX_VALUE);
			if(existingDistance >= newDistance) {
				nodeToDistanceMap.put(newNode, newDistance);
			}
		}
	}


	private static Set<Character> getAllKeysInVault() {
		Set<Character> keys = new HashSet<>();
		for(int y = 0; y < maze.size(); y++) {
			for (int x = 0; x < maze.get(y).length(); x++) {
				char tile = maze.get(y).charAt(x);
				if(isKey(tile)) {
					keys.add(tile);
				}
			}
		}
		return keys;
	}

	private static Set<Location> getAllReachableKeys(Node node) {
		searchLocations = new LinkedList<>();
		node.getLocations().stream().map(Location::reset).forEach(location -> searchLocations.add(location));
		visitedLocations = new HashSet<>();
		Set<Location> reachableKeyLocations = new HashSet<>();
		while(!searchLocations.isEmpty()) {
			findKeyLocations(node.getAvailableKeySet()).ifPresent(reachableKeyLocations::add);
		}
		return reachableKeyLocations;
	}


	private static Optional<Location> findKeyLocations(Set<Character> currentKeySet) {
		Location currentLocation = searchLocations.remove();
		char currentTile = currentLocation.getTile();
		if(isKey(currentTile) && !currentKeySet.contains(currentTile)) {
			return Optional.of(currentLocation);
		} else if(isDoor(currentTile)) {
			char key = Character.toLowerCase(currentTile);
			if(currentKeySet.contains(key)) {
				findNextPaths(currentLocation);
			}
		} else {
			findNextPaths(currentLocation);
		}
		return Optional.empty();
	}

	private static boolean isKey(char currentTile) {
		return Character.isLetter(currentTile) && Character.isLowerCase(currentTile);
	}

	private static boolean isDoor(char currentTile) {
		return Character.isLetter(currentTile) && Character.isUpperCase(currentTile);
	}

	private static void findNextPaths(Location currentLocation) {
		findHall(currentLocation).stream()
				.filter(path -> !visitedLocations.contains(path))
				.forEach(path -> {
					searchLocations.add(path);
					visitedLocations.add(path);
				});
	}

	private static List<Location> findHall(Location location) {
		Location northPath = new Location(location.x, location.y + 1, location.distance + 1, location.originalLocation);
		Location southPath = new Location(location.x, location.y - 1, location.distance + 1, location.originalLocation);
		Location eastPath = new Location(location.x + 1, location.y, location.distance + 1, location.originalLocation);
		Location westPath = new Location(location.x -1, location.y, location.distance + 1, location.originalLocation);
		return Stream.of(northPath, southPath, eastPath, westPath)
				.filter(path -> path.getTile() != WALL)
				.collect(toList());
	}

	private static class Node {
		Set<Character> availableKeySet;
		Set<Location> location;

		public Node(Set<Character> availableKeySet, Set<Location> location) {
			this.availableKeySet = availableKeySet;
			this.location = location;
		}

		public Set<Character> getAvailableKeySet() {
			return availableKeySet;
		}

		public Set<Location> getLocations() {
			return location;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			Node node = (Node) o;
			return Objects.equals(availableKeySet, node.availableKeySet) &&
					Objects.equals(location, node.location);
		}

		@Override
		public int hashCode() {
			return Objects.hash(availableKeySet, location);
		}
	}

	private static class Location {
		int x;
		int y;
		int distance;
		Location originalLocation;

		public Location(int x, int y, int distance) {
			this.x = x;
			this.y = y;
			this.distance = distance;
			originalLocation = this;
		}

		public Location(int x, int y, int distance, Location location) {
			this.x = x;
			this.y = y;
			this.distance = distance;
			this.originalLocation = location;
		}

		char getTile() {
			return maze.get(y).charAt(x);
		}

		Location reset() {
			return new Location(x, y, 0);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			Location location = (Location) o;
			return x == location.x &&
					y == location.y;
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
	}

}
