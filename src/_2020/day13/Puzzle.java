package _2020.day13;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

public class Puzzle {

	private static final int OFFSET = 0;
	private static final int ID = 1;

	public static void main(String[] args) throws IOException {

		List<String> schedule = Files.lines(Paths.get("src/_2020/day13/input.txt"))
				.collect(toList());

		int startTime = parseInt(schedule.get(0));
		String[] busIds = schedule.get(1).split(",");
		List<int[]> busses = IntStream.range(0, busIds.length)
				.filter(i -> !busIds[i].equals("x"))
				.mapToObj(i -> new int[] { i, parseInt(busIds[i]) })
				.collect(toList());

		getTimeUntilFirstBus(busses, startTime);
		getTimestampSubsequentBusses(busses);
	}

	private static void getTimeUntilFirstBus(List<int[]> busses, int startTime) {
		for(int i = startTime;; i++) {
			for (int[] bus : busses) {
				if(i % bus[ID] == 0 ){
					System.out.println((i - startTime) * bus[ID]);
					return;
				}
			}
		}
	}

	private static void getTimestampSubsequentBusses(List<int[]> busses) {
		long current = 1;
		long sum = 1;
		for (int[] bus : busses) {
			while(((current + bus[OFFSET]) % bus[ID]) != 0) {
				current += sum;
			}
			sum *= bus[ID];
		}
		System.out.println(current);
	}
}
