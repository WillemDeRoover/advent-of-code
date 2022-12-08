package y2022.d08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

public class Puzzle {

    public static void main(String[] args) throws IOException {
        int[][] trees = Files.lines(Paths.get("src/y2022/d08/input.txt"))
                .map(s -> IntStream.range(0, s.length()).mapToObj(s::charAt).mapToInt(c -> parseInt(c.toString())).toArray())
                .toArray(int[][]::new);

        int visible = 0;
        int max = 0;

        for (int y = 0; y < trees.length; y++) {
            for (int x = 0; x < trees[0].length; x++) {
                if(isVisible(trees, x, y)) {
                    visible++;
                }
                max = Integer.max(max, calculateScenic(trees, x, y));
            }
        }
        System.out.println("visible = " + visible);
        System.out.println("max = " + max);
    }

    private static boolean isVisible(int[][] trees, int x, int y) {
        int tree = trees[y][x];

        return Arrays.stream(trees[y], 0, x).allMatch(value -> value < tree)
                || IntStream.range(0, y).map(i -> trees[i][x]).allMatch(value1 -> value1 < tree)
                || Arrays.stream(trees[y], x + 1, trees[0].length).allMatch(value2 -> value2 < tree)
                || IntStream.range(y + 1, trees.length).map(i1 -> trees[i1][x]).allMatch(value3 -> value3 < tree);
    }

    private static int calculateScenic(int[][] trees, int x, int y) {
        int tree = trees[y][x];

        return calculateVisibility(tree, IntStream.range(0, x).map(i -> (x - 1) - i).mapToObj(i -> trees[y][i]).toList())
                * calculateVisibility(tree, IntStream.range(0, y).map(i1 -> (y - 1) - i1).mapToObj(i1 -> trees[i1][x]).toList())
                * calculateVisibility(tree, IntStream.range(x + 1, trees[0].length).mapToObj(i2 -> trees[y][i2]).toList())
                * calculateVisibility(tree, IntStream.range(y + 1, trees.length).mapToObj(i3 -> trees[i3][x]).toList());
    }

    private static int calculateVisibility(int tree, List<Integer> trees) {
        int count = 0;
        for (Integer value : trees) {
            count++;
            if (value >= tree) break;
        }
        return count;
    }

}