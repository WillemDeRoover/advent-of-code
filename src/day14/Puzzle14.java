package day14;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Puzzle14 {

	private static final String FUEL = "FUEL";
	private static final String ORE = "ORE";
	private static Map<String, Process> processMap;
	private static final Long TRILLION = 1000000000000L;
	private static List<String> lines;

	static {
		try {
			lines = Files.lines(Paths.get("day14input.txt")).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("The minimum amount of ORE required to produce exactly 1 FUEL is: " + calculateOreAmountRequiredForFuel(1));
		System.out.println("The maximum amount of FUEL that can be produced from 1 Trillion ORE is: " + calculateOreAmountRequiredForTrillionFuel());
	}

	private static double calculateOreAmountRequiredForFuel(int fuelAmount) {
		initProcessMap();

		processMap.get(FUEL).addToProduction(fuelAmount);
		return processMap.values().stream()
				.filter(Process::isOreProcess)
				.mapToDouble(Process::calculateOre)
				.sum();
	}

	private static void initProcessMap() {
		processMap = lines.stream()
					.map(line -> line.split(("=>")))
					.collect(Collectors.toMap(inputOutput -> getResourceName(inputOutput[1]), Puzzle14::createResourceRequirement));
	}

	static Process createResourceRequirement(String[] inputOutput) {
		return new Process(inputOutput[0], inputOutput[1]);
	}

	static String getResourceName(String string) {
		return string.trim().split(" ")[1];
	}

	private static long calculateOreAmountRequiredForTrillionFuel() {
		double start = Math.ceil(TRILLION / calculateOreAmountRequiredForFuel(1));
		long min = (int) start;
		long max = (int) start * 2;
		long fuelAmount;
		long resultIndex = 0;

		while(min <= max) {
			fuelAmount = (min + max)/2;
			if(calculateOreAmountRequiredForFuel((int)fuelAmount) < TRILLION) {
				resultIndex = fuelAmount;
				min = fuelAmount + 1;
			} else {
				max  = fuelAmount - 1;
			}
		}
		return resultIndex;
	}

	public static class Process {
		String output;
		int outputPerCycle;
		List<Resource> inputResources;

		double requestedProductionCycles = 0;
		double requestedProductionAmount = 0;

		Process(String input, String output) {
			this.output = output.trim().split(" ")[1];
			this.outputPerCycle = Integer.parseInt(output.trim().split(" ")[0]);
			this.inputResources = Stream.of(input.split(", ")).map(Resource::new).collect(toList());
		}

		void addToProduction(double amount) {
			requestedProductionAmount += amount;
			long requiredProductionCycles = (long) Math.ceil(requestedProductionAmount / outputPerCycle);
			if(requiredProductionCycles > requestedProductionCycles) {
				if (!isOreProcess()) {
					inputResources.forEach(resource -> processMap.get(resource.getName()).addToProduction((requiredProductionCycles - requestedProductionCycles) * resource.getAmount()));
				}
				requestedProductionCycles = requiredProductionCycles;
			}
		}

		boolean isOreProcess() {
			return inputResources.stream().anyMatch(resource -> resource.getName().equals(ORE));
		}

		double calculateOre() {
			return requestedProductionCycles * inputResources.get(0).amount;
		}

	}

	public static class Resource {
		String name;
		int amount;

		Resource(String string) {
			String[] s = string.trim().split(" ");
			this.name = s[1];
			this.amount = Integer.parseInt(s[0]);
		}

		String getName() {
			return name;
		}

		int getAmount() {
			return amount;
		}

	}

}
