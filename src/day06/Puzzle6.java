package day06;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.tools.javac.util.Pair;

public class Puzzle6 {

	public static void main(String[] args) throws IOException {
		Map<String, List<String>> centerOf = Files.lines(Paths.get("day6input.txt"))
				.map(Puzzle6::splitLine)
				.collect(groupingBy(pair -> pair.fst, mapping(pair -> pair.snd, toList())));

		Map<String, List<String>> orbitMap = new HashMap<>();
		String currentPlant = "COM";
		orbitMap.put(currentPlant, new ArrayList<>());

		computeOrbitMap(centerOf, currentPlant, orbitMap);
		System.out.println("orbit count checksums: " + orbitMap.values().stream().mapToInt(List::size).sum());

		computeOrbitalTransfers(orbitMap);
	}

	private static Pair<String, String> splitLine(String line) {
		String[] split = line.split("\\)");
		return Pair.of(split[0], split[1]);
	}

	private static void computeOrbitMap(Map<String, List<String>> centerOf, String currentPlant, Map<String, List<String>> orbitMap) {
		List<String> orbitalPlanets = centerOf.getOrDefault(currentPlant,new ArrayList<>());
		for(String orbitalPlanet : orbitalPlanets) {
			List<String> orbitList = new ArrayList<>();
			orbitList.add(currentPlant);
			orbitList.addAll(orbitMap.get(currentPlant));
			orbitMap.put(orbitalPlanet, orbitList);
			computeOrbitMap(centerOf, orbitalPlanet, orbitMap);
		}
	}

	private static void computeOrbitalTransfers(Map<String, List<String>> orbits) {
		List<String> myOrbits = orbits.get("YOU");
		List<String> sanOrbits = orbits.get("SAN");

		myOrbits.stream()
				.filter(sanOrbits::contains)
				.findFirst()
				.map(planet -> myOrbits.indexOf(planet) +  sanOrbits.indexOf(planet) )
				.ifPresent(transferCount -> System.out.println("minimum number of orbital transfers required: " + transferCount));
	}
}
