package y2022.d12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Puzzle2 {

    public static final int START = 83;
    public static final int END = 69;
    private static int[][] heightMap;
    private static Map<Location, Integer> nodes = new HashMap<>();
    private static Set<Location> unvisited = new HashSet<>();
    private static Set<Location> destinations = new HashSet<>();

    public static void main(String[] args) throws IOException {
        heightMap = Files.lines(Paths.get("src/y2022/d12/input.txt"))
                .map(s -> s.chars().toArray())
                .toArray(int[][]::new);

        for (int y = 0; y < heightMap.length; y++) {
            for (int x = 0; x < heightMap[0].length; x++) {
                unvisited.add(new Location(x, y));
                if (heightMap[y][x] == START) {
                    heightMap[y][x] = 97;
                }
                if (heightMap[y][x] == END) {
                    nodes.put(new Location(x, y), 0);
                    heightMap[y][x] = 122;
                }
                if(heightMap[y][x] == 97) {
                    destinations.add(new Location(x, y));
                }
            }
        }

        while(unvisited.containsAll(destinations)) {
            Location current = unvisited.stream().min(Comparator.comparing(l -> nodes.getOrDefault(l, Integer.MAX_VALUE))).get();
            unvisited.remove(current);
            for (Location neighbour : current.getNeighbours()) {
                if(current.getHeight() - neighbour.getHeight() <= 1) {
                    Integer length = nodes.getOrDefault(neighbour, Integer.MAX_VALUE);
                    Integer lengthFromCurrent = nodes.get(current) + 1;
                    if(lengthFromCurrent < length) {
                        nodes.put(neighbour, lengthFromCurrent);
                    }
                }
            }
        }

        destinations.removeAll(unvisited);
        destinations.stream().map(nodes::get).forEach(System.out::println);
    }

    record Location(int x, int y) {

        int getHeight() {
            return heightMap[y][x];
        }

        List<Location> getNeighbours() {
            ArrayList<Location> neighBours = new ArrayList<>();
            if (y - 1 >= 0) neighBours.add(new Location(x, y - 1));
            if (y + 1 < heightMap.length) neighBours.add(new Location(x, y + 1));
            if (x - 1 >= 0) neighBours.add(new Location(x - 1, y));
            if (x + 1 < heightMap[0].length) neighBours.add(new Location(x + 1, y));
            return neighBours;
        }
    }

}