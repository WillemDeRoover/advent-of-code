package y2022.d19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Puzzle {

    private static final int ORE = 0;
    private static final int CLAY = 1;
    private static final int OBSIDIAN = 2;
    private static final int GEODE = 3;


    private static final Pattern pattern = Pattern.compile("\\d+");
    private static int minutes;

    public static void main(String[] args) throws IOException {
        List<Blueprints> blueprints = Files.lines(Paths.get("src/y2022/d19/input.txt"))
                .map(Puzzle.Blueprints::parse)
                .toList();

        part1(blueprints);
        part2(blueprints);
    }

    private static void part1(List<Blueprints> Blueprints) {
        minutes = 24;
        int part1 = 0;
        for (int i = 0; i < Blueprints.size(); i++) {
            int quality = (i + 1) * collectMax(Blueprints.get(i));
            part1 += quality;
        }
        System.out.println("part1 = " + part1);
    }

    private static void part2(List<Blueprints> Blueprints) {
        minutes = 32;
        long part2 = 1;
        for (int i = 0; i < 3; i++) {
            part2 *= collectMax(Blueprints.get(i));
        }
        System.out.println("part2 = " + part2);
    }

    private static int collectMax(Blueprints blueprints) {
        int[] robots = {1, 0, 0, 0};
        int[] resources = {0, 0, 0, 0};

        return collectMax(new State(robots, resources, 1), blueprints, null);
    }

    private static int collectMax(State current, Blueprints blueprints, BluePrint nextRobot) {

        
        if (current.time > minutes) {
            return current.resources[GEODE];
        }

        if (nextRobot == null) {
            return current.nextRobots(blueprints).stream()
                    .mapToInt(nr -> collectMax(current, blueprints, nr))
                    .max()
                    .getAsInt();
        } else {
            boolean hadEnoughResources = current.hasEnoughResource(nextRobot);
            State next = current.harvest();
            if (hadEnoughResources) {
                next = next.build(nextRobot);
                return collectMax(next, blueprints, null);
            }
            return collectMax(next, blueprints, nextRobot);

        }

    }

    record State(int[] robots, int[] resources, int time) {

        public State harvest() {
            int[] nextResources = IntStream.range(0, 4)
                    .map(i -> resources[i] + robots[i])
                    .toArray();
            return new State(robots, nextResources, time + 1);
        }

        public List<BluePrint> nextRobots(Blueprints blueprints) {
            List<BluePrint> nextBluePrints = new ArrayList<>();

            if (robots[OBSIDIAN] == 0
                    && blueprints.asList().stream().anyMatch(bp -> robots[ORE] < bp.required[ORE])
                    && time < 10) {
                nextBluePrints.add(blueprints.oreBluePrint);
            }
            if (robots[GEODE] == 0
                    && blueprints.asList().stream().anyMatch(bp -> robots[CLAY] < bp.required[CLAY])
                    && time < 20) {
                nextBluePrints.add(blueprints.clayRobot);
            }
            if (robots[CLAY] != 0) {
                nextBluePrints.add(blueprints.obsidianBluePrint);
            }
            if (robots[OBSIDIAN] != 0) {
                nextBluePrints.add(blueprints.geodeBluePrint);
            }

            return nextBluePrints;
        }

        public boolean hasEnoughResource(BluePrint bluePrint) {
            return IntStream.range(0, 3)
                    .allMatch(i -> bluePrint.required[i] <= resources[i]);
        }

        public State build(BluePrint nextRobot) {
            int[] nextResources = IntStream.range(0, 4)
                    .map(i -> resources[i] - nextRobot.required[i])
                    .toArray();
            int[] nextRobots = Arrays.copyOf(robots, robots.length);
            nextRobots[nextRobot.kind]++;
            return new State(nextRobots, nextResources, time);
        }
    }


    record BluePrint(int[] required, int kind) {

    }

    record Blueprints(BluePrint oreBluePrint, BluePrint clayRobot, BluePrint obsidianBluePrint,
                      BluePrint geodeBluePrint) {


        static Blueprints parse(String s) {
            Matcher matcher = pattern.matcher(s);
            List<Integer> integers = matcher.results()
                    .map(MatchResult::group)
                    .map(Integer::parseInt)
                    .toList();

            return new Blueprints(
                    new BluePrint(new int[]{integers.get(1), 0, 0, 0}, ORE),
                    new BluePrint(new int[]{integers.get(2), 0, 0, 0}, CLAY),
                    new BluePrint(new int[]{integers.get(3), integers.get(4), 0, 0}, OBSIDIAN),
                    new BluePrint(new int[]{integers.get(5), 0, integers.get(6), 0}, GEODE)
            );
        }

        List<BluePrint> asList() {
            return List.of(oreBluePrint, clayRobot, obsidianBluePrint, geodeBluePrint);
        }
    }


}
