package day5;

import java.io.IOException;
import java.util.Arrays;

public class Puzzle5 {

	private static String inputValues = "3,225,1,225,6,6,1100,1,238,225,104,0,1101,90,64,225,1101,15,56,225,1,14,153,224,101,-147,224,224,4,224,1002,223,8,223,1001,224,3,224,1,224,223,223,2,162,188,224,101,-2014,224,224,4,224,1002,223,8,223,101,6,224,224,1,223,224,223,1001,18,81,224,1001,224,-137,224,4,224,1002,223,8,223,1001,224,3,224,1,223,224,223,1102,16,16,224,101,-256,224,224,4,224,1002,223,8,223,1001,224,6,224,1,223,224,223,101,48,217,224,1001,224,-125,224,4,224,1002,223,8,223,1001,224,3,224,1,224,223,223,1002,158,22,224,1001,224,-1540,224,4,224,1002,223,8,223,101,2,224,224,1,223,224,223,1101,83,31,225,1101,56,70,225,1101,13,38,225,102,36,192,224,1001,224,-3312,224,4,224,1002,223,8,223,1001,224,4,224,1,224,223,223,1102,75,53,225,1101,14,92,225,1101,7,66,224,101,-73,224,224,4,224,102,8,223,223,101,3,224,224,1,224,223,223,1101,77,60,225,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999,1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105,1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0,0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999,7,226,677,224,1002,223,2,223,1005,224,329,1001,223,1,223,1007,226,677,224,1002,223,2,223,1005,224,344,101,1,223,223,108,226,226,224,1002,223,2,223,1006,224,359,101,1,223,223,7,226,226,224,102,2,223,223,1005,224,374,101,1,223,223,8,677,677,224,1002,223,2,223,1005,224,389,1001,223,1,223,107,677,677,224,102,2,223,223,1006,224,404,101,1,223,223,1107,677,226,224,102,2,223,223,1006,224,419,1001,223,1,223,1008,226,226,224,1002,223,2,223,1005,224,434,1001,223,1,223,7,677,226,224,102,2,223,223,1006,224,449,1001,223,1,223,1107,226,226,224,1002,223,2,223,1005,224,464,101,1,223,223,1108,226,677,224,102,2,223,223,1005,224,479,101,1,223,223,1007,677,677,224,102,2,223,223,1006,224,494,1001,223,1,223,1107,226,677,224,1002,223,2,223,1005,224,509,101,1,223,223,1007,226,226,224,1002,223,2,223,1006,224,524,101,1,223,223,107,226,226,224,1002,223,2,223,1005,224,539,1001,223,1,223,1108,677,677,224,1002,223,2,223,1005,224,554,101,1,223,223,1008,677,226,224,102,2,223,223,1006,224,569,1001,223,1,223,8,226,677,224,102,2,223,223,1005,224,584,1001,223,1,223,1008,677,677,224,1002,223,2,223,1006,224,599,1001,223,1,223,108,677,677,224,102,2,223,223,1006,224,614,1001,223,1,223,108,226,677,224,102,2,223,223,1005,224,629,101,1,223,223,8,677,226,224,102,2,223,223,1005,224,644,101,1,223,223,107,677,226,224,1002,223,2,223,1005,224,659,101,1,223,223,1108,677,226,224,102,2,223,223,1005,224,674,1001,223,1,223,4,223,99,226";

	public static void main(String[] args) throws IOException {
		System.out.println("result:" + calculate(splitInputValues(inputValues)));

	}

	private static int[] splitInputValues(String input) {
		return Arrays.stream(input.split(",")).mapToInt(Integer::valueOf).toArray();
	}

