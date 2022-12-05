package y2022.d03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Puzzle {

    public static void main(String[] args) throws IOException {
        List<List<Integer>> lines = Files.lines(Paths.get("src/y2022/d03/input.txt"))
                .map(s -> s.chars().mapToObj(c -> c > 96 ? c - 96 : c - 38).toList())
                .toList();

        lines.stream()
                .map(Puzzle::getDuplicate)
                .reduce(Integer::sum)
                .ifPresent(System.out::println);

        IntStream.range(0, lines.size()/3)
                .map(i -> i * 3)
                .mapToObj(i -> lines.subList(i, i + 3))
                .map(Puzzle::findBadge)
                .reduce(Integer::sum)
                .ifPresent(System.out::println);
    }

    private static int getDuplicate(List<Integer> characters) {
        Set<Integer> comp1 = new HashSet<>(characters.subList(0, characters.size() / 2));
        Set<Integer> comp2 = new HashSet<>(characters.subList(characters.size() / 2, characters.size()));
        comp1.retainAll(comp2);
        return comp1.stream().findFirst().get();
    }

    private static int findBadge(List<List<Integer>> group) {
        return group.stream()
                .map(HashSet::new)
                .reduce(new HashSet<>(group.get(0)), (i, s) -> { i.retainAll(s); return i;})
                .stream().findFirst().get();
    }
}
