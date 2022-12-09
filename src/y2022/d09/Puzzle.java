package y2022.d09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class Puzzle {

    private static Integer TAIL_KNOTS = 9;

    public static void main(String[] args) throws IOException {
        List<Move> moves = Files.lines(Paths.get("src/y2022/d09/input.txt"))
                .map(s -> new Move("" + s.charAt(0), Integer.parseInt(s.substring(2))))
                .toList();
        Map<Integer, Position> knots = IntStream.rangeClosed(0, TAIL_KNOTS).boxed()
                .collect(toMap(identity(), __ -> new Position(0, 0)));

        Set<Position> tailPositions = new HashSet<>();
        tailPositions.add(knots.get(TAIL_KNOTS));

        for (Move move : moves) {
            for (int i = 0; i < move.times; i++) {
                knots.computeIfPresent(0, (__, v) -> v.move(move.direction));
                for (int j = 1; j <= TAIL_KNOTS; j++) {
                    while(calculateDistance(knots.get(j - 1), knots.get(j)) >= 2) {
                        Position head = knots.get(j - 1);
                        Position tail = knots.get(j);
                        if(head.x == tail.x) {
                            knots.computeIfPresent(j, (k,v) -> v.move(move.direction));
                        } else if( head.y == tail.y) {
                            knots.computeIfPresent(j, (k,v) -> v.move(move.direction));
                        } else {
                            if(tail.x < head.x && tail.y < head.y) {
                                knots.computeIfPresent(j, (k,v) -> v.move("R").move("U"));
                            } else if (tail.x < head.x && tail.y > head.y){
                                knots.computeIfPresent(j, (k,v) -> v.move("R").move("D"));
                            } else if (tail.x > head. x && tail.y < head.y) {
                                knots.computeIfPresent(j, (k,v) -> v.move("L").move("U"));
                            } else {
                                knots.computeIfPresent(j, (k,v) -> v.move("L").move("D"));
                            }
                        }
                    }
                }
                tailPositions.add(knots.get(TAIL_KNOTS));
            }
        }

        System.out.println(tailPositions.size());

    }

    private static double calculateDistance(Position p1, Position p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    private record Move(String direction, int times) {
    }

    private record Position(int x, int y) {

        public Position move(String direction) {
            return switch(direction) {
                case "L" -> new Position(x - 1, y);
                case "R" -> new Position(x + 1, y);
                case "U" -> new Position(x, y + 1);
                case "D" -> new Position(x, y - 1);
                default -> throw new RuntimeException();
            };
        }
    }
}