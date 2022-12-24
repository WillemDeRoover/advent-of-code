package y2022.d24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

public class Puzzle {

    private static int length;
    private static int height;

    public static void main(String[] args) throws IOException {
        List<List<Set<Character>>> initMap = Files.readAllLines(Paths.get("src/y2022/d24/input.txt")).stream()
                .filter(s -> !s.contains("##"))
                .map(s -> s.substring(1, s.length() - 1))
                .map(s -> s.chars().mapToObj(i -> (char) i).map(c -> c == '.' ? Set.<Character>of() : Set.of(c)).toList())
                .toList();

        height = initMap.size();
        length = initMap.get(0).size();

        State traversal1 = traverse(new Location(-1, 0), new Location(height - 1, length - 1), initMap);
        System.out.println("part1 = " + traversal1.count);

        State traversal2 = traverse(new Location(height, length - 1), new Location(0, 0), traversal1.map);
        State traversal3 = traverse(new Location(-1, 0), new Location(height - 1, length - 1), traversal2.map);
        System.out.println("part2 = " + Stream.of(traversal1, traversal2, traversal3)
                .mapToInt(State::count)
                .sum()
        );
    }

    private static State traverse(Location start, Location end, List<List<Set<Character>>> map) {
        Set<Location> currentPositions = Set.of(start);
        List<List<Set<Character>>> currentMap = map;
        int minute = 1;

        while (!currentPositions.contains(end)) {
            List<List<Set<Character>>> nextMap = calculateNextMap(currentMap);
            Set<Location> nextPositions = currentPositions.stream()
                    .flatMap(cp -> calculateNextPositions(cp, nextMap).stream())
                    .collect(toCollection(HashSet::new));

            minute++;
            currentMap = nextMap;
            currentPositions = nextPositions;
        }

        return new State(calculateNextMap(currentMap), minute);
    }

    private static List<Location> calculateNextPositions(Location pos, List<List<Set<Character>>> next) {
        return Stream.concat(getNeighbours(pos).stream(), Stream.of(pos))
                .filter(l -> getBlizzards(l, next).isEmpty())
                .toList();
    }

    private static List<Location> getNeighbours(Location pos) {
        return Stream.of(
                        new Location(pos.row + 1, pos.col),
                        new Location(pos.row - 1, pos.col),
                        new Location(pos.row, pos.col + 1),
                        new Location(pos.row, pos.col - 1))
                .filter(l -> l.row > -1 && l.row < height)
                .filter(l -> l.col > -1 && l.col < length)
                .toList();

    }

    private static Set<Character> getBlizzards(Location pos, List<List<Set<Character>>> map) {
        try {
            return map.get(pos.row).get(pos.col);
        } catch (IndexOutOfBoundsException e) {
            return Set.of();
        }
    }

    private static List<List<Set<Character>>> calculateNextMap(List<List<Set<Character>>> map) {
        List<List<Set<Character>>> next = initMap(height, length);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < length; col++) {
                Set<Character> currentDirections = map.get(row).get(col);
                for (Character currentDirection : currentDirections) {
                    if (Stream.of('<', '^', '>', 'v').anyMatch(c -> c == currentDirection)) {
                        switch (currentDirection) {
                            case '<' -> next.get(row).get(((col - 1) + length) % length).add('<');
                            case '^' -> next.get(((row - 1) + height) % height).get(col).add('^');
                            case '>' -> next.get(row).get((col + 1) % length).add('>');
                            case 'v' -> next.get((row + 1) % height).get(col).add('v');
                            default -> throw new RuntimeException();
                        }
                    }
                }

            }
        }
        return next;
    }

    private static List<List<Set<Character>>> initMap(int rows, int cols) {
        List<List<Set<Character>>> map = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            ArrayList<Set<Character>> column = new ArrayList<>();
            for (int col = 0; col < cols; col++) {
                column.add(new HashSet<>());
            }
            map.add(column);
        }
        return map;
    }

    record Location(int row, int col) {
    }

    record State(List<List<Set<Character>>> map, int count) {
    }

}
