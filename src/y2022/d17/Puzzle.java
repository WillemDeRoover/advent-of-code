package y2022.d17;

    import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

public class Puzzle {

    private static long height = 0;
    private static Set<Location> rocks = new HashSet<>();
    private static List<Function<Long, Shape>> createShapes = List.of(Puzzle::createHorizontalLine, Puzzle::createCross, Puzzle::createL, Puzzle::createVerticalLine, Puzzle::createSquare);
    private static int currentShape = 0;
    private static int wind = 0;
    private static Map<Long, Long> heightMap = new HashMap<>();
    private static Map<Long, Shape> stateMap = new HashMap<>();

    public static final int LENGTH = 10000;

    public static void main(String[] args) throws IOException {
        String winds = Files.readAllLines(Paths.get("src/y2022/d17/input.txt")).get(0);

        for (long i = 0; i < LENGTH; i++) {
            Shape nextShape = getNextShape(height);
            boolean falling = true;

            while (falling) {
                if (winds.charAt(wind) == '<') {
                    Shape leftMove = nextShape.left();
                    if (leftMove.locations.stream().noneMatch(location -> location.x < 0 || rocks.contains(location))) {
                        nextShape = leftMove;
                    }
                }
                if (winds.charAt(wind) == '>') {
                    Shape rightMove = nextShape.right();
                    if (rightMove.locations.stream().noneMatch(location -> location.x > 6 || rocks.contains(location))) {
                        nextShape = rightMove;
                    }
                }

                wind = (wind + 1) % winds.length();

                Shape fallMove = nextShape.fall();
                if (fallMove.locations.stream().noneMatch(l -> l.y < 0 || rocks.contains(l))) {
                    nextShape = fallMove;
                } else {
                    rocks.addAll(nextShape.locations);
                    height = rocks.stream().mapToLong(Location::y).max().getAsLong() + 1;
                    heightMap.put(i, height);
                    falling = false;
                    stateMap.put(i, nextShape.normalize());

                }

            }

            if(i == 2022) {
                System.out.println("height after 2022 blocks: " + height);
            }

        }


        int maxPatternLength = LENGTH / 3;
        for (long currentPatternLength = maxPatternLength; currentPatternLength > 0; currentPatternLength--) {

            long startPattern1 = 0;
            while (startPattern1 + (3 * currentPatternLength) <= maxPatternLength * 3) {

                boolean patterMatch = true;
                for (long j = 0; j < maxPatternLength; j++) {
                    if (!stateMap.get(startPattern1 + j).equals(stateMap.get(currentPatternLength + startPattern1 + j)) || !stateMap.get(startPattern1 + j).equals(stateMap.get( 2 * currentPatternLength + startPattern1 + j))) {
                        patterMatch = false;
                        break;
                    }
                }
                if (patterMatch) {

                    long offset = startPattern1 - 1;
                    System.out.println("startPattern1 = " + startPattern1);
                    long endPattern1 = startPattern1 + currentPatternLength - 1;
                    long startPattern2 = endPattern1 + 1;
                    long endPattern2 = startPattern2 + currentPatternLength - 1;

                    long patternCount = 1000000000000L / currentPatternLength;//589970501
                    long mod = 1000000000000L % 1695L;
                    long remainingRocks = mod - offset - 1; //we start counting from 0;

                    long offsetHeight = heightMap.get(offset) - 1;
                    long patternHeight = heightMap.get(endPattern2) - heightMap.get(endPattern1);
                    long remainingHeight = heightMap.get(endPattern2 + remainingRocks) - heightMap.get(endPattern2);

                    System.out.println("height after 1000000000000 blocks: " + (offsetHeight + (patternCount * patternHeight) + remainingHeight));
                    break;
                }

                startPattern1++;
            }
        }

    }

    private static Shape getNextShape(long height) {
        Shape shape = createShapes.get(currentShape).apply(height);
        currentShape = (currentShape + 1) % 5;
        return shape;
    }

    private static Shape createHorizontalLine(long height) {
        return new Shape("Horizontal Line", IntStream.range(0, 4).mapToObj(i -> new Location(i + 2, height + 3)).collect(toSet()));
    }

    private static Shape createCross(long height) {
        HashSet<Location> locations = new HashSet<>();
        locations.add(new Location(3, height + 5));
        locations.addAll(IntStream.range(0, 3).mapToObj(i -> new Location(i + 2, height + 4)).collect(toSet()));
        locations.add(new Location(3, height + 3));
        return new Shape("Cross", Set.copyOf(locations));
    }

    private static Shape createL(long height) {
        HashSet<Location> locations = new HashSet<>();
        locations.addAll(IntStream.range(0, 3).mapToObj(i -> new Location(i + 2, height + 3)).collect(toSet()));
        locations.addAll(IntStream.range(1, 3).mapToObj(i -> new Location(4, i + height + 3)).collect(toSet()));
        return new Shape("L", Set.copyOf(locations));
    }

    private static Shape createVerticalLine(long height) {
        return new Shape("Vertical Line", IntStream.range(0, 4).mapToObj(i -> new Location(2, i + (height + 3))).collect(toSet()));
    }

    private static Shape createSquare(long height) {
        HashSet<Location> locations = new HashSet<>();
        locations.addAll(IntStream.range(0, 2).mapToObj(i -> new Location(i + 2, height + 4)).collect(toSet()));
        locations.addAll(IntStream.range(0, 2).mapToObj(i -> new Location(i + 2, height + 3)).collect(toSet()));
        return new Shape("Square", Set.copyOf(locations));
    }

    record Shape(String type, Set<Location> locations) {

        Shape fall() {
            return new Shape(type, locations().stream().map(Location::down).collect(toSet()));
        }

        Shape right() {
            return new Shape(type, locations.stream().map(Location::right).collect(toSet()));
        }

        Shape left() {
            return new Shape(type, locations.stream().map(Location::left).collect(toSet()));
        }

        Shape normalize() {
            long smallestY = locations.stream().mapToLong(Location::y).min().getAsLong();
            Set<Location> normalisedLocations = locations.stream().map(l -> new Location(l.x, l.y - smallestY)).collect(toSet());
            return new Shape(type, normalisedLocations);
        }

    }

    record Location(long x, long y) {

        Location down() {
            return new Location(x, y - 1);
        }

        Location right() {
            return new Location(x + 1, y);
        }

        Location left() {
            return new Location(x - 1, y);
        }
    }


}
