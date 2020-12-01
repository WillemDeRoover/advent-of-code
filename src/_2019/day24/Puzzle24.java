package _2019.day24;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Puzzle24 {

	private static final char BUG = '#';
	private static int TOTAL_MINUTES = 200;

	public static void main(String[] args) throws IOException {
		detectCycle();
		part2();
	}

	private static void detectCycle() throws IOException {
		List<String> eris = Files.lines(Paths.get("src/y2020.day24/input")).collect(toList());
		eris = extendedWithBorder(eris);
		Set<Long> bioDiversityRatings = new HashSet<>();
		while (true) {
			eris = processOneMinute(eris);
			long bioDiversity = calculateBioDiversity(eris);
			if (bioDiversityRatings.contains(bioDiversity)) {
				System.out.println(bioDiversity);
				break;
			}
			bioDiversityRatings.add(bioDiversity);
		}
	}

	private static List<String> processOneMinute(List<String> eris) {
		List<String> newEris = new ArrayList<>();

		for (int y = 1; y < 6; y++) {
			String newLine = "";
			for (int x = 1; x < 6; x++) {
				char tile = calculateBioDiversity(x, y, eris);
				newLine = newLine + tile;
			}
			newEris.add(newLine);
		}
		return extendedWithBorder(newEris);
	}

	private static List<String> extendedWithBorder(List<String> grid) {
		for (int y = 0; y < grid.size(); y++) {
			String newLine = "." + grid.get(y) + ".";
			grid.set(y, newLine);
		}
		String emptyLine = IntStream.range(0, grid.size() + 2).mapToObj(e -> ".").collect(Collectors.joining(""));
		grid.add(emptyLine);
		grid.add(0, emptyLine);
		return grid;
	}

	private static char calculateBioDiversity(int x, int y, List<String> eris) {
		char north = eris.get(y - 1).charAt(x);
		char south = eris.get(y + 1).charAt(x);
		char east = eris.get(y).charAt(x + 1);
		char west = eris.get(y).charAt(x - 1);
		long bugCount = Stream.of(north, south, east, west).filter(tile -> tile == BUG).count();
		char currentTile = eris.get(y).charAt(x);

		if (currentTile == BUG) {
			if (bugCount == 1) {
				return BUG;
			}
		} else {
			if (bugCount == 1 || bugCount == 2) {
				return BUG;
			}
		}
		return '.';
	}

	private static long calculateBioDiversity(List<String> grid) {
		int tileNumber = 0;
		long biodiversity = 0;

		for (int y = 1; y < 6; y++) {
			for (int x = 1; x < 6; x++) {
				if (grid.get(y).charAt(x) == BUG) {
					biodiversity += Math.pow(2, tileNumber);
				}
				tileNumber++;
			}
		}
		return biodiversity;
	}

	private static void part2() throws IOException {
		List<String> grid = Files.lines(Paths.get("src/y2020.day24/recursiveinput")).collect(toList());
		Map<Integer, List<String>> levelMap = new HashMap<>();
		Level eris = new Level(grid, 0);
		for (int i = 0; i < TOTAL_MINUTES; i++) {
			eris.processOneMinute();
		}
		System.out.println(eris.calculateBugs());
	}

	private static class Level {
		private List<String> current;
		private int level;
		private Level upperLevel;
		private Level lowerLevel;

		Level(List<String> current, int level) {
			this.current = current;
			this.level = level;
		}

		private Level(int level, Level upperLevel, Level lowerLevel) {
			this.level = level;
			this.current = createEmptyGrid();
			this.upperLevel = upperLevel;
			this.lowerLevel = lowerLevel;
		}

		private List<String> createEmptyGrid() {
			List<String> grid = new ArrayList<>();
			grid.add(".....");
			grid.add(".....");
			grid.add("..?..");
			grid.add(".....");
			grid.add(".....");
			return grid;
		}

		private Optional<Level> getUpperLevel() {
			return Optional.ofNullable(upperLevel);
		}

		private Optional<Level> getLowerLevel() {
			return Optional.ofNullable(lowerLevel);
		}

		long calculateBugs() {
			long total = 0;
			for(int i = 0; i < current.size(); i++) {
				total += current.get(i).chars().filter(tile -> tile == BUG).count();
			}

			if(level >= 0 && upperLevel != null) {
				total += upperLevel.calculateBugs();
			}

			if(level <= 0 && lowerLevel != null) {
				total += lowerLevel.calculateBugs();
			}

			return total;

		}

		private void processOneMinute() {
			List<String> next = new ArrayList<>();

			for (int y = 0; y < 5; y++) {
				String newLine = "";
				for (int x = 0; x < 5; x++) {
					char tile = '?';
					if(x != 2 || y!=2) {
						tile = calculateTile(x, y, current);
					}
					newLine = newLine + tile;
				}
				next.add(newLine);
			}

			if(level >= 0) {
				processUpperLevel();
			}

			if(level <= 0) {
				processLowerLevel();
			}

			current = next;
		}

		private void processUpperLevel() {
			if(upperLevel == null) {
				if (getBugCountOuterTiles() > 0) {
					upperLevel = new Level(level + 1, null, this);
					upperLevel.processOneMinute();
				}
			} else {
				upperLevel.processOneMinute();
			}
		}

		private long getBugCountOuterTiles() {
			return getBugCountOuterNorthTiles() + getBugCountOuterSouthTiles() + getBugCountOuterEastTiles() + getBugCountOuterWestTiles();
		}

		private void processLowerLevel() {
			if(lowerLevel == null) {
				if(getBugCountInnerTiles() > 0) {
					lowerLevel = new Level(level -1, this, null);
					lowerLevel.processOneMinute();
				}
			} else {
				lowerLevel.processOneMinute();
			}
		}

		private long getBugCountInnerTiles() {
			return getBugCountInnerNorthTile() + getBugCountInnerSouthTile() + getBugCountInnerEastTile() + getBugCountInnerWestTile();
		}

		private char calculateTile(int x, int y, List<String> eris) {
			long bugCountNorth = getBugCountNorthTiles(x, y, eris);
			long bugCountSouth = getBugCountSouthTiles(x, y, eris);
			long bugCountWest = getBugCountWestTiles(x, y, eris);
			long bugCountEast = getBugCountEastTiles(x, y, eris);

			long bugCount = bugCountNorth + bugCountSouth + bugCountWest + bugCountEast;
			char currentTile = eris.get(y).charAt(x);

			if (currentTile == BUG) {
				if (bugCount == 1) {
					return BUG;
				}
			} else {
				if (bugCount == 1 || bugCount == 2) {
					return BUG;
				}
			}
			return '.';
		}

		private long getBugCountNorthTiles(int x, int y, List<String> eris) {
			if(y == 0) {
				return getUpperLevel().map(Level::getBugCountInnerNorthTile).orElse(0L);
			} else if ( y == 3 && x ==2) {
				return getLowerLevel().map(Level::getBugCountOuterSouthTiles).orElse(0L);
			} else {
				return eris.get(y-1).charAt(x) == BUG ? 1 : 0;
			}
		}

		private long getBugCountSouthTiles(int x, int y, List<String> eris) {
			if(y == 4) {
				return getUpperLevel().map(Level::getBugCountInnerSouthTile).orElse(0L);
			} else if ( y == 1 && x ==2) {
				return getLowerLevel().map(Level::getBugCountOuterNorthTiles).orElse(0L);
			} else {
				return eris.get(y+1).charAt(x) == BUG ? 1 : 0;
			}
		}

		private long getBugCountWestTiles(int x, int y, List<String> eris) {
			if(x == 0) {
				return getUpperLevel().map(Level::getBugCountInnerWestTile).orElse(0L);
			} else if ( y == 2 && x ==3) {
				return getLowerLevel().map(Level::getBugCountOuterEastTiles).orElse(0L);
			} else {
				return eris.get(y).charAt(x-1) == BUG ? 1 : 0;
			}
		}

		private long getBugCountEastTiles(int x, int y, List<String> eris) {
			if(x == 4) {
				return getUpperLevel().map(Level::getBugCountInnerEastTile).orElse(0L);
			} else if ( y == 2 && x ==1) {
				return getLowerLevel().map(Level::getBugCountOuterWestTiles).orElse(0L);
			} else {
				return eris.get(y).charAt(x+1) == BUG ? 1 : 0;
			}
		}

		long getBugCountOuterNorthTiles() {
			return current.get(0).chars().filter(tile -> tile == BUG).count();
		}

		long getBugCountInnerNorthTile() {
			return current.get(1).charAt(2) == BUG ? 1 : 0;
		}

		long getBugCountOuterSouthTiles() {
			return current.get(4).chars().filter(tile -> tile == BUG).count();
		}

		long getBugCountInnerSouthTile() {
			return current.get(3).charAt(2) == BUG ? 1 : 0;
		}

		long getBugCountOuterEastTiles() {
			return current.stream().map(line -> line.charAt(4)).filter(tile -> tile == BUG).count();
		}

		long getBugCountInnerEastTile() {
			return current.get(2).charAt(3) == BUG ? 1 : 0;
		}

		long getBugCountOuterWestTiles() {
			return current.stream().map(line -> line.charAt(0)).filter(tile -> tile == BUG).count();
		}

		long getBugCountInnerWestTile() {
			return current.get(2).charAt(1) == BUG ? 1 : 0;
		}

	}

}
