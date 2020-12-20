package _2020.day20;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Puzzle {
	public static Pattern imagePattern = Pattern.compile("(\\d+):\\n([#.\\s]*)");

	public static void main(String[] args) throws IOException {
		Map<Integer, char[][]> imageMap = Stream.of(Files.readString(Paths.get("src/_2020/day20/input.txt")).split("\\n\\n"))
				.map(imagePattern::matcher)
				.filter(Matcher::find)
				.collect(Collectors.toMap(matcher -> Integer.parseInt(matcher.group(1)), matcher -> getMatrix(matcher.group(2))));

		Map<Integer, Set<Integer>> neighboursMap = calculateNeighbours(imageMap);
		System.out.println(calculateCornerControlDigit(neighboursMap));

		Image cornerImage = getCornerImage(imageMap, neighboursMap);
		List<Image> topImageLine = createTopLine(cornerImage, imageMap, neighboursMap);
		List<List<Image>> fullImage = createFullImage(topImageLine, imageMap, neighboursMap);

		removeBorders(fullImage);
		char[][] image = mapToArray(fullImage);
		image = rotate(flip(image));

		List<int[]> seaMonsterCoordinates = getSeaMonsterCoordinates();

		System.out.println(countWaves(image) - (countSeaMonsters(image, seaMonsterCoordinates) *  seaMonsterCoordinates.size()));

	}

	private static char[][] getMatrix(String image) {
		String[] lines = image.split("\\n");
		return Stream.of(lines).map(String::toCharArray).toArray(char[][]::new);
	}

	private static Map<Integer, Set<Integer>> calculateNeighbours(Map<Integer, char[][]> imageMap) {
		Map<Integer, List<Set<Integer>>> sidesMap = imageMap.entrySet().stream()
				.collect(toMap(Map.Entry::getKey, entry -> getSides(entry.getValue())));

		Map<Integer, Set<Integer>> neighBourMap = new HashMap<>();
		for (Map.Entry<Integer, List<Set<Integer>>> outerImage : sidesMap.entrySet()) {
			for (Map.Entry<Integer, List<Set<Integer>>> innerImage : sidesMap.entrySet()) {
				if (!outerImage.getKey().equals(innerImage.getKey())) {
					List<Set<Integer>> outerImageSides = outerImage.getValue();
					List<Set<Integer>> innerImageSides = innerImage.getValue();
					if (outerImageSides.stream().anyMatch(innerImageSides::contains)) {
						Set<Integer> outerMatches = neighBourMap.getOrDefault(outerImage.getKey(), new HashSet<>());
						outerMatches.add(innerImage.getKey());
						neighBourMap.put(outerImage.getKey(), outerMatches);
						Set<Integer> innerMatches = neighBourMap.getOrDefault(innerImage.getKey(), new HashSet<>());
						innerMatches.add(outerImage.getKey());
						neighBourMap.put(innerImage.getKey(), innerMatches);
					}
				}
			}
		}
		return neighBourMap;
	}

	private static List<Set<Integer>> getSides(char[][] image) {
		int maxWidth = image.length - 1;

		Set<Integer> topRow = getTopRow(image);
		Set<Integer> bottomRow = getBottomRow(image);
		Set<Integer> leftRow = getLeftRow(image);
		Set<Integer> rightRow = getRightRow(image);

		List<Set<Integer>> boundaries = List.of(topRow, rightRow, bottomRow, leftRow);
		List<Set<Integer>> reversedBoundaries = boundaries.stream().map(boundary -> reverse(boundary, maxWidth)).collect(toList());
		return Stream.concat(boundaries.stream(), reversedBoundaries.stream()).collect(toList());
	}

	private static Set<Integer> getTopRow(char[][] image) {
		return IntStream.range(0, image.length).filter(i -> image[0][i] == '#').boxed().collect(toSet());
	}

	private static Set<Integer> getBottomRow(char[][] image) {
		return IntStream.range(0, image.length).filter(i -> image[image.length - 1][i] == '#').boxed().collect(toSet());
	}

	private static Set<Integer> getLeftRow(char[][] image) {
		return IntStream.range(0, image.length).filter(i -> image[i][0] == '#').boxed().collect(toSet());
	}

	private static Set<Integer> getRightRow(char[][] image) {
		return IntStream.range(0, image.length).filter(i -> image[i][image.length - 1] == '#').boxed().collect(toSet());
	}

	private static Set<Integer> reverse(Set<Integer> topRow, int maxWidth) {
		return topRow.stream().map(i -> maxWidth - i).collect(toSet());
	}

	private static long calculateCornerControlDigit(Map<Integer, Set<Integer>> neighBourMap) {
		return neighBourMap.entrySet()
				.stream().filter(entry -> entry.getValue().size() == 2)
				.peek(entry -> System.out.println(entry.getKey()))
				.mapToLong(Map.Entry::getKey)
				.reduce((e1, e2) -> e1 * e2).getAsLong();
	}

	private static Image getCornerImage(Map<Integer, char[][]> imageMap, Map<Integer, Set<Integer>> neighboursMap) {
		Integer cornerImageNumber = neighboursMap.entrySet().stream().filter(e -> e.getValue().size() == 2).map(Map.Entry::getKey).findFirst().get();
		Set<Integer> neighbourNumbers = neighboursMap.get(cornerImageNumber);
		char[][] cornerImage1 = imageMap.get(cornerImageNumber);
		List<List<Set<Integer>>> neighbourSides = neighbourNumbers.stream().map(imageMap::get).map(Puzzle::getSides).collect(toList());

		int fullRotationCount = 0;
		while (!neighbourSides.get(0).contains(getRightRow(cornerImage1)) || !neighbourSides.get(1).contains(getBottomRow(cornerImage1))) {
			if (++fullRotationCount == 4) {
				cornerImage1 = flip(cornerImage1);
			}
			cornerImage1 = rotate(cornerImage1);
		}
		Image cornerImage = new Image(cornerImageNumber, cornerImage1);
		return cornerImage;
	}

	private static char[][] flip(char[][] pixels) {
		int size = pixels.length;
		char[][] ret = new char[size][size];
		for (int i = 0; i < size; ++i)
			for (int j = 0; j < size; ++j)
				ret[i][j] = pixels[i][size - 1 - j];
		return ret;

	}

	private static char[][] rotate(char[][] pixels) {
		int size = pixels.length;
		char[][] ret = new char[size][size];

		for (int i = 0; i < size; ++i)
			for (int j = 0; j < size; ++j)
				ret[i][j] = pixels[size - j - 1][i]; //***

		return ret;
	}

	private static List<Image> createTopLine(Image cornerImage, Map<Integer, char[][]> imageMap, Map<Integer, Set<Integer>> neighboursMap) {
		int previousNumber = cornerImage.imageNumber;
		char[][] previousImage = cornerImage.pixels;

		List<Image> imageLine = new ArrayList<>();
		imageLine.add(cornerImage);
		for (int i = 1; i < 12; i++) {
			final char[][] anotherPreviousImage = previousImage;
			Integer currentNumber = neighboursMap.get(previousNumber).stream().filter(neighbour -> getSides(imageMap.get(neighbour)).contains(getRightRow(anotherPreviousImage))).findFirst().get();
			char[][] currentImage = imageMap.get(currentNumber);

			int fullRotationCount = 0;
			while (!getRightRow(previousImage).equals(getLeftRow(currentImage))) {
				if (++fullRotationCount == 4) {
					currentImage = flip(currentImage);
				}
				currentImage = rotate(currentImage);
			}

			imageLine.add(new Image(currentNumber, currentImage));
			previousNumber = currentNumber;
			previousImage = currentImage;
		}
		return imageLine;
	}

	private static List<List<Image>> createFullImage(List<Image> imageLine, Map<Integer, char[][]> imageMap, Map<Integer, Set<Integer>> neighboursMap) {
		List<List<Image>> fullImage = new ArrayList<>();
		fullImage.add(imageLine);
		for (int i = 0; i < 11; i++) {
			List<Image> upperImageLine = fullImage.get(i);
			List<Image> currentImageLine = new ArrayList<>();
			for (Image upperImage : upperImageLine) {
				Integer currentNumber = neighboursMap.get(upperImage.imageNumber).stream().filter(neighbour -> getSides(imageMap.get(neighbour)).contains(getBottomRow(upperImage.pixels))).findFirst().get();
				char[][] currentImage = imageMap.get(currentNumber);

				int fullRotationCount = 0;
				while (!getBottomRow(upperImage.pixels).equals(getTopRow(currentImage))) {
					if (++fullRotationCount == 4) {
						currentImage = flip(currentImage);
					}
					currentImage = rotate(currentImage);
				}
				currentImageLine.add(new Image(currentNumber, currentImage));
			}
			fullImage.add(currentImageLine);
		}
		return fullImage;
	}

	private static void removeBorders(List<List<Image>> fullImage) {
		for (List<Image> images : fullImage) {
			for (Image image : images) {
				image.pixels = removeBorders(image.pixels);
			}
		}
	}

	private static char[][] removeBorders(char[][] pixels) {
		int size = pixels.length;
		char[][] ret = new char[size-2][size-2];

		for (int i = 1; i < size - 1; ++i)
			for (int j = 1; j < size - 1; ++j) {
				ret[i - 1][j - 1] = pixels[i][j];
			}

		return ret;
	}

	private static char[][] mapToArray(List<List<Image>> fullImage) {
		char[][] image= new char[112][112];
		for(int i = 0; i < fullImage.size(); i++) {
			List<Image> images = fullImage.get(i);
			for (int j = 0; j < images.size(); j++) {
				char[][] currentImage = images.get(j).pixels;
				for (int y = 0; y < currentImage.length; y++) {
					for (int x = 0; x < currentImage[y].length; x++) {
						image[i * 8 + y][j * 8 + x] = currentImage[y][x];
					}
				}
			}
		}
		return image;
	}

	private static List<int[]> getSeaMonsterCoordinates() throws IOException {
		String[] split = Files.readString(Paths.get("src/_2020/day20/seamonster.txt")).split("\\n");
		List<int[]> seaMonsterCoordinates = new ArrayList<>();
		for (int y = 0; y < split.length; y++) {
			for (int x = 0; x < split[y].length(); x++) {
				if(split[y].charAt(x) == '#') {
					seaMonsterCoordinates.add(new int[] {y, x});
				}
			}
		}
		return seaMonsterCoordinates;
	}

	private static int countSeaMonsters(char[][] image, List<int[]> seaMonsterCoordinates) {
		int seamonstersCount = 0;
		char[][] newImage = image;
		for (int i = 0; i < image.length - 3; i++) {
			for (int j = 0; j < image[i].length - 20; j++) {
				int y = i;
				int x = j;
				if (seaMonsterCoordinates.stream().allMatch(baseCoordinates -> newImage[baseCoordinates[0] + y][baseCoordinates[1] + x] == '#')) {
					seamonstersCount++;
				}
			}
		}
		return seamonstersCount;
	}

	private static int countWaves(char[][] image) {
		int count = 0;
		for (int y = 0; y < image.length; y++) {
			for (int x = 0; x < image[y].length; x++) {
				if(image[y][x] == '#') {
					count ++;
				}
			}
		}
		return count;
	}

	private static class Image {
		int imageNumber;
		char[][] pixels;

		public Image(int imageNumber, char[][] pixels) {
			this.imageNumber = imageNumber;
			this.pixels = pixels;
		}
	}

}
