package y2022.d22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class Puzzle2 {

    private static final Pattern pattern = Pattern.compile("(\\d+)(\\w)?");
    private static final char OFF_GRID = ' ';
    private static String path;
    private static Map<SideMove, Function<Location, Location>> sideMoves = new HashMap<>();
    private static Map<Integer, char[][]> sideMaps;
    private static Map<Integer, Location> sideCoordinates;

    static {
        sideMoves.put(new SideMove(1, "U"), switchRc(4, "R"));
        sideMoves.put(new SideMove(1, "D"), traverse(2));
        sideMoves.put(new SideMove(1, "L"), invertRow(5));
        sideMoves.put(new SideMove(1, "R"), traverse(6));

        sideMoves.put(new SideMove(2, "U"), traverse(1));
        sideMoves.put(new SideMove(2, "D"), traverse(3));
        sideMoves.put(new SideMove(2, "L"), switchRc(5, "D"));
        sideMoves.put(new SideMove(2, "R"), switchRc(6, "U"));

        sideMoves.put(new SideMove(3, "U"), traverse(2));
        sideMoves.put(new SideMove(3, "L"), traverse(5));
        sideMoves.put(new SideMove(3, "D"), switchRc(4, "L"));
        sideMoves.put(new SideMove(3, "R"), invertRow(6));

        sideMoves.put(new SideMove(4, "U"), traverse(5));
        sideMoves.put(new SideMove(4, "L"), switchRc(1, "D"));
        sideMoves.put(new SideMove(4, "R"), switchRc(3, "U"));
        sideMoves.put(new SideMove(4, "D"), traverse(6));

        sideMoves.put(new SideMove(5, "L"), invertRow(1));
        sideMoves.put(new SideMove(5, "U"), switchRc(2, "R"));
        sideMoves.put(new SideMove(5, "R"), traverse(3));
        sideMoves.put(new SideMove(5, "D"), traverse(4));

        sideMoves.put(new SideMove(6, "L"), traverse(1));
        sideMoves.put(new SideMove(6, "U"), traverse(4));
        sideMoves.put(new SideMove(6, "D"), switchRc(2, "L"));
        sideMoves.put(new SideMove(6, "R"), invertRow(3));

        sideCoordinates = Stream.of(
                new Location(0, 50, null, 1),
                new Location(0, 100, null, 6),
                new Location(50, 50, null, 2),
                new Location(100, 50, null, 3),
                new Location(100, 0, null, 5),
                new Location(150, 0, null, 4)
        ).collect(toMap(Location::side, Function.identity()));

    }

    public static void main(String[] args) throws IOException {
        String[] inputs = Files.readString(Paths.get("src/y2022/d22/input.txt")).split("\\n\\n");
        char[][] map = Stream.of(inputs[0].split("\\n"))
                .map(String::toCharArray)
                .toArray(char[][]::new);
        sideMaps = sideCoordinates.values().stream()
                .collect(toMap(Location::side, l -> createSideMap(l, map)));
        path = inputs[1];

        Location curLoc = new Location(0, 0, "R", 1);
        Matcher matcher = pattern.matcher(path);

        while (matcher.find()) {
            int moves = Integer.parseInt(matcher.group(1));
            String direction = matcher.group(2);
            curLoc = calculateNext(curLoc, moves, direction);
        }

        Location offset = sideCoordinates.get(curLoc.side);
        System.out.println((offset.row + curLoc.row + 1) * 1000 + (offset.column + curLoc.column + 1) * 4 + 3);
    }

    private static Location calculateNext(Location curLoc, int moves, String direction) {
        for (int i = 0; i < moves; i++) {
            Location nextLoc = curLoc.move();
            if (getTile(nextLoc) == OFF_GRID) {
                nextLoc = sideMoves.get(new SideMove(curLoc.side, curLoc.direction)).apply(curLoc);
            }
            if (getTile(nextLoc) == '#') {
                break;
            }
            curLoc = nextLoc;
        }

        if (direction != null) {
            curLoc = curLoc.changeDirection(direction);
        }
        return curLoc;
    }

    private static Function<Location, Location> invertRow(int newSide) {
        return location -> {
            String newDirection = location.direction.equals("R") ? "L" : "R";
            return new Location(49 - location.row, location.column, newDirection, newSide);
        };
    }

    private static Function<Location, Location> traverse(int newSide) {
        return location -> switch (location.direction) {
            case "U" -> new Location(49, location.column, "U", newSide);
            case "D" -> new Location(0, location.column, "D", newSide);
            case "R" -> new Location(location.row, 0, "R", newSide);
            case "L" -> new Location(location.row, 49, "L", newSide);
            default -> throw new RuntimeException();
        };
    }

    private static Function<Location, Location> switchRc(int newSide, String newDirection) {
        return location -> new Location(location.column, location.row, newDirection, newSide);
    }

    static char getTile(Location location) {
        try {
            return sideMaps.get(location.side())[location.row][location.column];
        } catch (Exception e) {
            return ' ';
        }
    }

    private static char[][] createSideMap(Location l, char[][] map) {
        char[][] sideMap = new char[50][50];
        for (int row = 0; row < 50; row++) {
            for (int column = 0; column < 50; column++) {
                sideMap[row][column] = map[l.row + row][l.column + column];
            }
        }
        return sideMap;
    }

    record Location(int row, int column, String direction, int side) {

        Location move() {
            return switch (direction) {
                case "U" -> new Location(row - 1, column, direction, side);
                case "D" -> new Location(row + 1, column, direction, side);
                case "L" -> new Location(row, column - 1, direction, side);
                case "R" -> new Location(row, column + 1, direction, side);
                default -> throw new RuntimeException();
            };
        }

        public Location changeDirection(String change) {
            String newDirection = switch (direction) {
                case "U" -> change.equals("L") ? "L" : "R";
                case "D" -> change.equals("L") ? "R" : "L";
                case "L" -> change.equals("L") ? "D" : "U";
                case "R" -> change.equals("L") ? "U" : "D";
                default -> throw new RuntimeException();
            };
            return new Location(row, column, newDirection, side);
        }
    }

    record SideMove(int side, String direction) {
    }

}
