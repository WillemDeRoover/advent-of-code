package day22;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

public class Puzzle22 {

	private static final String CUT = "cut ";
	private static final String DEAL_INTO_NEW_STACK = "deal into new stack";
	private static final String DEAL_WITH_INCREMENT = "deal with increment ";
	private static String INPUT = "src/day22/input.txt";

	public static void main(String[] args) throws IOException {
		List<String> shuffles = Files.lines(Paths.get(INPUT)).collect(toList());
		System.out.println("The card with number 2019 can be found on position: " + findPositionOfCard(2019, shuffles));
		BigInteger card = getCardOnPosition(toBigInt(2020), toBigInt(119315717514047L), toBigInt(101741582076661L), shuffles);
		System.out.println("The card on position 2020 after '101741582076661' shuffles of a '119315717514047' card sized deck is: " + card);
	}

	private static int findPositionOfCard(int card, List<String> shuffleOrder) {
		List<Integer> cardDeck = IntStream.range(0, 10007).boxed().collect(toList());
		for (String shuffle : shuffleOrder) {
			if (shuffle.startsWith(CUT)) {
				cardDeck = cut(getCutValue(shuffle), cardDeck);
			} else if (shuffle.startsWith(DEAL_INTO_NEW_STACK)) {
				cardDeck = dealIntoNewStack(cardDeck);
			} else if (shuffle.startsWith(DEAL_WITH_INCREMENT)) {
				cardDeck = incrementWithN(getIncrementValue(shuffle), cardDeck);
			}
		}
		return cardDeck.indexOf(card);
	}

	private static List<Integer> cut(int valueOf, List<Integer> cardDeck) {
		int split = (valueOf + cardDeck.size())% cardDeck.size();
		List<Integer> result = cardDeck.subList(split, cardDeck.size());
		result.addAll(cardDeck.subList(0, split));
		return result;
	}

	private static Integer getCutValue(String shuffle) {
		return Integer.valueOf(shuffle.substring(CUT.length()));
	}

	private static List<Integer> dealIntoNewStack(List<Integer> cardDeck) {
		return IntStream.range(0, cardDeck.size())
				.map(index -> cardDeck.size()  - 1 - index)
				.map(cardDeck::get).boxed()
				.collect(toList());
	}

	private static List<Integer> incrementWithN(int increment, List<Integer> cardDeck) {
		List<Integer> result = IntStream.generate(() -> 0).boxed().limit(cardDeck.size()).collect(toList());
		for (int i = 0; i < cardDeck.size(); i++) {
			result.set((i * increment) % cardDeck.size(), cardDeck.get(i));
		}
		return result;
	}

	private static Integer getIncrementValue(String shuffle) {
		return Integer.valueOf(shuffle.substring(DEAL_WITH_INCREMENT.length()));
	}

	private static BigInteger getCardOnPosition(BigInteger card, BigInteger cardDeckSize, BigInteger shuffleAmount, List<String> shuffles) {
		BigInteger increment = BigInteger.ONE;
		BigInteger offset = BigInteger.ZERO;
		for(int i = shuffles.size() - 1; i >= 0 ; i--) {
			if (shuffles.get(i).startsWith(DEAL_INTO_NEW_STACK)) {
				increment = increment.multiply(toBigInt(-1));
				offset = offset.add(BigInteger.ONE).multiply(toBigInt(-1));
			} else if (shuffles.get(i).startsWith(CUT)) {
				offset = offset.add(toBigInt(getCutValue(shuffles.get(i))));
			} else if (shuffles.get(i).startsWith(DEAL_WITH_INCREMENT)) {
				Integer incrementValue = getIncrementValue(shuffles.get(i));
				BigInteger inverseMod = toBigInt(incrementValue).modInverse(cardDeckSize);
				increment = increment.multiply(inverseMod);
				offset = offset.multiply(inverseMod);
			}
			increment = increment.add(cardDeckSize).mod(cardDeckSize);
			offset = offset.add(cardDeckSize).mod(cardDeckSize);
		}

		BigInteger aN = increment.modPow(shuffleAmount, cardDeckSize);
		// a^n * x
		BigInteger aNx = aN.multiply(card);
		// b* (a^n - 1)
		BigInteger bAni = offset.multiply(aN.add(cardDeckSize).subtract(BigInteger.ONE));
		// 1/(a-1)
		BigInteger ai = toBigInt(increment.longValue() - 1).modPow(cardDeckSize.subtract(toBigInt(2)), cardDeckSize);
		// (a^n * x) + b * (a^n -1)/(a-1)
		return  aNx.add(bAni.multiply(ai)).mod(cardDeckSize);
	}

	static BigInteger toBigInt(long longValue) {
		return BigInteger.valueOf(longValue);
	}
}
