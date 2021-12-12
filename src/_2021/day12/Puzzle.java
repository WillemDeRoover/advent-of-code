package _2021.day12;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Puzzle {

	private static Map<String, List<String>> caveMap;

	public static void main(String[] args) throws IOException {
		caveMap = Files.lines(Paths.get("src/_2021/day12/input.txt"))
				.map(s -> s.split("-"))
				.flatMap(a -> List.of(a, new String[]{a[1], a[0]}).stream())
				.collect(groupingBy(a -> a[0], mapping(a -> a[1], toList())));

		int totalPaths = findPaths("start", new ArrayList<>(), false);
		System.out.println(totalPaths);
	}

	private static int findPaths(String currentNode, ArrayList<String> currentPath, boolean doublePass) {
		if ("end".equals(currentNode)) {
			return 1;
		}
		if (currentNode.equals(currentNode.toLowerCase()) && currentPath.contains(currentNode)) {
			if("start".equals(currentNode)) {
				return 0;
			}
			if(!doublePass) {
				doublePass = true;
			} else {
				return 0;
			}
		}

		int totalPaths = 0;
		List<String> newNodes = caveMap.get(currentNode);
		for (String newNode : newNodes) {
			ArrayList<String> newPath = new ArrayList<>(currentPath);
			newPath.add(currentNode);
			totalPaths += findPaths(newNode, newPath, doublePass);
		}
		return totalPaths;

	}

}
