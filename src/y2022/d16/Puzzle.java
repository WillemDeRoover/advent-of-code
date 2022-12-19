package y2022.d16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class Puzzle {

    private static Pattern pattern = Pattern.compile("Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? (.+)");
    private static Map<String, Valve> valveByName;
    private static Set<String> valveNames;
    private static Set<String> pressuredValveNames;

    public static void main(String[] args) throws IOException {
        valveByName = Files.lines(Paths.get("src/y2022/d16/input.txt"))
                .map(pattern::matcher)
                .map(m -> {
                    m.matches();
                    return m;
                })
                .collect(toMap(m -> m.group(1), m -> new Valve(parseInt(m.group(2)), asList(m.group(3).split(", ")))));
        valveNames = valveByName.keySet();
        pressuredValveNames = valveNames.stream().filter(v -> valveByName.get(v).pressure != 0).collect(toSet());
        Map<Path, Integer> distances = calculateDistances();

        Instant start = Instant.now();
        part2(distances);
        Instant end = Instant.now();
        System.out.println(ChronoUnit.MILLIS.between(start, end));

    }

    private static Map<Path, Integer> calculateDistances() {
        Map<Path, Integer> distances = new HashMap<>();
        for (String from : valveNames) {
            if (!from.equals("AA") && !pressuredValveNames.contains(from)) continue;
            for (String to : valveNames) {
                if (!pressuredValveNames.contains(to)) continue;
                Map<String, Integer> distanceMap = valveNames.stream().collect(Collectors.toMap(Function.identity(), __ -> Integer.MAX_VALUE));
                Set<String> unvisited = new HashSet<>(valveByName.keySet());
                distanceMap.put(from, 0);
                while (unvisited.contains(to)) {
                    String current = unvisited.stream().sorted().min(Comparator.comparing(distanceMap::get)).get();
                    unvisited.remove(current);
                    Integer currentDistance = distanceMap.get(current);
                    Valve currentValve = valveByName.get(current);
                    List<String> neighbours = currentValve.neighbours;
                    for (String neighbour : neighbours) {
                        if (unvisited.contains(neighbour)) {
                            Integer neighBourDistance = distanceMap.get(neighbour);
                            if (currentDistance + 1 < neighBourDistance) {
                                distanceMap.put(neighbour, currentDistance + 1);
                            }
                        }
                    }
                }
                distances.put(new Path(from, to), distanceMap.get(to) + 1);
            }
        }
        return distances;
    }

    private static void part1(Map<Path, Integer> distances) {
        Map<Integer, Map<String, State>> states = new HashMap<>();
        states.put(1, Map.of("AA", new State(Set.of("AA"), 0)));
        for (int i = 1; i <= 30; i++) {
            for (Map.Entry<String, State> entry : states.getOrDefault(i, Map.of()).entrySet()) {
                String currentValve = entry.getKey();
                for (String next : pressuredValveNames) {
                    State currentState = entry.getValue();
                    if (!next.equals(currentValve) && !currentState.visited.contains(next)) {
                        Path path = new Path(currentValve, next);
                        int time = distances.get(path);
                        int additionalPressure = (30 - (i - 1 + time)) * valveByName.get(next).pressure;
                        State nextState = currentState.add(next, additionalPressure);
                        Map<String, State> timedStates = states.getOrDefault(i + time, new HashMap<>());
                        states.put(i + time, timedStates);
                        State timedNextState = timedStates.getOrDefault(next, new State(Set.of(), Integer.MIN_VALUE));
                        if (nextState.currentPressure > timedNextState.currentPressure) {
                            timedStates.put(next, nextState);
                        }
                    }
                }
            }
        }
        states.values().stream().flatMap(m -> m.values().stream()).mapToInt(State::currentPressure).max().ifPresent(System.out::println);
    }

    private static void part2(Map<Path, Integer> distances) {
        Map<Integer, Map<Tuple<String>, DoubleState>> states = new HashMap<>();
        states.put(1, Map.of(new Tuple<>("AA", "AA"), new DoubleState(Set.of("AA"), new Tuple<>(1, 1), 0)));
        for (int i = 1; i <= 26; i++) {
            for (Map.Entry<Tuple<String>, DoubleState> entry : states.getOrDefault(i, Map.of()).entrySet()) {

                Tuple<String> currentPositions = entry.getKey();
                DoubleState currentState = entry.getValue();
                Set<String> nextMes = currentState.nextMoveAt.me != i ? Set.of(currentPositions.me) : pressuredValveNames.stream().filter(not(currentState.visited::contains)).collect(toSet());
                Set<String> nextElephants = currentState.nextMoveAt.elephant != i ? Set.of(currentPositions.elephant) : pressuredValveNames.stream().filter(not(currentState.visited::contains)).collect(toSet());

                for (String nextMe : nextMes) {
                    for (String nextElephant : nextElephants) {

                        if (nextMe.equals(nextElephant)) {
                            continue;
                        }

                        Tuple<String> nextPositions = currentPositions;
                        DoubleState nextState = currentState;

                        if (currentState.nextMoveAt.me == i) {
                            Path path = new Path(currentPositions.me, nextMe);
                            int time = distances.get(path);
                            int additionalPressure = (26 - (i - 1 + time)) * valveByName.get(nextMe).pressure;
                            int moveAt = i + time;
                            nextPositions = nextPositions.replaceMe(nextMe);
                            nextState = nextState.addMe(nextMe, moveAt, additionalPressure);
                        }

                        if (currentState.nextMoveAt.elephant == i) {
                            Path path = new Path(currentPositions.elephant, nextElephant);
                            int time = distances.get(path);
                            int additionalPressure = (26 - (i - 1 + time)) * valveByName.get(nextElephant).pressure;
                            int moveAt = i + time;
                            nextPositions = nextPositions.replaceElephant(nextElephant);
                            nextState = nextState.addElephant(nextElephant, moveAt, additionalPressure);
                        }

                        int nextTime = Math.min(nextState.nextMoveAt.me, nextState.nextMoveAt.elephant);
                        Map<Tuple<String>, DoubleState> timedStates = states.getOrDefault(nextTime, new HashMap<>());
                        states.put(nextTime, timedStates);

                        DoubleState timedNextState = timedStates.getOrDefault(nextPositions, new DoubleState(Set.of(), new Tuple<>(0, 0), Integer.MIN_VALUE));
                        if (nextState.currentPressure > timedNextState.currentPressure) {
                            timedStates.put(nextPositions, nextState);
                        }

                    }
                }
            }
        }

        states.values().stream().flatMap(m -> m.values().stream()).mapToInt(DoubleState::currentPressure).max().ifPresent(System.out::println);
    }

    record Valve(int pressure, List<String> neighbours) { }

    record Path(String from, String to) { }

    record State(Set<String> visited, int currentPressure) {

        public State add(String next, int additionalPressure) {
            Set<String> nextVisited = new HashSet<>();
            nextVisited.addAll(visited);
            nextVisited.add(next);
            return new State(nextVisited, currentPressure + additionalPressure);
        }
    }

    record Tuple<T>(T me, T elephant) {

        Tuple<T> replaceMe(T other) {
            return new Tuple<>(other, elephant);
        }

        Tuple<T> replaceElephant(T other) {
            return new Tuple<>(me, other);
        }
    }

    record DoubleState(Set<String> visited, Tuple<Integer> nextMoveAt, int currentPressure) {

        public DoubleState addMe(String next, int moveAt, int additionalPressure) {
            Set<String> nextVisited = new HashSet<>();
            nextVisited.addAll(visited);
            nextVisited.add(next);
            return new DoubleState(nextVisited, nextMoveAt.replaceMe(moveAt), currentPressure + additionalPressure);
        }

        public DoubleState addElephant(String next, int moveAt, int additionalPressure) {
            Set<String> nextVisited = new HashSet<>();
            nextVisited.addAll(visited);
            nextVisited.add(next);
            return new DoubleState(nextVisited, nextMoveAt.replaceElephant(moveAt), currentPressure + additionalPressure);
        }
    }

}
