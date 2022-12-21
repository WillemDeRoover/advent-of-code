package y2022.d13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;

public class Puzzle {

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("src/y2022/d13/input.txt"));
        part1(input);
        part2(input);
    }

    private static void part1(String input) {
        List<String[]> pairs = Arrays.stream(input.split("\\n\\n"))
                .map(p -> p.split("\\n"))
                .toList();

        int sum = 0;
        for (int i = 1; i <= pairs.size(); i++) {
            String[] pair = pairs.get(i - 1);
            if (0 > correctOrder(pair[0], pair[1])) {
                sum += i;
            }
        }
        System.out.println(sum);
    }

    private static void part2(String input) {
        List<String> pairs = Arrays.stream(input.split("\\n\\n"))
                .map(p -> p.split("\\n"))
                .flatMap(Stream::of)
                .collect(toList());
        pairs.addAll(List.of("[[2]]", "[[6]]"));

        List<String> sorted = pairs.stream().sorted((s1, s2) -> correctOrder(s1, s2)).toList();
        int i2 = sorted.indexOf("[[2]]") + 1;
        int i6 = sorted.indexOf("[[6]]") + 1;
        int decoderKey = i2 * i6;
        System.out.println("decoderKey = " + decoderKey);
    }

    private static int correctOrder(String left, String right) {

        if(left.isEmpty() || right.isEmpty()) {
            return Integer.compare(left.length(), right.length());
        }

        if (isList(left) || isList(right)) {
            String[] leftElements = isList(left) ? getElements(left) : new String[]{left};
            String[] rightElements = isList(right) ? getElements(right) : new String[]{right};
            for (int i = 0; i < leftElements.length && i < rightElements.length; i++) {
                int order = correctOrder(leftElements[i], rightElements[i]);
                if (order != 0) {
                    return order;
                }
            }
            return Integer.compare(leftElements.length, rightElements.length);
        }

        return Integer.compare(parseInt(left), parseInt(right));
    }

    private static String[] getElements(String list) {
        String substring = list.substring(1, list.length() - 1);

        int startCurrentElement = 0;
        ArrayList<String> elements = new ArrayList<>();
        int brackets = 0;
        for (int i = 0; i < substring.length(); i++) {
            if (substring.charAt(i) == '[') {
                brackets++;
            }
            if (substring.charAt(i) == ']') {
                brackets--;
            }
            if (substring.charAt(i) == ',' && brackets == 0) {
                elements.add(substring.substring(startCurrentElement, i));
                startCurrentElement = i + 1;
            }
        }
        elements.add(substring.substring(startCurrentElement, substring.length()));
        return elements.toArray(new String[0]);
    }

    private static boolean isList(String value) {
        return value.startsWith("[") && value.endsWith("]");
    }

}
