package _2020.day08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		List<String> operations = Files.lines(Paths.get("src/_2020/day08/input.txt"))
				.collect(Collectors.toList());

//		runOperations(operations); first part

		for(int i = 0; i < operations.size(); i++) {
			List<String> modifiedList = new ArrayList<>(operations);
			String operation = modifiedList.get(i);
			if(operation.startsWith("nop")) {
				modifiedList.set(i, operation.replace("nop", "jmp"));
				runOperations(modifiedList);
			} else if (operation.startsWith("jmp")) {
				modifiedList.set(i, operation.replace("jmp", "nop"));
				runOperations(modifiedList);
			}
		}
	}

	private static void runOperations(List<String> operations) {
		int accumulator = 0;
		int position = 0;
		Set<Integer> visited = new HashSet<>();

		while (!visited.contains(position) && position < operations.size()) {
			visited.add(position);
			String operation = operations.get(position);
			String instruction = operation.substring(0, 3);
			String amount = operation.substring(4);

			switch(instruction) {
				case "acc":
					accumulator += Integer.parseInt(amount);
					position++;
					break;
				case "jmp":
					position += Integer.parseInt(amount);
					break;
				case "nop":
					position++;
					break;
				}
		}

		if(position == operations.size()) { //remove for first part;
			System.out.println(accumulator);
		}
	}
}
