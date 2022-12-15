package y2022.d15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

public class Puzzle {

    private static final List<Location> sensors = new ArrayList<>();
    private static final List<Location> beacons = new ArrayList<>();
    private static final Pattern pattern = Pattern.compile("(-?\\d+)");

    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(Paths.get("src/y2022/d15/input.txt"));
        for (String line : inputs) {
            Matcher matcher = pattern.matcher(line);
            List<Integer> coordinates = matcher.results().map(MatchResult::group).map(Integer::parseInt).toList();
            sensors.add(new Location(coordinates.get(0), coordinates.get(1)));
            beacons.add(new Location(coordinates.get(2), coordinates.get(3)));
        }

        System.out.println(part1());
        part2().ifPresent(System.out::println);
    }

    private static long part1() {
        int minX = IntStream.range(0, sensors.size())
                .map(i -> sensors.get(i).x - calculateManhattan(sensors.get(i), beacons.get(i)))
                .min().getAsInt();

        int maxX = IntStream.range(0, sensors.size())
                .map(i -> sensors.get(i).x + calculateManhattan(sensors.get(i), beacons.get(i)))
                .max().getAsInt();

        return IntStream.rangeClosed(minX, maxX)
                .mapToObj(x -> new Location(x, 2000000))
                .filter(not(beacons::contains))
                .filter(not(Puzzle::canHaveBeacon))
                .count();
    }

    private static Optional<Long> part2() {
        for (int i = 0; i < sensors.size(); i++) {
            Location current = sensors.get(i);
            int distance = calculateManhattan(current, beacons.get(i));

            List<Location> locations = new ArrayList<>();
            locations.add(new Location(current.x, current.y - (distance + 1)));
            for (int j = 0; j < distance; j++) {
                locations.add(new Location(current.x - (j + 1), current.y - (distance - j)));
                locations.add(new Location(current.x + (j + 1), current.y - (distance - j)));
            }
            for (int j = 0; j < distance; j++) {
                locations.add(new Location(current.x - (j + 1), current.y + (distance - j)));
                locations.add(new Location(current.x + (j + 1), current.y + (distance - j)));
            }
            locations.add(new Location(current.x, current.y + (distance + 1)));

            Optional<Long> result = locations.stream()
                    .filter(Puzzle::closeToDistressBeacon)
                    .filter(Puzzle::canHaveBeacon)
                    .findFirst()
                    .map(l -> 4000000L * l.x + l.y);

            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    private static boolean closeToDistressBeacon(Location location) {
        return location.x >= 0 && location.x <= 4000000 && location.y >= 0 && location.y <= 4000000;
    }

    private static boolean canHaveBeacon(Location location) {
        for (int i = 0; i < sensors.size(); i++) {
            int distanceToLocation = calculateManhattan(location, sensors.get(i));
            int distanceToBeacon = calculateManhattan(sensors.get(i), beacons.get(i));
            if (distanceToBeacon >= distanceToLocation) {
                return false;
            }
        }
        return true;
    }

    private static int calculateManhattan(Location l1, Location l2) {
        return Math.abs(l1.x - l2.x) + Math.abs(l1.y - l2.y);
    }

    private record Location(int x, int y) {
    }

}
