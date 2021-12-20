package _2021.day18;

import static java.lang.Character.getNumericValue;
import static java.lang.Character.isDigit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Puzzle {

	private static List<PairNode> neighbours = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		List<String> pairLines = Files.lines(Paths.get("src/_2021/day18/input.txt")).collect(Collectors.toList());
		part1(pairLines);
		part2(pairLines);
	}

	private static void part1(List<String> pairLines) {
		PairNode parentPairNode = createPairNode(pairLines.get(0));
		for (int i = 1; i < pairLines.size(); i++) {
			PairNode leftPairNode = parentPairNode;
			parentPairNode = new PairNode();
			parentPairNode.setLeftPair(leftPairNode);
			parentPairNode.setRightPair(createPairNode(pairLines.get(i)));

			while(explode(parentPairNode) || split());
		}

		int magnitude = calculateMagnitude(parentPairNode);
		System.out.println(magnitude);
	}

	private static void part2(List<String> pairLines) {
		int highestMagnitude = 0;
		for (int i = 0; i < pairLines.size(); i++) {
			for (int j = 0; j < pairLines.size(); j++) {
				if(i == j) {
					continue;
				}
				neighbours = new ArrayList<>();
				PairNode parentPairNode = new PairNode(createPairNode(pairLines.get(i)), createPairNode(pairLines.get(j)));
				while(explode(parentPairNode) || split());

				int magnitude = calculateMagnitude(parentPairNode);
				if(magnitude > highestMagnitude) {
					highestMagnitude = magnitude;
				}

			}
		}

		System.out.println(highestMagnitude);
	}

	private static PairNode createPairNode(String pairString) {
		int braces = 0;
		if (isDigit(pairString.charAt(0))) {
			PairNode pairNode = new PairNode(getNumericValue(pairString.charAt(0)));
			neighbours.add(pairNode);
			return pairNode;
		}
		for (int i = 0; i < pairString.length(); i++) {
			char currentChar = pairString.charAt(i);
			if (currentChar == '[') {
				braces++;
			} else if (currentChar == ']') {
				braces--;
			} else if (currentChar == ',' && braces == 1) {
				PairNode leftPair = createPairNode(pairString.substring(1, i));
				PairNode rightPairNode = createPairNode(pairString.substring(i + 1, pairString.length() - 1));
				PairNode pairNode = new PairNode(leftPair, rightPairNode);
				return pairNode;
			}
		}
		return null;
	}

	private static Optional<PairNode> findExplode(PairNode currentNode, int level) {
		if (currentNode.isRegularPair() && level >= 4) {
			return Optional.of(currentNode);
		} else if (currentNode.isLeafNode) {
			return Optional.empty();
		} else {
			return findExplode(currentNode.leftPair, level + 1)
					.or(() -> findExplode(currentNode.rightPair, level + 1));
		}
	}

	private static boolean explode(PairNode parentNode) {
		Optional<PairNode> explode = findExplode(parentNode, 0);
		if(explode.isPresent()) {
			PairNode toExplode = explode.get();
			int i = neighbours.indexOf(toExplode.leftPair);
			PairNode newPairNode = new PairNode(0);
			neighbours.add(i + 1, newPairNode);

			PairNode parentPair = toExplode.parentPair;
			if(parentPair.leftPair.equals(toExplode)) {
				parentPair.setLeftPair(newPairNode);
			} else {
				parentPair.setRightPair(newPairNode);
			}

			PairNode leftPair = toExplode.leftPair;
			int indexLeftPair = neighbours.indexOf(leftPair);
			if(indexLeftPair > 0) {
				PairNode leftNeighbourNode = neighbours.get(indexLeftPair - 1);
				leftNeighbourNode.value += leftPair.value;
			}
			neighbours.remove(leftPair);

			PairNode rightPair = toExplode.rightPair;
			int indexRightPair = neighbours.indexOf(rightPair);
			if(indexLeftPair < neighbours.size() - 2) {
				PairNode rightNeighbourNode = neighbours.get(indexRightPair + 1);
				rightNeighbourNode.value += rightPair.value;
			}
			neighbours.remove(rightPair);

			return true;
		}
		return false;
	}

	private static boolean split() {
		Optional<PairNode> splitNode = neighbours.stream().filter(node -> node.value >= 10).findFirst();
		if (splitNode.isPresent()) {
			PairNode aSplitNode = splitNode.get();
			int leftValue = (int) Math.floor((double) aSplitNode.value / 2);
			int rightValue = (int) Math.ceil((double) aSplitNode.value / 2);
			PairNode leftPairNode = new PairNode(leftValue);
			PairNode rightPairNode = new PairNode(rightValue);
			PairNode newPairNode = new PairNode(leftPairNode, rightPairNode);

			PairNode parentPair = aSplitNode.parentPair;
			if (parentPair.leftPair.equals(aSplitNode)) {
				parentPair.setLeftPair(newPairNode);
			} else {
				parentPair.setRightPair(newPairNode);
			}

			int i = neighbours.indexOf(aSplitNode);
			neighbours.add(i, rightPairNode);
			neighbours.add(i, leftPairNode);
			neighbours.remove(aSplitNode);
			return true;
		}
		return false;
	}

	private static int calculateMagnitude(PairNode parentPairNode) {
		if(parentPairNode.isLeafNode) {
			return parentPairNode.value;
		} else {
			return (3 * calculateMagnitude(parentPairNode.leftPair)) + (2 * calculateMagnitude(parentPairNode.rightPair));
		}
	}

	private static class PairNode {

		PairNode parentPair;
		PairNode leftPair;
		PairNode rightPair;
		Integer value;
		boolean isLeafNode;

		public PairNode() {
			isLeafNode = false;
		}

		public PairNode(Integer value) {
			this.value = value;
			this.isLeafNode = true;
		}

		public PairNode(PairNode leftPair, PairNode rightPair) {
			this.leftPair = leftPair;
			leftPair.parentPair = this;
			this.rightPair = rightPair;
			rightPair.parentPair = this;
			this.isLeafNode = false;
		}

		boolean isRegularPair() {
			return leftPair != null && leftPair.isLeafNode && rightPair != null && rightPair.isLeafNode;
		}

		void setLeftPair(PairNode leftPair) {
			this.leftPair = leftPair;
			leftPair.parentPair = this;
		}

		void setRightPair(PairNode rightPair) {
			this.rightPair = rightPair;
			rightPair.parentPair = this;
		}
	}
}
