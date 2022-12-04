package y2022.d04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Puzzle {

    private static final Pattern pattern = Pattern.compile("(\\d+)-(\\d+),(\\d+)-(\\d+)");

    public static void main(String[] args) throws IOException {
        List<Matcher> pairs = Files.lines(Paths.get("src/y2022/d04/input.txt"))
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .toList();

        System.out.println("contains = " + pairs.stream().filter(Puzzle::contains).count());
        System.out.println("overlap = " + pairs.stream().filter(Puzzle::overlap).count());
    }

    public static boolean contains(Matcher matcher) {
        return parseInt(matcher.group(1)) <=  parseInt(matcher.group(3)) && parseInt(matcher.group(2)) >= parseInt(matcher.group(4))
                || parseInt(matcher.group(3)) <=  parseInt(matcher.group(1)) && parseInt(matcher.group(4)) >= parseInt(matcher.group(2));
    }

    public static boolean overlap(Matcher matcher) {
        return !(parseInt(matcher.group(1)) > parseInt(matcher.group(4))) && !(parseInt(matcher.group(2)) < parseInt(matcher.group(3)));
    }

}