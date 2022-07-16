package _2021.day22;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		List<String> instructions = Files.lines(Paths.get("src/_2021/day22/input.txt")).collect(toList());

		process(instructions.stream().limit(20).collect(toList()));
		process(instructions);
	}

	private static void process(List<String> instructions) {
		List<Cube> cubes = new ArrayList<>();

		for (String instruction : instructions) {
			Cube nextCube = new Cube(instruction);
			cubes = cubes.stream()
					.map(aCube -> split(aCube, nextCube))
					.flatMap(Collection::stream)
					.collect(toList());

			if(nextCube.on) {
				cubes.add(nextCube);
			}
		}

		long sum = cubes.stream().mapToLong(Cube::size).sum();
		System.out.println(sum);
	}

	private static List<Cube> split(Cube existingCube, Cube newCube) {
		if(newCube.contains(existingCube)) {
			return List.of();
		}

		if(!newCube.intersects(existingCube)) {
			return List.of(existingCube);
		}


		List<Long> newX = Stream.of(newCube.x1, newCube.x2).filter(x -> x > existingCube.x1 && x < existingCube.x2).collect(toList());
		ArrayList<Long> xCoordinates = new ArrayList<>(List.of(existingCube.x1, existingCube.x2));
		xCoordinates.addAll(1, newX);

		List<Long> newY = Stream.of(newCube.y1, newCube.y2).filter(y -> y > existingCube.y1 && y < existingCube.y2).collect(toList());
		ArrayList<Long> yCoordinates = new ArrayList<>(List.of(existingCube.y1, existingCube.y2));
		yCoordinates.addAll(1, newY);

		List<Long> newZ = Stream.of(newCube.z1, newCube.z2).filter(z -> z > existingCube.z1 && z < existingCube.z2).collect(toList());
		ArrayList<Long> zCoordinates = new ArrayList<>(List.of(existingCube.z1, existingCube.z2));
		zCoordinates.addAll(1, newZ);

		List<Cube> childCubes = new ArrayList<>();
		for (int i = 0; i < xCoordinates.size() - 1; i++) {
			for (int j = 0; j < yCoordinates.size() - 1; j++) {
				for (int k = 0; k < zCoordinates.size() - 1; k++) {
					Cube child = new Cube(true, xCoordinates.get(i), xCoordinates.get(i + 1), yCoordinates.get(j), yCoordinates.get(j + 1), zCoordinates.get(k), zCoordinates.get(k + 1));
					if(!newCube.contains(child)) {
						childCubes.add(child);
					}

				}
			}
		}
		return childCubes;
	}

	private static class Cube {
		boolean on;
		long x1;
		long x2;
		long y1;
		long y2;
		long z1;
		long z2;

		private static final Pattern pattern = Pattern.compile("x=(-?\\d+)\\.\\.(-?\\d+),y=(-?\\d+)\\.\\.(-?\\d+),z=(-?\\d+)\\.\\.(-?\\d+)");

		public Cube(String instruction) {
			on = instruction.split(" ")[0].equals("on");
			Matcher matcher = pattern.matcher(instruction);
			matcher.find();
			x1 = parseInt(matcher.group(1));
			x2 = parseInt(matcher.group(2)) + 1;
			y1 = parseInt(matcher.group(3));
			y2 = parseInt(matcher.group(4)) + 1;
			z1 = parseInt(matcher.group(5));
			z2 = parseInt(matcher.group(6)) + 1;
		}

		public Cube(boolean on, long x1, long x2, long y1, long y2, long z1, long z2) {
			this.on = on;
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
			this.z1 = z1;
			this.z2 = z2;
		}

		public boolean contains(Cube other) {
			return this.x1 <= other.x1 && this.x2 >= other.x2
				&& this.y1 <= other.y1 && this.y2 >= other.y2
				&& this.z1 <= other.z1 && this.z2 >= other.z2;
		}

		public boolean intersects(Cube other) {
			return this.x1 <= other.x2 && other.x1 <= this.x2
				&& this.y1 <= other.y2 && other.y1 <= this.y2
				&& this.z1 <= other.z2 && other.z1 <= this.z2;
		}

		public long size() {
			return (this.x2 - this.x1) * (this.y2 - this.y1) * (this.z2 - this.z1);
		}
	}
}
