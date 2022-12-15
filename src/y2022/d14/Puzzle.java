package y2022.d14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class Puzzle {

    private static Set<Location> caveMap;

    public static void main(String[] args) throws IOException {
        caveMap = Files.lines(Paths.get("src/y2022/d14/input.txt"))
                .map(s -> s.split(" -> "))
                .map(s -> Stream.of(s).map(Location::from).toList())
                .flatMap(locations -> {
                    List<Path> paths = new ArrayList<>();
                    for (int i = 0; i <= locations.size() - 2; i++) {
                        paths.add(new Path(locations.get(i), locations.get(i + 1)));
                    }
                    return paths.stream();
                })
                .map(Path::getLocations)
                .flatMap(List::stream)
                .collect(toSet());

        int floorHeight = caveMap.stream().mapToInt(Location::y).map(i -> i + 2).max().getAsInt();
        Set<Location> floor = IntStream.rangeClosed(0, 1000).mapToObj(i -> new Location(i, floorHeight)).collect(toSet());
        caveMap.addAll(floor);
        int rocks = caveMap.size();

        Location top = new Location(500, 0);
        a: while(!caveMap.contains(top)) {
            Location sand = top;
            boolean falling = true;
            while (falling) {

                Location drop = new Location(sand.x, sand.y + 1);
                Location dropLeft = new Location(sand.x - 1, sand.y + 1);
                Location dropRight = new Location(sand.x + 1, sand.y + 1);
                if (caveMap.contains(drop) && caveMap.contains(dropLeft) && caveMap.contains(dropRight)) {
                    caveMap.add(sand);
                    falling = false;
                } else if (!caveMap.contains(drop)) {
                    sand = new Location(sand.x, sand.y + 1);
                } else if (!caveMap.contains(dropLeft)) {
                    sand = new Location(sand.x - 1, sand.y + 1);
                } else {
                    sand = new Location(sand.x + 1, sand.y + 1);
                }

                //part1
                if(sand.y > floorHeight) {
                    break a;
                }
            }
        }

        System.out.println(caveMap.size() - rocks);


    }

    record Location(int x, int y) {
        private static Location from(String s) {
            String[] split = s.split(",");
            return new Location(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        }
    }

    record Path(Location start, Location end) {

        List<Location> getLocations() {
            if (start.x < end.x) {
                return IntStream.rangeClosed(start.x, end.x).mapToObj(x -> new Location(x, start().y)).toList();
            } else if (start.x > end.x) {
                return IntStream.rangeClosed(end.x, start.x).mapToObj(x -> new Location(x, start().y)).toList();
            } else if (start.y < end.y) {
                return IntStream.rangeClosed(start.y, end.y).mapToObj(y -> new Location(start.x, y)).toList();
            } else {
                return IntStream.rangeClosed(end.y, start.y).mapToObj(y -> new Location(start.x, y)).toList();
            }
        }

    }

}
