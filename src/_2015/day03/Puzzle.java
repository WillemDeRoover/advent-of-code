package _2015.day03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public class Puzzle {

    public static void main(String[] args) throws IOException {
        String directions = Files.readString(Paths.get("src/_2015/day03/input.txt"));

        System.out.println("visit = " + Set.copyOf(getRoute(directions, __ -> true)).size());

        Set<Coordinate> coordinates = new HashSet<>();
        coordinates.addAll(getRoute(directions, i -> i % 2 == 0));
        coordinates.addAll(getRoute(directions, i -> i % 2 == 1));
        System.out.println("duo visit = " + coordinates.size());
    }

    private static List<Coordinate> getRoute(String directions, IntPredicate filter) {
        return IntStream.range(0, directions.length())
                .filter(filter)
                .mapToObj(directions::charAt)
                .reduce(new ArrayList<>(List.of(Coordinate.DEFAULT)), Puzzle::move, noop());
    }

    private static List<Coordinate> move(List<Coordinate> route, Character direction) {
        Coordinate currentPosition = route.get(route.size() - 1);
        Coordinate newPosition = currentPosition.move(direction);
        route.add(newPosition);
        return route;
    }

    private static BinaryOperator<List<Coordinate>> noop() {
        return (_1, _2) -> {throw new IllegalStateException();};
    }

    private record Coordinate(int x, int y) {

        public static Coordinate DEFAULT = new Coordinate(0, 0);

        public Coordinate move(Character character) {
            return switch (character) {
                case '^' -> new Coordinate(x, y() + 1);
                case 'v' -> new Coordinate(x, y() - 1);
                case '<' -> new Coordinate(x - 1, y());
                case '>' -> new Coordinate(x + 1, y());
                default -> throw new IllegalStateException();
            };
        }
    }

}