	private static int calculate(int[] inputs) {
		int stored = 5;
		for (int position = 0; position < inputs.length; ) {
			int input = inputs[position];
			int operation = calculateOperation(input);
			int[] mode = calculateModes(input);
			if (operation == 1) {
				add(inputs, position, mode);
				position = position + 4;
			} else if (operation == 2) {
				multiply(inputs, position, mode);
				position = position + 4;
			} else if (operation == 3) {
				write(inputs, stored, position + 1);
				position += 2;
			} else if (operation == 4) {
				output(inputs, inputs[position + 1]);
				position += 2;
			} else if (operation == 5) {
				position = movePositionIfNonZero(inputs, position, mode);
			} else if (operation == 6) {
				position = movePositionIfZero(inputs, position, mode);
			} else if (operation == 7) {
				lessThan(inputs, position, mode);
				position += 4;
			} else if (operation == 8) {
				equals(inputs, position, mode);
				position += 4;
			} else if (operation == 99) {
				return inputs[0];
			}
		}
		return 0;
	}

	private static void add(int[] inputs, int position, int[] mode) {
		int input1 = inputs[position + 1];
		int input2 = inputs[position + 2];
		write(inputs, getParameter(inputs, mode[0], input1) + getParameter(inputs, mode[1], input2), position + 3);
	}

	private static void multiply(int[] inputs, int position, int[] mode) {
		int input1 = inputs[position + 1];
		int input2 = inputs[position + 2];
		write(inputs, getParameter(inputs, mode[0], input1) * getParameter(inputs, mode[1], input2), position + 3);
	}

	private static void write(int[] inputs, int stored, int locationReference) {
		int location = inputs[locationReference];
		inputs[location] = stored;
	}

	private static void output(int[] inputs, int input) {
		int location = input;
		System.out.println(inputs[location]);
	}

	private static int movePositionIfNonZero(int[] inputs, int operationPosition, int[] mode) {
		int input1 = inputs[operationPosition + 1];
		int output = inputs[operationPosition + 2];
		int parameter = getParameter(inputs, mode[0], input1);
		if (parameter > 0) {
			operationPosition = getParameter(inputs, mode[1], output);
		} else {
			operationPosition += 3;
		}
		return operationPosition;
	}

	private static int movePositionIfZero(int[] inputs, int operationPosition, int[] mode) {
		int input1 = inputs[operationPosition + 1];
		int output = inputs[operationPosition + 2];
		int parameter = getParameter(inputs, mode[0], input1);
		if (parameter == 0) {
			operationPosition = getParameter(inputs, mode[1], output);
		} else {
			operationPosition += 3;
		}
		return operationPosition;
	}

	private static void lessThan(int[] inputs, int operationPosition, int[] mode) {
		int input1 = inputs[operationPosition + 1];
		int input2 = inputs[operationPosition + 2];
		int output = inputs[operationPosition + 3];
		int parameter1 = getParameter(inputs, mode[0], input1);
		int parameter2 = getParameter(inputs, mode[1], input2);
		if (parameter1 < parameter2) {
			inputs[output] = 1;
		} else {
			inputs[output] = 0;
		}
	}

	private static void equals(int[] inputs, int operationPosition, int[] mode) {
		int input1 = inputs[operationPosition + 1];
		int input2 = inputs[operationPosition + 2];
		int output = inputs[operationPosition + 3];
		int parameter1 = getParameter(inputs, mode[0], input1);
		int parameter2 = getParameter(inputs, mode[1], input2);
		if (parameter1 == parameter2) {
			inputs[output] = 1;
		} else {
			inputs[output] = 0;
		}
	}

	private static int getParameter(int[] inputs, int mode, int input) {
		if (mode == 0) {
			return inputs[input];
		} else {
			return input;
		}
	}

	private static int calculateOperation(int operationDigit) {
		return operationDigit % 100;
	}

	private static int[] calculateModes(int operationDigit) {
		int[] modes = new int[3];
		int parametersDigit = operationDigit / 100;
		for (int i = 0; i < 3; i++) {
			modes[i] = parametersDigit % 10;
			parametersDigit /= 10;
		}
		return modes;
	}

}
