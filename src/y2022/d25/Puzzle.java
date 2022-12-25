package y2022.d25;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class Puzzle {

    private static final List<Snafu> snafus = List.of(
                new Snafu(0, 0, '0'),
                new Snafu(1, 1, '1'),
                new Snafu(2, 2, '2'),
                new Snafu(3, -2, '='),
                new Snafu(4, -1, '-')
            );

    private static final Map<Character, Integer> snafuNumberByRepresentation = snafus.stream()
            .collect(toMap(Snafu::representation, Snafu::number));

    private static final Map<Integer, Snafu> snafuByDecimal = snafus.stream()
            .collect(toMap(Snafu::decimal, Function.identity()));

    public static void main(String[] args) throws IOException {

        long decimal = Files.lines(Paths.get("src/y2022/d25/input.txt"))
                .mapToLong(Puzzle::toDecimal)
                .sum();
        System.out.println("decimal = " + decimal);
        System.out.println("snafu = " + toSnafu(decimal));
    }

    private static long toDecimal(String s) {
        long result = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(s.length() - (i + 1));
            double pow = Math.pow(5, i);
            result += (snafuNumberByRepresentation.get(c) * pow);
        }
        return result;
    }

    private static String toSnafu(long decimal) {
        if(decimal == 0) {
            return "";
        }
        long number = decimal % 5;
        Snafu snafu = snafuByDecimal.get((int) number);
        return toSnafu((decimal - snafu.number) /5) + snafu.representation;
    }

    record Snafu(int decimal, int number, char representation) {
    }

}
