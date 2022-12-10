package y2022.d10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

public class Puzzle {

    private static int X = 1;
    private static int cycle = 0;
    private static int signalStrength = 0;
    private static StringBuilder crtBuilder = new StringBuilder();
    private static int spritePosition = 0;

    public static void main(String[] args) throws IOException {
        List<Integer> instructions = Files.lines(Paths.get("src/y2022/d10/input.txt"))
                .map(s -> s.equals("noop") ? 0 : parseInt(s.substring(5)))
                .toList();

        for (Integer instruction : instructions) {
            if (instruction == 0) {
                increaseCycle();
            } else {
                increaseCycle();
                increaseCycle();
                X += instruction;
            }
        }

        System.out.println("signalStrength = " + signalStrength);

        String CRT = crtBuilder.toString();
        IntStream.range(0, 6)
                .mapToObj(i -> CRT.substring(i * 40, (i * 40) + 40))
                .forEach(System.out::println);
    }

    private static void increaseCycle() {
        cycle++;

        if (X -1 <= spritePosition % 40 && spritePosition % 40 <=  X + 1) {
            crtBuilder.append("#");
        } else {
            crtBuilder.append(" ");
        }
        spritePosition++;

        if ((cycle - 20) % 40 == 0) {
            signalStrength += cycle * X;
        }
    }

}