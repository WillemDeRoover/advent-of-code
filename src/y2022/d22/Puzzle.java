package y2022.d22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Puzzle {

    public static final char OFF_GRID = ' ';
    private static char[][] map;
    private static String path;
    private static final Pattern pattern = Pattern.compile("(\\d+)(\\w)?");

    public static void main(String[] args) throws IOException {
        String[] inputs = Files.readString(Paths.get("src/y2022/d22/input.txt")).split("\\n\\n");
        map = Stream.of(inputs[0].split("\\n"))
                .map(String::toCharArray)
                .toArray(char[][]::new);
        path = inputs[1];

        Location curLoc = new Location(0, getStartColumn(), "R");
        Matcher matcher = pattern.matcher(path);

        while (matcher.find()) {
            int moves = Integer.parseInt(matcher.group(1));
            String direction = matcher.group(2);
            curLoc = calculateNext(curLoc, moves, direction);
        }

        System.out.println((curLoc.row + 1) * 1000 + (curLoc.column + 1) * 4 + 2);
    }

    private static Location calculateNext(Location curLoc, int moves, String direction) {
        for (int i = 0; i < moves; i++) {
            Location nextLoc = curLoc.move();
            while (getTile(nextLoc) == OFF_GRID) {
                nextLoc = nextLoc.move();
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

    static char getTile(Location location) {
        try {
            return map[location.row][location.column];
        } catch (Exception e) {
            return ' ';
        }

    }

    private static int getStartColumn() {
        for (int column = 0; column < map.length; column++) {
            if (map[0][column] == '.') {
                return column;
            }
        }
        throw new RuntimeException();
    }

    record Location(int row, int column, String direction) {

        Location move() {
            return switch (direction) {
                case "U" -> new Location((row - 1 + map.length) % map.length, column, direction);
                case "D" -> new Location((row + 1) % map.length, column, direction);
                case "L" -> new Location(row, (column - 1 + map[0].length) % map[0].length, direction);
                case "R" -> new Location(row, (column + 1) % map[0].length, direction);
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
            return new Location(row, column, newDirection);
        }
    }

}
