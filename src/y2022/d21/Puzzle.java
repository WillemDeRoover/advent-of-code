package y2022.d21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Puzzle {

    public static final Map<String, Long> valueByMonkey = new HashMap<>();
    public static final Map<String, Calculation> calculationByMonkey = new HashMap<>();

    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Paths.get("src/y2022/d21/input.txt"));

        for (String line : input) {
            String[] split = line.split("\\s");
            if (split.length == 2) {
                valueByMonkey.put(split[0].substring(0, 4), Long.parseLong(split[1].trim()));
            } else {
                String[] bla = line.split(":");
                calculationByMonkey.put(bla[0], Calculation.of(bla[1].trim()));
            }
        }

        System.out.println("part1 = " + findValueFor("root"));
        System.out.println("part2 = " + calculateEquality());

    }

    private static long findValueFor(String monkey) {
        if (valueByMonkey.containsKey(monkey)) {
            return valueByMonkey.get(monkey);
        } else {
            Calculation calculation = calculationByMonkey.get(monkey);
            long leftValue = findValueFor(calculation.left);
            long rightValue = findValueFor(calculation.right);
            return calculate(calculation.calculation, leftValue, rightValue);
        }
    }

    private static long calculate(String calculation, long leftValue, long rightValue) {
        return switch (calculation) {
            case "+" -> leftValue + rightValue;
            case "-" -> leftValue - rightValue;
            case "*" -> leftValue * rightValue;
            case "/" -> leftValue / rightValue;
            default -> throw new RuntimeException();
        };
    }

    private static long calculateEquality() {
        Queue<String> parents = findParents("humn");
        parents.remove();
        return recalculate(parents, "hqpw", findValueFor("nprj"));
    }

    private static Queue<String> findParents(String child) {
        if (child.equals("root")) {
            return new LinkedList<>();
        } else {
            String parent = calculationByMonkey.entrySet().stream()
                    .filter(e -> e.getValue().contains(child))
                    .map(Map.Entry::getKey).findFirst().get();

            Queue<String> parents = findParents(parent);
            parents.add(child);
            return parents;
        }
    }

    private static long recalculate(Queue<String> parents, String current, long value) {

        if (current.equals("humn")) {
            return value;
        }

        Calculation currentCalculation = calculationByMonkey.get(current);
        String toBeUpdated = parents.remove();

        if (currentCalculation.left().equals(toBeUpdated)) {
            String inverse = currentCalculation.getInverseCalculation();
            long newValue = calculate(inverse, value, findValueFor(currentCalculation.right));
            return recalculate(parents, currentCalculation.left, newValue);
        } else {
            String inverse = currentCalculation.getInverseCalculation();
            if (currentCalculation.calculation.equals("-") || currentCalculation.calculation.equals("/")) {
                long newValue = calculate(currentCalculation.calculation, findValueFor(currentCalculation.left), value);
                return recalculate(parents, currentCalculation.right, newValue);
            } else {
                long newValue = calculate(inverse, value, findValueFor(currentCalculation.left));
                return recalculate(parents, currentCalculation.right, newValue);
            }
        }
    }
    record Calculation(String left, String calculation, String right) {


        public static Calculation of(String s) {
            String[] split = s.split("\\s");
            return new Calculation(split[0], split[1], split[2]);
        }

        boolean contains(String monkey) {
            return left.equals(monkey) || right.equals(monkey);
        }

        String getInverseCalculation() {
            return switch (calculation) {
                case "+" -> "-";
                case "-" -> "+";
                case "*" -> "/";
                case "/" -> "*";
                default -> throw new RuntimeException();
            };
        }
    }

}
