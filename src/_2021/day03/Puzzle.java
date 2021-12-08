package _2021.day03;

import static java.lang.Character.getNumericValue;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		List<String> diagnosticsReport = Files.lines(Paths.get("src/_2021/day03/input.txt"))
				.collect(toList());


		List<String> oxygenRate = new ArrayList<>(diagnosticsReport);
		for (int i = 0; i < diagnosticsReport.get(0).length(); i++) {
			int digit1 = countDigit(oxygenRate, i, 1);
			int digit0 = countDigit(oxygenRate, i, 0);
			final int pos = i;
			if(digit1 >= digit0) {
				oxygenRate = oxygenRate.stream().filter(s -> getNumericValue(s.charAt(pos)) == 1).collect(toList());
			} else {
				oxygenRate = oxygenRate.stream().filter(s -> getNumericValue(s.charAt(pos)) == 0).collect(toList());
			}
		}

		List<String> co2Rate = new ArrayList<>(diagnosticsReport);
		for (int i = 0; i < diagnosticsReport.get(0).length() && co2Rate.size() > 1; i++) {
			int digit1 = countDigit(co2Rate, i, 1);
			int digit0 = countDigit(co2Rate, i, 0);
			final int pos = i;
			if(digit1 < digit0) {
				co2Rate = co2Rate.stream().filter(s -> getNumericValue(s.charAt(pos)) == 1).collect(toList());
			} else {
				co2Rate = co2Rate.stream().filter(s -> getNumericValue(s.charAt(pos)) == 0).collect(toList());
			}
		}
		System.out.println(parseInt(oxygenRate.get(0), 2) * Integer.parseInt(co2Rate.get(0), 2));

	}

	private static void calculatePowerConsumption(List<String> diagnosticsReport) {
		StringBuilder gammaRateBuilder = new StringBuilder();
		StringBuilder epsilonRateBuilder = new StringBuilder();

		for (int i = 0; i < diagnosticsReport.get(0).length(); i++) {
			int digit1 = countDigit(diagnosticsReport, i, 1);

			if(digit1 > diagnosticsReport.size()/2) {
				gammaRateBuilder.append(1);
				epsilonRateBuilder.append(0);
			} else {
				gammaRateBuilder.append(0);
				epsilonRateBuilder.append(1);
			}
		}

		String gammaRate = gammaRateBuilder.toString();
		String epsilonRate = epsilonRateBuilder.toString();

		System.out.println(parseInt(gammaRate, 2) * parseInt(epsilonRate, 2));
	}

	private static int countDigit(List<String> diagnosticsReport, int position, int digit) {
		int total = 0;
		for (int j = 0; j < diagnosticsReport.size(); j++) {
			if(digit == getNumericValue(diagnosticsReport.get(j).charAt(position))) {
				total++;
			};
		}
		return total;
	}

}
