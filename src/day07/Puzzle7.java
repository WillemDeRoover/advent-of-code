package day07;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;

//Try to rewrite this concurrently with blocking queues
public class Puzzle7 {

	private static final String inputString = "3,8,1001,8,10,8,105,1,0,0,21,30,51,76,101,118,199,280,361,442,99999,3,9,102,5,9,9,4,9,99,3,9,102,4,9,9,1001,9,3,9,102,2,9,9,101,2,9,9,4,9,99,3,9,1002,9,3,9,1001,9,4,9,102,5,9,9,101,3,9,9,1002,9,3,9,4,9,99,3,9,101,5,9,9,102,4,9,9,1001,9,3,9,1002,9,2,9,101,4,9,9,4,9,99,3,9,1002,9,2,9,1001,9,3,9,102,5,9,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,99";

	public static void main(String[] args) {
		System.out.println("largest output signal: " + calculateHighestSignal(0, 4));
		System.out.println("largest output signal with feedback loop: " + calculateHighestSignal(5, 9));
	}

	private static int calculateHighestSignal(int startRange, int endRange) {
		List<Integer> phaseSettings = IntStream.range(startRange, endRange + 1).boxed().collect(toList());
		return calculateHighestSignal(phaseSettings, new int[5]);
	}

	private static int calculateHighestSignal(List<Integer> phaseSettings, int[] phaseSettingOrder) {
		if (phaseSettings.size() == 0) {
			return runComputers(phaseSettingOrder);
		} else {
			int highestSignal = 0;
			for (Integer phaseSetting : phaseSettings) {
				List<Integer> reducedPhaseSettings = phaseSettings.stream().filter(aPhaseSetting -> !aPhaseSetting.equals(phaseSetting)).collect(toList());
				phaseSettingOrder[phaseSettings.size() - 1] = phaseSetting;
				int signal = calculateHighestSignal(reducedPhaseSettings, phaseSettingOrder);
				if (signal > highestSignal) {
					highestSignal = signal;
				}
			}
			return highestSignal;
		}
	}

	private static int runComputers(int[] phaseSettingOrder) {
		List<IntComputer> intComputers = Arrays.stream(phaseSettingOrder)
				.mapToObj(phaseSetting -> new IntComputer(getProgram(), phaseSetting))
				.collect(toList());

		int input = 0;
		boolean finished = false;
		while (!finished) {
			for (IntComputer intComputer : intComputers) {
				input = intComputer.calculate(input);
			}
			finished = intComputers.get(4).isCompleted();
		}
		return input;
	}

	private static int[] getProgram() {
		return Arrays.stream(inputString.split(",")).mapToInt(Integer::valueOf).toArray();
	}

	public static class IntComputer {

		private int[] program;
		int position = 0;
		int output;
		boolean completed = false;
		Queue<Integer> inputQueue = new LinkedList<>();

		IntComputer(int[] program, int phaseSetting) {
			this.program = program;
			inputQueue.add(phaseSetting);
		}

		private Integer calculate(int input) {
			inputQueue.add(input);
			while (position < program.length) {
				int operationCode = program[position];
				int operation = calculateOperation(operationCode);
				int[] mode = calculateModes(operationCode);
				if (operation == 1) {
					add(position, mode);
					position = position + 4;
				} else if (operation == 2) {
					multiply(position, mode);
					position = position + 4;
				} else if (operation == 3) {
					writeInputs(position + 1);
					position += 2;
				} else if (operation == 4) {
					output(program[position + 1]);
					position += 2;
					return this.output;
				} else if (operation == 5) {
					position = movePositionIfNonZero(position, mode);
				} else if (operation == 6) {
					position = movePositionIfZero(position, mode);
				} else if (operation == 7) {
					lessThan(position, mode);
					position += 4;
				} else if (operation == 8) {
					equals(position, mode);
					position += 4;
				} else if (operation == 99) {
					completed = true;
					return output;
				}
			}
			return 0;
		}

		private boolean isCompleted() {
			return completed;
		}

		private void writeInputs(int position) {
			write(inputQueue.remove(), position);
		}

		private void add(int position, int[] mode) {
			int input1 = program[position + 1];
			int input2 = program[position + 2];
			write(getParameter(mode[0], input1) + getParameter(mode[1], input2), position + 3);
		}

		private void multiply(int position, int[] mode) {
			int input1 = program[position + 1];
			int input2 = program[position + 2];
			write(getParameter(mode[0], input1) * getParameter(mode[1], input2), position + 3);
		}

		private void write(int stored, int locationReference) {
			int location = program[locationReference];
			program[location] = stored;
		}

		private void output(int location) {
			this.output = program[location];
		}

		private int movePositionIfNonZero(int operationPosition, int[] mode) {
			int input1 = program[operationPosition + 1];
			int output = program[operationPosition + 2];
			int parameter = getParameter(mode[0], input1);
			if (parameter > 0) {
				operationPosition = getParameter(mode[1], output);
			} else {
				operationPosition += 3;
			}
			return operationPosition;
		}

		private int movePositionIfZero(int operationPosition, int[] mode) {
			int input1 = program[operationPosition + 1];
			int output = program[operationPosition + 2];
			int parameter = getParameter(mode[0], input1);
			if (parameter == 0) {
				operationPosition = getParameter(mode[1], output);
			} else {
				operationPosition += 3;
			}
			return operationPosition;
		}

		private void lessThan(int operationPosition, int[] mode) {
			int input1 = program[operationPosition + 1];
			int input2 = program[operationPosition + 2];
			int output = program[operationPosition + 3];
			int parameter1 = getParameter(mode[0], input1);
			int parameter2 = getParameter(mode[1], input2);
			if (parameter1 < parameter2) {
				program[output] = 1;
			} else {
				program[output] = 0;
			}
		}

		private void equals(int operationPosition, int[] mode) {
			int input1 = program[operationPosition + 1];
			int input2 = program[operationPosition + 2];
			int output = program[operationPosition + 3];
			int parameter1 = getParameter(mode[0], input1);
			int parameter2 = getParameter(mode[1], input2);
			if (parameter1 == parameter2) {
				program[output] = 1;
			} else {
				program[output] = 0;
			}
		}

		private int getParameter(int mode, int input) {
			if (mode == 0) {
				return program[input];
			} else {
				return input;
			}
		}

		private int calculateOperation(int operationDigit) {
			return operationDigit % 100;
		}

		private int[] calculateModes(int operationDigit) {
			int[] modes = new int[3];
			int parametersDigit = operationDigit / 100;
			for (int i = 0; i < 3; i++) {
				modes[i] = parametersDigit % 10;
				parametersDigit /= 10;
			}
			return modes;
		}
	}
}
