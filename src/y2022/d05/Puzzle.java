package y2022.d05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle {

    private static final int STACK_AMOUNT = 9;
    private static final Pattern pattern = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");
    private static final List<Stack<String>> stacks = IntStream.range(0, STACK_AMOUNT).mapToObj(__ -> new Stack<String>()).toList();

    public static void main(String[] args) throws IOException {
        List<String> setup = Files.lines(Paths.get("src/y2022/d05/input2.txt"))
                .takeWhile(Predicate.not(String::isEmpty))
                .collect(Collectors.toList());
        Collections.reverse(setup);
        setup.stream()
                .skip(1)
                .forEach(Puzzle::addToStack);

        Files.lines(Paths.get("src/y2022/d05/input2.txt"))
                .dropWhile(Predicate.not(String::isEmpty))
                .skip(1)
                .map(Puzzle::parseInstruction)
                .forEach(instruction -> replaceAll(instruction, stacks));

        stacks.stream().map(Stack::pop).forEach(System.out::print);
    }

    private static void addToStack(String s) {
        for (int i = 0; i < STACK_AMOUNT; i++) {
            String value = String.valueOf(s.charAt(1 + i * 4));
            if (!value.equals(" ")) {
                stacks.get(i).push(value);
            }
        }
    }

    private static int[] parseInstruction(String s) {
        Matcher matcher = pattern.matcher(s);
        matcher.matches();
        return IntStream.range(1, matcher.groupCount() + 1).mapToObj(matcher::group).mapToInt(Integer::parseInt).toArray();
    }

    private static void replace(int[] instruction, List<Stack<String>> stacks) {
        Queue<String> crane = new LinkedList<>();
        for (int i = 0; i < instruction[0]; i++) {
            crane.add(stacks.get(instruction[1] - 1).pop());
        }
        while (!crane.isEmpty()) {
            stacks.get(instruction[2] - 1).push(crane.remove());
        }
    }

    private static void replaceAll(int[] instruction, List<Stack<String>> stacks) {
        Stack<String> crane = new Stack<>();
        for (int i = 0; i < instruction[0]; i++) {
            crane.push(stacks.get(instruction[1] - 1).pop());
        }
        while (!crane.isEmpty()) {
            stacks.get(instruction[2] - 1).push(crane.pop());
        }
    }

}