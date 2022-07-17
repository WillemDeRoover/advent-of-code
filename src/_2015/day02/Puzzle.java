package _2015.day02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Puzzle {

    public static void main(String[] args) throws IOException {
        List<Box> boxes = Files.readAllLines(Paths.get("src/_2015/day02/input.txt")).stream()
                .map(Box::from)
                .toList();
        int size = boxes.stream()
                .mapToInt(Box::calculateSurface)
                .sum();
        System.out.println("size = " + size);

        int ribbonLength = boxes.stream()
                .mapToInt(Box::calculateRibbon)
                .sum();
        System.out.println("ribbonLength = " + ribbonLength);
    }

    private record Box(int[] dimensions) {

        static Box from(String description) {
            return new Box(getDimensions(description));
        }

        Box {
            Arrays.sort(dimensions);
        }

        private static int[] getDimensions(String string) {
            return Stream.of(string.split("x"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }

        int calculateSurface() {
            int surface = IntStream.range(0, dimensions.length)
                    .flatMap(i ->
                            IntStream.range(i + 1, dimensions.length)
                                    .map(j -> 2 * dimensions[i] * dimensions[j])
                    )
                    .sum();
            int smallestSurface = IntStream.of(dimensions).limit(2).reduce(Math::multiplyExact).getAsInt();
            return surface + smallestSurface;
        }

        int calculateRibbon() {
            int ribbonLength = IntStream.of(dimensions).limit(2).map(i -> i * 2).sum();
            int ribbonBow = IntStream.of(dimensions).reduce(Math::multiplyExact).getAsInt();
            return ribbonLength + ribbonBow;
        }
    }
}

