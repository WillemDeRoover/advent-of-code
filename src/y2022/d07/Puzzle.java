package y2022.d07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static java.util.Objects.requireNonNullElse;

public class Puzzle {

    private static Stack<String> pwd = new Stack<>();
    private static Map<String, Integer> directories = new HashMap<>();;

    public static void main(String[] args) throws IOException {
        List<String> navigation = Files.lines(Paths.get("src/y2022/d07/input.txt")).toList();

        for (String line : navigation) {
            if (line.startsWith("$ cd")) {
                String next = line.substring(5);
                if (next.equals("..")) {
                    pwd.pop();
                } else {
                    pwd.add(next);
                }
            } else if (!line.equals("$ ls") && !line.startsWith("dir")) {
                int size = Integer.parseInt(line.split(" ")[0]);
                String directory = "";
                for (String s : pwd) {
                    directory = directory.concat(s);
                    directories.compute(directory, (k, v) -> requireNonNullElse(v, 0) + size);
                }
            }
        }

        directories.values().stream()
                .filter(size -> size <= 100000)
                .reduce(Integer::sum)
                .ifPresent(System.out::println);

        int unusedSpace = 70000000 - directories.get("/");
        int requiredSpace = 30000000 - unusedSpace;
        directories.values().stream()
                .filter(size -> size >= requiredSpace)
                .reduce(Integer::min)
                .ifPresent(System.out::println);
    }

}