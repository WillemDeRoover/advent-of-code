package day16;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Puzzle16 {

	private static String input = "59756772370948995765943195844952640015210703313486295362653878290009098923609769261473534009395188480864325959786470084762607666312503091505466258796062230652769633818282653497853018108281567627899722548602257463608530331299936274116326038606007040084159138769832784921878333830514041948066594667152593945159170816779820264758715101494739244533095696039336070510975612190417391067896410262310835830006544632083421447385542256916141256383813360662952845638955872442636455511906111157861890394133454959320174572270568292972621253460895625862616228998147301670850340831993043617316938748361984714845874270986989103792418940945322846146634931990046966552";
	private static int[] basePattern = new int[] { 0, 1, 0, -1 };

	public static void main(String[] args) {
		verifyCorrectnessOfFlawedFrequencyTransmission();
		decodeSignalWithFlawedFrequencyTransmission();
	}

	private static void verifyCorrectnessOfFlawedFrequencyTransmission() {
		List<Integer> signal = createSignal(input, 0L);
		List<List<Integer>> patterns = createPatterns(signal);
		for(int iteration = 1; iteration <= 100; iteration++) {
			signal = transformSignal(signal, patterns);
		}
		System.out.println("The first eight digits of the signal after 100 iterations are: " + getMessage(signal));
	}

	private static List<Integer> createSignal(String inputSignal, Long offset) {
		return IntStream.range(0, inputSignal.length())
				.skip(offset)
				.map(index -> Character.getNumericValue(inputSignal.charAt(index)))
				.boxed()
				.collect(toList());
	}

	private static List<List<Integer>> createPatterns(List<Integer> signal) {
		return IntStream.range(0, signal.size())
				.mapToObj(index -> createPattern(index, signal.size()))
				.collect(toList());
	}

	private static List<Integer> createPattern(int index, int length) {
		ArrayList<Integer> pattern = new ArrayList<>();
		int repeating = index + 1;
		patternGenerator: for(int aPattern : basePattern) {
			for(int i = 0; i < repeating; i++) {
				pattern.add(aPattern);
				if(pattern.size() > length + 1) {
					break patternGenerator;
				}
			}
		}

		while(pattern.size() <= length + 1) {
			pattern.addAll(pattern);
		}

		return pattern.subList(1, length + 1);

	}

	private static List<Integer> transformSignal(List<Integer> signal, List<List<Integer>> patterns) {
		List<Integer> transformedSignal = new ArrayList<>();
		for (int i = 0; i < signal.size(); i++) {
			transformedSignal.add(applyPattern(patterns.get(i), signal));
		}
		return transformedSignal;
	}

	private static int applyPattern(List<Integer> pattern, List<Integer> signal) {
		int result = 0;
		for (int i = 0; i < pattern.size(); i++) {
			result += pattern.get(i) * signal.get(i);
		}
		return Math.abs(result%10);
	}

	private static String getMessage(List<Integer> signal) {
		return signal.stream()
				.limit(8)
				.map(String::valueOf)
				.collect(joining(""));
	}

	private static void decodeSignalWithFlawedFrequencyTransmission() {
		String repeatedInput = String.join("", Collections.nCopies(10000, input));
		Long messageOffset = Long.valueOf(input.substring(0, 7));
		List<Integer> signal = createSignal(repeatedInput, messageOffset);

		for(int i = 1; i <= 100; i++) {
			int result = 0;
			for(int position = signal.size() - 1; position >= 0; position--) {
				result += signal.get(position);
				result = Math.abs(result%10);
				signal.set(position, result);
			}
		}

		System.out.println("The eight-digit message embedded in the final output is: " + getMessage(signal));
	}

}
