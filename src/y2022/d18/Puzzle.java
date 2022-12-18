package y2022.d18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;

public class Puzzle {

    private static int maxX;
    private static int minX;
    private static int maxY;
    private static int minY;
    private static int maxZ;
    private static List<Cube> cubes;
    private static int minZ;

    public static void main(String[] args) throws IOException {
        cubes = Files.lines(Paths.get("src/y2022/d18/input.txt"))
                .filter(not(String::isEmpty))
                .map(s -> s.split(","))
                .map(Cube::of)
                .toList();

        List<Cube> nonAdjacentSides = getAllSurfaces();
        System.out.println("external surface = " + nonAdjacentSides.size());

        maxX = cubes.stream().mapToInt(Cube::x).max().getAsInt();
        minX = cubes.stream().mapToInt(Cube::x).min().getAsInt();
        maxY = cubes.stream().mapToInt(Cube::y).max().getAsInt();
        minY = cubes.stream().mapToInt(Cube::y).min().getAsInt();
        maxZ = cubes.stream().mapToInt(Cube::z).max().getAsInt();
        minZ = cubes.stream().mapToInt(Cube::z).min().getAsInt();

        int nonContainedSides = filterContained(nonAdjacentSides);
        System.out.println("nonContainedSides = " + nonContainedSides);
    }

    private static List<Cube> getAllSurfaces() {
        return cubes.stream()
                .map(Cube::getNeighbours)
                .flatMap(Collection::stream)
                .filter(not(cubes::contains))
                .toList();
    }

    private static int filterContained(List<Cube> nonAdjacentSides) {
        int count = 0;
        for (Cube nonAdjacant : nonAdjacentSides) {
            Set<Cube> recentSearchSpace = new HashSet<>();
            Set<Cube> currentSearchSpace = Set.of(nonAdjacant);

            if (search(currentSearchSpace, recentSearchSpace)) {
                count++;
            }
        }
        return count;
    }

    private static boolean search(Set<Cube> currentSearchSpace, Set<Cube> recentSearchSpace) {

        if (currentSearchSpace.isEmpty()) {
            return false;
        }

        Set<Cube> nextSearchSpace = new HashSet<>();
        for (Cube cube : currentSearchSpace) {

            if (cube.x > maxX || cube.x < minX || cube.y > maxY || cube.y < minY || cube.z > maxZ || cube.z < minZ) {
                return true;
            }
            recentSearchSpace.add(cube);
            Set<Cube> neighBours = cube.getNeighbours().stream()
                    .filter(not(cubes::contains))
                    .filter(not(recentSearchSpace::contains))
                    .collect(toSet());
            nextSearchSpace.addAll(neighBours);
        }

        return search(nextSearchSpace, recentSearchSpace);
    }

    record Cube(int x, int y, int z) {

        static Cube of(String[] sArray) {
            int x = Integer.parseInt(sArray[0]);
            int y = Integer.parseInt(sArray[1]);
            int z = Integer.parseInt(sArray[2]);
            return new Cube(x, y, z);
        }

        Set<Cube> getNeighbours() {
            return Stream.of(
                            Stream.of(-1, 1).map(i -> new Cube(x + i, y, z)),
                            Stream.of(-1, 1).map(i -> new Cube(x, y + i, z)),
                            Stream.of(-1, 1).map(i -> new Cube(x, y, z + i)))
                    .flatMap(Function.identity())
                    .collect(toSet());
        }
    }
}
