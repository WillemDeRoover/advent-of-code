package y2022.d06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

public class Puzzle {

    private static final int MARKER_LENGTH = 14;

    public static void main(String[] args) throws IOException {
        String signal = Files.readString(Paths.get("src/y2022/d06/input.txt"));
        IntStream.range(0, signal.length() - MARKER_LENGTH)
                .mapToObj(i -> signal.substring(i, i + MARKER_LENGTH))
                .filter(Puzzle::containsDistinctCharacters)
                .map(signal::indexOf)
                .map(i -> i + MARKER_LENGTH)
                .findFirst()
                .ifPresent(System.out::println);
    }

    private static boolean containsDistinctCharacters(String marker) {
        return marker.chars()
                .mapToObj(i -> (char) i)
                .collect(collectingAndThen(
                        toCollection(HashSet::new),
                        distinct -> distinct.size() == marker.length()));
    }

}