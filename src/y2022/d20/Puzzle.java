package y2022.d20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle {

    private static Map<Integer, Integer> initOrder = new HashMap<>();

    public static void main(String[] args) throws IOException {
        List<Long> initial = Files.lines(Paths.get("src/y2022/d20/input.txt"))
                .filter(Predicate.not(String::isEmpty))
                .map(Integer::parseInt)
                .map(i -> 811589153L * i)
                .toList();


        List<Pair> pairs = IntStream.range(0, initial.size())
                .mapToObj(i -> new Pair(initial.get(i), i))
                .collect(Collectors.toList());


        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < initial.size(); i++) {
                Pair pair = new Pair(initial.get(i), i);
                int index = pairs.indexOf(pair);
                pairs.remove(index);

                long nextIndex = index + pair.value;
                while (nextIndex < 0 || nextIndex > initial.size()) {
                    nextIndex = (nextIndex + (initial.size() - 1)) % (initial.size() - 1);
                }
                pairs.add((int) nextIndex, pair);
            }
        }

        Pair zeroPair = pairs.stream()
                .filter(p -> p.value == 0)
                .findFirst().get();

        int i0 = pairs.indexOf(zeroPair);

        long sum = IntStream.rangeClosed(1, 3)
                .map(i -> i * 1000)
                .map(i -> (i + i0) % pairs.size())
                .mapToLong(i -> pairs.get(i).value)
                .sum();
        System.out.println("sum = " + sum);

    }

    record Pair(long value, int initial) {
    }

}
