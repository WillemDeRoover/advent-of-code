package day2;

import java.io.IOException;
import java.util.Arrays;

public class Puzzle2 {

	private static String inputValues = "1,12,2,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,10,19,1,6,19,23,1,10,23,27,2,27,13,31,1,31,6,35,2,6,35,39,1,39,5,43,1,6,43,47,2,6,47,51,1,51,5,55,2,55,9,59,1,6,59,63,1,9,63,67,1,67,10,71,2,9,71,75,1,6,75,79,1,5,79,83,2,83,10,87,1,87,5,91,1,91,9,95,1,6,95,99,2,99,10,103,1,103,5,107,2,107,6,111,1,111,5,115,1,9,115,119,2,119,10,123,1,6,123,127,2,13,127,131,1,131,6,135,1,135,10,139,1,13,139,143,1,143,13,147,1,5,147,151,1,151,2,155,1,155,5,0,99,2,0,14,0";

	public static void main(String[] args) throws IOException {
		System.out.println("result1:" + calculate(splitInputValues(inputValues)));
		System.out.println("result2:" + calculateInputsFor(19690720));
	}

	private static int[] splitInputValues(String input) {
		return Arrays.stream(input.split(",")).mapToInt(Integer::valueOf).toArray();
	}

	private static int calculate(int[] inputs) {
		int operations = inputs.length/4;
		for(int i = 0; i < operations; i++) {
			int currentPosition = 4 * i;
			int firstPos = inputs[currentPosition + 1];
			int secondPos = inputs[currentPosition + 2];
			int outputPos = inputs[currentPosition + 3];

			if(inputs[currentPosition] == 1) {
				inputs[outputPos] = inputs[firstPos] + inputs[secondPos];
			} else if(inputs[currentPosition] == 2){
				inputs[outputPos] = inputs[firstPos] * inputs[secondPos];
			} else if(inputs[currentPosition] == 99) {
				return inputs[0];
			}
		}
		return 0;
	}

	private static int calculateInputsFor(int output) {
		for(int i = 0; i < 100; i++) {
			for(int j = 0; j < 100; j++) {
				int[] inputs = resetInputs(i, j);
				if(calculate(inputs) == output) {
					return (100 * i) + j;
				}
			}
		}
		return 0;
	}

	private static int[] resetInputs(int i, int j) {
		int[] inputs = splitInputValues(inputValues);
		inputs[1] = i;
		inputs[2] = j;
		return inputs;
	}

}
