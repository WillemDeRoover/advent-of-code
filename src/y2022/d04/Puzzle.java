package y2022.d04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Puzzle {

    private static final Pattern pattern = Pattern.compile("(\\d+)-(\\d+),(\\d+)-(\\d+)");

    public static void main(String[] args) throws IOException {
        List<List<Integer>> pairs = Files.lines(Paths.get("src/y2022/d04/input.txt"))
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(m -> IntStream.range(1, m.groupCount() + 1).mapToObj(m::group).map(Integer::parseInt).toList())
                .toList();

        System.out.println("contains = " + pairs.stream().filter(Puzzle::contains).count());
        System.out.println("overlap = " + pairs.stream().filter(Puzzle::overlap).count());
    }

    public static boolean contains(List<Integer> ranges) {
        return (ranges.get(0) <= ranges.get(2) && ranges.get(1) >= ranges.get(3))
                || (ranges.get(2) <=  ranges.get(0) && ranges.get(3) >= ranges.get(1));
    }

    public static boolean overlap(List<Integer> ranges) {
        return ranges.get(0) <= ranges.get(3) && ranges.get(1) >= ranges.get(2);
    }

}