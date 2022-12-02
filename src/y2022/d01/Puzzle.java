package y2022.d01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.Collections.reverseOrder;

public class Puzzle {

    public static void main(String[] args) throws IOException {
        var calories = Stream.of(Files.readString(Paths.get("src/y2022/d01/input.txt")).split("\n\n"))
                .map(c -> Stream.of(c.split("\n")).mapToInt(Integer::parseInt).sum())
                .sorted(reverseOrder())
                .limit(3)
                .toList();

        System.out.println(calories.get(0));
        System.out.println(calories.stream().reduce(0, Integer::sum));
    }

}




