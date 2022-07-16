package _2015.day01;

import util.functional.TailCall;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Puzzle {

    public static void main(String[] args) throws IOException {
        String instructions = Files.readString(Paths.get("src/_2015/day01/input.txt"));
        System.out.println(travelStairs(0, instructions).invoke());
        System.out.println(findBasement(0, 1, instructions).invoke());
    }

    private static TailCall<Integer> travelStairs(int floor, String directions) {
        if (!directions.isEmpty()) {
            int nextFloor = directions.charAt(0) == '(' ? ++floor : --floor;
            return () -> travelStairs(nextFloor, directions.substring(1));
        }
        return TailCall.result(floor);
    }

    private static TailCall<Integer> findBasement(int floor, int position, String directions) {
        int newFloor = directions.charAt(0) == '(' ? ++floor : --floor;
        if (newFloor == -1) {
            return TailCall.result(position);
        }
        return () -> findBasement(newFloor, position + 1, directions.substring(1));
    }
}

