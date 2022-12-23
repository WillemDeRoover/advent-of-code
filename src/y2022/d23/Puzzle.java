package y2022.d23;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNullElse;
import static y2022.d23.Puzzle.Direction.EAST;
import static y2022.d23.Puzzle.Direction.NORTH;
import static y2022.d23.Puzzle.Direction.SOUTH;
import static y2022.d23.Puzzle.Direction.WEST;

public class Puzzle {

    private static final List<Direction> DIRECTIONS = List.of(NORTH, SOUTH, WEST, EAST);
    private static Set<Location> current;

    public static void main(String[] args) throws IOException {
        List<String> strings = Files.readAllLines(Paths.get("src/y2022/d23/input.txt"));
        Set<Location> initLocations =
                IntStream.range(0, strings.size())
                        .mapToObj(r -> IntStream.range(0, strings.get(0).length())
                                .filter(c -> strings.get(r).charAt(c) == '#')
                                .mapToObj(c -> new Location(r, c)))
                        .flatMap(Function.identity()).collect(Collectors.toCollection(HashSet::new));


        Set<Location> next = initLocations;
        current = null;
        int startDirection = 0;
        int round = 0;

        while (!next.equals(current)) {
            current = next;
            Map<Location, Set<Location>> proposals = proposeMoves(startDirection, current);
            next = execute(proposals);
            startDirection++;
            round++;
        }

        System.out.println(round);
    }

    private static Map<Location, Set<Location>> proposeMoves(int startDirection, Set<Location> positions) {
        Map<Location, Set<Location>> moveMap = new HashMap<>();
        a:
        for (Location location : positions) {

            if (!location.hasNeighbours(positions)) {
                add(moveMap, location, location);
                continue a;
            }

            for (int j = 0; j < 4; j++) {
                Direction direction = DIRECTIONS.get((startDirection + j) % DIRECTIONS.size());
                if (direction.canMove.test(location)) {
                    Location newLocation = direction.move.apply(location);
                    add(moveMap, newLocation, location);
                    continue a;
                }
            }

            add(moveMap, location, location);

        }
        return moveMap;
    }

    private static void add(Map<Location, Set<Location>> moveMap, Location newLocation, Location location) {
        moveMap.compute(newLocation, (nl, from) -> {
            Set<Location> nextFrom = requireNonNullElse(from, new HashSet<>());
            nextFrom.add(location);
            return nextFrom;
        });
    }

    private static Set<Location> execute(Map<Location, Set<Location>> proposals) {
        return proposals.entrySet().stream()
                .map(entry -> {
                    if (entry.getValue().size() == 1) {
                        return Set.of(entry.getKey());
                    } else {
                        return entry.getValue();
                    }
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    enum Direction {
        NORTH(l -> IntStream.rangeClosed(-1, 1).noneMatch(i -> current.contains(new Location(l.row - 1, l.column + i)))
                , l -> new Location(l.row - 1, l.column)),
        SOUTH(l -> IntStream.rangeClosed(-1, 1).noneMatch(i -> current.contains(new Location(l.row + 1, l.column + i)))
                , l -> new Location(l.row + 1, l.column)),
        WEST(l -> IntStream.rangeClosed(-1, 1).noneMatch(i -> current.contains(new Location(l.row - i, l.column - 1)))
                , l -> new Location(l.row, l.column - 1)),
        EAST(l -> IntStream.rangeClosed(-1, 1).noneMatch(i -> current.contains(new Location(l.row - i, l.column + 1)))
                , l -> new Location(l.row, l.column + 1));

        Direction(Predicate<Location> test, Function<Location, Location> move) {
            this.canMove = test;
            this.move = move;
        }

        private final Predicate<Location> canMove;
        private final Function<Location, Location> move;
    }

    record Location(int row, int column) {

        boolean hasNeighbours(Set<Location> positions) {
            return IntStream.rangeClosed(row - 1, row + 1)
                    .mapToObj(r -> IntStream.rangeClosed(column - 1, column + 1)
                            .filter(c -> c != column || r != row)
                            .mapToObj(c -> new Location(r, c))
                    )
                    .flatMap(Function.identity())
                    .anyMatch(positions::contains);
        }
    }

}
