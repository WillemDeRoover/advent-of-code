package y2022.d02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Puzzle {

    public static void main(String[] args) throws IOException {
        List<Round> rounds = Files.lines(Paths.get("src/y2022/d02/input.txt"))
                .map(s -> s.split(" "))
                .map(Round::new)
                .toList();

        rounds.stream()
                .map(Round::play1)
                .reduce(Integer::sum).ifPresent(System.out::println);

        rounds.stream()
                .map(Round::play2)
                .reduce(Integer::sum).ifPresent(System.out::println);
    }

    private record Round(Left left, Right right) {

        Round(String[] split) {
            this(Left.valueOf(split[0]), Right.valueOf(split[1]));
        }

        int play1() {
            return Puzzle.score[this.left().ordinal()][right.ordinal()] + right.score;
        }

        int play2() {
            int[] scores = Puzzle.score[left.ordinal()];
            for (int i = 0; i < scores.length; i++) {
                if (scores[i] == right.result) {
                    return Right.values()[i].score + scores[i];
                }
            }
            throw new RuntimeException();
        }
    }

    private static final int[][] score = {{3, 6, 0}, {0, 3, 6}, {6, 0, 3}};

    private enum Left {A, B, C}
    private enum Right {
        X(1, 0),
        Y(2, 3),
        Z(3, 6);

        final int score;
        final int result;

        Right(int score, int result) {
            this.score = score;
            this.result = result;
        }
    }
}
